package com.nabass.lime;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import com.nabass.lime.db.CustomCP;
import com.nabass.lime.db.DBConstants;

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
        values.put(DBConstants.COL_MSG_TYPE, DBConstants.MsgDirection.DIRECTION_INCOMING.ordinal());
        values.put(DBConstants.COL_MSG, msg);
        values.put(DBConstants.COL_OTHER_ID, email_from);
        values.put(DBConstants.COL_SELF_ID, email_to);
        Uri mUri = resolve.insert(DBConstants.DB_MSGS, values);
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
        values.put(DBConstants.COL_MSG_TYPE, DBConstants.MsgDirection.DIRECTION_INCOMING.ordinal());
        values.put(DBConstants.COL_MSG, msg);
        values.put(DBConstants.COL_OTHER_ID, email_from);
        values.put(DBConstants.COL_SELF_ID, email_to);
        Uri mUri = resolve.insert(DBConstants.DB_MSGS, values);
        // Query
        String[] projection = {DBConstants.COL_MSG};
        String selection = DBConstants.COL_MSG + " = '" + msg + "'";
        Cursor cursor = resolve.query(DBConstants.DB_MSGS, projection, selection, null, null);
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
        values.put(DBConstants.COL_MSG_TYPE, DBConstants.MsgDirection.DIRECTION_INCOMING.ordinal());
        values.put(DBConstants.COL_MSG, msg);
        values.put(DBConstants.COL_OTHER_ID, email_from);
        values.put(DBConstants.COL_SELF_ID, email_to);
        Uri mUri = resolve.insert(DBConstants.DB_MSGS, values);
        // Delete
        String where = DBConstants.COL_MSG + "=?";
        String[] whereArgs = {msg};
        int res = resolve.delete(DBConstants.DB_MSGS, where,whereArgs);
        assertNotSame(0, res);
        // Query
        String[] projection = {DBConstants.COL_MSG};
        String selection = DBConstants.COL_MSG + " = '" + msg + "'";
        Cursor cursorQue = resolve.query(DBConstants.DB_MSGS, projection, selection, null, null);
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
        values.put(DBConstants.COL_MSG_TYPE, DBConstants.MsgDirection.DIRECTION_INCOMING.ordinal());
        values.put(DBConstants.COL_MSG, msg);
        values.put(DBConstants.COL_OTHER_ID, email_from);
        values.put(DBConstants.COL_SELF_ID, email_to);
        Uri mUri = resolve.insert(DBConstants.DB_MSGS, values);
        // Update
        String msg_new = "Hello World Again";
        ContentValues valuesUp = new ContentValues(2);
        valuesUp.put(DBConstants.COL_MSG, msg_new);
        String whereUp = DBConstants.COL_OTHER_ID + "=? AND " + DBConstants.COL_SELF_ID + "=?";
        String[] whereArgsUp = {email_from, email_to};
        int res = resolve.update(DBConstants.DB_MSGS, valuesUp, whereUp, whereArgsUp);
        assertNotSame(0, res);
        // Query
        String[] projection = {DBConstants.COL_MSG};
        String selection = DBConstants.COL_MSG + " = '" + msg + "'";
        Cursor cursorQue = resolve.query(DBConstants.DB_MSGS, projection, selection, null, null);
        /*assertNotNull(cursorQue.moveToFirst());*/
        /*cursorQue.moveToFirst();
        String msg_ACTUAL = cursorQue.getString(0);
        assertNotSame(msg, msg_ACTUAL);*/
    }
}
