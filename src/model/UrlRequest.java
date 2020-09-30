package model;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class UrlRequest {
    public String getInfo(String urlString) {
        try {
             return Jsoup.connect(urlString).ignoreContentType(true).execute().body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

