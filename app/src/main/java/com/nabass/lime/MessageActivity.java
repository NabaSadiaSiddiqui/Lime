package com.nabass.lime;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nabass.lime.db.DBConstants;
import com.nabass.lime.db.DBExtended;
import com.nabass.lime.fragments.EditContact;
import com.nabass.lime.fragments.Message;
import com.nabass.lime.network.ServerUtilities;

import java.io.IOException;

public class MessageActivity extends FragmentActivity implements Message.OnFragmentInteractionListener, EditContact.OnFragmentInteractionListener, OnClickListener {

    private EditText msgEdit;
    private Button sendBtn;
    private String profileId;
    private String profileEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        profileId = getIntent().getStringExtra(Constants.CONTACT_ID);
        msgEdit = (EditText) findViewById(R.id.msg_edit);
        sendBtn = (Button) findViewById(R.id.send_btn);
        sendBtn.setOnClickListener(this);

        Cursor c = getContentResolver().query(Uri.withAppendedPath(DBConstants.DB_CONTACTS, profileId), null, null, null, null);
        if (c.moveToFirst()) {
            profileEmail = c.getString(c.getColumnIndex(DBConstants.COL_EMAIL));
        }
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

    private void send(final String message) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    ContentValues values = new ContentValues(2);
                    values.put(DBConstants.COL_MSG_TYPE,  DBConstants.MsgDirection.DIRECTION_OUTGOING.ordinal());
                    values.put(DBConstants.COL_MSG, message);
                    values.put(DBConstants.COL_RECIPIENT_ID, profileEmail);
                    values.put(DBConstants.COL_SENDER_ID, Init.getClientEmail());
                    DBExtended.insertOutgoingMsg(getContentResolver(), values);
                    //TODO: sleep process for 100 ms so that vertical orientation of messages view is fine
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ServerUtilities.send(message, profileEmail);

                } catch (IOException ex) {
                    return Constants.ERR_MSG_DELIVERY_FAILED;
                }
                return Constants.STR_NULL;
            }

            @Override
            protected void onPostExecute(String status) {
                if(status.equals(Constants.ERR_MSG_DELIVERY_FAILED)) {
                    Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
                }
            }
        }.execute(null, null, null);
    }

    @Override
    protected void onPause() {
        DBExtended.resetFreshMsgCount(getContentResolver(), profileEmail);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
