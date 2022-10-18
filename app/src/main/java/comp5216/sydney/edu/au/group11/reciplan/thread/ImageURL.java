package comp5216.sydney.edu.au.group11.reciplan.thread;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageURL {
    public static void requestImg(Handler handler,String path) {
        new Thread(() -> {
            Bitmap bitmap = getImage(path);
            Message message = new Message();
            message.obj = bitmap;
            handler.sendMessage(message);
        }).start();
    }

    private static Bitmap getImage(String path) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.connect();
            if(connection.getResponseCode() == 200) {
                InputStream in = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(in);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
