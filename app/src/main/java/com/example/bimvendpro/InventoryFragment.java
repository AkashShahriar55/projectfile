package com.example.bimvendpro;

import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

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
    private TextView noIngredientsTextView;

    private SearchView searchViewExpense;
    private RecyclerView inventoryItemRecyclerView;
    private List<InventoryItem> itemList = new ArrayList<>();
    private InventoryItemAdapter mAdapter;
    private SwipeRefreshLayout refreshlayout;

    private OnFragmentInteractionListener mListener;

    public InventoryFragment() {


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

        refreshlayout = view.findViewById(R.id.refreshingLayout);
        refreshlayout.setRefreshing(true);
        refreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                readDataFromFirebase();
            }
        });


        noIngredientsTextView = view.findViewById(R.id.noIngredentsMsg);


        searchViewExpense = view.findViewById(R.id.ingredient_search);
        searchViewExpense.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });


        initializeRecyclerView();

        readDataFromFirebase();
        super.onViewCreated(view, savedInstanceState);
    }


    private void initializeRecyclerView() {
        inventoryItemRecyclerView = getView().findViewById(R.id.inventory_item_container_recyclerview);

        mAdapter = new InventoryItemAdapter(itemList, getContext());
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
        FirebaseUtilClass.getDatabaseReference().child("Inventory").child("Items").orderByChild("productName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemList.clear();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    itemList.add(dsp.getValue(InventoryItem.class)); //add result into array list
                }
                mAdapter.notifyDataSetChanged();
                refreshlayout.setRefreshing(false);
                if (itemList.size() == 0) {
                    noIngredientsTextView.setVisibility(View.VISIBLE);
                } else {
                    noIngredientsTextView.setVisibility(View.GONE);
                }
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
