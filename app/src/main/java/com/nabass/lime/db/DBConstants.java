package com.nabass.lime.db;

import android.content.UriMatcher;
import android.net.Uri;

public class DBConstants {

    public static final String CONTENT_PROVIDER = "com.nabass.lime.provider";
    public static final String COL_ID = "_id";

    /*
     * Contacts
     */
    public static final String TBL_CONTACTS = "contacts";
    public static final String COL_NAME = "name";
    public static final String COL_EMAIL = "email";
    public static final String COL_MSG_FRESH = "count"; // number of unread messages
    public static final String COL_MSG_TOTAL = "total"; // total number of messages

    /*
     * Messages
     */
    public static final String TBL_MSGS = "messages";
    public static final String COL_MSG_TYPE = "type";
    public static final String COL_SENDER_ID = "senderEmail";
    public static final String COL_RECIPIENT_ID = "receiverEmail";
    public static final String COL_MSG = "message";
    public static final String COL_TIME = "time";

    /*
     * Build tree of Uri matcher objects
     */
    static final int ALL_MSGS = 1;
    static final int ONE_MSG = 2;
    static final int ALL_CONTACTS = 3;
    static final int ONE_CONTACT = 4;

    static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        DBConstants.sURIMatcher.addURI(DBConstants.CONTENT_PROVIDER, "messages", DBConstants.ALL_MSGS);
        DBConstants.sURIMatcher.addURI(DBConstants.CONTENT_PROVIDER, "messages/#", DBConstants.ONE_MSG);
        DBConstants.sURIMatcher.addURI(DBConstants.CONTENT_PROVIDER, "contacts", DBConstants.ALL_CONTACTS);
        DBConstants.sURIMatcher.addURI(DBConstants.CONTENT_PROVIDER, "contacts/#", DBConstants.ONE_CONTACT);
    }

    public enum MsgDirection {
        DIRECTION_INCOMING, DIRECTION_OUTGOING
    }

    public static final Uri DB_MSGS = Uri.parse("content://" + CONTENT_PROVIDER + "/messages");
    public static final Uri DB_CONTACTS = Uri.parse("content://" + CONTENT_PROVIDER + "/contacts");
}
