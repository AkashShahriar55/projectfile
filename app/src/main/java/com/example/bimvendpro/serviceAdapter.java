package com.example.bimvendpro;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class serviceAdapter extends PagerAdapter {

    private List<serviceNotificationItem> services;
    private LayoutInflater layoutInflater;
    private Context context;

    public serviceAdapter(List<serviceNotificationItem> services, Context context) {
        this.services = services;
        this.context = context;
    }

    @Override
    public int getCount() {
        return services.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.services_item,container,false);


        TextView serviceTitle;

        serviceTitle = view.findViewById(R.id.serviceTitle);


        serviceTitle.setText(services.get(position).getLocation());

        container.addView(view,0);
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
