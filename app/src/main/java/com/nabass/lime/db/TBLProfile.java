package com.nabass.lime.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import com.nabass.lime.Constants;

public class TBLProfile {
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

    public static void setProfileStatus(ContentResolver cr, String status) {
        //TODO: use another selection mechanism when switching from Google+
        String selection = DBConstants.TBL_PROFILE_COLS.COL_EMAIL + " = ?";
        String[] selectionArgs = new String[] {getProfileEmail(cr)};

        ContentValues values = new ContentValues(1);
        values.put(DBConstants.TBL_PROFILE_COLS.COL_STATUS, status);

        cr.update(DBConstants.DB_PROFILE, values, selection, selectionArgs);
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
}
