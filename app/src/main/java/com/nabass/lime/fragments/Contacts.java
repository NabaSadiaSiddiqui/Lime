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
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.nabass.lime.MainActivity;
import com.nabass.lime.R;
import com.nabass.lime.contacts.adapter.ContactsCursorAdapter;
import com.nabass.lime.Constants;
import com.nabass.lime.db.DBConstants;
import com.nabass.lime.db.DBExtended;

public class Contacts extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private static final String TAG = "Contacts";

    private OnFragmentInteractionListener mListener;
    private ContactsCursorAdapter mAdapter;
    private boolean queryListener = false;
    public static SearchView sv;
    public static DialogFragment contactsActionsDialog;

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
        ListView mListView = (ListView) view.findViewById(R.id.contacts);
        // Set the adapter
        mAdapter = new ContactsCursorAdapter(getActivity().getApplicationContext(), null);
        mListView.setAdapter(mAdapter);

        // Define filter query provider which will run a query on a given character sequence
        mAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence charSequence) {
                Log.e(TAG, "Calling runQuery");
                Cursor c = DBExtended.dynamicSearchContactByEmail(MainActivity.contentResolver, charSequence);
                if (c.moveToFirst()) {
                    return c;
                } else {
                    Log.e(TAG, "No contact found");
                    return c;
                }
            }
        });

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener((AdapterView.OnItemClickListener) this);
        mListView.setOnItemLongClickListener((AdapterView.OnItemLongClickListener) this);
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
        sv = (SearchView) menu.findItem(R.id.action_search).getActionView();

        sv.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // implementing the search view listener

                if (!queryListener) {
                    sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                        @Override
                        public boolean onQueryTextSubmit(String s) {
                            Toast.makeText(getActivity().getApplicationContext(), s, Toast.LENGTH_SHORT)
                                    .show();
                            Cursor c = DBExtended.searchContactByEmail(MainActivity.contentResolver, s);
                            if (c != null) {
                                Log.e(TAG, "C IS NOT NULL");
                                Log.e(TAG, c.toString());
                            } else {
                                Log.e(TAG, "YOU ARE SEARCHING FOR ALIENS");
                            }
                            // Return false to let the SearchView perform the default action.
                            // Returning true indicates that the listener already performed teh default action
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            //execute the adapter filter
                            mAdapter.getFilter().filter(newText);
                            return true;
                        }
                    });

                    queryListener = true;
                }
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
        // Open conversation
        Bundle bundle = new Bundle();
        bundle.putString(Constants.CONTACT_ID, String.valueOf(l));
        mListener.onFragmentInteraction(Constants.FRAG_CONTACTS, bundle);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        // Open dialog with option to view, block or delete
        TextView contactEmailView = (TextView) view.findViewById(R.id.contact_id);
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
        contactsActionsDialog = ContactsActions.newInstance(bundle);
        contactsActionsDialog.show(ft, Constants.TAG_DIALOG);

        // Returns true to stop event propagation to onItemClick
        return true;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String frag, Bundle bundle);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity().getApplicationContext(), DBConstants.DB_CONTACTS, null, null, null, DBConstants.COL_ID + " DESC");
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
