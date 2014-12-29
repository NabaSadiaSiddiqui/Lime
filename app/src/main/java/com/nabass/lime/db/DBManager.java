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
                + DBConstants.COL_MSG_TYPE +" integer, "
                + DBConstants.COL_MSG +" text, "
                + DBConstants.COL_SENDER_ID +" text, "
                + DBConstants.COL_RECIPIENT_ID +" text, "
                + DBConstants.COL_TIME 			  +" datetime default current_timestamp);");

        db.execSQL("create table " + DBConstants.TBL_CONTACTS + " ("
                + "_id integer primary key autoincrement, "
                + DBConstants.COL_NAME 	  +" text, "
                + DBConstants.COL_EMAIL   +" text unique, "
                + DBConstants.COL_MSG_FRESH +" integer default 0);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}