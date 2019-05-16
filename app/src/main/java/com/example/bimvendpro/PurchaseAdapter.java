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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.PurchaseViewHolder> implements Filterable {
    private List<PurchaseClass> itemList;
    private Context context;
    private MainActivity mainActivity;
    private List<PurchaseClass> itemListFiltered=new ArrayList<>();
    public class PurchaseViewHolder extends RecyclerView.ViewHolder {
        private TextView purDateTextView, supplierTextView, productNoTextView, amountTextView;
        private PurchaseClass item;
        private LinearLayout parent;

        public PurchaseViewHolder(View view) {
            super(view);
            purDateTextView = view.findViewById(R.id.purDate);
            supplierTextView = view.findViewById(R.id.sellerName);
            productNoTextView = view.findViewById(R.id.productNo);
            amountTextView = view.findViewById(R.id.amount);
            parent=view.findViewById(R.id.parent);
        }

        public PurchaseClass getItem() {
            return item;
        }

        public void setItem(PurchaseClass item) {
            this.item = item;
        }
    }

    public PurchaseAdapter(List<PurchaseClass> moviesList, Context context, MainActivity mainActivity) {
        this.itemList = moviesList;
        this.context=context;
        this.itemListFiltered = itemList;
        this.mainActivity=mainActivity;
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
                    List<PurchaseClass> filteredList = new ArrayList<>();

                    for (PurchaseClass row : itemList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getSupplier().toLowerCase().contains(charString.toLowerCase())
                                || row.getPurchaseDate().toLowerCase().contains(charString.toLowerCase())
                                || String.valueOf(row.getProductNo()).toLowerCase().contains(charString.toLowerCase())
                                || String.valueOf(row.getTotalCost()).toLowerCase().contains(charString.toLowerCase())
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
                itemListFiltered = (ArrayList<PurchaseClass>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public PurchaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.purshase_item, parent, false);

        return new PurchaseViewHolder(itemView);
    }
    @Override
    public int getItemCount() {

        return itemListFiltered.size();
    }
    @Override
    public void onBindViewHolder(PurchaseViewHolder holder, int position) {
        final PurchaseClass item = itemListFiltered.get(position);
        holder.purDateTextView.setText(item.getPurchaseDate());
        holder.supplierTextView.setText(item.getSupplier());
        holder.productNoTextView.setText(String.valueOf(item.getProductNo()));
        holder.amountTextView.setText(String.valueOf(item.getTotalCost()));
        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                String[] colors = { "Edit","Products"};

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(item.getSupplier());
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                new PurchaseAddDialogue(context,item).show();
                                break;
                            case 1:
                               // new InventoryItemAddDialogue(context,item).show();
                                mainActivity.changeFragment(new PurchaseProductFragment(),item.getSupplier(),item.getPushId(),MainActivity.PRODUCTS);

                                break;
                        }
                    }
                });
                builder.show();

                return false;
            }
        });

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.changeFragment(new PurchaseProductFragment(),item.getSupplier(),item.getPushId(),MainActivity.PRODUCTS);

            }
        });

        holder.setItem(item);
    }
}
