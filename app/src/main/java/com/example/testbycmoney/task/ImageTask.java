package com.example.testbycmoney.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.example.testbycmoney.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageTask extends AsyncTask<Object, Integer, Bitmap> {
    private final static String TAG = "ImageTask";
    private String url;
    private int imageSize;
    private WeakReference<ImageView> imageViewWeakReference;


    public ImageTask(String url, int imageSize, ImageView imageView) {
        this.url = url;
        this.imageSize = imageSize;
        this.imageViewWeakReference = new WeakReference<>(imageView);
    }

    public ImageTask(String url, ImageView imageView) {
        this.url = url;
        this.imageViewWeakReference = new WeakReference<>(imageView);
    }

    @Override
    protected Bitmap doInBackground(Object... objects) {
        return getRemoteImage(url);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        ImageView imageView = imageViewWeakReference.get();
        if (isCancelled() || imageView == null) {
            return;
        }
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.noimages);
        }
    }
    private Bitmap getRemoteImage(String url) {
        HttpURLConnection connection = null;
        Bitmap bitmap = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setUseCaches(false);
            Log.d(TAG, "URL: " + url);
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                bitmap = BitmapFactory.decodeStream(new BufferedInputStream(connection.getInputStream()));
            } else {
                Log.d(TAG, "response code: " + responseCode);
            }
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return bitmap;
    }
}

