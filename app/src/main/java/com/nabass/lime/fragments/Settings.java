package com.nabass.lime.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.nabass.lime.R;

public class Settings extends PreferenceFragment {


    public Settings() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

}
