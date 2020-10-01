import model.UrlPlayer;

public class MainApp {
    public static void main(String[] args) {
        UrlPlayer player = new UrlPlayer("chil", "high");
//        player.getStationList();

        //update info from api
        Runnable runnable = () -> {
            try {
                while (true) {
                    int secToWait = 1000 * 5;
                    int lastId = player.getTrack().getId();
                    Thread.currentThread().sleep(secToWait);
                    player.updateInfo();
                    // if track changed view changes
                    if (player.getTrack().getId() != lastId ) {
                        System.out.println("\n_____NOW___PLAYING_____");
                        System.out.printf("Station: %s", player.getTitle() + "\n");
                        System.out.printf("Artist: %s", player.getArtist() + "\n");
                        System.out.printf("Song: %s", player.getSong() + "\n");
//                        System.out.printf("Share Url: %s", player.getTrack().getShareUrl() + "\n");
                        System.out.printf("ImageUrl: %s", player.getCover() + "\n");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        //start thread update info about playing music
        Thread threadInfo = new Thread(runnable);
        threadInfo.start();

        //start playing music
        Thread threadPlayer = new Thread(player);
        threadPlayer.start();

        //first load info about now playing
        new Thread(() -> {
            try {
                Thread.currentThread().sleep(1000);
                System.out.println("\n_____NOW___PLAYING_____");
                System.out.printf("Station: %s", player.getTitle() + "\n");
                System.out.printf("Artist: %s", player.getArtist() + "\n");
                System.out.printf("Song: %s", player.getSong() + "\n");
//                System.out.printf("Share Url: %s", player.getTrack().getShareUrl() + "\n");
                System.out.printf("ImageUrl: %s", player.getCover() + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
