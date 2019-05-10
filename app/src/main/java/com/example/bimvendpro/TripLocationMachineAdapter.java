package com.example.bimvendpro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TripLocationMachineAdapter extends RecyclerView.Adapter<TripLocationMachineAdapter.MachineViewHolder>{
    private List<Machine> itemList;
    private Context context;

    @NonNull
    @Override
    public TripLocationMachineAdapter.MachineViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trip_location_machine_item,viewGroup,false);

        return new TripLocationMachineAdapter.MachineViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TripLocationMachineAdapter.MachineViewHolder machineViewHolder, int i) {
        final Machine item = itemList.get(i);

        machineViewHolder.textViewName.setText(String.valueOf(item.getName()));
        machineViewHolder.textViewType.setText(String.valueOf(item.getType()));

        machineViewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MachineViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName, textViewType;
        private Machine item;

        public MachineViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.trip_location_machine_name);
            textViewType = itemView.findViewById(R.id.trip_location_machine_type);
        }

        public Machine getItem() {
            return item;
        }

        public void setItem(Machine item) {
            this.item = item;
        }
    }

    public TripLocationMachineAdapter(List<Machine> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

}
