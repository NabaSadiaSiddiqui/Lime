package com.nabass.lime.conversation.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.nabass.lime.R;
import com.nabass.lime.db.DBConstants;

import static com.nabass.lime.Util.getTime;

public class ConversationCursorAdapter extends CursorAdapter {

    public ConversationCursorAdapter(Context context, Cursor c) {
			super(context, c, 0);
	}

    @Override public int getCount() {
        return getCursor() == null ? 0 : super.getCount();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int _position) {
        Cursor cursor = (Cursor) getItem(_position);
        return getItemViewType(cursor);
    }

    private int getItemViewType(Cursor _cursor) {
        int typeIdx = _cursor.getColumnIndex(DBConstants.TBL_MSGS_COLS.COL_MSG_TYPE);
        int type = _cursor.getInt(typeIdx);
        return type == 0 ? 0 : 1;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        View itemLayout = null;
        switch(getItemViewType(cursor)){
        case 0:
            itemLayout = LayoutInflater.from(context).inflate(R.layout.msg_list_item_other, parent, false);
            break;
        case 1:
            itemLayout = LayoutInflater.from(context).inflate(R.layout.msg_list_item_self, parent, false);
            break;
        }
        itemLayout.setTag(holder);
        holder.msg = (TextView) itemLayout.findViewById(R.id.im_msg);
        holder.time = (TextView) itemLayout.findViewById(R.id.im_time);
        return itemLayout;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.msg.setText(new String(cursor.getBlob(cursor.getColumnIndex(DBConstants.TBL_MSGS_COLS.COL_MSG))));
        holder.time.setText(getTime(cursor.getString(cursor.getColumnIndex(DBConstants.TBL_MSGS_COLS.COL_TIME))));
    }

    private static class ViewHolder {
        TextView time;
        TextView msg;
    }
}