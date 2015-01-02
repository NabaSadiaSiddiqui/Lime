package com.nabass.lime.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.nabass.lime.Constants;
import com.nabass.lime.Init;

public class DBExtended {

    private static final String TAG = "DBExtended";

    public DBExtended() {

    }

    public static void clearChatByEmail(ContentResolver cr, String email) {
        // Delete all messages for this person
        String selection = DBConstants.COL_FROM + "=?";
        String[] selectionArgs = new String[] {email};
        cr.delete(DBConstants.DB_MSGS, selection, selectionArgs);

        // Update recent message column in the contact database
        unsetRecentMessage(cr, email);
    }

    public static void deleteChatByEmail(ContentResolver cr, String email) {
        clearChatByEmail(cr, email);

        // Update total for other in the contacts table
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
        // return if user is already added
        if(searchContactByEmail(cr, email) == null) {
            ContentValues values = new ContentValues(1);
            values.put(DBConstants.COL_EMAIL, email);
            cr.insert(DBConstants.DB_CONTACTS, values);
        }
    }

    public static void insertOutgoingMsg(ContentResolver cr, String message, String email) {
        ContentValues values = new ContentValues(4);
        int msgType = DBConstants.MsgDirection.DIRECTION_OUTGOING.ordinal();
        byte[] msg = message.getBytes();
        String to = TBLProfile.getProfileEmail(cr);
        String from = email;

        values.put(DBConstants.COL_MSG_TYPE, msgType);
        values.put(DBConstants.COL_MSG, msg);
        values.put(DBConstants.COL_TO, to);
        values.put(DBConstants.COL_FROM, from);
        values.put(DBConstants.COL_TIME, System.currentTimeMillis());

        cr.insert(DBConstants.DB_MSGS, values);
        updateRecentMessage(cr, from, message);
        incrTotalMsgCount(cr, email);
    }

    public static void insertIncomingMsg(ContentResolver cr, Intent intent){
        ContentValues values = new ContentValues(4);
        int msgType = DBConstants.MsgDirection.DIRECTION_INCOMING.ordinal();
        String msg = intent.getStringExtra(DBConstants.COL_MSG);
        byte[] msgByte = msg.getBytes();
        String from = intent.getStringExtra(DBConstants.COL_FROM);
        String to = intent.getStringExtra(DBConstants.COL_TO);

        values.put(DBConstants.COL_MSG_TYPE, msgType);
        values.put(DBConstants.COL_MSG, msgByte);
        values.put(DBConstants.COL_FROM, from);
        values.put(DBConstants.COL_TO, to);
        values.put(DBConstants.COL_TIME, System.currentTimeMillis());

        cr.insert(DBConstants.DB_MSGS, values);
        incrFreshMsgCount(cr, from);
        updateRecentMessage(cr, from, msg);
        incrTotalMsgCount(cr, from);
    }

    public static void resetFreshMsgCount(ContentResolver cr, String email) {
        ContentValues values = new ContentValues(1);
        values.put(DBConstants.COL_MSG_FRESH, 0);

        String selection = DBConstants.COL_EMAIL + " = ? ";
        String[] selectionArgs = new String[] {email};
        cr.update(DBConstants.DB_CONTACTS, values, selection, selectionArgs);
    }

    private static void updateRecentMessage(ContentResolver cr, String email, String message) {
        ContentValues values = new ContentValues(1);
        values.put(DBConstants.COL_MSG_RECENT, message);

        String selection = DBConstants.COL_EMAIL + " = ?";
        String[] selectionArgs = new String[] {email};

        cr.update(DBConstants.DB_CONTACTS, values, selection, selectionArgs);
    }

    private static void unsetRecentMessage(ContentResolver cr, String email) {
        ContentValues values = new ContentValues(1);
        values.put(DBConstants.COL_MSG_RECENT, Constants.STR_NULL);

        String selection = DBConstants.COL_EMAIL + " = ?";
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

    public static boolean checkIsBlockedByEmail(ContentResolver cr, String email) {
        String[] projection = new String[] {DBConstants.COL_BLOCKED};
        String selection = DBConstants.COL_EMAIL + " = ?";
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
        String selection = DBConstants.COL_EMAIL + " = ?";
        String[] selectionArgs = new String[] {email};
        ContentValues values = new ContentValues(1);
        values.put(DBConstants.COL_BLOCKED, 0);

        cr.update(DBConstants.DB_CONTACTS, values, selection, selectionArgs);
    }

    public static void unblockContactByEmail(ContentResolver cr, String email) {
        String selection = DBConstants.COL_EMAIL + " = ?";
        String[] selectionArgs = new String[] {email};
        ContentValues values = new ContentValues(1);
        values.put(DBConstants.COL_BLOCKED, 1);

        cr.update(DBConstants.DB_CONTACTS, values, selection, selectionArgs);
    }
}