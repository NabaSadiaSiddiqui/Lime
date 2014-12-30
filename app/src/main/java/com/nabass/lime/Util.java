package com.nabass.lime;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.nabass.lime.widgets.CircleImageView;

import java.io.InputStream;

public class Util {

    /**
     * Background Async task to load user profile picture from url
     * */
    public static class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        CircleImageView bmImage;

        public LoadProfileImage(CircleImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
