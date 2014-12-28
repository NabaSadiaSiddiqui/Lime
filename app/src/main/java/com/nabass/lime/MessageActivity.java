package com.nabass.lime;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nabass.lime.db.DBConstants;
import com.nabass.lime.fragments.EditContact;
import com.nabass.lime.fragments.Message;
import com.nabass.lime.network.GcmUtil;
import com.nabass.lime.network.ServerUtilities;

import java.io.IOException;

public class MessageActivity extends FragmentActivity implements Message.OnFragmentInteractionListener, EditContact.OnFragmentInteractionListener, OnClickListener {

    private EditText msgEdit;
    private Button sendBtn;
    private String profileId;
    private String profileName;
    private String profileEmail;
    private GcmUtil gcmUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        profileId = getIntent().getStringExtra(Init.PROFILE_ID);
        msgEdit = (EditText) findViewById(R.id.msg_edit);
        sendBtn = (Button) findViewById(R.id.send_btn);
        sendBtn.setOnClickListener(this);

        Cursor c = getContentResolver().query(Uri.withAppendedPath(DBConstants.DB_CONTACTS, profileId), null, null, null, null);
        if (c.moveToFirst()) {
            profileName = c.getString(c.getColumnIndex(DBConstants.COL_NAME));
            profileEmail = c.getString(c.getColumnIndex(DBConstants.COL_EMAIL));
        }

        registerReceiver(registrationStatusReceiver, new IntentFilter(Init.ACTION_REGISTER));
        gcmUtil = new GcmUtil(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //TODO: copy MainActivity.opOptionsItemSelected
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.send_btn:
                send(msgEdit.getText().toString());
                msgEdit.setText(null);
                break;
        }
    }

    @Override
    public void onEditContact(String name) {
        //TODO: edit contact name
    }

    @Override
    public String getProfileEmail() {
        return profileEmail;
    }

    private void send(final String txt) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    ContentValues values = new ContentValues(2);
                    values.put(DBConstants.COL_MSG_TYPE,  DBConstants.MsgDirection.DIRECTION_OUTGOING.ordinal());
                    values.put(DBConstants.COL_MSG, txt);
                    values.put(DBConstants.COL_RECIPIENT_ID, profileEmail);
                    values.put(DBConstants.COL_SENDER_ID, Init.getPreferredEmail());
                    getContentResolver().insert(DBConstants.DB_MSGS, values);
                    //TODO: sleep process for 100 ms so that vertical orientation of messages view is fine
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ServerUtilities.send(txt, profileEmail);

                } catch (IOException ex) {
                    msg = "Message could not be sent";
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (!TextUtils.isEmpty(msg)) {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
            }
        }.execute(null, null, null);
    }

    @Override
    protected void onPause() {
        ContentValues values = new ContentValues(1);
        values.put(DBConstants.COL_MSG_COUNT, 0);
        getContentResolver().update(Uri.withAppendedPath(DBConstants.DB_CONTACTS, profileId), values, null, null);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(registrationStatusReceiver);
        gcmUtil.cleanup();
        super.onDestroy();
    }

    private BroadcastReceiver registrationStatusReceiver = new  BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && Init.ACTION_REGISTER.equals(intent.getAction())) {
                switch (intent.getIntExtra(Init.EXTRA_STATUS, 100)) {
                    case Init.STATUS_SUCCESS:
                        sendBtn.setEnabled(true);
                        break;

                    case Init.STATUS_FAILED:
                        break;
                }
            }
        }
    };

}
