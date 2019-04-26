package com.example.bimvendpro;

import android.content.Context;
import android.content.Intent;
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

public class LocationItemAdapter extends RecyclerView.Adapter<LocationItemAdapter.LocationViewHolder>
    implements Filterable {

    private List<Location> itemList;
    private List<Location> itemListFiltered;
    private Context context;




    public class LocationViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewCode,textViewLocation,textViewAddress,textViewCity,textViewZip,textViewServicedays,textViewLastvisit,textViewNextvisit,textViewNoofmachine;
        private LinearLayout container;
        private Location item;
        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCode = itemView.findViewById(R.id.loc_code);
            textViewLocation = itemView.findViewById(R.id.loc_location);
            textViewAddress = itemView.findViewById(R.id.loc_address);
            textViewCity = itemView.findViewById(R.id.loc_city);
            textViewZip = itemView.findViewById(R.id.loc_zip);
            textViewServicedays = itemView.findViewById(R.id.loc_servicedays);
            textViewLastvisit = itemView.findViewById(R.id.loc_lastvisit);
            textViewNextvisit = itemView.findViewById(R.id.loc_nextvisit);
            textViewNoofmachine = itemView.findViewById(R.id.loc_noOfMachine);
            container = itemView.findViewById(R.id.loc_container);

        }

        public Location getItem() {
            return item;
        }

        public void setItem(Location item) {
            this.item = item;
        }
    }

    public LocationItemAdapter(List<Location> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
        this.itemListFiltered = itemList;
    }

    @NonNull
    @Override
    public LocationItemAdapter.LocationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.location_item_layout,viewGroup,false);

        return new LocationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationItemAdapter.LocationViewHolder locationViewHolder, int i) {
        final Location item = itemListFiltered.get(i);

        locationViewHolder.textViewCode.setText(String.valueOf(item.getCode()));
        locationViewHolder.textViewLocation.setText(String.valueOf(item.getLocation()));
        locationViewHolder.textViewAddress.setText(String.valueOf(item.getAddress()));
        locationViewHolder.textViewCity.setText(String.valueOf(item.getCity()));
        locationViewHolder.textViewZip.setText(String.valueOf(item.getZip()));
        locationViewHolder.textViewServicedays.setText(String.valueOf(item.getTheDay()) + " " + String.valueOf(item.getIntervalDay()));
        locationViewHolder.textViewLastvisit.setText(String.valueOf(item.getLastVisit()));
        locationViewHolder.textViewNextvisit.setText(String.valueOf(item.getNextVisit()));
        locationViewHolder.textViewNoofmachine.setText(String.valueOf(item.getNoOfMachines()));
        locationViewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ViewEditLocation.class);
                intent.putExtra("itemData",item);
                context.startActivity(intent);
            }
        });

        locationViewHolder.setItem(item);

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
                    List<Location> filteredList = new ArrayList<>();
                    for (Location row : itemList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getLocation().toLowerCase().contains(charString.toLowerCase())
                                || row.getCode().toLowerCase().contains(charString.toLowerCase())
                                || row.getAddress().toLowerCase().contains(charString.toLowerCase())
                                || row.getCity().toLowerCase().contains(charString.toLowerCase())
                                || row.getZip().toLowerCase().contains(charString.toLowerCase())
                                || row.getTheDay().toLowerCase().contains(charString.toLowerCase())
                                || row.getLastVisit().toLowerCase().contains(charString.toLowerCase())
                                || row.getNextVisit().toLowerCase().contains(charString.toLowerCase())
                                || String.valueOf(row.getIntervalDay()).contains(charString.toLowerCase())
                                || String.valueOf(row.getNoOfMachines()).contains(charString.toLowerCase())) {
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
                itemListFiltered = (ArrayList<Location>) results.values;
                notifyDataSetChanged();
            }
        };
    }


}
