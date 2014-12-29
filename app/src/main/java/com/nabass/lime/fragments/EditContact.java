package com.nabass.lime.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputFilter;
import android.text.TextUtils;
import android.widget.EditText;

import com.nabass.lime.Constants;
import com.nabass.lime.db.DBConstants;

public class EditContact extends DialogFragment {
	
	private OnFragmentInteractionListener mListener;
	public static EditContact newInstance() {
		EditContact fragment = new EditContact();
		return fragment;
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
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Context ctx = getActivity();
		final String profileId = getArguments().getString(Constants.PROFILE_ID);
		String profileName = getArguments().getString(DBConstants.COL_NAME);
		final EditText et = new EditText(ctx);
		et.setText(profileName);
		et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
			return new AlertDialog.Builder(ctx)
			.setTitle("Edit Contact")
			.setView(et)
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String name = et.getText().toString();
					if (TextUtils.isEmpty(name)) return;
					
					ContentValues values = new ContentValues(1);
					values.put(DBConstants.COL_NAME, name);
					ctx.getContentResolver().update(Uri.withAppendedPath(DBConstants.DB_CONTACTS, profileId), values, null, null);
					
					mListener.onEditContact(name);
				}
			})
			.setNegativeButton(android.R.string.cancel, null)
			.create();
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}
	
	public interface OnFragmentInteractionListener {
		public void onEditContact(String name);
	}	
}