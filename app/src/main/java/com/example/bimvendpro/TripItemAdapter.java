package com.example.bimvendpro;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TripItemAdapter extends RecyclerView.Adapter<TripItemAdapter.TripViewHolder>
        implements Filterable {
    private List<TripsItem> itemList;
    private List<TripsItem> itemListFiltered;
    private Context context;

    public TripItemAdapter(List<TripsItem> itemList, Context context) {
        this.itemList = itemList;
        this.itemListFiltered = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public TripItemAdapter.TripViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trip_item_layout,viewGroup,false);

        return new TripItemAdapter.TripViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TripItemAdapter.TripViewHolder tripViewHolder, int i) {
        final TripsItem item = itemListFiltered.get(i);

        tripViewHolder.textViewName.setText(String.valueOf(item.getTripNumber()));
        tripViewHolder.textViewDate.setText(String.valueOf(item.getTripDate()));
        tripViewHolder.textViewStatus.setText(String.valueOf(item.getStatus()));
        if(item.getStatus().equals("created")){
            tripViewHolder.textViewStatus.setTextColor(Color.RED);
        }else{
            tripViewHolder.textViewStatus.setTextColor(Color.GREEN);
        }
        tripViewHolder.textViewNoOfLocation.setText(String.valueOf(item.getNoOfLocation()));
        tripViewHolder.textViewNoOfMachine.setText(String.valueOf(item.getNoOfMachines()));
        tripViewHolder.textViewCollection.setText(String.valueOf(item.getCashCollected()));
        tripViewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,TripEdit.class);
                intent.putExtra("item",item);
                context.startActivity(intent);
            }
        });

        tripViewHolder.setItem(item);

    }

    @Override
    public int getItemCount() {
        return itemListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    itemListFiltered = itemList;
                } else {
                    List<TripsItem> filteredList = new ArrayList<>();
                    for (TripsItem row : itemList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTripNumber().toLowerCase().contains(charString.toLowerCase())
                                || row.getTripDate().toLowerCase().contains(charString.toLowerCase())
                                || row.getStatus().contains(charString.toLowerCase())
                                || String.valueOf(row.getCashCollected()).contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    itemListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = itemListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                itemListFiltered = (ArrayList<TripsItem>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class TripViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewDate;
        private TextView textViewStatus;
        private TextView textViewNoOfLocation;
        private TextView textViewNoOfMachine;
        private TextView textViewCollection;
        private LinearLayout container;
        private TripsItem item;
        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.trip_number);
            textViewDate = itemView.findViewById(R.id.trip_date);
            textViewStatus = itemView.findViewById(R.id.trip_status);
            textViewNoOfLocation = itemView.findViewById(R.id.trip_location_no);
            textViewNoOfMachine = itemView.findViewById(R.id.trip_machine_no);
            textViewCollection = itemView.findViewById(R.id.trip_collected_money);
            container = itemView.findViewById(R.id.trip_container);
        }

        public TripsItem getItem() {
            return item;
        }

        public void setItem(TripsItem item) {
            this.item = item;
        }
    }
}
