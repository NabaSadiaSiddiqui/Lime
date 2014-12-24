package com.nabass.lime.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.SQLException;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nabass.lime.db.CustomCP;

public class AddContact extends DialogFragment {
	private AlertDialog alertDialog;
	private EditText et;

	public static AddContact newInstance() {
		AddContact fragment = new AddContact();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		et = new EditText(getActivity());
		et.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		et.setHint("abc@example.com");
		alertDialog = new AlertDialog.Builder(getActivity())
		.setTitle("Add Contact").setMessage("Add Contact")
		.setPositiveButton(android.R.string.ok, null)
		.setNegativeButton(android.R.string.cancel, null)
		.setView(et).create();
		alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
			@Override
			public void onShow(DialogInterface dialog) {
				Button okBtn = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
				okBtn.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						String email = et.getText().toString();
						if (!isEmailValid(email)) {
							et.setError("Invalid email!");
							return;
						}
						try {
							ContentValues values = new ContentValues(2);
							values.put(CustomCP.COL_NAME, email.substring(0, email.indexOf('@')));
							values.put(CustomCP.COL_EMAIL, email);
							getActivity().getContentResolver().insert(CustomCP.CONTENT_URI_PROFILE, values);
						} catch (SQLException sqle) {}
						alertDialog.dismiss();
					}
				});
			}
		});
		return alertDialog;
	}

	private boolean isEmailValid(CharSequence email) {
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}	
}