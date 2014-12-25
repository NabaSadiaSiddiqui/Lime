package com.nabass.lime.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nabass.lime.Constants;
import com.nabass.lime.R;
import com.nabass.lime.db.CustomCP;

public class AddContact extends Fragment {

    private static EditText emailTxt;
    private static EditText pinTxt;
    private static EditText phoneTxt;
    private OnFragmentInteractionListener mListener;


    public AddContact(){
        //Nothing for now
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_add_contact, container, false);

        emailTxt = (EditText) rootView.findViewById(R.id.add_email_address);
        pinTxt = (EditText) rootView.findViewById(R.id.add_pin);
        phoneTxt = (EditText) rootView.findViewById(R.id.add_phone);
        Button btn_ok = (Button) rootView.findViewById(R.id.button_add_contact_ok);
        Button btn_cancel = (Button) rootView.findViewById(R.id.button_add_contact_cancel);

        // Invalidate text (if entered) in other views when a view is focused
        invalidateOtherViews();

        // Set OnClickListener
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addContactToDB(view);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invalidateOtherViews();
                mListener.goToHome();
            }
        });

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void goToHome();
    }

    private void addContactToDB(View view) {
        String email = emailTxt.getText().toString();
        String pin = pinTxt.getText().toString();
        String phone = phoneTxt.getText().toString();

        if(email!=null) {
            if(!isValid(Constants.TYPE_ADD_EMAIL, email)) {
                emailTxt.setError(Constants.ERR_INVALID_EMAIL);
            } else { // add to database
                try {
                    ContentValues values = new ContentValues(2);
                    values.put(CustomCP.COL_NAME, email.substring(0, email.indexOf('@')));
                    values.put(CustomCP.COL_EMAIL, email);
                    getActivity().getContentResolver().insert(CustomCP.CONTENT_URI_PROFILE, values);
                } catch (SQLException e) {
                    throw new SQLException(view.toString() + " has an exception");
                } finally {
                    mListener.goToHome();
                }
            }
        } else if(pin!=null) {
            //TODO: add by pin
            Toast.makeText(view.getContext(), "Add contact by pin", Toast.LENGTH_LONG)
                    .show();
        } else if(phone!=null) {
            //TODO: add by phone number
            Toast.makeText(view.getContext(), "Add contact by phone number", Toast.LENGTH_LONG)
                    .show();
        }
    }

    private boolean isValid(String type, CharSequence input) {
        if(type.equals(Constants.TYPE_ADD_EMAIL)) {
            return Patterns.EMAIL_ADDRESS.matcher(input).matches();
        } else if(type.equals(Constants.TYPE_ADD_PIN)) {
            //TODO: do input check here for pin
            return true;
        } else if(type.equals(Constants.TYPE_ADD_PHONE)) {
            //TODO: do input check here for phone
            return true;
        } else {
            return false;
        }
    }

    private void invalidateOtherViews() {
        emailTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    if(!pinTxt.getText().toString().isEmpty()) {
                        pinTxt.setText(Constants.STR_NULL);
                        pinTxt.setHint(R.string.add_contact_pin_hint);
                    }
                    if(!phoneTxt.getText().toString().isEmpty()) {
                        phoneTxt.setText(Constants.STR_NULL);
                        phoneTxt.setHint(R.string.add_contact_phone_hint);
                    }
                }
            }
        });

        pinTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    if(!emailTxt.getText().toString().isEmpty()) {
                        emailTxt.setText(Constants.STR_NULL);
                        emailTxt.setHint(R.string.add_contact_email_hint);
                    }
                    if(!phoneTxt.getText().toString().isEmpty()) {
                        phoneTxt.setText(Constants.STR_NULL);
                        phoneTxt.setHint(R.string.add_contact_phone_hint);
                    }
                }
            }
        });

        phoneTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    if(!emailTxt.getText().toString().isEmpty()) {
                        emailTxt.setText(Constants.STR_NULL);
                        emailTxt.setHint(R.string.add_contact_email_hint);
                    }
                    if(!pinTxt.getText().toString().isEmpty()) {
                        pinTxt.setText(Constants.STR_NULL);
                        pinTxt.setHint(R.string.add_contact_pin_hint);
                    }
                }
            }
        });
    }
}