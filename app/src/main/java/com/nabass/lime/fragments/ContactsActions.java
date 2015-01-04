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
import com.nabass.lime.db.DBExtended;

public class ContactsActions extends DialogFragment {

    private static final String TAG = "ContactsAction";

    private static Bundle contactBundle;
    private static String contactEmail;
    private static boolean isBlocked = false;

    public ContactsActions() {
        // Required empty public constructor
    }

    public static ContactsActions newInstance(Bundle bundle) {
        contactBundle = bundle;
        contactEmail = contactBundle.getString(Constants.CONTACT_EMAIL);

        return new ContactsActions();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String actionView;
        String actionBlockUnblock;
        String actionDelete;

        int padding = getResources().getDimensionPixelSize(R.dimen.chat_actions_padding);

        isBlocked = DBExtended.checkIsBlockedByEmail(MainActivity.contentResolver, contactEmail);
        actionView = getResources().getString(R.string.contact_actions_view);

        if(isBlocked) {
            actionBlockUnblock = getResources().getString(R.string.contact_actions_unblock);
        } else {
            actionBlockUnblock = getResources().getString(R.string.contact_actions_block);
        }
        actionDelete = getResources().getString(R.string.contact_action_delete);


        View divider1 = new View(getActivity());
        divider1.setBackgroundColor(Color.BLACK);

        View divider2 = new View(getActivity());
        divider2.setBackgroundColor(Color.BLACK);

        TextView viewContact = new TextView(getActivity());
        viewContact.setText(actionView);
        viewContact.setPadding(padding, padding, padding, padding);
        viewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), contactEmail, Toast.LENGTH_LONG)
                        .show();
                refreshSearchView();
                Contacts.contactsActionsDialog.dismiss();
            }
        });

        TextView blockContact = new TextView(getActivity());
        blockContact.setText(actionBlockUnblock);
        blockContact.setPadding(padding, padding, padding, padding);
        blockContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isBlocked) {
                    DBExtended.unblockContactByEmail(MainActivity.contentResolver, contactEmail);
                    Toast.makeText(getActivity().getApplicationContext(), contactEmail + " unblocked", Toast.LENGTH_LONG)
                            .show();
                } else {
                    DBExtended.blockContactByEmail(MainActivity.contentResolver, contactEmail);
                    Toast.makeText(getActivity().getApplicationContext(), contactEmail + " blocked", Toast.LENGTH_LONG)
                            .show();
                }
                refreshSearchView();
                Contacts.contactsActionsDialog.dismiss();
            }
        });

        TextView deleteContact = new TextView(getActivity());
        deleteContact.setText(actionDelete);
        deleteContact.setPadding(padding, padding, padding, padding);
        deleteContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBExtended.deleteContactByEmail(MainActivity.contentResolver, contactEmail);
                Toast.makeText(getActivity().getApplicationContext(), contactEmail + " deleted", Toast.LENGTH_LONG)
                        .show();
                refreshSearchView();
                Contacts.contactsActionsDialog.dismiss();
            }
        });

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2);

        LinearLayout ll = new LinearLayout(getActivity().getApplicationContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(viewContact);
        ll.addView(divider1, params);
        ll.addView(blockContact);
        ll.addView(divider2, params);
        ll.addView(deleteContact);

        return new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .setView(ll)
                .create();
    }

    /*
     * Hack to refresh search view when a person simultaneously searches for a contact and performs an action on a view like "Delete"
     */
    public static void refreshSearchView() {
        String some = Contacts.sv.getQuery().toString();
        Contacts.sv.setQuery(some+" ", false);
        Contacts.sv.setQuery(some, false);
    }
}