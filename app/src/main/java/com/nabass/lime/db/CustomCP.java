package com.nabass.lime.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class CustomCP extends ContentProvider {

    private static final String TAG = "CustomCP";

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
            case DBConstants.MSG_URI:
                qb.setTables(DBConstants.TBL_MSGS);
                break;
            case DBConstants.CONTACTS_URI:
                qb.setTables(DBConstants.TBL_CONTACTS);
                break;
            case DBConstants.PROFILE_URI:
                qb.setTables(DBConstants.TBL_PROFILE);
                break;
            default:
                break;
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
            case DBConstants.MSG_URI:
                id = db.insertOrThrow(DBConstants.TBL_MSGS, null, values);
                break;
            case DBConstants.CONTACTS_URI:
                id = db.insertOrThrow(DBConstants.TBL_CONTACTS, null, values);
                break;
            case DBConstants.PROFILE_URI:
                id = db.insertOrThrow(DBConstants.TBL_PROFILE, null, values);
                break;
            default:
                id = -1;
                break;
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
            case DBConstants.MSG_URI:
                count = db.update(DBConstants.TBL_MSGS, values, selection, selectionArgs);
                break;
            case DBConstants.CONTACTS_URI:
                count = db.update(DBConstants.TBL_CONTACTS, values, selection, selectionArgs);
                break;
            case DBConstants.PROFILE_URI:
                count = db.update(DBConstants.TBL_PROFILE, values, selection, selectionArgs);
                break;
            default:
                count = 0;
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbm.getWritableDatabase();

        int count;
        switch(DBConstants.sURIMatcher.match(uri)) {
            case DBConstants.MSG_URI:
                count = db.delete(DBConstants.TBL_MSGS, selection, selectionArgs);
                break;
            case DBConstants.CONTACTS_URI:
                count = db.delete(DBConstants.TBL_CONTACTS, selection, selectionArgs);
                break;
            case DBConstants.PROFILE_URI:
                count = db.delete(DBConstants.TBL_PROFILE, selection, selectionArgs);
                break;
            default:
                count = 0;
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        String type = null;
        switch(DBConstants.sURIMatcher.match(uri)) {
            case DBConstants.MSG_URI:
                type = DBConstants.DB_MSGS.toString();
                break;
            case DBConstants.CONTACTS_URI:
                type = DBConstants.DB_CONTACTS.toString();
                break;
            case DBConstants.PROFILE_URI:
                type = DBConstants.DB_PROFILE.toString();
                break;
            default:
                break;
        }
        return type;
    }
}