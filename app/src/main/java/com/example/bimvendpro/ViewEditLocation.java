package com.example.bimvendpro;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ViewEditLocation extends FragmentActivity implements OnMapReadyCallback,LocationInstallMachineDialog.listener,LocationMachineAdapter.notifydata {

    private GoogleMap mMap;
    private Location item;

    private EditText editTextStatus,editTextCode,editTextAddress,editTextCity,editTextState,editTextZip,editTextCountry,editTextName,editTextPhone,editTextEmail
            ,editTextNotes,editTextWorkingHour,editTextWeek,editTextCommissionPercent,editTextLocation,editTextTax;

    private TextView textViewMode;

    private Spinner commissionSpinner,daysSpinner,dwmSpinner;

    private Button buttonEdit,buttonDelete,buttonUpdateLocation,buttonInstallMachine;
    private Boolean editmode;

    private FusedLocationProviderClient fusedLocationClient;
    private double longitude;
    private double latitude;

    protected static final int REQUEST_CHECK_SETTINGS = 0x3;

    private List<MachineInstall> machineInstallList = new ArrayList<>();
    private RecyclerView recyclerViewMachineInstall;
    private LocationMachineAdapter mAdapter;

    private int noOfMachine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_edit_location);

        item = (Location) getIntent().getExtras().getSerializable("itemData");
        noOfMachine = item.getNoOfMachines();
        hideKeyboard();


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        editTextStatus = findViewById(R.id.loc_editxt_status);
        editTextCode = findViewById(R.id.loc_editxt_code);
        editTextAddress = findViewById(R.id.loc_editxt_address);
        editTextCity = findViewById(R.id.loc_editxt_city);
        editTextState = findViewById(R.id.loc_editxt_state);
        editTextZip = findViewById(R.id.loc_editxt_zip);
        editTextCountry = findViewById(R.id.loc_editxt_country);
        editTextLocation = findViewById(R.id.loc_editxt_location);

        editTextName = findViewById(R.id.loc_editxt_name);
        editTextPhone = findViewById(R.id.loc_editxt_phone);
        editTextEmail = findViewById(R.id.loc_editxt_email);
        editTextNotes = findViewById(R.id.loc_editxt_notes);
        editTextWorkingHour =findViewById(R.id.loc_editxt_workinghour);
        editTextWeek = findViewById(R.id.loc_editxt_week);
        editTextCommissionPercent = findViewById(R.id.loc_editxt_commissionpercent);
        editTextTax =findViewById(R.id.loc_editxt_taxes);

        textViewMode = findViewById(R.id.loc_view_mode);

        commissionSpinner = findViewById(R.id.loc_spinner_commission);
        daysSpinner = findViewById(R.id.loc_spinner_days);
        dwmSpinner = findViewById(R.id.loc_spinner_DWM);

        buttonEdit = findViewById(R.id.loc_button_add);
        buttonDelete = findViewById(R.id.loc_button_cancel);
        buttonUpdateLocation =findViewById(R.id.loc_update_location);
        buttonInstallMachine = findViewById(R.id.loc_install_machine);

        init();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        initializeRecyclerView();
    }

    private void hideKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }


    }



    private void init() {

        editmode = false;
        buttonEdit.setText("Edit");
        buttonDelete.setText("Delete");

        buttonUpdateLocation.setEnabled(false);
        buttonUpdateLocation.setText(item.getLocation());

        textViewMode.setText("View Mode");

        editTextStatus.setText(item.getStatus());
        editTextCode.setText(item.getCode());
        editTextAddress.setText(item.getAddress());
        editTextCity.setText(item.getCity());
        editTextState.setText(item.getState());
        editTextZip.setText(item.getZip());
        editTextCountry.setText(item.getCountry());
        editTextLocation.setText(item.getLocation());

        editTextName.setText(item.getContactName());
        editTextPhone.setText(item.getContactPhone());
        editTextEmail.setText(item.getContactEmail());
        editTextNotes.setText(item.getNotes());
        editTextWorkingHour.setText(item.getWorkingHour());

        editTextCommissionPercent.setText(String.valueOf(item.getCommission()));
        editTextTax.setText(String.valueOf(item.getTax()));

        commissionSpinner.setSelection(getIndexFromSpinner(commissionSpinner,item.getCommissionType()));

        if(item.getTheDay().equals("day")){
            dwmSpinner.setSelection(0);
            daysSpinner.setVisibility(View.GONE);
            editTextWeek.setText(String.valueOf(item.getIntervalDay()));
        }else if(item.getTheDay().equals("month")){
            dwmSpinner.setSelection(2);
            daysSpinner.setVisibility(View.GONE);
            editTextWeek.setText(String.valueOf(item.getIntervalDay()/30));
        }
        else{
            dwmSpinner.setSelection(1);
            daysSpinner.setVisibility(View.VISIBLE);
            editTextWeek.setText(String.valueOf(item.getIntervalDay()/7));
            daysSpinner.setSelection(getIndexFromSpinner(daysSpinner,item.getTheDay()));
        }


        longitude = item.getLongitude();
        latitude = item.getLatitude();

        setEditTextEnabled(false);

        FirebaseUtilClass.getDatabaseReference().child("Location").child("Locations").child(item.getCode()).child("machineInstall").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                machineInstallList.clear();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    machineInstallList.add(new MachineInstall(dsp.getKey(),dsp.getValue().toString()));
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ViewEditLocation.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editmode){
                    AlertDialog dialog = new AlertDialog.Builder(ViewEditLocation.this)
                            .setMessage("Are you sure? Your Data will be Change")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    editData();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }
                else {
                    editmode = true;
                    setEditTextEnabled(true);
                    buttonEdit.setText("Done");
                    buttonDelete.setText("Cancel");
                    buttonUpdateLocation.setEnabled(true);
                    buttonUpdateLocation.setText("Update Location Info");
                    textViewMode.setText("Edit Mode");

                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editmode){
                    editmode = false;
                    init();
                }
                else {

                    if(noOfMachine > 0){
                        AlertDialog dialog = new AlertDialog.Builder(ViewEditLocation.this)
                                .setMessage("Please Uninstall Machine")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).show();

                    }else {
                        AlertDialog dialog1 = new AlertDialog.Builder(ViewEditLocation.this)
                                .setMessage("Are you sure? Your Data will be Change")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        delete();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();
                    }



                }
            }
        });

        buttonUpdateLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createLocationRequest();
            }
        });

        buttonInstallMachine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LocationInstallMachineDialog(ViewEditLocation.this,item.getCode(),noOfMachine).show();

            }
        });

        dwmSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0){
                    daysSpinner.setVisibility(View.GONE);
                }
                else if(i == 2){
                    daysSpinner.setVisibility(View.GONE);
                }
                else{
                    daysSpinner.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }




    private void delete() {
        FirebaseUtilClass.getDatabaseReference().child("Location").child("Locations").child(item.getCode()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@android.support.annotation.NonNull Task<Void> task) {
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@android.support.annotation.NonNull Exception e) {
            }
        });
    }

    private void editData() {
        boolean error =  false;

        String status = editTextStatus.getText().toString();
        if (TextUtils.isEmpty(status.trim())) {
            editTextStatus.setError("Field mustn't be empty");
            editTextStatus.requestFocus();
            error = true;
            return;
        }

        String code = editTextCode.getText().toString();
        if (TextUtils.isEmpty(code.trim())) {
            editTextCode.setError("Field mustn't be empty");
            editTextCode.requestFocus();
            error = true;
            return;
        }

        boolean isCodeChanged ;
        if(code.equals(item.getCode())){
            isCodeChanged = false;
        }
        else {
            isCodeChanged = true;
        }

        String address = editTextAddress.getText().toString();
        if (TextUtils.isEmpty(address.trim())) {
            editTextAddress.setError("Field mustn't be empty");
            editTextAddress.requestFocus();
            error = true;
            return;
        }

        String city = editTextCity.getText().toString();
        String state = editTextState.getText().toString();
        String zip = editTextZip.getText().toString();
        String notes = editTextNotes.getText().toString();

        String country = editTextCountry.getText().toString();
        if (TextUtils.isEmpty(country.trim())) {
            editTextCountry.setError("Field mustn't be empty");
            editTextCountry.requestFocus();
            error = true;
            return;
        }

        String location = editTextLocation.getText().toString();
        if (TextUtils.isEmpty(location.trim())) {
            editTextLocation.setError("Field mustn't be empty");
            editTextLocation.requestFocus();
            error = true;
            return;
        }

        String name = editTextName.getText().toString();
        String phone = editTextPhone.getText().toString();
        String email = editTextEmail.getText().toString();

        String commissiontype = commissionSpinner.getSelectedItem().toString();
        float commission = (float) 0.0;
        if(!commissiontype.equals("None")){
            try {
                commission = Float.parseFloat(editTextCommissionPercent.getText().toString());
            }
            catch (NumberFormatException e){
                editTextCommissionPercent.setError("Give a valid percentage");
                editTextCommissionPercent.requestFocus();
                error = true;
                return;
            }
        }

        float tax = (float) 0.0;
        String taxstr = editTextTax.getText().toString();
        if(!TextUtils.isEmpty(taxstr.trim())){
            try {
                tax = Float.parseFloat(editTextTax.getText().toString());
            }
            catch (NumberFormatException e){
                editTextTax.setError("Give a valid percentage");
                editTextTax.requestFocus();
                error = true;
                return;
            }
        }


        String workinghours = editTextWorkingHour.getText().toString();


        int intervalday;
        try {
            if(dwmSpinner.getSelectedItem().equals("Days")){
                intervalday = Integer.parseInt(editTextWeek.getText().toString());
            }
            else if(dwmSpinner.getSelectedItem().equals("Week")){
                intervalday = Integer.parseInt(editTextWeek.getText().toString()) * 7;
            }
            else if(dwmSpinner.getSelectedItem().equals("Month")){
                intervalday = Integer.parseInt(editTextWeek.getText().toString()) * 30;
            }
            else{
                intervalday = Integer.parseInt(editTextWeek.getText().toString());
            }

        }
        catch (NumberFormatException e){
            editTextWeek.setError("Give a service interval");
            editTextWeek.requestFocus();
            error = true;
            return;
        }

        String nextVisit;
        String day;

        if(dwmSpinner.getSelectedItem().equals("Days")){
            day = "day";
            Calendar date = Calendar.getInstance();
            date.add(Calendar.DAY_OF_MONTH, intervalday);
            DateFormat df = new SimpleDateFormat("dd-MM-yy");
            nextVisit = df.format(date.getTime());
        }
        else if(dwmSpinner.getSelectedItem().equals("Week")){
            day = daysSpinner.getSelectedItem().toString();


            int dow = daysSpinner.getSelectedItemPosition()+1;
            Calendar date = Calendar.getInstance();
            int diff = dow - date.get(Calendar.DAY_OF_WEEK);
            if (diff <= 0) {
                diff += intervalday;
            }
            date.add(Calendar.DAY_OF_MONTH, diff);
            DateFormat df = new SimpleDateFormat("dd-MM-yy");
            nextVisit = df.format(date.getTime());
        }
        else{
            day = "month";


            Calendar date = Calendar.getInstance();
            date.add(Calendar.DAY_OF_MONTH, intervalday);
            DateFormat df = new SimpleDateFormat("dd-MM-yy");
            nextVisit = df.format(date.getTime());
        }

        item.setNoOfMachines(noOfMachine);
        item.setState(status);
        item.setAddress(address);
        item.setCity(city);
        item.setState(state);
        item.setCountry(country);
        item.setZip(zip);
        item.setLocation(location);
        item.setContactName(name);
        item.setContactPhone(phone);
        item.setContactEmail(email);
        item.setCommissionType(commissiontype);
        item.setCommission(commission);
        item.setTax(tax);
        item.setWorkingHour(workinghours);
        item.setIntervalDay(intervalday);
        item.setTheDay(day);
        item.setLatitude(latitude);
        item.setLongitude(longitude);
        item.setNotes(notes);
        item.setNextVisit(nextVisit);



        if(!error){
            writeDataToFirebase(item,isCodeChanged);
        }

        for(int i =0;i<machineInstallList.size();i++){
            FirebaseUtilClass.getDatabaseReference().child("Location").child("Locations").child(code).child("machineInstall").child(machineInstallList.get(i).getLocation()).setValue(machineInstallList.get(i).getInstallationDate()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {


                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ViewEditLocation.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }



    }

    public void writeDataToFirebase(final Location item, final boolean isCodeChanged) {
        final ProgressDialog dialog = ProgressDialog.show(ViewEditLocation.this, "",
                "Loading. Please wait...", true);
        FirebaseUtilClass.getDatabaseReference().child("Location").child("Locations").child(item.getCode()).setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ViewEditLocation.this, "Updated", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                finish();

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ViewEditLocation.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private  void setEditTextEnabled(boolean bool){

        editTextStatus.setEnabled(bool);
        editTextCode.setEnabled(false);
        editTextAddress.setEnabled(bool);
        editTextCity.setEnabled(bool);
        editTextState.setEnabled(bool);
        editTextZip.setEnabled(bool);
        editTextCountry.setEnabled(bool);
        editTextLocation.setEnabled(bool);

        editTextName.setEnabled(bool);
        editTextPhone.setEnabled(bool);
        editTextEmail.setEnabled(bool);
        editTextNotes.setEnabled(bool);
        editTextWorkingHour.setEnabled(bool);
        editTextWeek.setEnabled(bool);
        editTextCommissionPercent.setEnabled(bool);
        editTextTax.setEnabled(bool);


        commissionSpinner.setEnabled(bool);
        daysSpinner.setEnabled(bool);
        dwmSpinner.setEnabled(bool);

    }

    private int getIndexFromSpinner(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }

        return 0;
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
        mMap.clear();

        // Add a marker in Sydney and move the camera
        LatLng location = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(location).title(item.getLocation()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15),2000,null);
    }

    protected void createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getLocation();
                return;
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // LocationFragment settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(ViewEditLocation.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }

    public void getLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }else{
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<android.location.Location>() {
                @Override
                public void onSuccess(android.location.Location location) {
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();

                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(ViewEditLocation.this, Locale.getDefault());



                    try {
                        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        editTextAddress.setText(addresses.get(0).getAddressLine(0));// If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        editTextCity.setText(addresses.get(0).getLocality());
                        editTextState.setText(addresses.get(0).getAdminArea());
                        editTextCountry.setText(addresses.get(0).getCountryName());
                        editTextZip.setText(addresses.get(0).getPostalCode());
                        editTextLocation.setText(addresses.get(0).getFeatureName());
                    } catch (IOException e) {
                        e.printStackTrace();

                    }

                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(ViewEditLocation.this);

                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ViewEditLocation.this, "Failed to get location", Toast.LENGTH_LONG).show();
                    longitude = 0;
                    latitude = 0;

                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(ViewEditLocation.this);
                }
            });
        }

    }

    @Override
    public void onMachineInstall() {
        noOfMachine = noOfMachine+1;
        updateMachineNo();
    }

    @Override
    public void isInstalling() {
        buttonInstallMachine.setEnabled(false);
        buttonInstallMachine.setText("wait");
        buttonInstallMachine.setTextColor(Color.GRAY);
    }

    private void initializeRecyclerView() {
        recyclerViewMachineInstall = findViewById(R.id.LocationMachineRecycler);

        mAdapter = new LocationMachineAdapter(machineInstallList,this,item.getCode(),noOfMachine);
        RecyclerView.LayoutManager mLayoutmanager =new LinearLayoutManager(this);
        recyclerViewMachineInstall.setLayoutManager(mLayoutmanager);
        recyclerViewMachineInstall.setItemAnimator(new DefaultItemAnimator());
        recyclerViewMachineInstall.setAdapter(mAdapter);
    }

    @Override
    public void onNoOfMachineDelete() {
        noOfMachine = noOfMachine-1;
        updateMachineNo();
    }

    private void updateMachineNo() {
        FirebaseUtilClass.getDatabaseReference().child("Location").child("Locations").child(item.getCode()).child("noOfMachines").setValue(noOfMachine).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                buttonInstallMachine.setEnabled(true);
                buttonInstallMachine.setText("Install");
                buttonInstallMachine.setTextColor(getResources().getColor(R.color.colorhighlight2));

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ViewEditLocation.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
