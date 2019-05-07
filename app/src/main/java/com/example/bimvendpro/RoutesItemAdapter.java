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

public class RoutesItemAdapter extends RecyclerView.Adapter<RoutesItemAdapter.RoutesViewHolder>
        implements Filterable {

    private List<RouteItem> itemList;
    private List<RouteItem> itemListFiltered;
    private Context context;




    public class RoutesViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName,textViewDescription,textViewNoOfLocation,textViewNoOfMachine;
        private LinearLayout container;
        private RouteItem item;
        public RoutesViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.rout_name);
            textViewDescription = itemView.findViewById(R.id.rout_description);
            textViewNoOfLocation = itemView.findViewById(R.id.rout_nolocation);
            textViewNoOfMachine = itemView.findViewById(R.id.rout_nomachine);
            container = itemView.findViewById(R.id.rout_container);

        }

        public RouteItem getItem() {
            return item;
        }

        public void setItem(RouteItem item) {
            this.item = item;
        }
    }

    public RoutesItemAdapter(List<RouteItem> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
        this.itemListFiltered = itemList;
    }

    @NonNull
    @Override
    public RoutesItemAdapter.RoutesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.routes_item_layout,viewGroup,false);

        return new RoutesItemAdapter.RoutesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RoutesItemAdapter.RoutesViewHolder routesViewHolder, int i) {
        final RouteItem item = itemListFiltered.get(i);

        routesViewHolder.textViewName.setText(String.valueOf(item.getName()));
        routesViewHolder.textViewDescription.setText(String.valueOf(item.getDescription()));
        routesViewHolder.textViewNoOfLocation.setText(String.valueOf(item.getNoOfLocation()));
        routesViewHolder.textViewNoOfMachine.setText(String.valueOf(item.getNoOfMachines()));
        routesViewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,AddViewEditRoutes.class);
                intent.putExtra("item",item);
                intent.putExtra("mode","view");
                context.startActivity(intent);
            }
        });

        routesViewHolder.setItem(item);

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
                    List<RouteItem> filteredList = new ArrayList<>();
                    for (RouteItem row : itemList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())
                                || row.getDescription().toLowerCase().contains(charString.toLowerCase())
                                || String.valueOf(row.getNoOfLocation()).contains(charString.toLowerCase())
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
                itemListFiltered = (ArrayList<RouteItem>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
