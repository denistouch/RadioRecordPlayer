import model.ComboItem;
import model.Station;
import model.UrlPlayer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
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
        player = new UrlPlayer("rr", "low");
        initGUI();
        //update info from api
        getInfo = () -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    int secToWait = 1000 * 5;
                    int lastId = player.getTrack().getId();
                    String lastURL = player.getUrlString();
                    Thread.currentThread().sleep(secToWait);
                    player.getInfo();
                    if ((player.getTrack().getId() != lastId) || (!lastURL.equals(player.getUrlString()))) {
                        frame.setTitle(player.getSong() + " - " + player.getArtist());
                        setIcon();
                        drawCover(player.getCover());
                    }
                }
            } catch (InterruptedException e) {
                frame.setTitle(player.getSong() + " - " + player.getArtist());
                setIcon();
                drawCover(player.getCover());
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        //start playing music
        threadPlayer = new Thread(player);
        threadPlayer.start();

        //start thread update info about playing music
        threadInfo = new Thread(getInfo);
        threadInfo.start();
    }

    private static void testAllStation() {
        for (Station station : player.getStationList()) {
            change(station.getPrefix(), player.getStream());
        }
    }

    private static void change(String prefix, String stream) {
        Thread testThread = new Thread(() -> {
            player.setPlayer(prefix, stream);
            try {
                threadPlayer.stop();
                player.stop();
                threadPlayer.join();
            } catch (InterruptedException e) {
                System.out.println(e.getLocalizedMessage());
            }

        });
        testThread.start();
        try {
            testThread.join();
            threadPlayer = new Thread(player);
            threadPlayer.start();
            threadInfo.interrupt();
        } catch (InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }

    }

    private static void setIcon() {
        try {
            Image image = ImageIO.read(new URL(player.getStation().getIconFillColored()));
            frame.setIconImage(image);
//            BufferedImage image = ImageIO.read(new URL(player.getStation().getIconFillColored()));
//            Graphics2D graphics2D = image.createGraphics();
//            graphics2D.setPaint(Color.DARK_GRAY);
//            graphics2D.fill(new Ellipse2D.Double(0,0,image.getHeight(),image.getWidth()));
//            graphics2D.drawImage(ImageIO.read(new URL(player.getStation().getIconFillColored())),0,0,null);
//            frame.setIconImage(image);
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
                //wake up threadInfo
                threadInfo.interrupt();
                threadInfo = new Thread(getInfo);
                threadInfo.start();
            }
        });
        stationJComboBox.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    System.exit(0);
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        frame.pack();
    }

    private static void setLocation() {
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = graphicsDevice.getDisplayMode().getWidth();
        int height = graphicsDevice.getDisplayMode().getHeight();
        int k = 2;
        frame.setLocation(width - frame.getWidth() + 5, height - frame.getHeight() - 30);
//        frame.setLocation(1105, 365);
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

    private static void initGUI() {
        frame = new JFrame();
        cover = new JLabel();
        stationJComboBox = new JComboBox<>();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(500, 500));
        frame.setResizable(false);
        frame.getContentPane().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    try {
                        Desktop.getDesktop().browse(new URI(player.getTrack().getShareUrl()));
                    } catch (Exception exception) {
                        System.out.println(exception.getLocalizedMessage());
                    }

                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }


        });

        new Thread(() -> {
            try {
                Thread.currentThread().sleep(50);
                frame.setTitle(player.getSong() + " - " + player.getArtist());
                setIcon();
                drawCover(player.getCover());
                setStations(player.getStationList());
                setLocation();
                frame.setVisible(true);
                System.out.println(player.toString());
            } catch (InterruptedException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }).start();
    }

}
