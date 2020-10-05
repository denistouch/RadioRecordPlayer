package model;

import org.jsoup.Jsoup;

import java.io.IOException;

public class UrlRequest {
    public String getContent(String urlString) {
        try {
             return Jsoup.connect(urlString).ignoreContentType(true).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

