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

public class MachineIngredientsAdapter extends RecyclerView.Adapter<MachineIngredientsAdapter.MachineIngredientsViewHolder> {
    private List<MachineIngredients> itemList;
    private Context context;

    public class MachineIngredientsViewHolder extends RecyclerView.ViewHolder {
        private TextView codeTextView, productNameTextView, canisterTextView, maxTextView;
        private MachineIngredients item;
        private LinearLayout parent;

        public MachineIngredientsViewHolder(View view) {
            super(view);
            codeTextView = view.findViewById(R.id.codeTextView);
            productNameTextView = view.findViewById(R.id.productNameTextView);
            canisterTextView = view.findViewById(R.id.canisterTextView);
            maxTextView = view.findViewById(R.id.maxTextView);
            parent=view.findViewById(R.id.parent);
        }

        public MachineIngredients getItem() {
            return item;
        }

        public void setItem(MachineIngredients item) {
            this.item = item;
        }
    }

    public MachineIngredientsAdapter(List<MachineIngredients> moviesList, Context context) {
        this.itemList = moviesList;
        this.context=context;
    }

    @Override
    public MachineIngredientsAdapter.MachineIngredientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.machine_ingredients_items, parent, false);

        return new MachineIngredientsAdapter.MachineIngredientsViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MachineIngredientsViewHolder holder, int position) {
        final MachineIngredients item = itemList.get(position);
        holder.codeTextView.setText(item.getCode());
        holder.productNameTextView.setText(item.getName());
        holder.canisterTextView.setText(String.valueOf(item.getCanister()));
        holder.maxTextView.setText(String.valueOf(item.getMax()));

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
                                new MachineIngredientsAddDialogue(context,item.getCode(),item).show();
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
        return itemList.size();
    }
}
