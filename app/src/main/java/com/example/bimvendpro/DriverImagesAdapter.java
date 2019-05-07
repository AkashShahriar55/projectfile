package com.example.bimvendpro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class DriverImagesAdapter  extends PagerAdapter {
    private List<DriverImages> images;
    private LayoutInflater layoutInflater;
    private Context context;
    private onDeleteClick callBack;
    private String mode;


    public DriverImagesAdapter(List<DriverImages> images, Context context,onDeleteClick callBack,String mode) {
        this.images = images;
        this.context = context;
        this.callBack = callBack;
        this.mode = mode;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        layoutInflater = LayoutInflater.from(context);
        final View view = layoutInflater.inflate(R.layout.driver_images,container,false);


        ImageView imageView;
        FloatingActionButton fab;
        LinearLayout linearLayoutAddImage;


        imageView = view.findViewById(R.id.drv_img);
        fab = view.findViewById(R.id.delete_img);

        if(mode.equals("view")){
            view.findViewById(R.id.delete_img).setVisibility(View.GONE);
        }

        Log.d("something", "initializeViewMode: " + images.get(position).getImgUrl());
        Picasso.get().load(images.get(position).getImgUrl()).into(imageView);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setMessage("Are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                callBack.onDeleteClick(position);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();

            }
        });


        container.addView(view,0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    public interface onDeleteClick{
        void onDeleteClick(int pos);
    }

    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }
}
