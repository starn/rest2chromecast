import su.litvak.chromecast.api.v2.ChromeCast;
import su.litvak.chromecast.api.v2.ChromeCasts;
import su.litvak.chromecast.api.v2.MediaStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by starn on 03/07/2018.
 */
public class ChromecastApiWrapper {

    public void play(){
        try {
            List<ChromeCast> allChromecast = getChromecasts();

            for (ChromeCast chromecast : allChromecast) {
                System.out.println("connect to chromecast " + chromecast.getTitle());
                System.out.println("chromecast is running app " + chromecast.getAppTitle());
                System.out.println("play");
                chromecast.play();
            }

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void pause(){
        try {
            List<ChromeCast> allChromecast = getChromecasts();

            for (ChromeCast chromecast : allChromecast) {

                System.out.println("connect to chromecast " + chromecast.getTitle());
                System.out.println("chromecast is running app " + chromecast.getAppTitle());


                System.out.println("pause");
                chromecast.pause();
            }

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void seek(){
        try {
            List<ChromeCast> allChromecast = getChromecasts();

            for (ChromeCast chromecast : allChromecast) {

                System.out.println("connect to chromecast " + chromecast.getTitle());
                System.out.println("chromecast is running app " + chromecast.getAppTitle());


                System.out.println("skip 30s");
                double newTime = chromecast.getMediaStatus().currentTime + 30;
                chromecast.seek(newTime);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void next(){
        try {
            List<ChromeCast> allChromecast = getChromecasts();

            for (ChromeCast chromecast : allChromecast) {

                System.out.println("skip 30s");
                MediaStatus mediaStatus = chromecast.getMediaStatus();
                double duration = mediaStatus.media.duration;
                chromecast.seek(duration);
            }

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void stop(){
        try {
            List<ChromeCast> allChromecast = getChromecasts();
            for (ChromeCast chromecast : allChromecast) {
                chromecast.stopApp();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void kill(){
        System.exit(0);
    }

    private static List<ChromeCast> getChromecasts() {
        List<ChromeCast> result = new ArrayList<ChromeCast>();

        for (ChromeCast item: ChromeCasts.get()){
            if (item.getModel().equals("Chromecast")){
                System.out.println("found chromecast: "+item.getTitle()+" running "+item.getAppTitle());
                result.add(item);
            }
        }

        if (result.size()==0) throw new RuntimeException("No chromecast found");

        return result;
    }

    public static String findChromecasts() {
        List<ChromeCast> result = new ArrayList<ChromeCast>();
        StringBuffer output = new StringBuffer();
        try {
            ChromeCasts.stopDiscovery();
            ChromeCasts.startDiscovery();
            Thread.sleep(20000);
            for (ChromeCast item: ChromeCasts.get()){
                if (item.getModel().equals("Chromecast")){
                    System.out.println("found chromecast: "+item.getTitle()+" running "+item.getAppTitle());
                    output.append("found chromecast: "+item.getTitle()+" running "+item.getAppTitle());
                    result.add(item);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        if (result.size()==0) {
            output.append("No chromecast found");
            System.out.println("No chromecast found");
        }

        return output.toString();
    }
}
