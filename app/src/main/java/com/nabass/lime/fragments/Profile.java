package com.nabass.lime.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nabass.lime.R;

import static com.nabass.lime.Init.getDisplayName;
import static com.nabass.lime.Init.getPhoneNum;
import static com.nabass.lime.Init.getSenderId;

public class Profile extends Fragment {

    public Profile(){
        //Nothing for now
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView name = (TextView) rootView.findViewById(R.id.profile_name);
        TextView phone = (TextView) rootView.findViewById(R.id.profile_phone_num);
        TextView senderID = (TextView) rootView.findViewById(R.id.profile_sender_id);

        name.setText(getDisplayName());
        phone.setText(getPhoneNum());
        senderID.setText(getSenderId());

        return rootView;
    }
}
