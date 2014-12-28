package com.nabass.lime.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.nabass.lime.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class Settings extends PreferenceFragment {


    public Settings() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        // Hide search bar
        menu.findItem(R.id.action_search).setVisible(false).setEnabled(false);
    }

}
