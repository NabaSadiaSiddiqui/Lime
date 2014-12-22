package com.nabass.lime;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.nabass.lime.db.CustomCP;

public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {
    ListView listView;
    private ContactCursorAdapter ContactCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if user is logged in remote/local model and redirect appropriately
        if(Common.MODE==null) {
            Intent intent = new Intent(this, AuthPage.class);
            startActivity(intent);
        } else {
            setContentView(R.layout.activity_main);
            listView = (ListView) findViewById(R.id.contactslist);
            listView.setOnItemClickListener(this);
            ContactCursorAdapter = new ContactCursorAdapter(this, null);
            listView.setAdapter(ContactCursorAdapter);
            getLoaderManager().initLoader(0, null, this);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_add:
                AddContactDialog addContact = AddContactDialog.newInstance();
                addContact.show(getFragmentManager(), "Add Contact");
                return true;
            case R.id.action_settings:
                //TODO: implement settings
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
        //TODO: open conversation activity
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(this,
                CustomCP.CONTENT_URI_PROFILE,
                new String[]{CustomCP.COL_ID, CustomCP.COL_NAME, CustomCP.COL_EMAIL, CustomCP.COL_COUNT},
                null,
                null,
                CustomCP.COL_ID + " DESC");
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        ContactCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        ContactCursorAdapter.swapCursor(null);
    }

    public class ContactCursorAdapter extends CursorAdapter {

        private LayoutInflater mInflater;

        public ContactCursorAdapter(Context context, Cursor c) {
            super(context, c, 0);
            this.mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override public int getCount() {
            return getCursor() == null ? 0 : super.getCount();
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View itemLayout = mInflater.inflate(R.layout.main_list_item, parent, false);
            ViewHolder holder = new ViewHolder();
            itemLayout.setTag(holder);
            holder.text1 = (TextView) itemLayout.findViewById(R.id.text1);
            holder.text2 = (TextView) itemLayout.findViewById(R.id.text2);
            holder.textEmail = (TextView) itemLayout.findViewById(R.id.textEmail);
            holder.avatar = (ImageView) itemLayout.findViewById(R.id.avatar);
            return itemLayout;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ViewHolder holder = (ViewHolder) view.getTag();
            holder.text1.setText(cursor.getString(cursor.getColumnIndex(CustomCP.COL_NAME)));
            holder.textEmail.setText(cursor.getString(cursor.getColumnIndex(CustomCP.COL_EMAIL)));
            int count = cursor.getInt(cursor.getColumnIndex(CustomCP.COL_COUNT));
            if (count > 0){
                holder.text2.setVisibility(View.VISIBLE);
                holder.text2.setText(String.format("%d new message%s", count, count==1 ? "" : "s"));
            }else
                holder.text2.setVisibility(View.GONE);
        }
    }

    private static class ViewHolder {
        TextView text1;
        TextView text2;
        TextView textEmail;
        ImageView avatar;
    }
}
