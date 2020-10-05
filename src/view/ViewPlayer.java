package view;

import model.ComboItem;
import model.Station;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ViewPlayer {
    private JFrame frame;
    private JLabel cover;
    private JComboBox stationJComboBox;
    private int contentWidth;
    public String prefix;

    public ViewPlayer() {
        frame = new JFrame();
        cover = new JLabel();
        stationJComboBox = new JComboBox<>();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation();
    }

    public void setIcon(String iconURL) {
        try {
            frame.setIconImage(ImageIO.read(new URL(iconURL)));
        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException: " + e.getLocalizedMessage());
        } catch (IOException e) {
            System.out.println("IOException: " + e.getLocalizedMessage());
        }
    }

    public void setTitle(String title) {
        frame.setTitle(title);
    }

    public void drawCover(String coverUrl) {
        frame.getContentPane().remove(cover);
        cover = loadImage(coverUrl);
        frame.getContentPane().add(cover, BorderLayout.CENTER);
        setLocation();
        frame.pack();
    }

    public void setStations(Station[] stations) {
        frame.getContentPane().remove(stationJComboBox);
        for (Station station : stations) {
            stationJComboBox.addItem(new ComboItem(station.getTitle(), station.getPrefix()));
        }
        frame.add(stationJComboBox, BorderLayout.SOUTH);
        stationJComboBox.addActionListener(e -> prefix = ((ComboItem) stationJComboBox.getSelectedItem()).getPrefix());
        stationJComboBox.setSelectedIndex(0);
        frame.pack();
    }

    private void setLocation() {
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = graphicsDevice.getDisplayMode().getWidth();
        int height = graphicsDevice.getDisplayMode().getHeight();
        frame.setLocation((width - contentWidth) / 2, (height - contentWidth) / 2);
    }

    private JLabel loadImage(String coverUrl) {
        try {
            URL url = new URL(coverUrl);
            BufferedImage image = ImageIO.read(url);
            contentWidth = image.getWidth();
            return new JLabel(new ImageIcon(image));
        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException: " + e.getLocalizedMessage());
            return null;
        } catch (IOException e) {
            System.out.println("IOException: " + e.getLocalizedMessage());
            return null;
        }
    }

    public void show() {
        frame.setVisible(true);
    }
}
