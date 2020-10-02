import model.Track;
import model.UrlPlayer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public class MainApp {
    private static JFrame f = new JFrame();

    public static void main(String[] args) throws InterruptedException {
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = graphicsDevice.getDisplayMode().getWidth();
        int height = graphicsDevice.getDisplayMode().getHeight();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocation(width/2-300, height/2-300);
        f.setVisible(true);


        UrlPlayer player = new UrlPlayer("bighits", "high");
        boolean test = false;
        if (test) {
            player.getStationList();
            return;
        }

        //update info from api
        Runnable runnable = () -> {
            try {
                Thread.currentThread().sleep(1000);
                System.out.println(player.toString());
                drawCover(player.getCover());
                while (!Thread.currentThread().isInterrupted()) {
                    int secToWait = 1000 * 5;
                    int lastId = player.getTrack().getId();
                    Thread.currentThread().sleep(secToWait);
                    player.updateInfo();
                    if (player.getTrack().getId() != lastId) {
                        System.out.println(player.toString());
                        drawCover(player.getCover());
                    } else {
                        System.out.print(".");
                    }
                }
            } catch (InterruptedException e) {
                System.out.println(e.getLocalizedMessage());
            }
        };

        //start thread update info about playing music
        Thread threadInfo = new Thread(runnable);
        threadInfo.start();

        //start playing music
        Thread threadPlayer = new Thread(player);
        threadPlayer.start();


        //change station - rewrite in player
//        Thread testThread = new Thread(() -> {
//            try {
//                Thread.currentThread().sleep(10000);
//                player.setPrefix("bighits");
//                player.setStream("low");
//                player.stop();
//            } catch (InterruptedException e) {
//                System.out.println("Thread is interrupted");
//            }
//        });
//        testThread.start();
//        testThread.join();
//        threadPlayer = new Thread(player);
//        threadPlayer.start();

    }

    public static void drawCover(String coverUrl) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        }
        try {
            URL url = new URL(coverUrl);
            BufferedImage image = ImageIO.read(url);
            JLabel label = new JLabel(new ImageIcon(image));
            if (f.getContentPane().getComponentCount() != 0)
                f.getContentPane().remove(0);
            f.getContentPane().add(label);
            f.pack();
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

}
