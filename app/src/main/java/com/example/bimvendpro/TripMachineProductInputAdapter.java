package com.example.bimvendpro;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class TripMachineProductInputAdapter extends RecyclerView.Adapter<TripMachineProductInputAdapter.TripMachineProductInputViewHolder>{

    private List<TripMachineProduct> itemList;
    private Context context;
    private passData passData;

    public TripMachineProductInputAdapter(List<TripMachineProduct> itemList, Context context, passData passData) {
        this.itemList = itemList;
        this.context = context;
        this.passData = passData;
    }

    @NonNull
    @Override
    public TripMachineProductInputAdapter.TripMachineProductInputViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trip_machine_product_input_items,viewGroup,false);

        return new TripMachineProductInputAdapter.TripMachineProductInputViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final TripMachineProductInputAdapter.TripMachineProductInputViewHolder tripMachineViewHolder, final int i) {
        final TripMachineProduct item = itemList.get(i);


        tripMachineViewHolder.textViewName.setText(item.getProductName());
        tripMachineViewHolder.textViewLastCount.setText(String.valueOf(item.getLastCount()));
        tripMachineViewHolder.textViewCurrentCount.setText(String.valueOf(item.getPresentCount()));
        tripMachineViewHolder.textViewFilled.setText(String.valueOf(item.getFilled()));
        tripMachineViewHolder.textViewNewCount.setText(String.valueOf(item.getNewCount()));
        tripMachineViewHolder.textViewSoldWin.setText(String.valueOf(item.getSoldOrWin()));

        TextWatcher textWatcher1 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int lastCount = 0;
                int currentCount = 0;
                int newCount = 0;
                int filled = 0;

                try {
                    lastCount = Integer.parseInt(tripMachineViewHolder.textViewLastCount.getText().toString());
                } catch (Exception e) {
                    lastCount = 0;
                }

                try {
                    currentCount = Integer.parseInt(tripMachineViewHolder.textViewCurrentCount.getText().toString());
                } catch (Exception e) {
                    currentCount = 0;
                }

                try {
                    filled = Integer.parseInt(tripMachineViewHolder.textViewFilled.getText().toString());
                } catch (Exception e) {
                    filled = 0;
                }

                newCount = currentCount+filled;
                tripMachineViewHolder.textViewSoldWin.setText(String.valueOf(lastCount - currentCount));
                tripMachineViewHolder.textViewNewCount.setText(String.valueOf(newCount));

                item.setSoldOrWin(lastCount - currentCount);
                item.setNewCount(newCount);
                item.setPresentCount(currentCount);
                item.setFilled(filled);
            }

            @Override
            public void afterTextChanged(Editable s) {
                passData.passData(item,i);
            }
        };

        tripMachineViewHolder.textViewCurrentCount.addTextChangedListener(textWatcher1);

        tripMachineViewHolder.textViewFilled.addTextChangedListener(textWatcher1);


        tripMachineViewHolder.setItem(item);

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }



    public class TripMachineProductInputViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private EditText textViewLastCount;
        private EditText textViewCurrentCount;
        private EditText textViewSoldWin;
        private EditText textViewNewCount;
        private EditText textViewFilled;
        private TripMachineProduct item;
        public TripMachineProductInputViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.Trip_machine_product_name);
            textViewLastCount = itemView.findViewById(R.id.trip_machine_product_lastcount);
            textViewCurrentCount = itemView.findViewById(R.id.trip_machine_product_currentcount);
            textViewSoldWin = itemView.findViewById(R.id.trip_machine_product_soldwin);
            textViewNewCount = itemView.findViewById(R.id.trip_machine_product_newcount);
            textViewFilled = itemView.findViewById(R.id.trip_machine_product_filled);
        }

        public TripMachineProduct getItem() {
            return item;
        }

        public void setItem(TripMachineProduct item) {
            this.item = item;
        }
    }

    public interface passData{
        void passData(TripMachineProduct tripMachineProduct,int index);
    }
}
