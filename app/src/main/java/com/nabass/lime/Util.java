package com.nabass.lime;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.CursorAdapter;

import com.nabass.lime.widgets.CircleImageView;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.nabass.lime.Init.getClientImg;

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

    public static String getTime(String datetime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat[] df = new DateFormat[] { DateFormat.getDateInstance(), DateFormat.getTimeInstance()};
        Date now = new Date();

        try {
            Date dt = sdf.parse(datetime);
            if (now.getYear()==dt.getYear() && now.getMonth()==dt.getMonth() && now.getDate()==dt.getDate()) {
                return df[1].format(dt);
            }
            return df[0].format(dt);
        } catch (ParseException e) {
            return datetime;
        }
    }

    // Set the client's profile picture
    public static void setClientPic(CircleImageView view){
        Util.LoadProfileImage profileLoader = new Util.LoadProfileImage(view);
        profileLoader.execute(getClientImg());
    }
}
