package cs65.confuse;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by LukeHudspeth on 10/28/17.
 */

public class BitmapGet {
    //From Stack Overflow: https://stackoverflow.com/questions/8992964/android-load-from-url-to-bitmap


    public static Bitmap getBitmapFromURL(String src) {



        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
