package com.example.bimvendpro;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class AddViewEditRoutes extends FragmentActivity implements OnMapReadyCallback,RouteDialogFragment.NoticeDialogListener,RouteAddLocationAdapter.onDeleteClick {

    private GoogleMap mMap;

    private EditText name;
    private EditText description;
    private Button addLocation;
    private Button printRoute,buttonAdd,buttonCancel;
    private TextView dummy;
    private TextView textViewMode;
    private String mode;
    private CardView cardViewMap;

    private List<Location> locations = new ArrayList<>();
    private List<String> locationCode = new ArrayList<>();
    private List<LatLng> latLngLocation = new ArrayList<>();
    List<CharSequence> locationNames = new ArrayList<>();
    private RecyclerView recyclerViewAddLocation;
    private RouteAddLocationAdapter mAdapter;

    private RouteItem routeItem;
    private int noOfMachines = 0;
    private int noOfLocation = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_view_edit_routes);




        name = findViewById(R.id.rut_name);
        description = findViewById(R.id.rut_description);
        addLocation = findViewById(R.id.rut_addLocation);
        printRoute = findViewById(R.id.rut_printroute);
        dummy = findViewById(R.id.routes_addLocationDummy);
        buttonAdd = findViewById(R.id.rut_button_add);
        buttonCancel = findViewById(R.id.rut_button_cancel);
        textViewMode =findViewById(R.id.rut_view_mode);

        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        printRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });





        initializeRecyclerView();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        mode = (String) getIntent().getExtras().getSerializable("mode");

        if(mode.equals("add")){
            initializeAddMode();
        }
        else if(mode.equals("view")){
            initializeViewMode();
        }

    }

    private void initializeViewMode() {

        routeItem = (RouteItem) getIntent().getExtras().getSerializable("item");
        textViewMode.setText("View Mode");

        name.setText(routeItem.getName());
        description.setText(routeItem.getDescription());
        name.setEnabled(false);
        description.setEnabled(false);
        addLocation.setEnabled(false);
        printRoute.setEnabled(false);
        noOfLocation = routeItem.getNoOfLocation();
        noOfMachines = routeItem.getNoOfMachines();

        buttonAdd.setText("Edit");
        buttonCancel.setText("Delete");



        mAdapter.notifyDataSetChanged();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeEditMode();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFromDataBase();
            }
        });

        getLocationData();
    }

    private void getLocationData() {
        FirebaseUtilClass.getDatabaseReference().child("Location").child("Locations").orderByChild("location").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int nl = 0,nm = 0;
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    String code = dsp.getValue(Location.class).getCode();
                    if(!(routeItem.getLocationCode() == null)){
                        if(routeItem.getLocationCode().contains(code)){
                            dummy.setVisibility(GONE);
                            locations.add(dsp.getValue(Location.class));
                            locationCode.add(dsp.getValue(Location.class).getCode());
                            locationNames.add(dsp.getValue(Location.class).getLocation());
                            latLngLocation.add(new LatLng(dsp.getValue(Location.class).getLatitude()
                                    ,dsp.getValue(Location.class).getLongitude()));
                            nl += 1;
                            nm += dsp.getValue(Location.class).getNoOfMachines();
                        }
                    }

                }
                noOfLocation = nl;
                noOfMachines = nm;
                updateMap();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddViewEditRoutes.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteFromDataBase() {

    }

    private void initializeEditMode() {
        textViewMode.setText("Edit Mode");
        name.setEnabled(true);
        description.setEnabled(true);
        addLocation.setEnabled(true);
        printRoute.setEnabled(true);

        buttonAdd.setText("Done");
        buttonCancel.setText("Cancel");

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryWriteData();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeViewMode();
            }
        });
    }

    private void initializeAddMode() {

        buttonAdd.setText("Add");
        buttonCancel.setText("Cancel");

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryWriteData();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initializeRecyclerView() {
        recyclerViewAddLocation = findViewById(R.id.rut_recyclerview);

        mAdapter = new RouteAddLocationAdapter(locations,this,this);
        RecyclerView.LayoutManager mLayoutmanager =new LinearLayoutManager(this);
        recyclerViewAddLocation.setLayoutManager(mLayoutmanager);
        recyclerViewAddLocation.setItemAnimator(new DefaultItemAnimator());
        recyclerViewAddLocation.setAdapter(mAdapter);
    }

    private void updateMap(){

        mMap.clear();

        for(int i = 0 ; i < latLngLocation.size();i++){
            mMap.addMarker(new MarkerOptions().position(latLngLocation.get(i)).title(locations.get(i).getAddress()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngLocation.get(i)));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15),2000,null);

        }


    }

    private void openDialog() {
        RouteDialogFragment routeDialogFragment = new RouteDialogFragment();
        routeDialogFragment.show(getSupportFragmentManager(),"Add Location");
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        updateMap();


    }


    @Override
    public void passData(Location location) {
        if(locationCode.contains(location.getCode())){
            Toast.makeText(this,"Already Added",Toast.LENGTH_SHORT).show();
        }
        else{
            locationCode.add(location.getCode());
            locations.add(location);
            locationNames.add(location.getLocation());
            latLngLocation.add(new LatLng(location.getLatitude(),location.getLongitude()));
            noOfMachines += location.getNoOfMachines();
            noOfLocation += 1;
            mMap.clear();
            updateMap();
            mAdapter.notifyDataSetChanged();
            dummy.setVisibility(GONE);
        }

    }

    private void updateLocationList() {

    }

    private void tryWriteData(){

        boolean error =  false;

        String nameStr = name.getText().toString();
        if (TextUtils.isEmpty(nameStr.trim())) {
            name.setError("Field mustn't be empty");
            name.requestFocus();
            error = true;
            return;
        }

        String descriptionStr = description.getText().toString();
        if (TextUtils.isEmpty(descriptionStr.trim())) {
            description.setError("Field mustn't be empty");
            description.requestFocus();
            error = true;
            return;
        }




        if(!error){
            writeDataToFirebase(new RouteItem(nameStr,descriptionStr,locationCode,noOfLocation,noOfMachines));
        }
    }

    public void writeDataToFirebase(final RouteItem item) {
        final ProgressDialog dialog = ProgressDialog.show(AddViewEditRoutes.this, "",
                "Loading. Please wait...", true);
        FirebaseUtilClass.getDatabaseReference().child("Route").child("Routes").child(item.getName()).setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AddViewEditRoutes.this, "Updated", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddViewEditRoutes.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void deleteClicked(Location itemPass) {
        locations.remove(itemPass);
        locationCode.remove(itemPass.getCode());
        locationNames.remove(itemPass.getLocation());
        latLngLocation.remove(new LatLng(itemPass.getLatitude(),itemPass.getLongitude()));
        noOfMachines -= itemPass.getNoOfMachines();
        noOfLocation -= 1;
        mMap.clear();
        updateMap();
        mAdapter.notifyDataSetChanged();
        if(locationCode.isEmpty()){
            dummy.setVisibility(View.VISIBLE);
        }
    }
}
