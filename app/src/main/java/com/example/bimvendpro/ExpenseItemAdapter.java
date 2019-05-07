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

public class ExpenseItemAdapter extends RecyclerView.Adapter<ExpenseItemAdapter.ExpenseViewHolder>
        implements Filterable {

    private List<ExpenseItem> itemList;
    private List<ExpenseItem> itemListFiltered;
    private Context context;

    public ExpenseItemAdapter(List<ExpenseItem> itemList, Context context) {
        this.itemList = itemList;
        this.itemListFiltered = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.expense_item_layout,viewGroup,false);

        return new ExpenseItemAdapter.ExpenseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder expenseViewHolder, int i) {
        final ExpenseItem item = itemListFiltered.get(i);

        expenseViewHolder.textViewCatagory.setText(String.valueOf(item.getCatagory()));
        expenseViewHolder.textViewDate.setText(String.valueOf(item.getDate()));
        expenseViewHolder.textViewPayee.setText(String.valueOf(item.getPayee()));
        expenseViewHolder.textViewAmount.setText(String.valueOf(item.getAmount()));
        expenseViewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,AddViewEditExpenses.class);
                intent.putExtra("item",item);
                intent.putExtra("mode","view");
                context.startActivity(intent);
            }
        });

        expenseViewHolder.setItem(item);

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
                    List<ExpenseItem> filteredList = new ArrayList<>();
                    for (ExpenseItem row : itemList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getCatagory().toLowerCase().contains(charString.toLowerCase())
                                || row.getDate().toLowerCase().contains(charString.toLowerCase())
                                || row.getPayee().contains(charString.toLowerCase())
                                || row.getAmount().contains(charString.toLowerCase())) {
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
                itemListFiltered = (ArrayList<ExpenseItem>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ExpenseViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewCatagory;
        private TextView textViewDate;
        private TextView textViewPayee;
        private TextView textViewAmount;
        private LinearLayout container;
        private ExpenseItem item;
        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCatagory = itemView.findViewById(R.id.expense_catagory);
            textViewDate = itemView.findViewById(R.id.expense_date);
            textViewPayee = itemView.findViewById(R.id.expense_payee);
            textViewAmount = itemView.findViewById(R.id.expense_amount);
            container = itemView.findViewById(R.id.expense_container);
        }

        public ExpenseItem getItem() {
            return item;
        }

        public void setItem(ExpenseItem item) {
            this.item = item;
        }
    }
}
