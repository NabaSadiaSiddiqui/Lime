package com.nabass.lime.fragments;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nabass.lime.Constants;
import com.nabass.lime.MainActivity;
import com.nabass.lime.R;
import com.nabass.lime.conversation.adapter.ConversationCursorAdapter;
import com.nabass.lime.db.DBConstants;
import com.nabass.lime.db.TBLMsgs;
import com.nabass.lime.db.TBLProfile;
import com.nabass.lime.network.ServerUtilities;

import java.io.IOException;

public class Conversation extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "Conversation";

    private ConversationCursorAdapter mAdapter;
    private static String contactsEmail;
    private static EditText msgWidget;

    public Conversation() {
        // Required empty public constructor
    }

    public static Conversation newInstance(Bundle bundle) {
        contactsEmail = bundle.getString(Constants.CONTACT_EMAIL);
        return new Conversation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);
        // Set the adapter
        ListView mListView = (ListView) view.findViewById(R.id.convoList);
        mAdapter = new ConversationCursorAdapter(getActivity().getApplicationContext(), null);
        mListView.setAdapter(mAdapter);

        // Set onClickListener for send button
        Button send = (Button) view.findViewById(R.id.btn_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                switch(id) {
                    case R.id.btn_send:
                        String message = msgWidget.getText().toString();
                        msgWidget.setText(null);
                        sendMessage(message);
                        break;
                }
            }
        });

        msgWidget = (EditText) view.findViewById(R.id.msg);

        Bundle args = new Bundle();
        args.putString(DBConstants.TBL_CONTACTS_COLS.COL_EMAIL, contactsEmail);
        getLoaderManager().initLoader(0, args, this);

        TextView identifier = (TextView) getActivity().getActionBar().getCustomView().findViewById(R.id.identifier);
        identifier.setText(contactsEmail);

        return view;
    }

    @Override
    public void onPause() {
        TBLMsgs.resetFreshMsgCount(MainActivity.contentResolver, contactsEmail);
        TextView identifier = (TextView) getActivity().getActionBar().getCustomView().findViewById(R.id.identifier);
        identifier.setText(Constants.STR_NULL);
        super.onPause();
    }

    private void sendMessage(final String message) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    TBLMsgs.insertOutgoingMsg(MainActivity.contentResolver, message, contactsEmail);
                    //TODO: sleep process for 100 ms so that vertical orientation of messages view is fine
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ServerUtilities.send(message, contactsEmail);

                } catch (IOException ex) {
                    return Constants.ERR_MSG_DELIVERY_FAILED;
                }
                return Constants.STR_NULL;
            }

            @Override
            protected void onPostExecute(String status) {
                if(status.equals(Constants.ERR_MSG_DELIVERY_FAILED)) {
                    Toast.makeText(getActivity().getApplicationContext(), status, Toast.LENGTH_LONG).show();
                }
            }
        }.execute(null, null, null);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String senderEmail = args.getString(DBConstants.TBL_CONTACTS_COLS.COL_EMAIL);
        String receiverEmail = TBLProfile.getProfileEmail(MainActivity.contentResolver);
        String selection = DBConstants.TBL_MSGS_COLS.COL_FROM + " = ? and " + DBConstants.TBL_MSGS_COLS.COL_TO + " = ? ";
        String[] selectionArgs = new String[] {senderEmail, receiverEmail};
        String sort = DBConstants.TBL_MSGS_COLS.COL_TIME + " ASC";
        return new CursorLoader(getActivity(), DBConstants.DB_MSGS, null, selection, selectionArgs, sort);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}