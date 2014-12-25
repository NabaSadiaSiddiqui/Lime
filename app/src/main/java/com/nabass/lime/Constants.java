package com.nabass.lime;

public interface Constants {

    /**
     * Base URL of the Demo Server (such as http://my_host:8080/gcm-demo)
     */

	String SERVER_URL = "http://1-dot-bold-flash-798.appspot.com";

	/**
     * Google API project id registered to use GCM.
     */
	String SENDER_ID = "374736890011";

    public static final String STR_NULL = "";

    public static final String MODE_REMOTE = "mode_remote";
    public static final String MODE_LOCAL = "mode_local";

    public static final String FRAG_CHAT = "frag_chat";
    public static final String FRAG_CONTACTS = "frag_contacts";

    public static final String ERR_INVALID_EMAIL = "Email is not valid";

    public static final String TYPE_ADD_EMAIL = "email";
    public static final String TYPE_ADD_PIN = "pin";
    public static final String TYPE_ADD_PHONE = "phone";

}
