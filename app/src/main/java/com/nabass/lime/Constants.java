package com.nabass.lime;

public interface Constants {
    // Authenticating service
    public static final String AUTH_SERVICE = "com.google";

    // Preference manager keys
    public static final String KEY_MODE_USER = "mode_user";
    public static final String KEY_CLIENT_EMAIL = "client_email";
    public static final String KEY_NEW_MSG = "notifications_msg";
    public static final String KEY_RINGTONE = "notifications_ringtone";
    public static final String KEY_SERVER_URL = "server_url";
    public static final String KEY_SENDER_ID = "sender_id";

    public static final String STR_NULL = "";

    public static final String MODE_REMOTE = "mode_remote";
    public static final String MODE_LOCAL = "mode_local";

    // Fragment names --> Used in OnFragmentInteraction method
    public static final String FRAG_CHAT = "frag_chat";
    public static final String FRAG_CONTACTS = "frag_contacts";
    public static final String FRAG_CHAT_ACTIONS = "frag_chat_actions";

    public static final String ERR_INVALID_EMAIL = "Email is not valid";

    public static final String TYPE_ADD_EMAIL = "email";
    public static final String TYPE_ADD_PIN = "pin";
    public static final String TYPE_ADD_PHONE = "phone";

    public static final String TAG_DIALOG = "dialog";

    // GCM constants/parameters
    public static String SERVER_URL = "http://1-dot-bold-flash-798.appspot.com";
    public static String SENDER_ID = "374736890011";
    public static String SENDER_EMAIL = "senderEmail";
    public static String RECEIVER_EMAIL = "receiverEmail";
    public static String REG_ID = "regId";
    public static String MESSAGE = "message";
    public static String KEY_REG_ID = "gcm_registration_id";
    public static String PROFILE_ID = "profile_id";
    public static String ACTION_REGISTER = "com.nabass.lime.REGISTER";
    public static String EXTRA_STATUS = "status";
    public static int STATUS_SUCCESS = 1;
    public static int STATUS_FAILED = 0;
}
