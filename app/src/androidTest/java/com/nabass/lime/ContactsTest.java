package com.nabass.lime;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import com.nabass.lime.db.CustomCP;

import java.net.URI;

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
        values.put(CustomCP.COL_NAME, name);
        values.put(CustomCP.COL_EMAIL, email);
        values.put(CustomCP.COL_COUNT,count );
        Uri mUri = resolve.insert(CustomCP.CONTENT_URI_PROFILE, values);
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
        values.put(CustomCP.COL_NAME, name);
        values.put(CustomCP.COL_EMAIL, email);
        values.put(CustomCP.COL_COUNT,count );
        Uri mUri = resolve.insert(CustomCP.CONTENT_URI_PROFILE, values);
        assertNotNull(mUri);
        // Query
        String[] projection = {CustomCP.COL_COUNT};
        String selection = CustomCP.COL_EMAIL + " = '" + email + "'";
        Cursor cursor = resolve.query(CustomCP.CONTENT_URI_PROFILE, projection, selection, null, null);
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
        values.put(CustomCP.COL_NAME, name);
        values.put(CustomCP.COL_EMAIL, email);
        values.put(CustomCP.COL_COUNT,count );
        Uri mUri = resolve.insert(CustomCP.CONTENT_URI_PROFILE, values);
        assertNotNull(mUri);
        // Delete
        String where = CustomCP.COL_EMAIL + "=?";
        String[] whereArgs = {email};
        int res = resolve.delete(CustomCP.CONTENT_URI_PROFILE, where,whereArgs);
        assertNotSame(0, res);
        // Query
        String[] projection = {CustomCP.COL_COUNT};
        String selection = CustomCP.COL_EMAIL + " = '" + email + "'";
        Cursor cursorQue = resolve.query(CustomCP.CONTENT_URI_PROFILE, projection, selection, null, null);
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
        values.put(CustomCP.COL_NAME, name);
        values.put(CustomCP.COL_EMAIL, email);
        values.put(CustomCP.COL_COUNT,count );
        Uri mUri = resolve.insert(CustomCP.CONTENT_URI_PROFILE, values);
        assertNotNull(mUri);
        // Update
        String email_new = "test2@gmail.com";
        ContentValues valuesUp = new ContentValues(2);
        valuesUp.put(CustomCP.COL_EMAIL, email_new);
        String whereUp = CustomCP.COL_EMAIL + "=? AND " + CustomCP.COL_NAME + "=?";
        String[] whereArgsUp = {email, name};
        int res = resolve.update(CustomCP.CONTENT_URI_PROFILE, valuesUp, whereUp, whereArgsUp);
        assertNotSame(0, res);

        // Query
        String[] projection = {CustomCP.COL_COUNT};
        String selection = CustomCP.COL_EMAIL + " = '" + email + "'";
        Cursor cursorQue = resolve.query(CustomCP.CONTENT_URI_PROFILE, projection, selection, null, null);
        assertFalse(cursorQue.moveToFirst());

    }
}

