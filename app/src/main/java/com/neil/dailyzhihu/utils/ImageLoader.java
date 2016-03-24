package com.neil.dailyzhihu.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Neil on 2016/3/22.
 */
public class ImageLoader {


    public interface OnFinishListener {
        void onFinish(Object s);
    }

    public static void loadImage(final ImageView iv, final String imgUrl, final OnFinishListener onFinishListener) {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap bm = null;
                try {
                    bm = BitmapFactory.decodeStream(new URL(imgUrl).openStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return bm;
            }

            @Override
            protected void onPostExecute(Bitmap bm) {
                super.onPostExecute(bm);
                iv.setImageBitmap(bm);
                if (onFinishListener != null)
                    onFinishListener.onFinish(bm);
            }
        }.execute();
    }

    public static void loadString(final String imgUrl, final OnFinishListener onFinishListener) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(imgUrl).build();
                try {
                    Response response = client.newCall(request).execute();
                    if (!response.isSuccessful()) {
                        return null;
                    }
                    return response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                onFinishListener.onFinish(s);
            }
        }.execute();
    }

}
