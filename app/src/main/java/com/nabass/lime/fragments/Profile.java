package com.nabass.lime.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import com.nabass.lime.R;
import static com.nabass.lime.Init.getDisplayName;
import static com.nabass.lime.Init.getPhoneNum;
import static com.nabass.lime.Init.getSenderId;

public class Profile extends DialogFragment {

    public Profile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.fragment_profile, null, false);
        TextView name = (TextView) dialoglayout.findViewById(R.id.profile_name);
        TextView phone = (TextView) dialoglayout.findViewById(R.id.profile_phone_num);
        TextView senderID = (TextView) dialoglayout.findViewById(R.id.profile_sender_id);

        //name.setText(getDisplayName());
        phone.setText(getPhoneNum());
        senderID.setText(getSenderId());

        AlertDialog profileDialog = new AlertDialog.Builder(getActivity())
            .setCancelable(true)
            .setView(dialoglayout)
            .create();

        /*
         * Attributes to reposition the dialog box
         */
        WindowManager.LayoutParams wmlp = profileDialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.TOP | Gravity.LEFT;
        wmlp.x = 100;
        wmlp.y = 100;

        return profileDialog;
    }
}
