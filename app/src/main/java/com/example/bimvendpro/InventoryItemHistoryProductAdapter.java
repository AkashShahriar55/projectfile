package com.example.bimvendpro;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class InventoryItemHistoryProductAdapter extends RecyclerView.Adapter<InventoryItemHistoryProductAdapter.InventoryViewHolder> {

    private List<InventoryItemHistoryProduct> itemList;
    private Context context;




    public class InventoryViewHolder extends RecyclerView.ViewHolder {
        private TextView typeTextView, dateTextView, totalSoldItemTextView, dollarPerItemTextView, locationTextView, totalDollarTextView;
        private InventoryItemHistoryProduct item;

        public InventoryViewHolder(View view) {
            super(view);
            typeTextView = view.findViewById(R.id.typeTextView);
            dateTextView = view.findViewById(R.id.dateTextView);
            totalSoldItemTextView = view.findViewById(R.id.totalSoldItemTextView);
            dollarPerItemTextView = view.findViewById(R.id.dollarPerItemTextView);
            locationTextView = view.findViewById(R.id.locationTextView);
            totalDollarTextView = view.findViewById(R.id.totalDollarTextView);

        }

        public InventoryItemHistoryProduct getItem() {
            return item;
        }

        public void setItem(InventoryItemHistoryProduct item) {
            this.item = item;
        }
    }

    public InventoryItemHistoryProductAdapter(Context context, List<InventoryItemHistoryProduct> itemList){
        this.context=context;
        this.itemList=itemList;
    }

    @NonNull
    @Override
    public InventoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.inventory_item_layout, viewGroup, false);

        return new InventoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryViewHolder holder, int position) {
        final InventoryItemHistoryProduct item = itemList.get(position);
        holder.typeTextView.setText(item.getType());
        holder.dateTextView.setText(String.valueOf(item.getHistoryDate()));
        holder.totalSoldItemTextView.setText(String.valueOf(item.getItems()));
        holder.dollarPerItemTextView.setText(String.valueOf(item.getUnitPerItem()));
        holder.locationTextView.setText(String.valueOf(item.getLocation()));
        holder.totalDollarTextView.setText(item.getTotalDollar());
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}

