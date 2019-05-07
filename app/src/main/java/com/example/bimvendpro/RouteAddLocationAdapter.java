package com.example.bimvendpro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class RouteAddLocationAdapter extends RecyclerView.Adapter<RouteAddLocationAdapter.LocationViewHolder> {
    private List<Location> itemList;
    private Context context;
    private onDeleteClick callBack;

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.add_location_item,viewGroup,false);

        return new RouteAddLocationAdapter.LocationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder locationViewHolder, int i) {
        final Location item = itemList.get(i);

        locationViewHolder.textViewCode.setText(String.valueOf(item.getCode()));
        locationViewHolder.textViewLocation.setText(String.valueOf(item.getLocation()));
        locationViewHolder.textViewAddress.setText(String.valueOf(item.getAddress()));
        locationViewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ViewEditLocation.class);
                intent.putExtra("itemData",item);
                context.startActivity(intent);
            }
        });
        locationViewHolder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setMessage("Are you want to delete " + item.getLocation())
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                callBack.deleteClicked(item);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();

            }
        });

        locationViewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewCode, textViewLocation, textViewAddress;
        private LinearLayout container;
        private Location item;
        private ImageButton imageButton;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCode = itemView.findViewById(R.id.add_loc_code);
            textViewLocation = itemView.findViewById(R.id.add_loc_location);
            textViewAddress = itemView.findViewById(R.id.add_loc_address);
            container = itemView.findViewById(R.id.add_loc_container);
            imageButton = itemView.findViewById(R.id.rut_loc_del);

        }

        public Location getItem() {
            return item;
        }

        public void setItem(Location item) {
            this.item = item;
        }
    }

    public RouteAddLocationAdapter(List<Location> itemList, Context context,onDeleteClick callBack) {
        this.itemList = itemList;
        this.context = context;
        this.callBack = callBack;
    }

    public interface onDeleteClick{
        void deleteClicked(Location itemPass);
    }
}
