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

public class MachineIngredientsAdapter extends RecyclerView.Adapter<MachineIngredientsAdapter.MachineIngredientsViewHolder> implements Filterable {
    private List<MachineIngredients> itemList;
    private Context context;
    private List<MachineIngredients> itemListFiltered;
    private String machineId;
    public class MachineIngredientsViewHolder extends RecyclerView.ViewHolder {
        private TextView codeTextView, productNameTextView, canisterTextView, maxTextView,lastTextView;
        private MachineIngredients item;
        private LinearLayout parent;


        public MachineIngredientsViewHolder(View view) {
            super(view);
            codeTextView = view.findViewById(R.id.codeTextView);
            productNameTextView = view.findViewById(R.id.productNameTextView);
            canisterTextView = view.findViewById(R.id.canisterTextView);
            maxTextView = view.findViewById(R.id.maxTextView);
            parent=view.findViewById(R.id.parent);
            lastTextView=view.findViewById(R.id.lastTextView);
        }

        public MachineIngredients getItem() {
            return item;
        }

        public void setItem(MachineIngredients item) {
            this.item = item;
        }
    }

    public MachineIngredientsAdapter(List<MachineIngredients> moviesList, Context context, String machineId) {
        this.itemList = moviesList;
        this.context=context;
        this.machineId=machineId;
        this.itemListFiltered = itemList;
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
                    List<MachineIngredients> filteredList = new ArrayList<>();

                    for (MachineIngredients row : itemList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getCode().toLowerCase().contains(charString.toLowerCase())
                                || row.getName().toLowerCase().contains(charString.toLowerCase())
                                || String.valueOf(row.getLastCount()).toLowerCase().contains(charString.toLowerCase())
                                || String.valueOf(row.getVendPrice()).toLowerCase().contains(charString.toLowerCase())
                                || String.valueOf(row.getMax()).toLowerCase().contains(charString.toLowerCase())
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
                itemListFiltered = (ArrayList<MachineIngredients>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    @Override
    public MachineIngredientsAdapter.MachineIngredientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.machine_ingredients_items, parent, false);

        return new MachineIngredientsAdapter.MachineIngredientsViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MachineIngredientsViewHolder holder, int position) {
        final MachineIngredients item = itemListFiltered.get(position);
        holder.codeTextView.setText(item.getCode());
        holder.productNameTextView.setText(item.getName());
        holder.canisterTextView.setText(String.valueOf(item.getVendPrice()));
        holder.maxTextView.setText(String.valueOf(item.getMax()));
        holder.lastTextView.setText(String.valueOf(item.getLastCount()));

        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                String[] colors = { "Edit"};

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(item.getName());
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                new MachineIngredientsAddDialogue(context,machineId,item).show();
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
