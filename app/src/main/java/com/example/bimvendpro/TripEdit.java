package com.example.bimvendpro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public class TripEdit extends AppCompatActivity {

    private TripsItem tripsItem;
    private List<TripMachines> tripMachinesList;
    private RecyclerView recyclerViewMachines;
    private TripMachineAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trip);

        tripsItem = (TripsItem) getIntent().getExtras().getSerializable("item");
        if(tripsItem.getTripMachines() != null){
            tripMachinesList = tripsItem.getTripMachines();
            initializeRecyclerView();
        }


    }

    private void initializeRecyclerView() {
        recyclerViewMachines = findViewById(R.id.trip_machine_recyclerview);

        mAdapter = new TripMachineAdapter(tripMachinesList,this);
        RecyclerView.LayoutManager mLayoutmanager =new LinearLayoutManager(this);
        recyclerViewMachines.setLayoutManager(mLayoutmanager);
        recyclerViewMachines.setItemAnimator(new DefaultItemAnimator());
        recyclerViewMachines.setAdapter(mAdapter);
    }
}
