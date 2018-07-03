import java.io.*;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestParser extends Thread {
    private Socket socket = null;
    private DataOutputStream out;
    private static ChromecastApiWrapper chromecast = new ChromecastApiWrapper();

    public RequestParser(Socket socket) {
        super("RequestParser");
        this.socket = socket;
    }

    public void run() {
        DataInputStream in = null;

        try {
            System.out.println("Connection Made");
            in = new DataInputStream(this.socket.getInputStream());
            this.out = new DataOutputStream(this.socket.getOutputStream());
            BufferedReader ex = new BufferedReader(new InputStreamReader(in));

            String line;
            while((line = ex.readLine()) != null) {
                if(line.length() ==0) break;

                if (!line.startsWith("GET ")) continue;
                System.out.println("received line " + line);

                Map<String,String> httpGetParams = splitQuery(line);
                for (String key: httpGetParams.keySet()){
                    String value = httpGetParams.get(key);
                    System.out.println(key+" - "+value);
                }

                String action = httpGetParams.get("action");
                System.out.println("action: "+action);

                if (action == null || "".equals(action)) {
                    writeError(out,"get parameter 'action' is missing. exemple:\nhttp://127.0.0.1/action=play\navailable actions are: play, pause");
                    return;
                }

                try {
                    Object result = chromecast.getClass().getMethod(action).invoke(chromecast);
                    String resultString = "";
                    if (result !=null && result instanceof String){
                    resultString = "\nresult: "+(String)result;
                    }
                    writeSuccess(out, "action "+action+" executed."+resultString);
                } catch (Exception e) {
                    e.printStackTrace();
                    String errorMessage = "";
                    if (e.getCause()!=null && e.getCause().getMessage()!=null) errorMessage=e.getCause().getMessage();
                    if (e.getMessage()!=null) errorMessage=e.getMessage();

                    StringBuffer availableActions = new StringBuffer();
                    for (Method m: chromecast.getClass().getMethods()){
                        if (m.getParameterTypes().length==0 && m.getDeclaringClass() == ChromecastApiWrapper.class) availableActions.append(" ").append(m.getName());
                    }

                    errorMessage+="\n\nuse URL like: http://127.0.0.1?action=play (available actions are: "+availableActions+")";
                    writeError(out,errorMessage);
                }

            }

        } catch (IOException var15) {
            Logger.getLogger(RequestParser.class.getName()).log(Level.SEVERE, (String)null, var15);
        } finally {
            try {
                if(in != null) {
                    in.close();
                }
                if (out!=null) out.close();
            } catch (IOException var14) {
                Logger.getLogger(RequestParser.class.getName()).log(Level.SEVERE, (String)null, var14);
            }

        }

    }

    public void writeSuccess(DataOutputStream out, String message) throws IOException{
        out.writeBytes("HTTP/1.1 200 OK");
        out.writeByte('\n');
        out.writeBytes("Connection: Closed");
        out.writeByte('\n');
        out.writeByte('\n');
        out.writeBytes(message);
        out.writeByte('\n');
        out.flush();
        out.close();
    }

    public void writeError(DataOutputStream out, String message) throws IOException{
        out.writeBytes("HTTP/1.1 200 OK");
        out.writeByte('\n');
        out.writeBytes("Connection: Closed");
        out.writeByte('\n');
        out.writeByte('\n');
        out.writeBytes(message);
        out.writeByte('\n');
        out.flush();
        out.close();
        return;
    }

    public void executePowerCommandMessages(String line) {
        if(line.equals("Shutdown")) {
            System.out.println("Received instruction to shutdown");
            this.sendMessage("ShuttingDown");
        }

        if(line.equals("Restart")) {
            System.out.println("Received instruction to restart");
            this.sendMessage("Restarting");
        }

        if(line.equals("Sleep")) {
            System.out.println("Received instruction to sleep");
            this.sendMessage("Sleeping");
        }

    }

    public void sendMessage(String messageToSend) {
        messageToSend = messageToSend + "\n";
        byte[] msg = messageToSend.getBytes();

        try {
            this.out.write(msg);
            this.out.flush();
            System.out.print("Sending message to client: " + messageToSend);
        } catch (IOException var4) {
            var4.printStackTrace();
        }

    }

    public static Map<String, String> splitQuery(String url) throws UnsupportedEncodingException {
        //remove method from url
        if (url.startsWith("GET ")) url = url.substring(4);

        //find start and end  index of http params
        int paramStartIdx = url.indexOf("?");
        if (paramStartIdx==-1) return new HashMap<String, String>();
        int paramEndIdx = url.indexOf(" ");
        if (paramEndIdx==-1) paramEndIdx = url.length();

        String query = url.substring(paramStartIdx+1,paramEndIdx);
        Map<String, String> query_pairs = new LinkedHashMap<String, String>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return query_pairs;
    }
}
