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
            case ("rr"):
                this.station = station;
                break;
            case ("deep"):
                this.station = station;
                break;
            case ("chil"):
                this.station = station;
                break;
            case ("mix"):
                this.station = station;
                break;
            case ("bighits"):
                this.station = station;
                break;
            case ("tm"):
                this.station = station;
                break;
            case ("gold"):
                this.station = station;
                break;
            case ("rmx"):
                this.station = station;
                break;
            case ("jackin"):
                this.station = station;
                break;
            case ("trancehits"):
                this.station = station;
                break;
            case ("ps"):
                this.station = station;
                break;
            case ("mini"):
                this.station = station;
                break;
            case ("drumhits"):
                this.station = station;
                break;
            case ("brks"):
                this.station = station;
                break;
            case ("rap"):
                this.station = station;
                break;
            case ("dream"):
                this.station = station;
                break;
            case ("uplift"):
                this.station = station;
                break;
            case ("neurofunk"):
                this.station = station;
                break;
            case ("dub"):
                this.station = station;
                break;
            case ("pump"):
                this.station = station;
                break;
            case ("darkside"):
                this.station = station;
                break;
            case ("mmbt"):
                this.station = station;
                break;
            case ("jungle"):
                this.station = station;
                break;
            case ("rock"):
                this.station = station;
                break;
            case ("mdl"):
                this.station = station;
                break;
            default:
                this.station = "rr";
                break;

        }
    }

    public void setBitrate(String bitrate) {
        switch (bitrate) {
            case "320":
                this.bitrate = bitrate;
                break;
            case "64":
                this.bitrate = bitrate;
                break;
            default:
                this.bitrate = "128";
                break;
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
            System.out.printf(e.toString());
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
            //Document document = Jsoup.connect(urlString).timeout(10000).get();
            ParsePage("ru");
            Document document = response.parse();
            System.out.printf("Status Code: %d\n", getSitemapStatus());
            if (getSitemapStatus() == 200) {
                Element body = document.body();
                Element headerTitle = body.selectFirst("h1");
                System.out.printf("Station name: %s\n", headerTitle.text());
                System.out.printf("Title: %s\nAuthor: %s\n", body.selectFirst("div.track-title").text(), body.selectFirst("div.track-author").text());
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
        int statusCode = response.statusCode();
        return statusCode;
    }


}
