package model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javazoom.jl.player.Player;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class UrlPlayer implements Runnable {
    private String prefix;
    private String stream;
    private int id;
    private String title;
    private String urlString;
    private Station station;
    private Track track;
    private Connection.Response response = null;

    public UrlPlayer(String prefix, String stream) {
        this.prefix = prefix;
        this.stream = stream;
        getInfo();
    }

    public String getTitle() {
        return station.getTitle();
    }

    public String getArtist() {
        return track.getArtist();
    }

    public String getSong() {
        return track.getSong();
    }

    public String getCover() {
        if (stream.equals("high")) {
            return track.getImage600();
        } else {
            return track.getImage200();
        }
    }

    public Station getStation() {
        return station;
    }

    public Track getTrack() {
        return track;
    }

    @Override
    public void run() {
        try {
            if (this.prefix == null || this.prefix.equals(""))
                throw new Exception("prefix not initialized");
            if (this.stream == null || this.stream.equals(""))
                throw new Exception("stream not initialized");
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

    public void updateInfo() {
        UrlRequest request = new UrlRequest();
        Gson gson = new Gson();
        String json = null;
        JsonObject jsonObject = null;
        JsonElement jsonElement = null;

        json = request.getContent("https://2019.radiorecord.ru/api/stations/now/");
        jsonObject = gson.fromJson(json, JsonObject.class);
        jsonElement = jsonObject.get("result");
        json = gson.toJson(jsonElement);
        Now[] nows = gson.fromJson(json, Now[].class);
        for (Now now : nows) {
            if (now.getId() == this.id) {
                this.track = now.getTrack();
            }
        }
    }

    private void getInfo() {
        UrlRequest request = new UrlRequest();
        Gson gson = new Gson();
        String json = null;
        JsonObject jsonObject = null;
        JsonElement jsonElement = null;

        json = request.getContent("https://2019.radiorecord.ru/api/stations/");
        jsonObject = gson.fromJson(json, JsonObject.class);
        jsonElement = jsonObject.get("result").getAsJsonObject().get("stations");
        json = gson.toJson(jsonElement);
        Station[] stations = gson.fromJson(json, Station[].class);
        for (Station station : stations) {
            //System.out.println(station.getTitle() + " : " + station.getPrefix());// get (title : prefix) list for all station
            if (station.getPrefix().equals(prefix)) {
                this.station = station;
                this.id = station.getId();
                this.title = station.getTitle();
                switch (stream) {
                    //case ("low") -> this.urlString = station.getStream64();
                    case ("high") -> this.urlString = station.getStream320();
                    default -> this.urlString = station.getStream128();
                }
            }
        }

        updateInfo();
    }

    public String getPrefix() {
        return prefix;
    }

    public String getStream() {
        return stream;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    private void ParsePage(String langLocale) {
        try {
            String urlString = "https://2019.radiorecord.ru/channels/" + prefix + "/";
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
