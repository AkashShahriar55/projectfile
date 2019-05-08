package com.example.bimvendpro;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
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

public class PurchaseFragment extends Fragment {
    private RecyclerView purchaseRecyclerView;
    private List<PurchaseClass> itemList = new ArrayList<>();
    private PurchaseAdapter mAdapter;
    private SwipeRefreshLayout refreshlayout;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        refreshlayout=view.findViewById(R.id.refreshingLayout);
        refreshlayout.setRefreshing(true);
        refreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                readDataFromFirebase();
            }
        });

        initializeRecyclerView();

        readDataFromFirebase();

    }


    private void initializeRecyclerView() {
        purchaseRecyclerView = getView().findViewById(R.id.purchases);

        mAdapter = new PurchaseAdapter(itemList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        purchaseRecyclerView.setLayoutManager(mLayoutManager);
        purchaseRecyclerView.setItemAnimator(new DefaultItemAnimator());
        purchaseRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_purchase, container, false);
    }

    public void readDataFromFirebase() {
        FirebaseUtilClass.getDatabaseReference().child("Purchases").child("Items").orderByChild("purchaseDate").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemList.clear();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    itemList.add(dsp.getValue(PurchaseClass.class)); //add result into array list
                }
                mAdapter.notifyDataSetChanged();
                refreshlayout.setRefreshing(false);
                //   Toast.makeText(getContext(), "Changed something", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                refreshlayout.setRefreshing(false);
            }
        });
    }
}
