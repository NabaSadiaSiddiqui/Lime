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

}