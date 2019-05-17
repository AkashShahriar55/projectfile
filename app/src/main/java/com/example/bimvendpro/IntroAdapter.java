package com.example.bimvendpro;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IntroAdapter extends PagerAdapter {


    Context context;
    LayoutInflater layoutInflater;

    public IntroAdapter(Context context) {
        this.context = context;
    }

    public int[] slideImages = {
        R.drawable.ic_intro_dash,
        R.drawable.ic_intro_machine,
        R.drawable.ic_intro_location,
        R.drawable.ic_intro_trip,
    };

    public String[] slide_headings = {
            "Dashboard",
            "Vending Machines",
            "Locations",
            "Trips",
    };

    public String[] slide_description = {
            "You can see your TOP FIVE machines in a Pie-chart and last five months income Bar statistics. There is also some important notices",
            "You can manage your machines,install them to locations,and add products to your machine.Track how is your machine do",
            "You can add location of your vending machines and you can find its locations in google map.You can install machine to locations and store info",
            "You can make trip to service your machine.And track your vending revenue records and how is your machine is doing.Enjoy",
    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (LinearLayout) o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.intro_item_layout,container,false);

        ImageView imageView = view.findViewById(R.id.intro_image);
        TextView textViewHeading = view.findViewById(R.id.intro_heading);
        TextView textViewDesc = view.findViewById(R.id.intro_description);

        imageView.setImageResource(slideImages[position]);
        textViewHeading.setText(slide_headings[position]);
        textViewDesc.setText(slide_description[position]);


        container.addView(view);


        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}
