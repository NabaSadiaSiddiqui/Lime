package com.nabass.lime.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.nabass.lime.R;
import com.nabass.lime.chat.adapter.ChatCursorAdapter;
import com.nabass.lime.Constants;
import com.nabass.lime.db.DBConstants;

public class Chat extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private OnFragmentInteractionListener mListener;
    private ChatCursorAdapter mAdapter;
    public static DialogFragment chatActionsDialog;

    public Chat() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        // Set the adapter
        ListView mListView = (ListView) view.findViewById(R.id.chatslist);
        mAdapter = new ChatCursorAdapter(getActivity().getApplicationContext(), null);

        mListView.setAdapter(mAdapter);
        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener((AdapterView.OnItemClickListener) this);
        mListView.setOnItemLongClickListener((AdapterView.OnItemLongClickListener) this);
        getLoaderManager().initLoader(0, null, this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        SearchView sv = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // implementing the search view listener
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                Toast.makeText(getActivity().getApplicationContext(), s, Toast.LENGTH_SHORT)
                        .show();
                Log.e("Chat.java", "Implement things here");
                // Return false to let the SearchView perform the default action.
                // Returning true indicates that the listener already performed teh default action
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //TODO: open conversation
        Bundle bundle = new Bundle();
        bundle.putString(Constants.CONTACT_ID, String.valueOf(l));
        mListener.onFragmentInteraction(Constants.FRAG_CHAT, bundle);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        //TODO: open dialog with option to view contact or delete chat
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.

        TextView contactEmailView = (TextView) view.findViewById(R.id.chat_id);
        String contactEmail = contactEmailView.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.CONTACT_EMAIL, contactEmail);


        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag(Constants.TAG_DIALOG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        chatActionsDialog = ChatActions.newInstance(bundle);
        chatActionsDialog.show(ft, Constants.TAG_DIALOG);

        // Returns true to stop event propagation to onItemClick
        return true;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Implement this in the main activity
        public void onFragmentInteraction(String frag, Bundle bundle);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(getActivity().getApplicationContext(),
                DBConstants.DB_CONTACTS,
                new String[]{DBConstants.COL_ID, DBConstants.COL_NAME, DBConstants.COL_EMAIL, DBConstants.COL_MSG_FRESH},
                null,
                null,
                DBConstants.COL_ID + " DESC");
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }

}
