import java.util.concurrent.TimeUnit;

/**
 * Created by starn on 03/07/2018.
 */
public class ChromecastDiscoveryScheduler extends Thread {
    private static long ONE_SECOND = 1000;
    private static long ONE_MINUTE = ONE_SECOND*60;

    @Override
    public void run() {
        while (true) {
            System.out.println("launch find");
            ChromecastApiWrapper.findChromecasts();
            try {
                Thread.sleep(ONE_MINUTE);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }

    }
}
