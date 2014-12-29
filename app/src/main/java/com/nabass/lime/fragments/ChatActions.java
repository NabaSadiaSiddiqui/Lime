package com.nabass.lime.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nabass.lime.Constants;
import com.nabass.lime.Init;
import com.nabass.lime.R;
import com.nabass.lime.db.CustomCP;


public class ChatActions extends DialogFragment {

    private static Bundle profileBundle;
    private Chat.OnFragmentInteractionListener mListener;


    public ChatActions() {
        // Required empty public constructor
    }

    public static ChatActions newInstance(Bundle bundle) {
        profileBundle = bundle;
        return new ChatActions();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int padding = getResources().getDimensionPixelSize(R.dimen.chat_actions_padding);

        TextView viewContact = new TextView(getActivity());
        viewContact.setPadding(padding, padding, padding, padding);
        viewContact.setText("View contact");
        viewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: view contact
                Toast.makeText(getActivity(), "View contact", Toast.LENGTH_LONG)
                        .show();
            }
        });

        View divider = new View(getActivity());
        divider.setBackgroundColor(Color.BLACK);

        TextView deleteChat = new TextView(getActivity());
        deleteChat.setText("Clear conversation");
        deleteChat.setPadding(padding, padding, padding, padding);
        deleteChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: clear conversation
                mListener.onFragmentInteraction(Constants.FRAG_CHAT_ACTIONS, profileBundle);
            }
        });

        LinearLayout ll = new LinearLayout(getActivity().getApplicationContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(viewContact);
        ll.addView(divider, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
        ll.addView(deleteChat);

        return new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .setView(ll)
                .create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (Chat.OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

}
