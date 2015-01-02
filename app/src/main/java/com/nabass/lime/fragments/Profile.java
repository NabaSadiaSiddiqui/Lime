package com.nabass.lime.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nabass.lime.MainActivity;
import com.nabass.lime.R;
import com.nabass.lime.Util;

import java.io.File;

import static com.nabass.lime.Init.getSenderId;
import static com.nabass.lime.db.TBLProfile.getProfileEmail;
import static com.nabass.lime.db.TBLProfile.getProfileName;
import static com.nabass.lime.db.TBLProfile.getProfileStatus;

public class Profile extends DialogFragment {

    private static final String TAG = "Profile";
    private static final int SELECT_PICTURE = 1;
    private Uri outputFileUri;

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
        TextView email = (TextView) dialoglayout.findViewById(R.id.profile_email);
        TextView status = (TextView) dialoglayout.findViewById(R.id.profile_status);
        TextView senderID = (TextView) dialoglayout.findViewById(R.id.profile_sender_id);
        ImageButton img = (ImageButton) dialoglayout.findViewById(R.id.profile_edit_img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    // start intent to select or take photo
                setSelectedImageUri();
                Intent pickIntent = new Intent();
                pickIntent.setType("image/*");
                pickIntent.setAction(Intent.ACTION_GET_CONTENT);

                Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                String pickTitle = getResources().getString(R.string.profile_img_desc);
                Intent chooserIntent = Intent.createChooser(pickIntent, pickTitle);
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePhotoIntent});

                startActivityForResult(chooserIntent, SELECT_PICTURE);
            }
        });


        // Query database to get person's details
        String personName = getProfileName(MainActivity.contentResolver);
        String personEmail = getProfileEmail(MainActivity.contentResolver);
        String personStatus = getProfileStatus(MainActivity.contentResolver);

        name.setText(personName);
        email.setText(personEmail);
        status.setText(personStatus);
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

    private void setSelectedImageUri() {
        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "MyDir" + File.separator);
        if(!root.mkdirs()) {
            Log.e(TAG, "Error creating directory");
        }
        final String fname = Util.getUniqueImageFileName();
        final File sdImageMainDirectory = new File(root, fname);
        outputFileUri = Uri.fromFile(sdImageMainDirectory);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK) {
            final boolean isCamera;
            if(data == null) {
                isCamera = true;
            } else {
                final String action = data.getAction();

                if(action == null) {
                    isCamera = false;
                } else
                {
                    isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                }
            }

            Uri selectedImageUri;
            if(isCamera) {
                selectedImageUri = outputFileUri;
            } else {
                selectedImageUri = data == null ? null : data.getData();
            }
            Util.changeUserPic(selectedImageUri);
        }
    }
}
