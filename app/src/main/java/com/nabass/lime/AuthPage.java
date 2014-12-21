package com.nabass.lime;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.nabass.lime.R;

public class AuthPage extends Activity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_page);

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
        Toast.makeText(getApplicationContext(), "in onActivityResult", Toast.LENGTH_SHORT);
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
        Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
    }

    // Try to reconnect to service if suspended
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    // Try to reconnect to service if disconnected
    public void onDisconnected() {
        //TODO: what to do if disconnected!
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sign_in_button
                && !mGoogleApiClient.isConnecting()) {
            Toast.makeText(view.getContext(), "Remote mode", Toast.LENGTH_SHORT)
                    .show();
            mSignInClicked = true;
            resolveSignInError();
            //TODO: set mode to SERVER and start application flow
        } else if(view.getId() == R.id.local_mode_button) {
            Toast.makeText(view.getContext(), "Local Mode", Toast.LENGTH_SHORT)
                    .show();
            //TODO: set mode to LOCAL and start application flow
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.auth_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
