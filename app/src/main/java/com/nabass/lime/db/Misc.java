package com.nabass.lime.db;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;

import com.nabass.lime.Constants;

public class Misc {

    private static final String TAG = "Misc";

    public static void syncLocalContacts(ContentResolver cr) {
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String lookup_key = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));

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
                }
                Log.e(TAG, id);
                Log.e(TAG, name);
                Log.e(TAG, lookup_key);
                Log.e(TAG, phoneNumber);
            }
        }
    }
}
