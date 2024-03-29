package com.nabass.lime.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "limeIM.db";

    private static final int DATABASE_VERSION = 1;

    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*
         * Create tables on application installation
         */
        db.execSQL("create table " + DBConstants.TBL_MSGS + " ("
                + "_id integer primary key autoincrement, "
                + DBConstants.TBL_MSGS_COLS.COL_MSG_TYPE + " integer, "
                + DBConstants.TBL_MSGS_COLS.COL_MSG + " blob, "
                + DBConstants.TBL_MSGS_COLS.COL_FROM + " text, "
                + DBConstants.TBL_MSGS_COLS.COL_TO + " text, "
                + DBConstants.TBL_MSGS_COLS.COL_SYNCED + " integer default 0, "
                + DBConstants.TBL_MSGS_COLS.COL_TIME + " datetime default current_timestamp);");

        db.execSQL("create table " + DBConstants.TBL_CONTACTS + " ("
                + "_id integer primary key autoincrement, "
                + DBConstants.TBL_CONTACTS_COLS.COL_NAME 	  +" text, "
                + DBConstants.TBL_CONTACTS_COLS.COL_EMAIL   +" text unique, "
                + DBConstants.TBL_CONTACTS_COLS.COL_PHONE + " text, "
                + DBConstants.TBL_CONTACTS_COLS.COL_LOCAL + " integer default 0,"
                + DBConstants.TBL_CONTACTS_COLS.COL_ANDROID_ID + " text, "
                + DBConstants.TBL_CONTACTS_COLS.COL_MSG_RECENT + " text, "
                + DBConstants.TBL_CONTACTS_COLS.COL_MSG_FRESH +" integer default 0, "
                + DBConstants.TBL_CONTACTS_COLS.COL_MSG_TOTAL + " integer default 0, "
                + DBConstants.TBL_CONTACTS_COLS.COL_BLOCKED + " integer default 1);");

        db.execSQL("create table " + DBConstants.TBL_PROFILE + " ("
                + "_id integer primary key autoincrement, "
                + DBConstants.TBL_PROFILE_COLS.COL_NAME + " text, "
                + DBConstants.TBL_PROFILE_COLS.COL_EMAIL + " text unique, "
                + DBConstants.TBL_PROFILE_COLS.COL_PHONE + " integer, "
                + DBConstants.TBL_PROFILE_COLS.COL_PIN + " text, "
                + DBConstants.TBL_PROFILE_COLS.COL_DEVICE_ID + " text, "
                + DBConstants.TBL_PROFILE_COLS.COL_IMG + " blob, "
                + DBConstants.TBL_PROFILE_COLS.COL_STATUS + " text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}