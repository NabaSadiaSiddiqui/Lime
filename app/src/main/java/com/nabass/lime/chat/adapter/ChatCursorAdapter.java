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
        View itemLayout = mInflater.inflate(R.layout.chat_list_item, parent, false);
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

    private static class ViewHolder {
        TextView text1;
        TextView text2;
        TextView textEmail;
        ImageView avatar;
    }
}
