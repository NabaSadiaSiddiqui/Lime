package com.nabass.lime.fragments;

import android.app.Activity;
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
import android.widget.Toast;

import com.nabass.lime.R;
import com.nabass.lime.contacts.adapter.ContactsCursorAdapter;
import com.nabass.lime.Constants;
import com.nabass.lime.db.DBConstants;

public class Contacts extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    private static final String TAG = "Contacts";

    private OnFragmentInteractionListener mListener;
    private ContactsCursorAdapter mAdapter;

    public Contacts() {
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
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        // Set the adapter
        ListView mListView = (ListView) view.findViewById(R.id.contacts);
        mAdapter = new ContactsCursorAdapter(getActivity().getApplicationContext(), null);

        mListView.setAdapter(mAdapter);
        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener((AdapterView.OnItemClickListener) this);
        getLoaderManager().initLoader(0, null, this);

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        // hide new icon and show search view
        menu.findItem(R.id.action_new).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(true);
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
                Log.e(TAG, "Implement things here");
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //TODO: open conversation
        Bundle bundle = new Bundle();
        bundle.putString(Constants.CONTACT_ID, String.valueOf(l));
        mListener.onFragmentInteraction(Constants.FRAG_CONTACTS, bundle);
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
        // TODO: Update argument type and name
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
