package com.nabass.lime;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.nabass.lime.db.TBLProfile;

public class AuthActivity extends Activity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    /* Variables involved in Google+ sign-in */
    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;
    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;
    /* A flag indicating that a PendingIntent is in progress and prevents
     * us from starting further intents
     */
    private boolean mIntentInProgress;
    /* Track whether the sign-in button has been clicked so that we know to resolve
     * all issues preventing sign-in without waiting.
     */
    private boolean mSignInClicked;
    /* Store the connection result from onConnectionFailed callbacks so that we can
     * resolve them when the user clicks sign-in.
     */
    private ConnectionResult mConnectionResult;

    /* Variables for intro video */
    private VideoView mVideo;
    private MediaController mediaControls;
    private ProgressDialog progressDialog;
    private int positionVideo = 0;
    private final String POSITION_VIDEO = "position_video";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        // Initialize GoogleApiClient object
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();

        // Register callback for remote and local sign-in mode button
        SignInButton remoteMode = (SignInButton) findViewById(R.id.sign_in_button);
        Button localMode = (Button) findViewById(R.id.local_mode_button);
        remoteMode.setOnClickListener(this);
        localMode.setOnClickListener(this);

        // Set media controller buttons
        if(mediaControls == null) {
            mediaControls = new MediaController(this);
        }

        // Initialize the VideoView
        mVideo = (VideoView) findViewById(R.id.videoView);
        // Create progress bar while video file is loading
        progressDialog = new ProgressDialog(this);
        // Set the progress bar not cancelable on user's touch
        progressDialog.setCancelable(false);
        // Show the progress bar
        progressDialog.show();

        try {
            // Set the media controller in the VideoView
            mVideo.setMediaController(mediaControls);
            // Set the URI of the video to be played
            String URI_VIDEO = "android.resource://" + getPackageName() + "/" + R.raw.intro;
            mVideo.setVideoURI(Uri.parse(URI_VIDEO));
        } catch(Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        mVideo.requestFocus();
        // Set an setOnPreparedListener to know when video is ready to be played
        mVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                // Close the progress bar and play the video
                progressDialog.dismiss();
                // If we have a position on savedInstanceState, the video should start from there
                mVideo.seekTo(positionVideo);
                if(positionVideo==0) {
                    mVideo.start();
                } else {
                    // Pause video playback if we have come from a resumed position
                    mVideo.pause();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Invoke GoogleApiClient.connect
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        //Invoke GoogleApiClient.disconnect
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Store the video playback position for orientation change
        savedInstanceState.putInt(POSITION_VIDEO, mVideo.getCurrentPosition());
        mVideo.pause();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Get video playback position to resume from previous state
        positionVideo = savedInstanceState.getInt(POSITION_VIDEO);
        mVideo.seekTo(positionVideo);
    }

    /* A helper method to resolve the current ConnectionResult error. */
    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                startIntentSenderForResult(mConnectionResult.getResolution().getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    /* When the GoogleApiClient object is unable to establish a connection,
     * the application is notified of the failure in this callback,
     * where we are passed a ConnectionResult that can be used to resolve the error
     * ConnectionResult.getResolution() is used to retrieve a PendingIntent which, when sent,
     * will allow Google Play services to solicit any user interaction needed to resolve sign in errors
     * (for example by asking the user to select an account, consent to permissions, enable networking, etc).
     */
    public void onConnectionFailed(ConnectionResult result) {
        if (!mIntentInProgress) {
            // Store the ConnectionResult so that we can use it later when the user clicks
            // 'sign-in'.
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }
    }

    // Capture result returned from starting the intent in onConnectionFailed
    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }

        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        mSignInClicked = false;
        //TODO: go back to reading account from Settings when removing Google+ sign in
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personEmail = Plus.AccountApi.getAccountName(mGoogleApiClient);
                String personPIN = currentPerson.getId();
                String personDeviceID = Util.getDeviceID();
                String phoneNum = Util.getDevicePhoneNum();
                // TODO: find a way to store image in the database as well
                Init.setSharedPref(Constants.KEY_CLIENT_IMG, personPhotoUrl);
                TBLProfile.createProfileInAuthMode(MainActivity.contentResolver, personName, personEmail, phoneNum, personPIN, null, personDeviceID);
                Init.setModeRemote();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Person information is null. Try local sign in", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Try to reconnect to service if suspended
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sign_in_button && !mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        } else if(view.getId() == R.id.local_mode_button) {
            Init.setModeLocal();
            setResult(RESULT_OK);
            finish();
        }
    }
}
