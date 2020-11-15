import model.ComboItem;
import model.Station;
import model.UrlPlayer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class MainApp {
    private static UrlPlayer player;
    private static Thread threadPlayer;
    private static Thread threadInfo;

    private static JFrame frame;
    private static JLabel cover;
    private static JComboBox stationJComboBox;

    private static Runnable getInfo;

    public static void main(String[] args) {
        player = new UrlPlayer("rr", "high");
        initGUI();

        //update info from api runnable;
        getInfo = () -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    int secToWait = 1000 * 5;
                    int lastId = player.getTrack().getId();
                    String lastURL = player.getUrlString();
                    Thread.currentThread().sleep(secToWait);
                    new Thread(() -> player.getInfo()).start();
                    if ((player.getTrack().getId() != lastId) || (!lastURL.equals(player.getUrlString()))) {
                        new Thread(() -> frame.setTitle(player.getSong() + " - " + player.getArtist())).start();
                        new Thread(() -> setIcon()).start();
                        new Thread(() -> drawCover(player.getCover())).start();
                    }
                }
            } catch (InterruptedException e) {
                new Thread(() -> frame.setTitle(player.getSong() + " - " + player.getArtist())).start();
                new Thread(() -> setIcon()).start();
                new Thread(() -> drawCover(player.getCover())).start();
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

//    private static void testAllStation() {
//        for (Station station : player.getStationList()) {
//            change(station.getPrefix(), player.getStream());
//        }
//    }

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
        if (!prefix.equals("")) {
            try {
                testThread.join();
                threadPlayer = new Thread(player);
                threadPlayer.start();
                threadInfo.interrupt();
            } catch (InterruptedException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
    }

    private static void setIcon() {
        try {
            Image image = ImageIO.read(new URL(player.getStation().getIconFillColored()));
            frame.setIconImage(image);
            //придумать как красиво рисовать значок
//            BufferedImage image = ImageIO.read(new URL(player.getStation().getIconFillWhite()));
//            Graphics2D graphics2D = image.createGraphics();
//            graphics2D.setPaint(new Color(14,14,14));
//            graphics2D.fill(new Ellipse2D.Double(0, 0, image.getHeight(), image.getWidth()));
//            graphics2D.drawImage(ImageIO.read(new URL(player.getStation().getIconFillWhite())), 0, 0, null);
//            frame.setIconImage(image);
        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException: " + e.getLocalizedMessage());
        } catch (IOException e) {
            System.out.println("IOException: " + e.getLocalizedMessage());
        }
    }

    private static void drawCover(String coverUrl) {
//        new Thread(() -> {
//
//        }).start();
        frame.getContentPane().removeAll();
        cover = loadImage(coverUrl);
        frame.getContentPane().add(cover, BorderLayout.CENTER);
        frame.getContentPane().add(stationJComboBox, BorderLayout.SOUTH);
        stationJComboBox.requestFocus();
        frame.pack();
//        System.out.printf("Component count : %d\n",frame.getContentPane().getComponentCount());
    }

    private static JLabel loadImage(String coverUrl) {
        try {
            URL url = new URL(coverUrl);
            BufferedImage image = ImageIO.read(url);
            Image resultingImage = image.getScaledInstance(frame.getWidth() / 5 * 4, frame.getHeight() / 5 * 4, Image.SCALE_SMOOTH);
            if (SystemTray.isSupported()) {
//                displayTray();
//                System.out.println(player.toString());
            } else {
                System.err.println("System tray not supported!");
            }
            return new JLabel(new ImageIcon(resultingImage));
        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException: " + e.getLocalizedMessage());
            return null;
        } catch (IOException e) {
            System.out.println("IOException: " + e.getLocalizedMessage());
            return null;
        } catch (Exception e) {
            System.out.println();
            return null;
        }
    }

    private static void setStations(Station[] stations) {
        frame.getContentPane().remove(stationJComboBox);
        for (Station station : stations) {
            stationJComboBox.addItem(new ComboItem(station.getTitle(), station.getPrefix()));
        }
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
        stationJComboBox.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    System.exit(0);
            }
        });
        frame.pack();
        stationJComboBox.setSelectedIndex(0);
    }

    private static void setLocation() {
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = graphicsDevice.getDisplayMode().getWidth();
        int height = graphicsDevice.getDisplayMode().getHeight();
        int k = 2;
        frame.setLocation(width - frame.getWidth() + 5, height - frame.getHeight() - 30);
//        frame.setLocation(1105, 365);
    }


    private static void initGUI() {
        frame = new JFrame();
        cover = new JLabel();
        stationJComboBox = new JComboBox<>();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(500, 500));
        frame.setResizable(false);
        stationJComboBox.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
//                catchMediaButton(e.paramString());
                if (catchMediaButton(e.paramString()) == 179)
                    playPause();
                else if (catchMediaButton(e.paramString()) == 176)
                    System.out.println("next");
                else if (catchMediaButton(e.paramString()) == 177)
                    System.out.println("previous");
//                System.out.println(e.getKeyCode());
            }
        });
        frame.getContentPane().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (e.getClickCount() == 2) {
                        try {
                            Desktop.getDesktop().browse(new URI(player.getTrack().getShareUrl()));
                        } catch (Exception exception) {
                            System.out.println(exception.getLocalizedMessage());
                        }
                    } else if (e.getClickCount() == 1) {
                        //action play pause
                        playPause();
                    }
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    //open browser search track vk
                    String url = "https://vk.com/audio?q=" + URLEncoder.encode(player.getTrack().getSong() + " " + player.getTrack().getArtist(), StandardCharsets.UTF_8);
                    try {
                        Desktop.getDesktop().browse(new URI(url));
                    } catch (Exception exception) {
                        System.out.println(exception.getLocalizedMessage());
                    }
                }
            }
        });
//        frame.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
//                    setLocation();
//                }
//            }
//        });
        frame.setVisible(true);
        new Thread(() -> frame.setTitle(player.getSong() + " - " + player.getArtist())).start();
        new Thread(() -> setIcon()).start();
        new Thread(() -> drawCover(player.getCover())).start();
        new Thread(() -> setStations(player.getStationList())).start();
        new Thread(() -> setLocation()).start();
    }

    private static void displayTray() throws AWTException {
        //Obtain only one instance of the SystemTray object
        SystemTray tray = SystemTray.getSystemTray();
        //If the icon is a file
        Image image = frame.getIconImage();
        PopupMenu popupMenu = new PopupMenu();
        popupMenu.addSeparator();
        TrayIcon trayIcon = new TrayIcon(image, player.getStation().getTitle(), popupMenu);
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip(frame.getTitle());
        for (TrayIcon icon : tray.getTrayIcons()) {
            if (icon.equals(trayIcon))
                tray.remove(icon);
        }
        tray.add(trayIcon);
        trayIcon.displayMessage(player.getTrack().getSong(), player.getTrack().getArtist(), TrayIcon.MessageType.NONE);
    }

    private static int catchMediaButton(String paramString) {
        String s1 = paramString.substring(paramString.indexOf("rawCode"));
        String s2 = s1.substring(s1.indexOf("=") + 1,s1.indexOf(","));
//        System.out.println(s2);
        return Integer.valueOf(s2);
    }

    private static void playPause() {
        if (player.getPrefix().equals(""))
            change(stationJComboBox.getSelectedItem().toString(), player.getStream());
        else
            change("", player.getStream());
    }

}
