package com.nabass.lime.db;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class DBExtended {

    private static final String TAG = "DBExtended";

    public DBExtended() {

    }

    private static String getIdByEmail(ContentResolver cr, String email) {
        String[] projection = new String[] {DBConstants.COL_ID};
        String where = DBConstants.COL_EMAIL +"=?";
        String[] selectionArgs = new String[] {email};
        Cursor c = cr.query(DBConstants.DB_CONTACTS, projection, where, selectionArgs, null);
        String id=null;
        if(c.moveToFirst()) {
            id = c.getString(c.getColumnIndex(DBConstants.COL_ID));
        }
        return id;
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

    public static void insertMsgByEmail(ContentResolver cr, String email) {
        /*String msg = intent.getStringExtra(DBConstants.COL_MSG);
        String senderEmail = intent.getStringExtra(DBConstants.COL_SENDER_ID);
        String receiverEmail = intent.getStringExtra(DBConstants.COL_RECIPIENT_ID);
        ContentValues values = new ContentValues(2);
        values.put(DBConstants.COL_MSG_TYPE,  DBConstants.MsgDirection.DIRECTION_INCOMING.ordinal());
        values.put(DBConstants.COL_MSG, msg);
        values.put(DBConstants.COL_SENDER_ID, senderEmail);
        values.put(DBConstants.COL_RECIPIENT_ID, receiverEmail);
        context.getContentResolver().insert(DBConstants.DB_MSGS, values);*/
        //Implement this
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
}
