package com.nabass.lime.chat.adapter;

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

import static com.nabass.lime.Init.formatStringCamelCase;

public class ChatCursorAdapter extends CursorAdapter {

    private LayoutInflater mInflater;

    public ChatCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override public int getCount() {
        return getCursor() == null ? 0 : super.getCount();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View rootView = mInflater.inflate(R.layout.chat_list_item, parent, false);
        ViewHolder holder = new ViewHolder();
        rootView.setTag(holder);
        holder.avatar = (ImageView) rootView.findViewById(R.id.avatar);
        holder.chat_name = (TextView) rootView.findViewById(R.id.chat_name);
        holder.chat_msg = (TextView) rootView.findViewById(R.id.chat_msg);
        return rootView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder rootView = (ViewHolder) view.getTag();
        String name = formatStringCamelCase(cursor.getString(cursor.getColumnIndex(CustomCP.COL_NAME)));
        rootView.chat_name.setText(name);

        int count = cursor.getInt(cursor.getColumnIndex(CustomCP.COL_COUNT));
        rootView.chat_msg.setText(String.format("%d new message%s", count, count==1 ? "" : "s"));
    }

    private static class ViewHolder {
        ImageView avatar;
        TextView chat_name;
        TextView chat_msg;
    }
}
