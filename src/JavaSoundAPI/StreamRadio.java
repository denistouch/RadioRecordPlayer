package JavaSoundAPI;

import javazoom.jl.player.*;

import java.io.BufferedInputStream;
import java.io.Console;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

public class StreamRadio
{
    public static void main(String[] args) {
        // Европа-Плюс
        String urlString = "https://air.radiorecord.ru:805/gold_320";
        //Console con = System.console();
        //String urlString = con.readLine("Введите url радио потока: ");
        try {
            URL url = new URL(urlString);
            InputStream fin = url.openStream();
            InputStream is = new BufferedInputStream(fin);

            Player player;
            player = new Player(is);
            player.play();
        }
        catch (FileNotFoundException e)
        {
            System.out.printf("Url %s не найден:", urlString);
        }
        catch (Exception e)
        {
            System.out.printf("При проигрывании с потока %s возникла следующая ошибка:", urlString);
            System.out.printf(e.toString());
        }
    }
}