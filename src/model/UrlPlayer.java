package model;

import javazoom.jl.player.Player;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class UrlPlayer implements Runnable {
    private String station = "gold";
    private String bitrate = "128";
    Connection.Response response = null;

    //not all station
    public void setStation(String station) {
        switch (station) {
            case ("rr") -> this.station = station;
            case ("deep") -> this.station = station;
            case ("chil") -> this.station = station;
            case ("mix") -> this.station = station;
            case ("bighits") -> this.station = station;
            case ("tm") -> this.station = station;
            case ("gold") -> this.station = station;
            case ("rmx") -> this.station = station;
            case ("jackin") -> this.station = station;
            case ("trancehits") -> this.station = station;
            case ("ps") -> this.station = station;
            case ("mini") -> this.station = station;
            case ("drumhits") -> this.station = station;
            case ("brks") -> this.station = station;
            case ("rap") -> this.station = station;
            case ("dream") -> this.station = station;
            case ("uplift") -> this.station = station;
            case ("neurofunk") -> this.station = station;
            case ("dub") -> this.station = station;
            case ("pump") -> this.station = station;
            case ("darkside") -> this.station = station;
            case ("mmbt") -> this.station = station;
            case ("jungle") -> this.station = station;
            case ("rock") -> this.station = station;
            case ("mdl") -> this.station = station;
            default -> this.station = "rr";
        }
    }

    public void setBitrate(String bitrate) {
        switch (bitrate) {
            case "320" -> this.bitrate = bitrate;
            case "64" -> this.bitrate = bitrate;
            default -> this.bitrate = "128";
        }
    }

    @Override
    public void run() {
        String urlString = "https://air.radiorecord.ru:805/" + station + "_" + bitrate;
        try {
            URL url = new URL(urlString);
            InputStream fin = url.openStream();
            InputStream is = new BufferedInputStream(fin);

            Player player;
            player = new Player(is);
            player.play();
        } catch (FileNotFoundException e) {
            System.out.printf("Url %s не найден:", urlString);
        } catch (Exception e) {
            System.out.printf("При проигрывании с потока %s возникла следующая ошибка:", urlString);
            System.out.println(e.toString());
        }
    }

    public String getStation() {
        return station;
    }

    public String getBitrate() {
        return bitrate;
    }

    public void getInfo() {
        //log("Hello World!");
        String urlString = "https://2019.radiorecord.ru/channels/" + station + "/";
        //String query = "хлеб";
        //urlString += "&q=" + query;
        try {
            ParsePage("ru");
            Document document = response.parse();
            System.out.printf("Status Code: %d\n", getSitemapStatus());
            if (getSitemapStatus() == 200) {
                Element body = document.body();
                Element headerTitle = body.selectFirst("h1");
                //System.out.printf("Station name: %s\n", headerTitle.text());
                //System.out.printf("Title: %s\nAuthor: %s\n", body.selectFirst("div.track-title").text(), body.selectFirst("div.track-author").text());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ParsePage(String langLocale) {
        try {
            String urlString = "https://2019.radiorecord.ru/channels/" + station + "/";
            response = Jsoup.connect(urlString)
                    .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
                    .timeout(10000)
                    .execute();
        } catch (IOException e) {
            System.out.println("io - " + e);
        }
    }

    public int getSitemapStatus() {
        return response.statusCode();
    }


}
