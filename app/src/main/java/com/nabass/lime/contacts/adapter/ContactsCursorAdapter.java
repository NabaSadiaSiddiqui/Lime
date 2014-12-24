package com.nabass.lime.contacts.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nabass.lime.R;
import com.nabass.lime.db.CustomCP;

public class ContactsCursorAdapter extends CursorAdapter {

    private LayoutInflater mInflater;

    public ContactsCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override public int getCount() {
        return getCursor() == null ? 0 : super.getCount();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View itemLayout = mInflater.inflate(R.layout.contacts_list_item, parent, false);
        ViewHolder holder = new ViewHolder();
        itemLayout.setTag(holder);
        holder.pic = (ImageView) itemLayout.findViewById(R.id.pic);
        holder.name = (TextView) itemLayout.findViewById(R.id.name);
        return itemLayout;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.name.setText(cursor.getString(cursor.getColumnIndex(CustomCP.COL_NAME)));
    }

    private static class ViewHolder {
        ImageView pic;
        TextView name;
    }
}
