package com.example.bimvendpro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TripMachineAdapter extends RecyclerView.Adapter<TripMachineAdapter.TripMachineViewHolder>{
    private List<TripMachines> itemList;
    private Context context;
    private String tripNumber;

    public TripMachineAdapter(List<TripMachines> itemList, Context context,String tripNumber) {
        this.itemList = itemList;
        this.context = context;
        this.tripNumber = tripNumber;
    }

    @NonNull
    @Override
    public TripMachineAdapter.TripMachineViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trip_result_machine_item,viewGroup,false);

        return new TripMachineAdapter.TripMachineViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TripMachineAdapter.TripMachineViewHolder tripMachineViewHolder, final int i) {
        final TripMachines item = itemList.get(i);

        tripMachineViewHolder.textViewName.setText(String.valueOf(item.getName()));
        tripMachineViewHolder.textViewType.setText(String.valueOf(item.getType()));
        tripMachineViewHolder.textViewLocation.setText(String.valueOf(item.getLocation()));
        tripMachineViewHolder.textViewCollectedCash.setText(String.valueOf(item.getCashCollected()));
        tripMachineViewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,TripMachineEnterResult.class);
                intent.putExtra("item",item);
                intent.putExtra("tripNumber",tripNumber);
                intent.putExtra("machineNumber",i);
                ((Activity)context).startActivityForResult(intent,1);
            }
        });

        tripMachineViewHolder.setItem(item);

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }



    public class TripMachineViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewType;
        private TextView textViewLocation;
        private TextView textViewCollectedCash;
        private LinearLayout container;
        private TripMachines item;
        public TripMachineViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.trip_machine_name);
            textViewType = itemView.findViewById(R.id.trip_machine_type);
            textViewLocation = itemView.findViewById(R.id.trip_machine_location);
            textViewCollectedCash = itemView.findViewById(R.id.trip_machine_collected_cash);
            container = itemView.findViewById(R.id.trip_machine_container);
        }

        public TripMachines getItem() {
            return item;
        }

        public void setItem(TripMachines item) {
            this.item = item;
        }
    }
}
