
package com.example.bimvendpro;

import android.app.DatePickerDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TripAddtrips extends AppCompatActivity implements TripAddLocationDialog.NoticeDialogListener,TripLocationAdapter.onDeleteClick {


    private RecyclerView recyclerViewAddLocation;
    private TripLocationAdapter mAdapter;
    private Spinner spinnerDriver;
    private EditText editTextDate,editTextDescription;
    private Button buttonAddLocation;
    private TextView textViewDummy;
    private List<Location> locationList = new ArrayList<>();
    final Calendar myCalendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_addtrips);

        spinnerDriver = findViewById(R.id.trip_driver_name);
        loadMachinesToSpinner();

        editTextDate = findViewById(R.id.trip_date);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };


        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(TripAddtrips.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        editTextDescription = findViewById(R.id.trip_description);

        buttonAddLocation = findViewById(R.id.trip_addLocation);
        buttonAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        textViewDummy = findViewById(R.id.trip_addLocationDummy);

        initializeRecyclerView();

    }

    private void openDialog() {
        TripAddLocationDialog tripAddLocationDialog = new TripAddLocationDialog();
        tripAddLocationDialog.show(getSupportFragmentManager(),"Add Location");
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editTextDate.setText(sdf.format(myCalendar.getTime()));

    }




    private void loadMachinesToSpinner() {
        spinnerDriver.setEnabled(false);

        FirebaseUtilClass.getDatabaseReference().child("Driver").child("Drivers").orderByChild("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> drivers = new ArrayList<>();
                boolean hasDriver = false;
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    DriverItem m = dsp.getValue(DriverItem.class); //add result into array list
                    if(m != null){
                        hasDriver = true;
                        drivers.add(m.getName());
                        spinnerDriver.setEnabled(true);
                    }
                }

                if(!hasDriver){
                    drivers.add("No Location");
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(TripAddtrips.this, android.R.layout.simple_spinner_item, drivers);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerDriver.setAdapter(adapter);

                // mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(TripAddtrips.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void passData(Location location) {
        boolean hasAlready = false;
        for(int i = 0 ; i< locationList.size();i++){
            if(locationList.get(i).getCode().equals(location.getCode())){
                hasAlready = true;
            }
        }

        if(!hasAlready){
            locationList.add(location);
            textViewDummy.setVisibility(View.GONE);
        }



        mAdapter.notifyDataSetChanged();
    }

    private void initializeRecyclerView() {
        recyclerViewAddLocation = findViewById(R.id.rut_recyclerview);

        mAdapter = new TripLocationAdapter(locationList,this, this);
        RecyclerView.LayoutManager mLayoutmanager =new LinearLayoutManager(this);
        recyclerViewAddLocation.setLayoutManager(mLayoutmanager);
        recyclerViewAddLocation.setItemAnimator(new DefaultItemAnimator());
        recyclerViewAddLocation.setAdapter(mAdapter);
    }

    @Override
    public void deleteClicked(Location itemPass) {

    }
}
