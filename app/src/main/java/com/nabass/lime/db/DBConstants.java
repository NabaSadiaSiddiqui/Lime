package com.nabass.lime.db;

import android.content.UriMatcher;
import android.net.Uri;

public class DBConstants {

    public static final String CONTENT_PROVIDER = "com.nabass.lime.provider";
    public static final String COL_ID = "_id";

    /*
     * Profile
     */
    public static final String TBL_PROFILE = "profile";
    public static class TBL_PROFILE_COLS {
        public static final String COL_NAME = "name";
        public static final String COL_EMAIL = "email";
        public static final String COL_STATUS = "status";
        public static final String COL_PHONE = "phone_number";
        public static final String COL_PIN = "pin";
        public static final String COL_DEVICE_ID = "device_id";
    }

    /*
     * Contacts
     */
    public static final String TBL_CONTACTS = "contacts";
    public static final String COL_NAME = "name";
    public static final String COL_EMAIL = "email";         // primary id for now
    public static final String COL_MSG_FRESH = "fresh";     // number of unread messages
    public static final String COL_MSG_TOTAL = "total";     // total number of messages
    public static final String COL_MSG_RECENT = "recent";   // last message exchanged between participants
    public static final String COL_BLOCKED = "blocked";     // either of 1 (not blocked) or 0 (blocked)

    /*
     * Messages
     */
    public static final String TBL_MSGS = "messages";
    public static final String COL_MSG_TYPE = "type";
    public static final String COL_FROM = "senderEmail";
    public static final String COL_TO = "receiverEmail";
    public static final String COL_MSG = "message";
    public static final String COL_TIME = "time";

    /*
     * Build tree of Uri matcher objects
     */
    static final int MSG_URI = 1;
    static final int CONTACTS_URI = 2;
    static final int PROFILE_URI = 3;

    static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        DBConstants.sURIMatcher.addURI(DBConstants.CONTENT_PROVIDER, TBL_MSGS, DBConstants.MSG_URI);
        DBConstants.sURIMatcher.addURI(DBConstants.CONTENT_PROVIDER, TBL_CONTACTS, DBConstants.CONTACTS_URI);
        DBConstants.sURIMatcher.addURI(DBConstants.CONTENT_PROVIDER, TBL_PROFILE, DBConstants.PROFILE_URI);
    }

    public enum MsgDirection {
        DIRECTION_INCOMING, DIRECTION_OUTGOING
    }

    public static final Uri DB_MSGS = Uri.parse("content://" + CONTENT_PROVIDER + "/" + TBL_MSGS);
    public static final Uri DB_CONTACTS = Uri.parse("content://" + CONTENT_PROVIDER + "/" + TBL_CONTACTS);
    public static final Uri DB_PROFILE = Uri.parse("content://" + CONTENT_PROVIDER + "/" + TBL_PROFILE);
}
