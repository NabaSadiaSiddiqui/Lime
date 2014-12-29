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

    private static class ViewHolder {
        ImageView contact_img;
        TextView contact_id;
    }

    @Override public int getCount() {
        return getCursor() == null ? 0 : super.getCount();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View itemLayout = mInflater.inflate(R.layout.contacts_list_item, parent, false);

        ViewHolder holder = new ViewHolder();
        itemLayout.setTag(holder);

        holder.contact_img = (ImageView) itemLayout.findViewById(R.id.contact_img);
        holder.contact_id = (TextView) itemLayout.findViewById(R.id.contact_id);

        return itemLayout;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        String email = cursor.getString(cursor.getColumnIndex(DBConstants.COL_EMAIL));
        String initial = getFirstToUpper(email);
        String[] colors = view.getResources().getStringArray(R.array.chat_img_view);
        int position = mapLetterInAlphabets(initial);
        int color = Color.parseColor(colors[position]);
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(initial, color);

        holder.contact_img.setImageDrawable(drawable);
        holder.contact_id.setText(email);
    }
}
