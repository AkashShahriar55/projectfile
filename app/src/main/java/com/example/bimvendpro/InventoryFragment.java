package com.example.bimvendpro;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class InventoryFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;
    private ImageView addImageView;


    private RecyclerView inventoryItemRecyclerView;
    private List<InventoryItem> itemList = new ArrayList<>();
    private InventoryItemAdapter mAdapter;

    private OnFragmentInteractionListener mListener;

    public InventoryFragment() {


    }


    public static InventoryFragment newInstance(String param1, String param2) {
        InventoryFragment fragment = new InventoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        initializeAddItemButton();
        initializeRecyclerView();

        readDataFromFirebase();

    }


    private void initializeAddItemButton() {
        addImageView = getView().findViewById(R.id.invaddImageView);
        addImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              // writeDataToFirebase();
            }
        });
    }

    private void initializeRecyclerView() {
        inventoryItemRecyclerView = getView().findViewById(R.id.inventory_item_container_recyclerview);

        mAdapter = new InventoryItemAdapter(itemList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        inventoryItemRecyclerView.setLayoutManager(mLayoutManager);
        inventoryItemRecyclerView.setItemAnimator(new DefaultItemAnimator());
        inventoryItemRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_inventory, container, false);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void readDataFromFirebase() {
        FirebaseUtilClass.getDatabaseReference().child("Inventory").orderByChild("productName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {



                    itemList.add(dsp.getValue(InventoryItem.class)); //add result into array list
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
