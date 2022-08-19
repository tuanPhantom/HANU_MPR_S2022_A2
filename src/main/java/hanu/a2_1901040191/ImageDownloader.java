package hanu.a2_1901040191;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
    private final ImageView imageView;

    public ImageDownloader(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        for (String link : strings) {
            return downloadImage(link);
        }
        return null;
    }

    // Interacts with UI
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (bitmap == null) {
            Toast.makeText(imageView.getContext(), "Oops, failed to connect!", Toast.LENGTH_SHORT).show();
        } else {
            imageView.setImageBitmap(bitmap);
        }
    }

    private Bitmap downloadImage(String link) {
        URL url;
        HttpURLConnection connection = null;
        InputStream is = null;
        try {
            url = new URL(link);
            connection = (HttpURLConnection) url.openConnection();
            is = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // close resources
            if (connection != null) {
                connection.disconnect();
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
