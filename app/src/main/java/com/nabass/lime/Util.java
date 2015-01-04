package com.nabass.lime;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.nabass.lime.db.TBLProfile;
import com.nabass.lime.widgets.CircleImageView;
import java.io.InputStream;
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
        Date date = new Date();
        date.setTime(Long.parseLong(datetime));
        String formattedDate=new SimpleDateFormat("MMM d, yyyy").format(date);
        return formattedDate;
    }

    // Set the client's profile picture
    public static void setUserPicFromURL(CircleImageView view){
        Util.LoadProfileImage profileLoader = new Util.LoadProfileImage(view);
        profileLoader.execute(getClientImg());
    }

    // Get unique file name
    public static String getUniqueImageFileName() {
        return "img"+ System.currentTimeMillis() + ".jpg";
    }

    // Change profile picture of the user
    public static void changeUserPic(Uri uri) {
        MainActivity.clientImg.setImageURI(uri);
    }

    public static String getDeviceID() {
        return Settings.Secure.getString(MainActivity.contentResolver, Settings.Secure.ANDROID_ID);
    }

    public static String getDevicePhoneNum() {
        TelephonyManager tMgr = (TelephonyManager) MainActivity.ctx.getSystemService(Context.TELEPHONY_SERVICE);
        return tMgr.getLine1Number();
    }

    public static void updateUserImg() {
        MainActivity.clientImg.setImageBitmap(TBLProfile.getProfileImg(MainActivity.contentResolver));
    }
}
