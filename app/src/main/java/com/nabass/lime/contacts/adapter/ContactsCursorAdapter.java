package com.nabass.lime.contacts.adapter;

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

public class ContactsCursorAdapter extends CursorAdapter {

    private LayoutInflater mInflater;

    public ContactsCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private static class ContactsViewHolder {
        ImageView contact_img;
        TextView contact_id;
        ImageView contact_type;
    }

    @Override public int getCount() {
        return getCursor() == null ? 0 : super.getCount();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View itemLayout = mInflater.inflate(R.layout.contacts_list_item, parent, false);

        ContactsViewHolder holder = new ContactsViewHolder();
        itemLayout.setTag(holder);

        holder.contact_img = (ImageView) itemLayout.findViewById(R.id.contact_img);
        holder.contact_id = (TextView) itemLayout.findViewById(R.id.contact_id);
        holder.contact_type = (ImageView) itemLayout.findViewById(R.id.contact_type);

        return itemLayout;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ContactsViewHolder holder = (ContactsViewHolder) view.getTag();

        int type = cursor.getInt(cursor.getColumnIndex(DBConstants.TBL_CONTACTS_COLS.COL_LOCAL));

        if(type==0) {   // server-based contact
            String email = cursor.getString(cursor.getColumnIndex(DBConstants.TBL_CONTACTS_COLS.COL_EMAIL));
            int blocked = cursor.getInt(cursor.getColumnIndex(DBConstants.TBL_CONTACTS_COLS.COL_BLOCKED));
            String initial = getFirstToUpper(email);
            String[] colors = view.getResources().getStringArray(R.array.chat_img_view);
            int position = mapLetterInAlphabets(initial);
            int color = Color.parseColor(colors[position]);
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(initial, color);

            holder.contact_img.setImageDrawable(drawable);
            holder.contact_id.setText(email);
            holder.contact_type.setVisibility(View.GONE);

            if(blocked == 0) {  // Contact is blocked
                view.setBackgroundColor(context.getResources().getColor(R.color.contact_blocked));
            } else {
                view.setBackgroundColor(context.getResources().getColor(R.color.contact_bg));
            }
        } else {    // local contact
            String name = cursor.getString(cursor.getColumnIndex(DBConstants.TBL_CONTACTS_COLS.COL_NAME));
            int blocked = cursor.getInt(cursor.getColumnIndex(DBConstants.TBL_CONTACTS_COLS.COL_BLOCKED));
            String initial = getFirstToUpper(name);
            String[] colors = view.getResources().getStringArray(R.array.chat_img_view);
            int position = mapLetterInAlphabets(initial);
            int color = Color.parseColor(colors[position]);
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(initial, color);

            holder.contact_img.setImageDrawable(drawable);
            holder.contact_id.setText(name);
            holder.contact_type.setVisibility(View.INVISIBLE);

            if(blocked == 0) {  // Contact is blocked
                view.setBackgroundColor(context.getResources().getColor(R.color.contact_blocked));
            } else {
                view.setBackgroundColor(context.getResources().getColor(R.color.contact_bg));
            }
        }

    }
}
