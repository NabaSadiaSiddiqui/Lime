package com.nabass.lime.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

public class DBExtended {

    private static final String TAG = "DBExtended";

    public DBExtended() {

    }

    public static void clearChatByEmail(ContentResolver cr, String email) {
        // Delete all messages for this person
        String selection = DBConstants.COL_RECIPIENT_ID + "=?";
        String[] selectionArgs = new String[] {email};
        cr.delete(DBConstants.DB_MSGS, selection, selectionArgs);
    }

    public static void deleteChatByEmail(ContentResolver cr, String email) {
        clearChatByEmail(cr, email);

        // Update total for that recipient in the contacts table
        ContentValues values = new ContentValues(1);
        values.put(DBConstants.COL_MSG_TOTAL, 0);
        String selection = DBConstants.COL_EMAIL + "=?";
        String[] selectionArgs = new String[] {email};
        cr.update(DBConstants.DB_CONTACTS, values, selection, selectionArgs);
    }

    public static void deleteContactByEmail(ContentResolver cr, String email) {
        // Delete this contact
        String selection = DBConstants.COL_EMAIL + "=?";
        String[] selectionArgs = new String[] {email};
        cr.delete(DBConstants.DB_CONTACTS, selection, selectionArgs);
    }

    public static Cursor searchContactByEmail(ContentResolver cr, String email) {
        String selection = DBConstants.COL_EMAIL + "=?";
        String[] selectionArgs = new String[] {email};
        Cursor c = cr.query(DBConstants.DB_CONTACTS, null, selection, selectionArgs, null);
        if(c.moveToFirst()) {
            return c;
        }
        return null;
    }

    public static Cursor dynamicSearchContactByEmail(ContentResolver cr, CharSequence ch) {
        String selection = DBConstants.COL_EMAIL + " like '" + ch + "%'";
        Cursor c = cr.query(DBConstants.DB_CONTACTS, null, selection, null, null);
        Log.e(TAG, c.toString());
        return c;
    }

    public static void addContactByEmail(ContentResolver cr, String email) {
        try {
            ContentValues values = new ContentValues(1);
            values.put(DBConstants.COL_EMAIL, email);
            cr.insert(DBConstants.DB_CONTACTS, values);
        } catch (SQLException e) {
            throw new SQLException(TAG + " has an exception");
        }
    }

    public static void insertOutgoingMsg(ContentResolver cr, ContentValues values, String email) {
        cr.insert(DBConstants.DB_MSGS, values);
        incrTotalMsgCount(cr, email);
    }

    public static void insertIncomingMsg(ContentResolver cr, ContentValues values, String email) {
        cr.insert(DBConstants.DB_MSGS, values);
        incrFreshMsgCount(cr, email);
        incrTotalMsgCount(cr, email);
    }

    public static void resetFreshMsgCount(ContentResolver cr, String email) {
        ContentValues values = new ContentValues(1);
        values.put(DBConstants.COL_MSG_FRESH, 0);
        String selection = DBConstants.COL_EMAIL + " = ? ";
        String[] selectionArgs = new String[] {email};
        cr.update(DBConstants.DB_CONTACTS, values, selection, selectionArgs);
    }

    private static void incrFreshMsgCount(ContentResolver cr, String email) {
        String[] projection = new String[] {DBConstants.COL_MSG_FRESH};
        String selection = DBConstants.COL_EMAIL + " = ?";
        String[] selectionArgs = new String[] {email};

        Cursor c = cr.query(DBConstants.DB_CONTACTS, projection, selection, selectionArgs, null);
        if(c!=null) {
            if(c.moveToFirst()) {
                int new_count = c.getInt(0) + 1;

                ContentValues values = new ContentValues(1);
                values.put(DBConstants.COL_MSG_FRESH, new_count);

                cr.update(DBConstants.DB_CONTACTS, values, selection, selectionArgs);
            }
            c.close();
        }
    }

    private static void incrTotalMsgCount(ContentResolver cr, String email) {
        String[] projection = new String[] {DBConstants.COL_MSG_TOTAL};
        String selection = DBConstants.COL_EMAIL + " = ? ";
        String[] selectionArgs = new String[] {email};

        Cursor c = cr.query(DBConstants.DB_CONTACTS, projection, selection, selectionArgs, null);
        if(c!=null) {
            if(c.moveToFirst()) {
                int new_total = c.getInt(0) + 1;

                ContentValues values = new ContentValues(1);
                values.put(DBConstants.COL_MSG_TOTAL, new_total);

                cr.update(DBConstants.DB_CONTACTS, values, selection, selectionArgs);
            }
        }
    }

}