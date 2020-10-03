import model.Station;
import model.UrlPlayer;
import view.ViewPlayer;

public class MainApp {
    static UrlPlayer player;
    static ViewPlayer viewPlayer;
    static Thread threadPlayer;
    static Thread threadInfo;

    public static void main(String[] args) {
        player = new UrlPlayer("bighits", "high");
        viewPlayer = new ViewPlayer();

        Thread changeThread = new Thread(() -> {
            try {
                while (true) {
                    String nowPlayed = viewPlayer.prefix;
                    Thread.currentThread().sleep(10);
                    if (!nowPlayed.equals(viewPlayer.prefix)) {
                        change(viewPlayer.prefix, "high");
                        viewPlayer.setTitle(player.getSong() + " - " + player.getArtist());
                        viewPlayer.setIcon(player.getStation().getIconFillColored());
                        viewPlayer.drawCover(player.getCover());
                    }
                }
            } catch (InterruptedException e) {
                System.out.println(e.getLocalizedMessage());
            }
        });

        //update info from api
        Runnable runnable = () -> {
            try {
                Thread.currentThread().sleep(1000);
                System.out.println(player.toString());
                viewPlayer.setTitle(player.getSong() + " - " + player.getArtist());
                viewPlayer.setIcon(player.getStation().getIconFillColored());
                viewPlayer.drawCover(player.getCover());
                viewPlayer.setStations(player.getStationList());
                viewPlayer.show();
                changeThread.start();
                while (!Thread.currentThread().isInterrupted()) {
                    int secToWait = 1000 * 5;
                    int lastId = player.getTrack().getId();
                    String lastURL = player.getUrlString();
                    Thread.currentThread().sleep(secToWait);
                    player.updateInfo();
                    if ((player.getTrack().getId() != lastId) || (!lastURL.equals(player.getUrlString()))) {
                        System.out.println(player.toString());
                        viewPlayer.setTitle(player.getSong() + " - " + player.getArtist());
                        viewPlayer.setIcon(player.getStation().getIconFillColored());
                        viewPlayer.drawCover(player.getCover());
                    } else {
                        System.out.print(".");
                    }
                }
            } catch (InterruptedException e) {
                System.out.println(e.getLocalizedMessage());
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }
        };

        //start thread update info about playing music
        threadInfo = new Thread(runnable);
        threadInfo.start();

        //start playing music
        threadPlayer = new Thread(player);
        threadPlayer.start();


//        testAllStation();
//        change();
//        change("rus","high");
    }

    private static void testAllStation() {
        for (Station station : player.getStationList()) {
            change(station.getPrefix(), "high");
        }
    }

    private static void change() {
        change("gold", "high");
    }

    private static void change(String prefix, String stream) {
        Thread testThread = new Thread(() -> {
            try {
//                Thread.currentThread().sleep(10000);
                player.setPrefix(prefix);
                player.setStream(stream);
                player.stop();
            } catch (InterruptedException e) {
                System.out.println(e.getLocalizedMessage());
            }
        });
        testThread.start();
        try {
            testThread.join();
        } catch (InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }
        threadPlayer = new Thread(player);
        threadPlayer.start();
    }
}
