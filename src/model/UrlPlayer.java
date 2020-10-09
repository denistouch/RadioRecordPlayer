package model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
import org.jsoup.Connection;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

public class UrlPlayer implements Runnable {
    private String prefix;
    private String stream;
    private int id;
    private String urlString;
    private Station station;
    private Track track;
    private Connection.Response response = null;
    private AdvancedPlayer player;
    private boolean played;

    private UrlRequest request = new UrlRequest();
    private Gson gson = new Gson();
    private String json = null;
    private JsonObject jsonObject = null;
    private JsonElement jsonElement = null;

    public String getUrlString() {
        return urlString;
    }

    public UrlPlayer(String prefix, String stream) {
        this.prefix = prefix;
        this.stream = stream;
        getInfo();
    }

    public void setPlayer(String prefix, String stream) {
        this.prefix = prefix;
        this.stream = stream;
        getInfo();
    }

    public String getArtist() {
        return track.getArtist();
    }

    public String getSong() {
        return track.getSong();
    }

    public String getCover() {
//        if (stream.toLowerCase().equals("high")) {
        return track.getImage600();
//        } else {
//            return track.getImage200();
//        }
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
            player = new AdvancedPlayer(is);
            player.setPlayBackListener(new PlaybackListener() {
                @Override
                public void playbackFinished(PlaybackEvent event) {
                    player.close();
                    super.playbackFinished(event);
                    Thread.currentThread().interrupt();
                }
            });
            player.play();
            Thread.currentThread().interrupt();
        } catch (FileNotFoundException e) {
            System.out.printf("Url %s не найден:", urlString);
        } catch (Exception e) {
            System.out.printf("При проигрывании с потока %s возникла следующая ошибка:\n", urlString);
            System.out.println(e.toString());
        }
    }

    @Override
    public String toString() {
        return "\n_____NOW___PLAYING_____\n" +
                "Station: " + station.getTitle() + "\n" +
                "Stream: " + urlString + "\n" +
                "TrackId: " + track.getId() + "\n" +
                "Artist: " + track.getArtist() + "\n" +
                "Song: " + track.getSong() + "\n" +
                "ShareUrl: " + track.getShareUrl() + "\n" +
                "ImageUrl: " + getCover();
    }

    public void stop() {
        player.close();
    }

    public void getInfo() {
        json = request.getContent("https://2019.radiorecord.ru/api/stations/");
        jsonObject = gson.fromJson(json, JsonObject.class);
        jsonElement = jsonObject.get("result").getAsJsonObject().get("stations");
        json = gson.toJson(jsonElement);
        Station[] stations = gson.fromJson(json, Station[].class);
        for (Station station : stations) {
            if (station.getPrefix().equals(prefix)) {
                this.station = station;
                this.id = station.getId();
                if (stream.toLowerCase().equals("high")) {
                    this.urlString = station.getStream320();
                } else {
                    this.urlString = station.getStream128();
                }
            }
        }

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

    public Station[] getStationList() {
        json = request.getContent("https://2019.radiorecord.ru/api/stations/");
        jsonObject = gson.fromJson(json, JsonObject.class);
        jsonElement = jsonObject.get("result").getAsJsonObject().get("stations");
        json = gson.toJson(jsonElement);

        return gson.fromJson(json, Station[].class);
    }

    public String getPrefix() {
        return prefix;
    }

    public String getStream() {
        return stream;
    }

    public int getSitemapStatus() {
        return response.statusCode();
    }


}
