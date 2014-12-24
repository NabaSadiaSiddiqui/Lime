package com.nabass.lime.nav.drawer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

    Integer[] imageIDs;
    private Context context;

    public ImageAdapter(Context c, Integer[] imgIDs)
    {
        context = c;
        imageIDs = imgIDs;
    }

    // Returns the number of images
    @Override
    public int getCount() {
        return imageIDs.length;
    }

    // Returns the ID of an item
    @Override
    public Object getItem(int position) {
        return position;
    }

    // Returns the ID of an item
    @Override
    public long getItemId(int position) {
        return position;
    }

    // returns an ImageView view
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(imageIDs[position]);
        imageView.setLayoutParams(new Gallery.LayoutParams(300, 300));
        return imageView;
    }
}
