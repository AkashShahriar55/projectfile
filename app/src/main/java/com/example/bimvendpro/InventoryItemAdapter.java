package com.example.bimvendpro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class InventoryItemAdapter extends RecyclerView.Adapter<InventoryItemAdapter.InventoryViewHolder>   implements Filterable {
    private List<InventoryItem> itemList;
    private Context context;
    private List<InventoryItem> itemListFiltered;

    public class InventoryViewHolder extends RecyclerView.ViewHolder {
        private TextView codeTextView, inMachineTextView, inStock, inWareHouseTextView, lastCostTextView, productNameTextView, productTypeTextView, unitPerCaseTextView;
        private InventoryItem item;
        private LinearLayout parent;

        public InventoryViewHolder(View view) {
            super(view);
            codeTextView = view.findViewById(R.id.inv_code);
            inMachineTextView = view.findViewById(R.id.inv_machines);
            inStock = view.findViewById(R.id.inv_stock);
            inWareHouseTextView = view.findViewById(R.id.inv_warehouse);
            lastCostTextView = view.findViewById(R.id.inv_lastcost);
            productNameTextView = view.findViewById(R.id.inv_name);
            productTypeTextView = view.findViewById(R.id.inv_type);
            unitPerCaseTextView = view.findViewById(R.id.inv_unitpercase);
            parent=view.findViewById(R.id.dataContainer);
        }

        public InventoryItem getItem() {
            return item;
        }

        public void setItem(InventoryItem item) {
            this.item = item;
        }
    }


    public InventoryItemAdapter(List<InventoryItem> moviesList, Context context) {
        this.itemList = moviesList;
        this.itemListFiltered = itemList;
        this.context=context;
    }

    @Override
    public InventoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inventory_item_layout, parent, false);

        return new InventoryViewHolder(itemView);
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
                    List<InventoryItem> filteredList = new ArrayList<>();

                    for (InventoryItem row : itemList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getProductType().toLowerCase().contains(charString.toLowerCase())
                                || row.getProductName().toLowerCase().contains(charString.toLowerCase())
                                || row.getCode().toLowerCase().contains(charString.toLowerCase())
                                ) {
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
                itemListFiltered = (ArrayList<InventoryItem>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public void onBindViewHolder(InventoryViewHolder holder, int position) {
        final InventoryItem item = itemListFiltered.get(position);
        holder.codeTextView.setText(item.getCode());
        holder.inMachineTextView.setText(String.valueOf(item.getInMachine()));
        holder.inStock.setText(String.valueOf(item.getInStock()));
        holder.inWareHouseTextView.setText(String.valueOf(item.getInWarehouse()));
        holder.lastCostTextView.setText(String.valueOf(item.getLastCost()));
        holder.productNameTextView.setText(item.getProductName());
        holder.productTypeTextView.setText(item.getProductType());
        holder.unitPerCaseTextView.setText(String.valueOf(item.getUnitPerCase()));
        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                String[] colors = {"Edit"};

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(item.getProductName());
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){

                            case 0:
                                new InventoryItemAddDialogue(context,item).show();

                                break;
                        }
                    }
                });
                builder.show();

                return false;
            }
        });

        holder.setItem(item);
    }


    @Override
    public int getItemCount() {
        return itemListFiltered.size();
    }
}
