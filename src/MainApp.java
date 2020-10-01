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
        UrlPlayer player = new UrlPlayer("chil","high");
        Thread thread = new Thread(player);
        thread.start();

        System.out.println("_____NOW___PLAYING_____");
        System.out.printf("Station: %s",player.getTitle() + "\n");
        System.out.printf("Artist: %s",player.getArtist() + "\n");
        System.out.printf("Song: %s",player.getSong() + "\n");
        System.out.printf("ImageUrl: %s",player.getCover() + "\n");

    }

}
