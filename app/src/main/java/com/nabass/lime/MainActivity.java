package com.nabass.lime;

import android.accounts.Account;
import android.app.ActionBar;
import android.app.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nabass.lime.db.DBConstants;
import com.nabass.lime.db.TBLProfile;
import com.nabass.lime.fragments.About;
import com.nabass.lime.fragments.AddContact;
import com.nabass.lime.fragments.Chat;
import com.nabass.lime.fragments.Contacts;
import com.nabass.lime.fragments.Conversation;
import com.nabass.lime.fragments.Profile;
import com.nabass.lime.fragments.Settings;
import com.nabass.lime.nav.drawer.adapter.NavDrawerListAdapter;
import com.nabass.lime.nav.drawer.model.NavDrawerItem;
import com.nabass.lime.synchronization.SyncUtils;
import com.nabass.lime.widgets.CircleImageView;

import java.util.ArrayList;

import static com.nabass.lime.Init.getClientImg;
import static com.nabass.lime.Init.getMode;


public class MainActivity extends Activity implements Chat.OnFragmentInteractionListener, Contacts.OnFragmentInteractionListener, AddContact.OnFragmentInteractionListener {

    private static final String TAG = "MainActivity";

    public static ContentResolver contentResolver;
    public static CircleImageView clientImg;
    public static Context ctx;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    // nav drawer title
    private CharSequence mDrawerTitle;
    // used to store app title
    private CharSequence mTitle;
    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    public static int AUTH_DONE = 1;

    // Constants for sync adapter
    // Instance fields
    public static TableObserver mObserver;
    private static Account mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contentResolver = getContentResolver();
        ctx = getApplicationContext();

        // TODO: create dummy account
        mAccount = SyncUtils.CreateSyncAccount(this);
        // Turn on automatic syncing for the default account and authority
        contentResolver.setSyncAutomatically(mAccount, SyncUtils.AUTHORITY, true);

        // TODO
        /*
         * Create a content observer object.
         * Its code does not mutate the provider, so set
         * selfChange to "false"
         */
         mObserver = new TableObserver(null);
        /*
         * Register the observer for the data table. The table's path
         * and any of its subpaths trigger the observer.
         */
        contentResolver.registerContentObserver(DBConstants.DB_MSGS, true, mObserver);

        mTitle = mDrawerTitle = getTitle();
        // load slide menu items from resources
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        navDrawerItems = new ArrayList<NavDrawerItem>();
        // adding nav drawer items to array
        // Home -- 0
        // Add a contact -- 1
        // etc.
        for (int i = 0; i < navMenuTitles.length; i++) {
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[i], navMenuIcons.getResourceId(i, -1)));
        }
        // Recycle the typed array
        navMenuIcons.recycle();
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setHomeButtonEnabled(true);


        // Set custom icon on action bar
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.actionbar_custom, null);
        clientImg = (CircleImageView) mCustomView.findViewById(R.id.profile_img);
        //If user has previously selected an image then uplaod that
        if(TBLProfile.getProfileImg(contentResolver)!=null) {
            Util.updateUserImg();
        } else if(!getClientImg().equals(Constants.STR_NULL)) {        // If person's G+ profile had an image -> use that
            Util.setUserPicFromURL(clientImg);
        }
        getActionBar().setCustomView(mCustomView);
        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_HOME_AS_UP);


        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // Load fragment intro video and auth if mode is not set
        if(getMode()==null) {
            Intent intent = new Intent(this, AuthActivity.class);
            startActivityForResult(intent, AUTH_DONE);
        } else {
            loadFrags();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        //contentResolver.unregisterContentObserver(mObserver);
    }

    // Called when the person clicks on his photo
    public void showProfile(View view) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag(Constants.TAG_DIALOG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        Profile profileDialog = new Profile();
        profileDialog.show(ft, Constants.TAG_DIALOG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*
         * IMPORTANT: call super.onActivityResult() so fragments'
         * onActivityResult get a chance to be run as well
         */
        super.onActivityResult(requestCode, resultCode, data);
        // Check which request we're responding to
        if (requestCode == AUTH_DONE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                loadFrags();
            }
        }
    }


    private void loadFrags() {
        // Load fragment with recent conversations
        Fragment fragment = new Chat();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, fragment).commit();
    }

    @Override
    public void onFragmentInteraction(String frag, Bundle bundle) {
        if(frag.equals(Constants.FRAG_CHAT) || frag.equals(Constants.FRAG_CONTACTS)) {
            Fragment fragment = Conversation.newInstance(bundle);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();
        }
    }


    @Override
    public void goToHome() {    // AddContact.OnFragmentInteractionListener which opens home fragment that contains all the recent chats
        setTitle("Home");
        Fragment fragment = new Chat();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, fragment).commit();
    }

    /**
     * Slide menu item click listener
     */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.action_search:
                return false;
            case R.id.action_new:
                fragment = new Contacts();
                break;
            default:
                break;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();
            return true;
        } else {
            return super.onOptionsItemSelected(item);

        }
    }

    /**
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_search).setVisible(!drawerOpen);
        menu.findItem(R.id.action_search).setVisible(false).setEnabled(false);
        menu.findItem(R.id.action_new).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Displaying fragment view for selected nav drawer list item
     */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new Chat();  // Home
                break;
            case 1:
                fragment = new AddContact();
                break;
            case 2:
                fragment = new Settings();
                break;
            case 3:
                fragment = new About();
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e(TAG, "Error in creating fragment");
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    public class TableObserver extends ContentObserver {

        public TableObserver(Handler handler) {
            super(handler);
        }

        /*
         * Define a method that's called when data in the
         * observed content provider changes.
         * This method signature is provided for compatibility with
         * older platforms.
         */
        @Override
        public void onChange(boolean selfChange) {
            /*
             * Invoke the method signature available as of
             * Android platform version 4.1, with a null URI.
             */
            Log.e(TAG, "In onChange");

            onChange(selfChange, null);
        }
        /*
         * Define a method that's called when data in the
         * observed content provider changes.
         */
        @Override
        public void onChange(boolean selfChange, Uri changeUri) {
            Log.e(TAG, "In onChange 2");
            /*
             * Ask the framework to run your sync adapter.
             * To maintain backward compatibility, assume that
             * changeUri is null.
             */
            ContentResolver.requestSync(mAccount, SyncUtils.AUTHORITY, null);
        }
    }
}