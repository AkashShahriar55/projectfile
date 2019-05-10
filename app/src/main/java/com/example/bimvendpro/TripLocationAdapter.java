package com.example.bimvendpro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TripLocationAdapter extends RecyclerView.Adapter<TripLocationAdapter.LocationViewHolder> {

    private List<Location> itemList;
    private Context context;
    private TripLocationAdapter.onDeleteClick callBack;
    private TripLocationMachineAdapter mAdapter;
    private List<Machine>  machineList = new ArrayList<>();

    @NonNull
    @Override
    public TripLocationAdapter.LocationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trip_location_list,viewGroup,false);

        return new TripLocationAdapter.LocationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TripLocationAdapter.LocationViewHolder locationViewHolder, int i) {
        final Location item = itemList.get(i);
        machineList = itemList.get(i).getMachines();
        locationViewHolder.textViewLocation.setText(String.valueOf(item.getLocation()));
        locationViewHolder.textViewAddress.setText(String.valueOf(item.getAddress()));
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

        if(itemList.get(i).getMachines() != null) {
            mAdapter = new TripLocationMachineAdapter(machineList, context);
            RecyclerView.LayoutManager mLayoutmanager = new LinearLayoutManager(context);
            locationViewHolder.recyclerViewMachine.setLayoutManager(mLayoutmanager);
            locationViewHolder.recyclerViewMachine.setItemAnimator(new DefaultItemAnimator());
            locationViewHolder.recyclerViewMachine.setAdapter(mAdapter);
        }
        locationViewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewLocation, textViewAddress;
        private Location item;
        private ImageView imageButton;
        private RecyclerView recyclerViewMachine;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewLocation = itemView.findViewById(R.id.trip_location_name);
            textViewAddress = itemView.findViewById(R.id.trip_location_address);
            imageButton = itemView.findViewById(R.id.trip_location_delete);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.deleteClicked(item);
                }
            });
            recyclerViewMachine = itemView.findViewById(R.id.trip_location_machine_holder);
        }

        public Location getItem() {
            return item;
        }

        public void setItem(Location item) {
            this.item = item;
        }
    }

    public TripLocationAdapter(List<Location> itemList, Context context, TripLocationAdapter.onDeleteClick callBack) {
        this.itemList = itemList;
        this.context = context;
        this.callBack = callBack;
    }

    public interface onDeleteClick{
        void deleteClicked(Location itemPass);
    }
}
