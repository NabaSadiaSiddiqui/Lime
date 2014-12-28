package com.nabass.lime.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class CustomCP extends ContentProvider {

    private DBManager dbm;

    @Override
    public boolean onCreate() {
        /*
         * A helper class to manage database creation and version management.
         */
        dbm = new DBManager(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        /*
         * Open and/or create database for reading
         */
        SQLiteDatabase db = dbm.getReadableDatabase();
        /*
         * This is a convenience class that helps build SQL queries to be sent to SQLiteDatabase objects.
         */
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        switch(DBConstants.sURIMatcher.match(uri)) {
            case DBConstants.ALL_MSGS:
                qb.setTables(DBConstants.TBL_MSGS);
                break;
            case DBConstants.ONE_MSG:
                qb.setTables(DBConstants.TBL_MSGS);
                qb.appendWhere(DBConstants.COL_ID + " = " + uri.getLastPathSegment());
                break;
            case DBConstants.ALL_CONTACTS:
                qb.setTables(DBConstants.TBL_CONTACTS);
                break;
            case DBConstants.ONE_CONTACT:
                qb.setTables(DBConstants.TBL_CONTACTS);
                qb.appendWhere(DBConstants.COL_ID + " = " + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbm.getWritableDatabase();

        long id;
        switch(DBConstants.sURIMatcher.match(uri)) {
            case DBConstants.ALL_MSGS:
                id = db.insertOrThrow(DBConstants.TBL_MSGS, null, values);
                if (values.get(DBConstants.COL_RECIPIENT_ID) == null) {
                    db.execSQL("update " + DBConstants.TBL_CONTACTS + " set count = count+1 where email = ?", new Object[]{values.get(DBConstants.COL_SENDER_ID)});
                    getContext().getContentResolver().notifyChange(DBConstants.DB_CONTACTS, null);
                }
                break;
            case DBConstants.ALL_CONTACTS:
                id = db.insertOrThrow(DBConstants.TBL_CONTACTS, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        Uri insertUri = ContentUris.withAppendedId(uri, id);
        getContext().getContentResolver().notifyChange(insertUri, null);
        return insertUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbm.getWritableDatabase();

        int count;
        switch(DBConstants.sURIMatcher.match(uri)) {
            case DBConstants.ALL_MSGS:
                count = db.update(DBConstants.TBL_MSGS, values, selection, selectionArgs);
                break;
            case DBConstants.ONE_MSG:
                selection = DBConstants.COL_ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                count = db.update(DBConstants.TBL_MSGS, values, selection, selectionArgs);
                break;
            case DBConstants.ALL_CONTACTS:
                count = db.update(DBConstants.TBL_CONTACTS, values, selection, selectionArgs);
                break;
            case DBConstants.ONE_CONTACT:
                selection = DBConstants.COL_ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                count = db.update(DBConstants.TBL_CONTACTS, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbm.getWritableDatabase();

        int count;
        switch(DBConstants.sURIMatcher.match(uri)) {
            case DBConstants.ALL_MSGS:
                count = db.delete(DBConstants.TBL_MSGS, selection, selectionArgs);
                break;
            case DBConstants.ONE_MSG:
                selection = DBConstants.COL_ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                count = db.delete(DBConstants.TBL_MSGS, selection, selectionArgs);
                break;
            case DBConstants.ALL_CONTACTS:
                count = db.delete(DBConstants.TBL_CONTACTS, selection, selectionArgs);
                break;
            case DBConstants.ONE_CONTACT:
                selection = DBConstants.COL_ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                count = db.delete(DBConstants.TBL_CONTACTS, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }
}