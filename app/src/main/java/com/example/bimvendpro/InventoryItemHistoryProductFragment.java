package com.example.bimvendpro;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class InventoryItemHistoryProductFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<InventoryItemHistoryProduct> itemList = new ArrayList<>();
    private InventoryItemAdapter mAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.inventory_item_history_product_fragment,container,false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        recyclerView = getView().findViewById(R.id.inventory_item_container_recyclerview);

        readDataFromFirebase();

    }

    private void readDataFromFirebase(){
        FirebaseUtilClass.getDatabaseReference().child("Inventory").child("History").child("Product").orderByChild("historyDate").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                    itemList.add(dsp.getValue(InventoryItemHistoryProduct.class)); //add result into array list
                }

                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
