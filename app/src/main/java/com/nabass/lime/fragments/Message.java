package com.nabass.lime.fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.nabass.lime.Init;
import com.nabass.lime.conversation.adapter.ConversationCursorAdapter;
import com.nabass.lime.db.DBConstants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final DateFormat[] df = new DateFormat[] {
            DateFormat.getDateInstance(), DateFormat.getTimeInstance()};

    private OnFragmentInteractionListener mListener;
    private CursorAdapter mAdapter;
    private static Date now;

    public Message() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        now = new Date();
        mAdapter = new ConversationCursorAdapter(getActivity(), null);
        setListAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setDivider(null);
        getListView().setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        getListView().setStackFromBottom(true);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getListView().setLayoutParams(params);
        Bundle args = new Bundle();
        args.putString(DBConstants.COL_EMAIL, mListener.getProfileEmail());
        getLoaderManager().initLoader(0, args, this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public String getProfileEmail();
    }

    public static String getDisplayTime(String datetime) {
        try {
            Date dt = sdf.parse(datetime);
            if (now.getYear()==dt.getYear() && now.getMonth()==dt.getMonth() && now.getDate()==dt.getDate()) {
                return df[1].format(dt);
            }
            return df[0].format(dt);
        } catch (ParseException e) {
            return datetime;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String profileEmail = args.getString(DBConstants.COL_EMAIL);
        CursorLoader loader = new CursorLoader(getActivity(),
                DBConstants.DB_MSGS,
                null,
                DBConstants.COL_SENDER_ID + " = ? and " + DBConstants.COL_RECIPIENT_ID + " = ?",
                new String[]{Init.getClientEmail(), profileEmail},
                DBConstants.COL_TIME + " ASC");
        return loader;
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

