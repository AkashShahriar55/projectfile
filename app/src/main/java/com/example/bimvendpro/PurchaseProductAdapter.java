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

public class PurchaseProductAdapter extends RecyclerView.Adapter<PurchaseProductAdapter.PurchaseProductViewHolder> implements Filterable {
    private List<PurchaseProductClass> itemList;
    private Context context;
    private String sellerPushId;
    private List<PurchaseProductClass> itemListFiltered;

    public class PurchaseProductViewHolder extends RecyclerView.ViewHolder {
        private TextView productNameTextView, noCasesTextView, noOfUnitTextView, costPerCaseTextView, unitCostTextView, totalCostTextView;
        private PurchaseProductClass item;
        private LinearLayout parent;

        public PurchaseProductViewHolder(View view) {
            super(view);
            productNameTextView = view.findViewById(R.id.productName);
            noCasesTextView = view.findViewById(R.id.noCases);
            noOfUnitTextView = view.findViewById(R.id.noOfUnit);
            costPerCaseTextView = view.findViewById(R.id.costPerCase);
            unitCostTextView = view.findViewById(R.id.unitCost);
            totalCostTextView = view.findViewById(R.id.totalCost);


            parent = view.findViewById(R.id.parent);
        }

        public PurchaseProductClass getItem() {
            return item;
        }

        public void setItem(PurchaseProductClass item) {
            this.item = item;
        }
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
                    List<PurchaseProductClass> filteredList = new ArrayList<>();

                    for (PurchaseProductClass row : itemList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getProductCode().toLowerCase().contains(charString.toLowerCase())
                                || row.getProductName().toLowerCase().contains(charString.toLowerCase())
                                || String.valueOf(row.getUnitPurchased()).toLowerCase().contains(charString.toLowerCase())
                                || String.valueOf(row.getUnitCost()).toLowerCase().contains(charString.toLowerCase())
                                || String.valueOf(row.getTotalCost()).toLowerCase().contains(charString.toLowerCase())
                                || String.valueOf(row.getCostPerCase()).toLowerCase().contains(charString.toLowerCase())
                                || String.valueOf(row.getUnitPerCase()).toLowerCase().contains(charString.toLowerCase())

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
                itemListFiltered = (ArrayList<PurchaseProductClass>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    public PurchaseProductAdapter(List<PurchaseProductClass> moviesList, Context context, String sellerPushId) {
        this.itemList = moviesList;
        this.context = context;
        this.itemListFiltered = itemList;
        this.sellerPushId = sellerPushId;
    }

    @Override
    public PurchaseProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item_layout, parent, false);

        return new PurchaseProductViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return itemListFiltered.size();
    }

    @Override
    public void onBindViewHolder(PurchaseProductViewHolder holder, int position) {
        final PurchaseProductClass item = itemListFiltered.get(position);
        holder.productNameTextView.setText(item.getProductName());
        holder.noCasesTextView.setText(String.valueOf(item.getCasesPurchased()));
        holder.noOfUnitTextView.setText(String.valueOf(item.getUnitPurchased()));
        holder.costPerCaseTextView.setText(String.valueOf(item.getCostPerCase()));
        holder.unitCostTextView.setText(String.valueOf(item.getUnitCost()));
        holder.totalCostTextView.setText(String.valueOf(item.getTotalCost()));
        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                String[] colors = {"Edit", "Products"};

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(item.getProductName());
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                new PurchaseProductAddDialogue(context, sellerPushId, item).show();
                                break;
                            case 1:
                                // new InventoryItemAddDialogue(context,item).show();
                                //   mainActivity.changeFragment(new PurchaseProductFragment(),item.getSupplier(),item.getPushId(),MainActivity.PRODUCTS);

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
}
