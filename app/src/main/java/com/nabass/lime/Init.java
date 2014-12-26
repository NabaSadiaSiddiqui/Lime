package com.nabass.lime;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Patterns;
import java.util.ArrayList;
import java.util.List;

public class Init extends Application {
    public static final String PROFILE_ID = "profile_id";
    public static final String ACTION_REGISTER = "com.nabass.lime.REGISTER";
    public static final String EXTRA_STATUS = "status";
    public static final int STATUS_SUCCESS = 1;
    public static final int STATUS_FAILED = 0;
    public static String[] email_arr;
    private static SharedPreferences prefs;

    @Override
    public void onCreate() {
        super.onCreate();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        List<String> emailList = getEmailList();
        email_arr = emailList.toArray(new String[emailList.size()]);

        // Set phone number of the device in shared pref --> will be used by Profile Fragment
        TelephonyManager tMgr = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        setSharedPref(Constants.USER_PHONE_NUM, tMgr.getLine1Number());
    }
    private List<String> getEmailList() {
        List<String> lst = new ArrayList<String>();
        Account[] accounts = AccountManager.get(this).getAccounts();
        for (Account account : accounts) {
            if (Patterns.EMAIL_ADDRESS.matcher(account.name).matches()) {
                lst.add(account.name);
            }
        }
        return lst;
    }
    public static String getPreferredEmail() {
        return prefs.getString("chat_email_id", email_arr.length==0 ? "" : email_arr[0]);
    }
    public static String getDisplayName() {
        String email = getPreferredEmail();
        return prefs.getString("display_name", email.substring(0, email.indexOf('@')));
    }
    public static boolean isNotify() {
        return prefs.getBoolean("notifications_new_message", true);
    }
    public static String getRingtone() {
        return prefs.getString("notifications_new_message_ringtone", android.provider.Settings.System.DEFAULT_NOTIFICATION_URI.toString());
    }

    public static String getServerUrl() {
        return prefs.getString("server_url_pref", Constants.SERVER_URL);
    }

    public static String getSenderId() {
        return prefs.getString("sender_id_pref", Constants.SENDER_ID);
    }

    public static void setModeLocal() {
        setSharedPref(Constants.MODE_USER, Constants.MODE_LOCAL);
    }

    public static void setModeRemote() {
        setSharedPref(Constants.MODE_USER, Constants.MODE_REMOTE);
    }

    public static String getMode() {
        return prefs.getString(Constants.MODE_USER, null);
    }


    public static String getPhoneNum() {
        return prefs.getString(Constants.USER_PHONE_NUM, null);
    }

    public static void setSharedPref(String key, String val) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, val);
        editor.commit();
    }

    // changes a string 'foo' to 'Foo'
    public static String formatStringCamelCase(String str) {
        return str.substring(0,1).toUpperCase() + str.substring(1,str.length()).toLowerCase();
    }

    // Returns the first character of the string
    public static String getFirst(String str) {
        return str.substring(0,1);
    }

    // Returns the first character of the string in upper case: foo -> F
    public static String getFirstToUpper(String str) {
        return getFirst(str).toUpperCase();
    }

    // Returns the first character of the string in lower case: foo -> f
    public static String getFirstToLower(String str) {
        return getFirst(str).toLowerCase();
    }

    // Returns the index of upper case letter ch in alphabets: A --> 0
    public static int mapLetterInAlphabets(String str) {
        char ch = str.charAt(0);
        int position;
        switch(ch) {
            case 'A':
                position=0;
                break;
            case 'B':
                position=1;
                break;
            case 'C':
                position=2;
                break;
            case 'D':
                position=3;
                break;
            case 'E':
                position=4;
                break;
            case 'F':
                position=5;
                break;
            case 'G':
                position=6;
                break;
            case 'H':
                position=7;
                break;
            case 'I':
                position=8;
                break;
            case 'J':
                position=9;
                break;
            case 'K':
                position=10;
                break;
            case 'L':
                position=11;
                break;
            case 'M':
                position=12;
                break;
            case 'N':
                position=13;
                break;
            case 'O':
                position=14;
                break;
            case 'P':
                position=15;
                break;
            case 'Q':
                position=16;
                break;
            case 'R':
                position=17;
                break;
            case 'S':
                position=18;
                break;
            case 'T':
                position=19;
                break;
            case 'U':
                position=20;
                break;
            case 'V':
                position=21;
                break;
            case 'W':
                position=22;
                break;
            case 'X':
                position=23;
                break;
            case 'Y':
                position=24;
                break;
            case 'Z':
                position=25;
                break;
            default:
                position=0;
                break;
        }
        return position;
    }
}