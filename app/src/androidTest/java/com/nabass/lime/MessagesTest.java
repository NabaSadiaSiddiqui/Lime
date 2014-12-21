package com.nabass.lime;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import com.nabass.lime.db.CustomCP;

import java.net.URI;

/**
 * Created by nabass on 21/12/14.
 */
public class MessagesTest extends ProviderTestCase2 {

    private static final String TAG = MessagesTest.class.getName();
    // User the MockContentResolver to make queries
    private static MockContentResolver resolve;

    public MessagesTest() {
        super(CustomCP.class, "com.nabass.lime.provider");
    }

    // sets up the environment for the text fixture
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        resolve = this.getMockContentResolver();
    }

    // Tears down the environment for the test fixture
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    // Test insertion
    public void testInsert() {
        String msg = "Hello World";
        String email_to = "test@gmail.com";
        String email_from = "im220@gmail.com";

        ContentValues values = new ContentValues(2);
        values.put(CustomCP.COL_TYPE, CustomCP.MessageType.INCOMING.ordinal());
        values.put(CustomCP.COL_MESSAGE, msg);
        values.put(CustomCP.COL_SENDER_EMAIL, email_from);
        values.put(CustomCP.COL_RECEIVER_EMAIL, email_to);
        Uri mUri = resolve.insert(CustomCP.CONTENT_URI_MESSAGES, values);
        assertNotNull(mUri);
    }

    // Test query
    public void testQuery() {
        // First insert and then match msg
        String msg = "Hello World";
        String email_to = "test@gmail.com";
        String email_from = "im220@gmail.com";
        // Insert
        ContentValues values = new ContentValues(2);
        values.put(CustomCP.COL_TYPE, CustomCP.MessageType.INCOMING.ordinal());
        values.put(CustomCP.COL_MESSAGE, msg);
        values.put(CustomCP.COL_SENDER_EMAIL, email_from);
        values.put(CustomCP.COL_RECEIVER_EMAIL, email_to);
        Uri mUri = resolve.insert(CustomCP.CONTENT_URI_MESSAGES, values);
        // Query
        String[] projection = {CustomCP.COL_MESSAGE};
        String selection = CustomCP.COL_MESSAGE + " = '" + msg + "'";
        Cursor cursor = resolve.query(CustomCP.CONTENT_URI_MESSAGES, projection, selection, null, null);
        assertNotNull(cursor.moveToFirst());
        cursor.moveToFirst();
        String msg_ACTUAL = cursor.getString(0);
        assertEquals(msg, msg_ACTUAL);
    }

    // Test delete
    public void testDelete() {
        // First insert, then delete and then query to check NULL
        String msg = "Hello World";
        String email_to = "test@gmail.com";
        String email_from = "im220@gmail.com";
        // Insert
        ContentValues values = new ContentValues(2);
        values.put(CustomCP.COL_TYPE, CustomCP.MessageType.INCOMING.ordinal());
        values.put(CustomCP.COL_MESSAGE, msg);
        values.put(CustomCP.COL_SENDER_EMAIL, email_from);
        values.put(CustomCP.COL_RECEIVER_EMAIL, email_to);
        Uri mUri = resolve.insert(CustomCP.CONTENT_URI_MESSAGES, values);
        // Delete
        String where = CustomCP.COL_MESSAGE + "=?";
        String[] whereArgs = {msg};
        int res = resolve.delete(CustomCP.CONTENT_URI_MESSAGES, where,whereArgs);
        assertNotSame(0, res);
        // Query
        String[] projection = {CustomCP.COL_MESSAGE};
        String selection = CustomCP.COL_MESSAGE + " = '" + msg + "'";
        Cursor cursorQue = resolve.query(CustomCP.CONTENT_URI_MESSAGES, projection, selection, null, null);
        // Check cursor is empty == false
        assertFalse(cursorQue.moveToFirst());
    }

    // Test Update
    public void testUpdate() {
        // First insert, then update and then query to check value is different
        String msg = "Hello World";
        String email_to = "test@gmail.com";
        String email_from = "im220@gmail.com";
        // Insert
        ContentValues values = new ContentValues(2);
        values.put(CustomCP.COL_TYPE, CustomCP.MessageType.INCOMING.ordinal());
        values.put(CustomCP.COL_MESSAGE, msg);
        values.put(CustomCP.COL_SENDER_EMAIL, email_from);
        values.put(CustomCP.COL_RECEIVER_EMAIL, email_to);
        Uri mUri = resolve.insert(CustomCP.CONTENT_URI_MESSAGES, values);
        // Update
        String msg_new = "Hello World Again";
        ContentValues valuesUp = new ContentValues(2);
        valuesUp.put(CustomCP.COL_MESSAGE, msg_new);
        String whereUp = CustomCP.COL_SENDER_EMAIL + "=? AND " + CustomCP.COL_RECEIVER_EMAIL + "=?";
        String[] whereArgsUp = {email_from, email_to};
        int res = resolve.update(CustomCP.CONTENT_URI_MESSAGES, valuesUp, whereUp, whereArgsUp);
        assertNotSame(0, res);
        // Query
        String[] projection = {CustomCP.COL_MESSAGE};
        String selection = CustomCP.COL_MESSAGE + " = '" + msg + "'";
        Cursor cursorQue = resolve.query(CustomCP.CONTENT_URI_MESSAGES, projection, selection, null, null);
        /*assertNotNull(cursorQue.moveToFirst());*/
        /*cursorQue.moveToFirst();
        String msg_ACTUAL = cursorQue.getString(0);
        assertNotSame(msg, msg_ACTUAL);*/
    }
}
