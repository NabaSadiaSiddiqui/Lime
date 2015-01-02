package com.nabass.lime;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import com.nabass.lime.db.CustomCP;
import com.nabass.lime.db.DBConstants;

public class ContactsTest extends ProviderTestCase2 {

    private static final String TAG = MessagesTest.class.getName();
    // User the MockContentResolver to make queries
    private static MockContentResolver resolve;

    public ContactsTest() {
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
        String name = "Hello World";
        String email = "test@gmail.com";
        String count = "2";

        ContentValues values = new ContentValues(2);
        values.put(DBConstants.TBL_CONTACTS_COLS.COL_NAME, name);
        values.put(DBConstants.TBL_CONTACTS_COLS.COL_EMAIL, email);
        values.put(DBConstants.TBL_CONTACTS_COLS.COL_MSG_FRESH,count );
        Uri mUri = resolve.insert(DBConstants.DB_CONTACTS, values);
        assertNotNull(mUri);
    }

    // Test query
    public void testQuery() {
        // First insert and then match msg
        String name = "Hello World";
        String email = "test@gmail.com";
        String count = "2";
        // Insert
        ContentValues values = new ContentValues(2);
        values.put(DBConstants.TBL_CONTACTS_COLS.COL_NAME, name);
        values.put(DBConstants.TBL_CONTACTS_COLS.COL_EMAIL, email);
        values.put(DBConstants.TBL_CONTACTS_COLS.COL_MSG_FRESH,count );
        Uri mUri = resolve.insert(DBConstants.DB_CONTACTS, values);
        assertNotNull(mUri);
        // Query
        String[] projection = {DBConstants.TBL_CONTACTS_COLS.COL_MSG_FRESH};
        String selection = DBConstants.TBL_CONTACTS_COLS.COL_EMAIL + " = '" + email + "'";
        Cursor cursor = resolve.query(DBConstants.DB_CONTACTS, projection, selection, null, null);
        assertNotNull(cursor.moveToFirst());
        cursor.moveToFirst();
        String count_actual = cursor.getString(0);
        assertEquals(count, count_actual);
    }

    // Test delete
    public void testDelete() {
        // First insert and then match msg
        String name = "Hello World";
        String email = "test@gmail.com";
        String count = "2";
        // Insert
        ContentValues values = new ContentValues(2);
        values.put(DBConstants.TBL_CONTACTS_COLS.COL_NAME, name);
        values.put(DBConstants.TBL_CONTACTS_COLS.COL_EMAIL, email);
        values.put(DBConstants.TBL_CONTACTS_COLS.COL_MSG_FRESH,count );
        Uri mUri = resolve.insert(DBConstants.DB_CONTACTS, values);
        assertNotNull(mUri);
        // Delete
        String where = DBConstants.TBL_CONTACTS_COLS.COL_EMAIL + "=?";
        String[] whereArgs = {email};
        int res = resolve.delete(DBConstants.DB_CONTACTS, where,whereArgs);
        assertNotSame(0, res);
        // Query
        String[] projection = {DBConstants.TBL_CONTACTS_COLS.COL_MSG_FRESH};
        String selection = DBConstants.TBL_CONTACTS_COLS.COL_EMAIL + " = '" + email + "'";
        Cursor cursorQue = resolve.query(DBConstants.DB_CONTACTS, projection, selection, null, null);
        assertFalse(cursorQue.moveToFirst());
    }

    // Test Update
    public void testUpdate() {
        // First insert, then update and then query to check value is different
        String name = "Hello World";
        String email = "test@gmail.com";
        String count = "2";
        // Insert
        ContentValues values = new ContentValues(2);
        values.put(DBConstants.TBL_CONTACTS_COLS.COL_NAME, name);
        values.put(DBConstants.TBL_CONTACTS_COLS.COL_EMAIL, email);
        values.put(DBConstants.TBL_CONTACTS_COLS.COL_MSG_FRESH,count );
        Uri mUri = resolve.insert(DBConstants.DB_CONTACTS, values);
        assertNotNull(mUri);
        // Update
        String email_new = "test2@gmail.com";
        ContentValues valuesUp = new ContentValues(2);
        valuesUp.put(DBConstants.TBL_CONTACTS_COLS.COL_EMAIL, email_new);
        String whereUp = DBConstants.TBL_CONTACTS_COLS.COL_EMAIL + "=? AND " + DBConstants.TBL_CONTACTS_COLS.COL_NAME + "=?";
        String[] whereArgsUp = {email, name};
        int res = resolve.update(DBConstants.DB_CONTACTS, valuesUp, whereUp, whereArgsUp);
        assertNotSame(0, res);

        // Query
        String[] projection = {DBConstants.TBL_CONTACTS_COLS.COL_MSG_FRESH};
        String selection = DBConstants.TBL_CONTACTS_COLS.COL_EMAIL + " = '" + email + "'";
        Cursor cursorQue = resolve.query(DBConstants.DB_CONTACTS, projection, selection, null, null);
        assertFalse(cursorQue.moveToFirst());

    }
}

