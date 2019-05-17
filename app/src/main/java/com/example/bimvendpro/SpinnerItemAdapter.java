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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class SpinnerItemAdapter extends RecyclerView.Adapter<SpinnerItemAdapter.SpinnerViewHolder> implements Filterable {
    private List<String> itemList;
    private int type;
    private Context context;

    private List<String> itemListFiltered = new ArrayList<>();

    public class SpinnerViewHolder extends RecyclerView.ViewHolder {
        private TextView itemName;
        private ImageView imageView;
        private String item;


        public SpinnerViewHolder(View view) {
            super(view);
            itemName = view.findViewById(R.id.type);
            imageView = view.findViewById(R.id.delme);

        }

        public String getItem() {
            return item;
        }

        public void setItem(String item) {
            this.item = item;
        }
    }

    public SpinnerItemAdapter(List<String> moviesList, Context context, int type) {
        this.itemList = moviesList;
        this.context = context;
        this.itemListFiltered = itemList;
        this.type = type;
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
                    List<String> filteredList = new ArrayList<>();

                    for (String row : itemList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.toLowerCase().contains(charString.toLowerCase())

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
                itemListFiltered = (ArrayList<String>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public SpinnerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_spinner_item, parent, false);

        return new SpinnerViewHolder(itemView);
    }

    @Override
    public int getItemCount() {

        return itemListFiltered.size();
    }

    @Override
    public void onBindViewHolder(SpinnerViewHolder holder, int position) {
        final String item = itemListFiltered.get(position);
        holder.itemName.setText(item);


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                deleteItem(item);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure to delete the item?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();


            }
        });

        holder.setItem(item);
    }

    private void deleteItem(String item) {
        if (type == AddSpinnerTypeActivity.MACHINE) {
            FirebaseUtilClass.getDatabaseReference().child("Machine").child("Types").child(item).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@android.support.annotation.NonNull Task<Void> task) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@android.support.annotation.NonNull Exception e) {

                }
            });
        } else if (type == AddSpinnerTypeActivity.PRODUCT) {
            FirebaseUtilClass.getDatabaseReference().child("Inventory").child("Types").child(item).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@android.support.annotation.NonNull Task<Void> task) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@android.support.annotation.NonNull Exception e) {

                }
            });
        }
    }
}
