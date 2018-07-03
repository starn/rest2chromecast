public class Main{
    public static void main(String args[]){
        int port = -1;
        if (args.length==0) {
            System.err.println("missing parameter for REST listening port. Usage java -jar rest2chromecast 8080");
            System.exit(1);
        }
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e){
            System.err.println("bad parameter for REST listening port. Usage java -jar rest2chromecast 8080");
            System.exit(1);
        }

        try  {
            try {
                new ChromecastDiscoveryScheduler().start();
                new HttpServer(port).start();
            }catch (Exception e){
                e.printStackTrace();
            }
        } catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        System.exit(0);
    }



}

