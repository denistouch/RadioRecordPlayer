import com.google.gson.*;
import model.*;

import java.io.FileReader;


//https://2019.radiorecord.ru/api/stations - json список радиостанций
//https://2019.radiorecord.ru/api/stations/now - json список песен играющих сейчас на разных радиостанциях
//парсить json станций выхватывать id и по id искать в now текущий трек, артиста и его обложку
public class MainApp {
    public static void main(String[] args) {
        UrlPlayer player = new UrlPlayer();
        player.setStation("chil");
        player.setBitrate("128");
        new Thread(player).start();
        test(534);
    }

    //тащим из API текущую песню и инфу о радиостанции
    private static void test(int id) {
        UrlRequest request = new UrlRequest();
        Gson gson = new Gson();

        String json = null;
        JsonObject jsonObject = null;
        JsonElement jsonElement = null;
        json = request.getInfo("https://2019.radiorecord.ru/api/stations/now/");
        jsonObject = gson.fromJson(json, JsonObject.class);
        jsonElement = jsonObject.get("result");
        json = gson.toJson(jsonElement);
        //System.out.println(json);
        Now[] nows = gson.fromJson(json,Now[].class);
        for (Now now : nows) {
            if (now.getId() == id)
                System.out.println(now.toString());
        }
        json = request.getInfo("https://2019.radiorecord.ru/api/stations/");
        jsonObject = gson.fromJson(json, JsonObject.class);
        jsonElement = jsonObject.get("result").getAsJsonObject().get("stations");
        json = gson.toJson(jsonElement);
//        System.out.println(json);
        Station[] stations = gson.fromJson(json,Station[].class);
        for (Station station : stations) {
            if (station.getId() == id) {
                System.out.println(station.toString());
            }
        }
        //System.exit(0);
    }
}
