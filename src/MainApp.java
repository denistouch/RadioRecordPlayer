import model.UrlPlayer;

public class MainApp {
    public static void main(String[] args) {
        UrlPlayer player = new UrlPlayer("bighits", "normal");
//        player.getStationList();
        boolean test = false;
        if (test) {
            player.getStationList();
            return;
        }
        //update info from api
        Runnable runnable = () -> {
            try {
                int tick = 0;
                int length = 0;
                while (true) {
                    int secToWait = 1000 * 5;
                    int lastId = player.getTrack().getId();
                    Thread.currentThread().sleep(secToWait);
                    player.updateInfo();
                    // if track changed view changes
                    if (player.getTrack().getId() != lastId) {
//                        int min = length / 30;
//                        int sec = length % 30;
//                        System.out.printf("\nLast track played : %d min %d seconds\n", min,sec);
                        System.out.println("\n_____NOW___PLAYING_____");
                        System.out.printf("Station: %s\n", player.getTitle());
//                        System.out.printf("TrackId: %d\n", player.getTrack().getId());
                        System.out.printf("Artist: %s\n", player.getArtist());
                        System.out.printf("Song: %s\n", player.getSong());
//                        System.out.printf("Listen Url: %s", player.getTrack().getListenUrl() + "\n");
                        System.out.printf("Share Url: %s", player.getTrack().getShareUrl() + "\n");
                        System.out.printf("ImageUrl: %s\n", player.getCover());
                        tick = 0;
                        length = 0;
                    } else {
                        switch (tick) {
                            case (14) -> {
                                System.out.println(".");
                                tick = 0;
                                length++;
                            }
                            default -> {
                                System.out.printf(".");
                                tick++;
                                length++;
                            }
                        }
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
                System.out.printf("Station: %s\n", player.getTitle());
                System.out.printf("TrackId: %d\n", player.getTrack().getId());
                System.out.printf("Artist: %s\n", player.getArtist());
                System.out.printf("Song: %s\n", player.getSong());
                System.out.printf("Listen Url: %s", player.getTrack().getListenUrl() + "\n");
                System.out.printf("Share Url: %s", player.getTrack().getShareUrl() + "\n");
                System.out.printf("ImageUrl: %s\n", player.getCover());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
