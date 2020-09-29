import model.UrlPlayer;
//https://2019.radiorecord.ru/api/stations - json список радиостанций
//https://2019.radiorecord.ru/api/stations/now - json список песен играющих сейчас на разных радиостанциях
//парсить json станций выхватывать id и по id искать в now текущий трек, артиста и его обложку
public class MainApp {
    public static void main(String[] args) {
        UrlPlayer now =  new UrlPlayer();
        now.setStation("chil");
        now.setBitrate("320");
        System.out.printf("Station: %s\nquality: %s\n",now.getStation(),now.getBitrate());
        new Thread(now).start();
        now.getInfo();

    }
}
