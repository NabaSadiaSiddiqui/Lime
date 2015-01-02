package com.nabass.lime.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.nabass.lime.Constants;

public class DBExtended {

    private static final String TAG = "DBExtended";

    public DBExtended() {

    }

    public static void deleteContactByEmail(ContentResolver cr, String email) {
        // Delete this contact
        String selection = DBConstants.TBL_CONTACTS_COLS.COL_EMAIL + "=?";
        String[] selectionArgs = new String[] {email};
        cr.delete(DBConstants.DB_CONTACTS, selection, selectionArgs);
    }

    public static Cursor searchContactByEmail(ContentResolver cr, String email) {
        String selection = DBConstants.TBL_CONTACTS_COLS.COL_EMAIL + "=?";
        String[] selectionArgs = new String[] {email};
        Cursor c = cr.query(DBConstants.DB_CONTACTS, null, selection, selectionArgs, null);
        if(c.moveToFirst()) {
            return c;
        }
        return null;
    }

    public static Cursor dynamicSearchContactByEmail(ContentResolver cr, CharSequence ch) {
        String selection = DBConstants.TBL_CONTACTS_COLS.COL_EMAIL + " like '" + ch + "%'";
        Cursor c = cr.query(DBConstants.DB_CONTACTS, null, selection, null, null);
        Log.e(TAG, c.toString());
        return c;
    }

    public static void addContactByEmail(ContentResolver cr, String email) {
        // return if user is already added
        if(searchContactByEmail(cr, email) == null) {
            ContentValues values = new ContentValues(1);
            values.put(DBConstants.TBL_CONTACTS_COLS.COL_EMAIL, email);
            cr.insert(DBConstants.DB_CONTACTS, values);
        }
    }

    public static boolean checkIsBlockedByEmail(ContentResolver cr, String email) {
        String[] projection = new String[] {DBConstants.TBL_CONTACTS_COLS.COL_BLOCKED};
        String selection = DBConstants.TBL_CONTACTS_COLS.COL_EMAIL + " = ?";
        String[] selectionArgs = new String[] {email};

        Cursor c = cr.query(DBConstants.DB_CONTACTS, projection, selection, selectionArgs, null);
        if(c!=null) {
            if(c.moveToFirst()) {
                int blocked = c.getInt(0);
                if(blocked == 1) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    public static void blockContactByEmail(ContentResolver cr, String email) {
        String selection = DBConstants.TBL_CONTACTS_COLS.COL_EMAIL + " = ?";
        String[] selectionArgs = new String[] {email};
        ContentValues values = new ContentValues(1);
        values.put(DBConstants.TBL_CONTACTS_COLS.COL_BLOCKED, 0);

        cr.update(DBConstants.DB_CONTACTS, values, selection, selectionArgs);
    }

    public static void unblockContactByEmail(ContentResolver cr, String email) {
        String selection = DBConstants.TBL_CONTACTS_COLS.COL_EMAIL + " = ?";
        String[] selectionArgs = new String[] {email};
        ContentValues values = new ContentValues(1);
        values.put(DBConstants.TBL_CONTACTS_COLS.COL_BLOCKED, 1);

        cr.update(DBConstants.DB_CONTACTS, values, selection, selectionArgs);
    }
}