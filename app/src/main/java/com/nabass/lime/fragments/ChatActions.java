package com.nabass.lime.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nabass.lime.Constants;
import com.nabass.lime.MainActivity;
import com.nabass.lime.R;
import com.nabass.lime.db.TBLMsgs;


public class ChatActions extends DialogFragment {

    private static Bundle contactBundle;

    public ChatActions() {
        // Required empty public constructor
    }

    public static ChatActions newInstance(Bundle bundle) {
        contactBundle = bundle;
        return new ChatActions();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int padding = getResources().getDimensionPixelSize(R.dimen.chat_actions_padding);

        View divider = new View(getActivity());
        divider.setBackgroundColor(Color.BLACK);

        TextView clearChat = new TextView(getActivity());
        clearChat.setText("Clear chat");
        clearChat.setPadding(padding, padding, padding, padding);
        clearChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contact_email = contactBundle.getString(Constants.CONTACT_EMAIL);
                TBLMsgs.clearChatByEmail(MainActivity.contentResolver, contact_email);
                Toast.makeText(getActivity().getApplicationContext(), "Chat with " + contact_email + " cleared", Toast.LENGTH_LONG)
                        .show();
                Chat.chatActionsDialog.dismiss();
            }
        });

        TextView deleteChat = new TextView(getActivity());
        deleteChat.setText("Delete chat");
        deleteChat.setPadding(padding, padding, padding, padding);
        deleteChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contact_email = contactBundle.getString(Constants.CONTACT_EMAIL);
                TBLMsgs.deleteChatByEmail(getActivity().getContentResolver(), contact_email);
                Toast.makeText(getActivity().getApplicationContext(), "Chat with " + contact_email + " deleted", Toast.LENGTH_LONG)
                        .show();
                Chat.chatActionsDialog.dismiss();
            }
        });

        LinearLayout ll = new LinearLayout(getActivity().getApplicationContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(clearChat);
        ll.addView(divider, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
        ll.addView(deleteChat);

        return new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .setView(ll)
                .create();
    }
}