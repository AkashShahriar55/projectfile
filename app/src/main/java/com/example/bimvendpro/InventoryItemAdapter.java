package com.example.bimvendpro;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class InventoryItemAdapter extends RecyclerView.Adapter<InventoryItemAdapter.InventoryViewHolder> {
    private List<InventoryItem> itemList;
    private Context context;

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
        this.context=context;
    }

    @Override
    public InventoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inventory_item_layout, parent, false);

        return new InventoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(InventoryViewHolder holder, int position) {
        final InventoryItem item = itemList.get(position);
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
                new AddItemDialogue(context,item).show();
                return false;
            }
        });
        holder.setItem(item);
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
