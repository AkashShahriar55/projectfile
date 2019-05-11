package com.example.bimvendpro;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MachineFragment extends Fragment {


    private RecyclerView machineRecyclerView;

    private List<Machine> itemList = new ArrayList<>();
    private MachineAdapter mAdapter;
    private TextView noIngredientsTextView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        swipeRefreshLayout = view.findViewById(R.id.refreshingLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                readDataFromFirebase();
            }
        });
        noIngredientsTextView = view.findViewById(R.id.noIngredentsMsg);
        initializeRecyclerView();

        readDataFromFirebase();

        swipeRefreshLayout.setRefreshing(true);
    }


    private void initializeRecyclerView() {
        machineRecyclerView = getView().findViewById(R.id.machine_container_recyclerview);

        mAdapter = new MachineAdapter(itemList, getContext(), (MainActivity) getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        machineRecyclerView.setLayoutManager(mLayoutManager);
        machineRecyclerView.setItemAnimator(new DefaultItemAnimator());
        machineRecyclerView.setAdapter(mAdapter);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_machine, container, false);
    }

    public void readDataFromFirebase() {
        FirebaseUtilClass.getDatabaseReference().child("Machine").child("Items").orderByChild("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemList.clear();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
//                    String val=dsp.toString();
                    itemList.add(dsp.getValue(Machine.class)); //add result into array list

                }

                mAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                if (itemList.size() == 0) {
                    noIngredientsTextView.setVisibility(View.VISIBLE);
                } else {
                    noIngredientsTextView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
