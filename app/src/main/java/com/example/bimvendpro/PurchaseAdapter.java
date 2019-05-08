package com.example.bimvendpro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.PurchaseViewHolder> {
    private List<PurchaseClass> itemList;
    private Context context;
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

    public PurchaseAdapter(List<PurchaseClass> moviesList, Context context) {
        this.itemList = moviesList;
        this.context=context;
    }

    @Override
    public PurchaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.purshase_item, parent, false);

        return new PurchaseViewHolder(itemView);
    }
    @Override
    public int getItemCount() {
        return itemList.size();
    }
    @Override
    public void onBindViewHolder(PurchaseViewHolder holder, int position) {
        final PurchaseClass item = itemList.get(position);
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
                              //  new InventoryItemHistoryDialogue(context,item.getCode()).show();
                                break;
                            case 1:
                               // new InventoryItemAddDialogue(context,item).show();

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
