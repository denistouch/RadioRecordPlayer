import model.*;

import java.awt.Container;
import java.awt.EventQueue;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class MainApp {
    public static void main(String[] args) {
        UrlPlayer player = new UrlPlayer("chil", "normal");

        //update info from api
        Runnable runnable = () -> {
            try {
                while (true) {
                    int secToWait = 1000 * 10;
                    int lastId = player.getTrack().getId();
                    Thread.currentThread().sleep(secToWait);
                    player.updateInfo();
                    // if track changed view changes
                    if (player.getTrack().getId() != lastId ) {
                        System.out.println("\n_____NOW___PLAYING_____");
                        System.out.printf("Station: %s", player.getTitle() + "\n");
                        System.out.printf("Artist: %s", player.getArtist() + "\n");
                        System.out.printf("Song: %s", player.getSong() + "\n");
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
                System.out.printf("ImageUrl: %s", player.getCover() + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
