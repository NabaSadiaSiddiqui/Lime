package com.nabass.lime.chat.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.nabass.lime.R;
import com.nabass.lime.db.DBConstants;

import static com.nabass.lime.Init.getFirstToUpper;
import static com.nabass.lime.Init.mapLetterInAlphabets;

public class ChatCursorAdapter extends CursorAdapter {

    private LayoutInflater mInflater;

    public ChatCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return getCursor() == null ? 0 : super.getCount();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View rootView = mInflater.inflate(R.layout.chat_list_item, parent, false);

        ViewHolder holder = new ViewHolder();
        rootView.setTag(holder);

        holder.chat_img = (ImageView) rootView.findViewById(R.id.chat_img);
        holder.chat_id = (TextView) rootView.findViewById(R.id.chat_id);
        holder.chat_msg = (TextView) rootView.findViewById(R.id.chat_msg);

        return rootView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder rootView = (ViewHolder) view.getTag();

        String email = cursor.getString(cursor.getColumnIndex(DBConstants.COL_EMAIL));
        String initial = getFirstToUpper(email);
        int count = cursor.getInt(cursor.getColumnIndex(DBConstants.COL_MSG_COUNT));
        String[] colors = view.getResources().getStringArray(R.array.chat_img_view);
        int position = mapLetterInAlphabets(initial);
        int color = Color.parseColor(colors[position]);
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(initial, color);

        rootView.chat_img.setImageDrawable(drawable);
        rootView.chat_id.setText(email);
        rootView.chat_msg.setText(String.format("%d new message%s", count, count==1 ? "" : "s"));

        // Highlight new message(s), if any
        if(count>0) {
            rootView.chat_msg.setTextColor(view.getResources().getColor(R.color.chat_new_msg));
        }
    }

    private static class ViewHolder {
        ImageView chat_img;
        TextView chat_id;
        TextView chat_msg;
    }
}
