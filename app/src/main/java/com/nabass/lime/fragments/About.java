package com.nabass.lime.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.nabass.lime.R;
import com.nabass.lime.nav.drawer.adapter.ImageAdapter;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class About extends Fragment {

    // Images to display
    Integer[] imageIDs = {
        R.drawable.team,
        R.drawable.member1,
        R.drawable.member2,
        R.drawable.member3,
        R.drawable.member4
    };

    static String[] tags;


    public About() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_about, container, false);

        tags = getActivity().getResources().getStringArray(R.array.about_tag);

        Gallery gallery = (Gallery) view.findViewById(R.id.gallery);
        gallery.setAdapter(new ImageAdapter(view.getContext(), imageIDs));
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position,long id)
            {
                // display the selected image
                ImageView imageView = (ImageView) view.findViewById(R.id.member_img);
                imageView.setImageResource(imageIDs[position]);
                // display the tag associated with the selected image
                TextView textView = (TextView) view.findViewById(R.id.member_tag);
                textView.setText(tags[position]);
            }
        });

        return view;
    }
}

