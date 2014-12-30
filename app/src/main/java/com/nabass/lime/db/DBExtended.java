package com.nabass.lime.db;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class DBExtended {

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

        // Update total for that recepient in the contacts table
        ContentValues values = new ContentValues(1);
        values.put(DBConstants.COL_MSG_TOTAL, 0);
        String selection = DBConstants.COL_EMAIL + "=?";
        String[] selectionArgs = new String[] {email};
        cr.update(DBConstants.DB_CONTACTS, values, selection, selectionArgs);
    }
}
