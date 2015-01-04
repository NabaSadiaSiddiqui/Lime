package com.nabass.lime.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;

import com.nabass.lime.Constants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TBLProfile {

    private static final String TAG = "TBLProfile";

    public TBLProfile() {
        // Empty Constructor
    }

    public static void createProfileInAuthMode(ContentResolver cr, String name, String email, String phone_num, String pin, String status, String deviceID) {
        ContentValues values = new ContentValues(6);
        values.put(DBConstants.TBL_PROFILE_COLS.COL_NAME, name);
        values.put(DBConstants.TBL_PROFILE_COLS.COL_EMAIL, email);
        values.put(DBConstants.TBL_PROFILE_COLS.COL_PHONE, phone_num);
        values.put(DBConstants.TBL_PROFILE_COLS.COL_STATUS, status);
        values.put(DBConstants.TBL_PROFILE_COLS.COL_PIN, pin);
        values.put(DBConstants.TBL_PROFILE_COLS.COL_DEVICE_ID, deviceID);
        cr.insert(DBConstants.DB_PROFILE, values);
    }

    public static void createProfileInLocalMode(ContentResolver cr, String name, String email, String phone_num, String deviceID) {
        ContentValues values = new ContentValues(5);
        values.put(DBConstants.TBL_PROFILE_COLS.COL_NAME, name);
        values.put(DBConstants.TBL_PROFILE_COLS.COL_EMAIL, email);
        values.put(DBConstants.TBL_PROFILE_COLS.COL_PHONE, phone_num);
        values.put(DBConstants.TBL_PROFILE_COLS.COL_STATUS, Constants.STR_NULL);
        values.put(DBConstants.TBL_PROFILE_COLS.COL_PIN, Constants.STR_NULL);
        values.put(DBConstants.TBL_PROFILE_COLS.COL_DEVICE_ID, deviceID);
        cr.insert(DBConstants.DB_PROFILE, values);
    }

    public static void updateProfileStatus(ContentResolver cr, String status) {
        //TODO: use another selection mechanism when switching from Google+
        String selection = DBConstants.TBL_PROFILE_COLS.COL_EMAIL + " = ?";
        String[] selectionArgs = new String[] {getProfileEmail(cr)};

        ContentValues values = new ContentValues(1);
        values.put(DBConstants.TBL_PROFILE_COLS.COL_STATUS, status);

        cr.update(DBConstants.DB_PROFILE, values, selection, selectionArgs);
    }

    public static void updateProfileImg(ContentResolver cr, Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(cr, uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream buffer = new ByteArrayOutputStream(bitmap.getWidth() * bitmap.getHeight());
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, buffer);
        byte[] imgByte = buffer.toByteArray();

        //TODO: use another selection mechanism when switching from Google+
        String selection = DBConstants.TBL_PROFILE_COLS.COL_EMAIL + " = ?";
        String[] selectionArgs = new String[] {getProfileEmail(cr)};

        ContentValues values = new ContentValues(1);
        values.put(DBConstants.TBL_PROFILE_COLS.COL_IMG, imgByte);

        cr.update(DBConstants.DB_PROFILE, values, selection, selectionArgs);

    }

    /*
     * Returns profile image as a bitmap...needs to be decoded
     */
    public static Bitmap getProfileImg(ContentResolver cr) {
        String[] projection = new String[] {DBConstants.TBL_PROFILE_COLS.COL_IMG};
        Cursor c = cr.query(DBConstants.DB_PROFILE, projection, null, null, null);

        byte[] imgByte = null;
        if(c!=null) {
            if(c.moveToFirst()) {
                imgByte = c.getBlob(0);
            }
            if(imgByte!=null) {
                return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
            }
            return null;
        }
        else {
            return null;
        }
    }

    public static String getProfileName(ContentResolver cr) {
        String[] projection = new String[] {DBConstants.TBL_PROFILE_COLS.COL_NAME};
        Cursor c = cr.query(DBConstants.DB_PROFILE, projection, null, null, null);

        String name = Constants.STR_NULL;
        if(c!=null) {
            if(c.moveToFirst()) {
                name = c.getString(0);
            }
        }
        return name;
    }

    public static String getProfileEmail(ContentResolver cr) {
        String[] projection = new String[] {DBConstants.TBL_PROFILE_COLS.COL_EMAIL};
        Cursor c = cr.query(DBConstants.DB_PROFILE, projection, null, null, null);

        String email = Constants.STR_NULL;
        if(c!=null) {
            if(c.moveToFirst()) {
                email = c.getString(0);
            }
        }
        return email;
    }

    public static int getProfilePhone(ContentResolver cr) {
        String[] projection = new String[] {DBConstants.TBL_PROFILE_COLS.COL_PHONE};
        Cursor c = cr.query(DBConstants.DB_PROFILE, projection, null, null, null);

        int phone = -1;
        if(c!=null) {
            if(c.moveToFirst()) {
                phone = c.getInt(0);
            }
        }
        return phone;
    }

    public static String getProfileStatus(ContentResolver cr) {
        String[] projection = new String[] {DBConstants.TBL_PROFILE_COLS.COL_STATUS};
        Cursor c = cr.query(DBConstants.DB_PROFILE, projection, null, null, null);

        String status = Constants.STR_NULL;
        if(c!=null) {
            if(c.moveToFirst()) {
                status = c.getString(0);
            }
        }
        return status;
    }

    public static String getProfilePIN(ContentResolver cr) {
        String[] projection = new String[] {DBConstants.TBL_PROFILE_COLS.COL_PIN};
        Cursor c = cr.query(DBConstants.DB_PROFILE, projection, null, null, null);

        String pin = Constants.STR_NULL;
        if(c!=null) {
            if(c.moveToFirst()) {
                pin = c.getString(0);
            }
        }
        return pin;
    }

    // Retrieve contact row for user's profile
    public static void getUserProfileLocal(ContentResolver cr) {
        // Sets the columns to retrieve for the user profile
        String[] mProjection = new String[] {
            ContactsContract.Profile._ID,
            ContactsContract.Profile.DISPLAY_NAME_PRIMARY,
            ContactsContract.Profile.LOOKUP_KEY,
            ContactsContract.Profile.PHOTO_THUMBNAIL_URI,
            ContactsContract.Profile.IS_USER_PROFILE
        };

        // Retrieves the profile from the Contacts Provider
        Cursor mProfileCursor = cr.query(
                        ContactsContract.Profile.CONTENT_URI,
                        mProjection,
                        null,
                        null,
                        null);

        if(mProfileCursor!=null) {
            while(mProfileCursor.moveToNext()) {
                if(mProfileCursor.getInt(mProfileCursor.getColumnIndex(ContactsContract.Profile.IS_USER_PROFILE))==1) {
                    String _id = mProfileCursor.getString(mProfileCursor.getColumnIndex(ContactsContract.Profile._ID));
                    String name = mProfileCursor.getString(mProfileCursor.getColumnIndex(ContactsContract.Profile.DISPLAY_NAME_PRIMARY));
                    String lookup_key = mProfileCursor.getString(mProfileCursor.getColumnIndex(ContactsContract.Profile.LOOKUP_KEY));
                    String uri = mProfileCursor.getString(mProfileCursor.getColumnIndex(ContactsContract.Profile.PHOTO_THUMBNAIL_URI));

                    Log.e(TAG, _id);
                    Log.e(TAG, name);
                    Log.e(TAG, lookup_key);
                }
            }
        }
    }
}
