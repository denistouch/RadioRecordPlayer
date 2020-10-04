import model.ComboItem;
import model.Station;
import model.UrlPlayer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MainApp {
    private static UrlPlayer player;
    private static Thread threadPlayer;
    private static Thread threadInfo;

    private static JFrame frame;
    private static JLabel cover;
    private static JComboBox stationJComboBox;

    private static Runnable getInfo;

    public static void main(String[] args) {
        //GUI here
        frame = new JFrame();
        cover = new JLabel();
        stationJComboBox = new JComboBox<>();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(500, 500));
        frame.setResizable(false);

        player = new UrlPlayer("rr", "high");

        //update info from api
        getInfo = () -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    int secToWait = 1000 * 5;
                    int lastId = player.getTrack().getId();
                    String lastURL = player.getUrlString();
                    Thread.currentThread().sleep(secToWait);
                    player.updateInfo();
                    if ((player.getTrack().getId() != lastId) || (!lastURL.equals(player.getUrlString()))) {
                        frame.setTitle(player.getSong() + " - " + player.getArtist());
                        setIcon(player.getStation().getIconFillColored());
                        drawCover(player.getCover());
                    }
                }
            } catch (InterruptedException e) {
                frame.setTitle(player.getSong() + " - " + player.getArtist());
                setIcon(player.getStation().getIconFillColored());
                drawCover(player.getCover());
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }
        };

        //start playing music
        threadPlayer = new Thread(player);
        threadPlayer.start();

        //generate player
        new Thread(() -> {
            try {
                Thread.currentThread().sleep(50);
                frame.setTitle(player.getSong() + " - " + player.getArtist());
                setIcon(player.getStation().getIconFillColored());
                drawCover(player.getCover());
                setStations(player.getStationList());
                setLocation();
                frame.setVisible(true);
            } catch (InterruptedException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }).start();

        //start thread update info about playing music
        threadInfo = new Thread(getInfo);
        threadInfo.start();

//        testAllStation();
    }

    private static void testAllStation() {
        for (Station station : player.getStationList()) {
            change(station.getPrefix(), player.getStream());
        }
    }

    private static void change(String prefix, String stream) {
        Thread testThread = new Thread(() -> {
            player.setPlayer(prefix, stream);
            player.stop();
        });
        testThread.start();
        try {
            testThread.join();
            threadPlayer = new Thread(player);
            threadPlayer.start();
        } catch (InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }

    }

    private static void setIcon(String iconURL) {
        try {
            frame.setIconImage(ImageIO.read(new URL(iconURL)));
        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException: " + e.getLocalizedMessage());
        } catch (IOException e) {
            System.out.println("IOException: " + e.getLocalizedMessage());
        }
    }

    private static void drawCover(String coverUrl) {
        new Thread(() -> {
            frame.getContentPane().remove(cover);
            cover = loadImage(coverUrl);
            frame.getContentPane().add(cover, BorderLayout.CENTER);
            frame.pack();
        }).start();
    }

    private static void setStations(Station[] stations) {
        frame.getContentPane().remove(stationJComboBox);
        for (Station station : stations) {
            stationJComboBox.addItem(new ComboItem(station.getTitle(), station.getPrefix()));
        }
        stationJComboBox.setSelectedIndex(0);
        frame.add(stationJComboBox, BorderLayout.SOUTH);
        stationJComboBox.addActionListener(e -> {
            String prefix = ((ComboItem) stationJComboBox.getSelectedItem()).getPrefix();
            if (!prefix.equals(player.getPrefix())) {
                change(prefix, player.getStream());
                frame.setTitle(player.getSong() + " - " + player.getArtist());
                setIcon(player.getStation().getIconFillColored());
                //wake up threadInfo
                threadInfo.interrupt();
                threadInfo = new Thread(getInfo);
                threadInfo.start();
            }
        });
        frame.pack();
    }

    private static void setLocation() {
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = graphicsDevice.getDisplayMode().getWidth();
        int height = graphicsDevice.getDisplayMode().getHeight();
        frame.setLocation((width - frame.getWidth()) / 2, (height - frame.getHeight()) / 2);
    }

    private static JLabel loadImage(String coverUrl) {
        try {
            URL url = new URL(coverUrl);
            BufferedImage image = ImageIO.read(url);
            Image resultingImage = image.getScaledInstance(frame.getWidth() / 3 * 2, frame.getHeight() / 3 * 2, Image.SCALE_SMOOTH);
            return new JLabel(new ImageIcon(resultingImage));
        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException: " + e.getLocalizedMessage());
            return null;
        } catch (IOException e) {
            System.out.println("IOException: " + e.getLocalizedMessage());
            return null;
        }
    }

}
