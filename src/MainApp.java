import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import model.Station;
import model.UrlPlayer;
import model.UrlRequest;

import java.io.FileReader;


//https://2019.radiorecord.ru/api/stations - json список радиостанций
//https://2019.radiorecord.ru/api/stations/now - json список песен играющих сейчас на разных радиостанциях
//парсить json станций выхватывать id и по id искать в now текущий трек, артиста и его обложку
public class MainApp {
    public static void main(String[] args) {
        test();
        UrlPlayer now = new UrlPlayer();
        now.setStation("chil");
        now.setBitrate("128");
        System.out.printf("Station: %s\nquality: %s\n", now.getStation(), now.getBitrate());
        new Thread(now).start();
        now.getInfo();
        UrlRequest request = new UrlRequest();
        String stations = request.getInfo("https://2019.radiorecord.ru/api/stations");
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(stations);
            String prettyJsonString = gson.toJson(jsonElement);
            System.out.printf("%s",prettyJsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.printf("%s");
    }

    private static void test() {
        String json = "[{\"id\":14723,\"prefix\":\"rr\",\"title\":\"Mainstream \\/ Record\",\"short_title\":\"Mainstream \\/ Record\",\"icon_gray\":\"https:\\/\\/2019.radiorecord.ru\\/upload\\/stations_images\\/rr_image600_gray_outline.png\",\"icon_fill_colored\":\"https:\\/\\/2019.radiorecord.ru\\/upload\\/stations_images\\/rr_image600_colored_fill.png\",\"icon_fill_white\":\"https:\\/\\/2019.radiorecord.ru\\/upload\\/stations_images\\/rr_image600_white_fill.png\",\"new\":false,\"stream_64\":\"https:\\/\\/air2.radiorecord.ru:9001\\/rr_aac_64\",\"stream_128\":\"https:\\/\\/air2.radiorecord.ru:9002\\/rr_128\",\"stream_320\":\"https:\\/\\/air2.radiorecord.ru:9003\\/rr_320\",\"genre\":[{\"id\":279,\"name\":\"TOP\\/HITS\"},{\"id\":283,\"name\":\"POP\"},{\"id\":286,\"name\":\"RUSSIAN\"},{\"id\":290,\"name\":\"ALL\"}],\"detail_page_url\":\"\\/channels\\/rr\\/\",\"shareUrl\":\"https:\\/\\/2019.radiorecord.ru\\/channels\\/rr\\/\"},{\"id\":537,\"prefix\":\"rus\",\"title\":\"Russian Mix\",\"short_title\":\"Russian Mix\",\"icon_gray\":\"https:\\/\\/2019.radiorecord.ru\\/upload\\/stations_images\\/rus_image600_gray_outline.png\",\"icon_fill_colored\":\"https:\\/\\/2019.radiorecord.ru\\/upload\\/stations_images\\/rus_image600_colored_fill.png\",\"icon_fill_white\":\"https:\\/\\/2019.radiorecord.ru\\/upload\\/stations_images\\/rus_image600_white_fill.png\",\"new\":false,\"stream_64\":\"https:\\/\\/air.radiorecord.ru:805\\/rus_aac_64\",\"stream_128\":\"https:\\/\\/air.radiorecord.ru:805\\/rus_128\",\"stream_320\":\"https:\\/\\/air.radiorecord.ru:805\\/rus_320\",\"genre\":[{\"id\":278,\"name\":\"EDM\\/DANCE\"},{\"id\":283,\"name\":\"POP\"},{\"id\":286,\"name\":\"RUSSIAN\"},{\"id\":290,\"name\":\"ALL\"}],\"detail_page_url\":\"\\/channels\\/rus\\/\",\"shareUrl\":\"https:\\/\\/2019.radiorecord.ru\\/channels\\/rus\\/\"}]";
        Gson gson = new Gson();
        Station[] stations = gson.fromJson(json,Station[].class);
        //System.out.println(stations.toString());
        for (Station station : stations)
            System.out.printf("%s\n-----------------------\n",station.toString());
        System.exit(0);
    }
}
