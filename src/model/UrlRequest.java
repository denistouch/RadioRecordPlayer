package model;

import org.jsoup.Jsoup;

public class UrlRequest {
    public String getContent(String urlString) {
        try {
             return Jsoup.connect(urlString).ignoreContentType(true).execute().body();
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }
}

