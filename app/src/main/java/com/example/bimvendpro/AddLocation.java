package com.example.bimvendpro;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.List;
import java.util.Locale;


public class AddLocation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    double lattitude;
    double longitude;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    Intent intent;
    String intetType;



    String knownName ="" ;

    private EditText editTextStatus,editTextCode,editTextAddress,editTextCity,editTextState,editTextZip,editTextCountry,editTextName,editTextPhone,editTextEmail
            ,editTextNotes,editTextWorkingHour,editTextWeek,editTextCommissionPercent,editTextLocation,editTextTax;

    private Spinner commissionSpinner,daysSpinner;

    private Button buttonAdd,buttonCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

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

        commissionSpinner = findViewById(R.id.loc_spinner_commission);
        daysSpinner = findViewById(R.id.loc_spinner_days);

        buttonAdd = findViewById(R.id.loc_button_add);
        buttonCancel = findViewById(R.id.loc_button_cancel);

        init();

    }

    private void init() {

        createLocationRequest();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryWriteData();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        editTextStatus.setText("Active");

    }

    private void tryWriteData(){

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
            intervalday = Integer.parseInt(editTextWeek.getText().toString()) * 7;
        }
        catch (NumberFormatException e){
            editTextWeek.setError("Give a service interval");
            editTextWeek.requestFocus();
            error = true;
            return;
        }

        String day = daysSpinner.getSelectedItem().toString();


        if(!error){
            writeDataToFirebase(new Location(status,code,address,city,state,zip,country,location,name,phone,email,commissiontype,commission,tax,workinghours,intervalday,day,longitude,lattitude,notes));
        }
    }

    public void writeDataToFirebase(final Location item) {
        final ProgressDialog dialog = ProgressDialog.show(AddLocation.this, "",
                "Loading. Please wait...", true);
        FirebaseUtilClass.getDatabaseReference().child("Location").child("Locations").child(item.getCode()).setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AddLocation.this, "Updated", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddLocation.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onBackPressed() {

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("Are you sure? Your input will be lost")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AddLocation.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();


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
    public void getLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }else{
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<android.location.Location>() {
                @Override
                public void onSuccess(android.location.Location location) {
                    longitude = location.getLongitude();
                    lattitude = location.getLatitude();

                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(AddLocation.this);

                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddLocation.this, "Failed to get location", Toast.LENGTH_LONG).show();
                    longitude = 0;
                    lattitude = 0;

                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(AddLocation.this);
                }
            });
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());



        try {
            addresses = geocoder.getFromLocation(lattitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            editTextAddress.setText(addresses.get(0).getAddressLine(0));// If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            editTextCity.setText(addresses.get(0).getLocality());
            editTextState.setText(addresses.get(0).getAdminArea());
            editTextCountry.setText(addresses.get(0).getCountryName());
            editTextZip.setText(addresses.get(0).getPostalCode());
            editTextLocation.setText(addresses.get(0).getFeatureName());
        } catch (IOException e) {
            e.printStackTrace();

        }


        // Add a marker in Sydney and move the camera
        Toast.makeText(this,String.valueOf(longitude) + String.valueOf(lattitude),Toast.LENGTH_LONG).show();
        LatLng sydney = new LatLng(lattitude, longitude);
        mMap.addMarker(new MarkerOptions().position(sydney).title(knownName));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
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
                        resolvable.startResolutionForResult(AddLocation.this,
                                REQUEST_CHECK_SETTINGS);
                        getLocation();
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }
}
