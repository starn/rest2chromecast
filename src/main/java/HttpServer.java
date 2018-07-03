import java.io.IOException;
import java.net.ServerSocket;

public class HttpServer {
    private int port;

    public HttpServer(int port) {
        this.port=port;
    }

    public void start() throws IOException {
        ServerSocket serverSocket = null;
        boolean listening = true;

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Listening on port "+port);
        } catch (IOException var4) {
            System.err.println("Could not listen on port: "+port+" " + var4);
            System.exit(-1);
        }

        while(listening) {
            (new RequestParser(serverSocket.accept())).start();
        }

        serverSocket.close();
    }
}
