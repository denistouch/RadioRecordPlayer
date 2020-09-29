import model.Info;
import model.UrlPlayer;

public class MainApp {
    public static void main(String[] args) {
        UrlPlayer now =  new UrlPlayer();
        now.setStation("chil");
        now.setBitrate("256");
        System.out.printf("Station: %s\nquality: %d\n",now.getStation(),now.getBitrate());
        new Thread(now).start();
        now.getInfo();

    }
}
