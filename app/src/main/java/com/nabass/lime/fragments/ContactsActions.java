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
import com.nabass.lime.R;
import com.nabass.lime.db.DBExtended;

public class ContactsActions extends DialogFragment {

    private static final String TAG = "ContactsAction";

    private static Bundle contactBundle;

    public ContactsActions() {
        // Required empty public constructor
    }

    public static ContactsActions newInstance(Bundle bundle) {
        contactBundle = bundle;
        return new ContactsActions();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int padding = getResources().getDimensionPixelSize(R.dimen.chat_actions_padding);

        View divider1 = new View(getActivity());
        divider1.setBackgroundColor(Color.BLACK);

        View divider2 = new View(getActivity());
        divider2.setBackgroundColor(Color.BLACK);

        TextView viewContact = new TextView(getActivity());
        viewContact.setText("View contact");
        viewContact.setPadding(padding, padding, padding, padding);
        viewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: view contact
                String contact_email = contactBundle.getString(Constants.CONTACT_EMAIL);
                Toast.makeText(getActivity().getApplicationContext(), contact_email, Toast.LENGTH_LONG)
                        .show();
                refreshSearchView();
                Contacts.contactsActionsDialog.dismiss();
            }
        });

        TextView blockContact = new TextView(getActivity());
        blockContact.setText("Block contact");
        blockContact.setPadding(padding, padding, padding, padding);
        blockContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: block contact
                String contact_email = contactBundle.getString(Constants.CONTACT_EMAIL);
                Toast.makeText(getActivity().getApplicationContext(), contact_email + " blocked", Toast.LENGTH_LONG)
                        .show();
                refreshSearchView();
                Contacts.contactsActionsDialog.dismiss();
            }
        });

        TextView deleteContact = new TextView(getActivity());
        deleteContact.setText("Delete contact");
        deleteContact.setPadding(padding, padding, padding, padding);
        deleteContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: delete contact
                String contact_email = contactBundle.getString(Constants.CONTACT_EMAIL);
                DBExtended.deleteContactByEmail(getActivity().getContentResolver(), contact_email);
                Toast.makeText(getActivity().getApplicationContext(), contact_email + " deleted", Toast.LENGTH_LONG)
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
