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

public class DriverItemAdapter extends RecyclerView.Adapter<DriverItemAdapter.DriversViewHolder>
        implements Filterable {
    private List<DriverItem> itemList;
    private List<DriverItem> itemListFiltered;
    private Context context;

    @NonNull
    @Override
    public DriversViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_layout_drivers,viewGroup,false);

        return new DriverItemAdapter.DriversViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DriversViewHolder driversViewHolder, int i) {
        final DriverItem item = itemListFiltered.get(i);

        driversViewHolder.textViewName.setText(String.valueOf(item.getName()));
        driversViewHolder.textViewAddress.setText(String.valueOf(item.getAddress()));
        driversViewHolder.textViewWorkingHour.setText(String.valueOf(item.getWorkingHour()));
        driversViewHolder.textViewlicense.setText(String.valueOf(item.getSidNo()));
        driversViewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,AddViewEditDrivers.class);
                intent.putExtra("item",item);
                intent.putExtra("mode","view");
                context.startActivity(intent);
            }
        });

        driversViewHolder.setItem(item);

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
                    List<DriverItem> filteredList = new ArrayList<>();
                    for (DriverItem row : itemList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())
                                || row.getAddress().toLowerCase().contains(charString.toLowerCase())
                                || row.getWorkingHour().contains(charString.toLowerCase())
                                || row.getSidNo().contains(charString.toLowerCase())) {
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
                itemListFiltered = (ArrayList<DriverItem>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public DriverItemAdapter(List<DriverItem> itemList, Context context) {
        this.itemList = itemList;
        this.itemListFiltered = itemList;
        this.context = context;
    }

    public class DriversViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewAddress;
        private TextView textViewWorkingHour;
        private TextView textViewlicense;
        private LinearLayout container;
        private DriverItem item;
        public DriversViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.drivers_name);
            textViewAddress = itemView.findViewById(R.id.drivers_address);
            textViewWorkingHour = itemView.findViewById(R.id.drivers_working_hour);
            textViewlicense = itemView.findViewById(R.id.drivers_license);
            container = itemView.findViewById(R.id.drivers_container);

        }

        public DriverItem getItem() {
            return item;
        }

        public void setItem(DriverItem item) {
            this.item = item;
        }
    }
}
