package com.nabass.lime;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;

import com.nabass.lime.db.DBExtended;
import com.nabass.lime.network.GcmUtil;

import java.util.ArrayList;

import static com.nabass.lime.db.TBLProfile.updateProfileStatus;

public class Init extends Application {
    private static SharedPreferences prefs;
    private static GcmUtil gcm;

    @Override
    public void onCreate() {
        super.onCreate();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Set client email (from accounts)
        //TODO: use this when removing Google+ sign in
        //setClientEmail();

        registerReceiver(gcmRegStatus, new IntentFilter(Constants.ACTION_REGISTER));
        gcm = new GcmUtil(getApplicationContext());

        syncLocalContacts(getContentResolver());
    }

    private static void syncLocalContacts(ContentResolver cr) {
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
                null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String phoneNumber = Constants.STR_NULL;
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                {
                    // Query phone here. Covered next
                    Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                    while (phones.moveToNext()) {
                        phoneNumber += phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)) + ",";
                    }
                    phones.close();

                    if (!TextUtils.isEmpty(phoneNumber)) {
                        phoneNumber = phoneNumber.substring(0,phoneNumber.length()-1);
                    }
                    //TODO: add local contacts
                    DBExtended.addLocalContact(cr, name, phoneNumber, id);
                }
            }
        }
    }


    public static void setSharedPref(String key, String val) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, val);
        editor.apply();
    }

    public static void setModeLocal() {
        setSharedPref(Constants.KEY_MODE_USER, Constants.MODE_LOCAL);
    }

    public static void setModeRemote() {
        setSharedPref(Constants.KEY_MODE_USER, Constants.MODE_REMOTE);
    }

    public static String getMode() {
        return prefs.getString(Constants.KEY_MODE_USER, null);
    }

    private static Account getAccount(AccountManager accountManager) {
        Account[] accounts = accountManager.getAccountsByType(Constants.AUTH_SERVICE);
        Account account;
        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        }
        return account;
    }

    public static String getClientImg() {
        return prefs.getString(Constants.KEY_CLIENT_IMG, Constants.STR_NULL);
    }

    public static boolean isNotify() {
        return prefs.getBoolean(Constants.KEY_NEW_MSG, true);
    }
    public static String getRingtone() {
        return prefs.getString(Constants.KEY_RINGTONE, android.provider.Settings.System.DEFAULT_NOTIFICATION_URI.toString());
    }

    public static String getServerUrl() {
        return prefs.getString(Constants.KEY_SERVER_URL, Constants.SERVER_URL);
    }

    public static String getSenderId() {
        return prefs.getString(Constants.KEY_SENDER_ID, Constants.SENDER_ID);
    }

    public static String getFirst(String str) {
        return str.substring(0,1);
    }

    public static String getFirstToUpper(String str) {
        return getFirst(str).toUpperCase();
    }

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

    private BroadcastReceiver gcmRegStatus = new  BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && Constants.ACTION_REGISTER.equals(intent.getAction())) {
                switch (intent.getIntExtra(Constants.EXTRA_STATUS, 100)) {
                    case Constants.STATUS_SUCCESS:
                        updateProfileStatus(getContentResolver(), "ONLINE");
                        break;
                    case Constants.STATUS_FAILED:
                        updateProfileStatus(getContentResolver(), "OFFLINE");
                        break;
                }
            }
        }
    };
}