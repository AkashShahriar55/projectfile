package com.example.bimvendpro;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TripEdit extends AppCompatActivity{

    private TripsItem tripsItem;
    private List<TripMachines> tripMachinesList;
    private RecyclerView recyclerViewMachines;
    private TripMachineAdapter mAdapter;

    private Spinner spinnerDriver;
    private EditText editTextDate,editTextDescription;

    private Button buttonUpdate,buttonDelete,buttonPost;

    final Calendar myCalendar = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trip);

        tripsItem = (TripsItem) getIntent().getExtras().getSerializable("item");
        if(tripsItem.getTripMachines() != null){
            tripMachinesList = tripsItem.getTripMachines();
            initializeRecyclerView();
        }

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
                new DatePickerDialog(TripEdit.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        editTextDescription = findViewById(R.id.trip_description);

        buttonUpdate = findViewById(R.id.trip_button_update);
        buttonDelete = findViewById(R.id.trip_button_delete);
        buttonPost = findViewById(R.id.trip_button_post);

        init();


    }

    private void init() {
        editTextDate.setText(tripsItem.getTripDate());
        editTextDescription.setText(tripsItem.getTripDescription());
        spinnerDriver.setSelection(getIndexFromSpinner(spinnerDriver,tripsItem.getDriverName()));

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryWriteData();
            }
        });
    }

    private void tryWriteData() {
        boolean error = false;
        String driverName = spinnerDriver.getSelectedItem().toString();

        String tripDate = editTextDate.getText().toString();
        if(TextUtils.isEmpty(tripDate.trim())) {
            editTextDate.setError("Field mustn't be empty");
            editTextDate.requestFocus();
            error = true;
            return;
        }

        String tripDescription = editTextDescription.getText().toString();

        if(tripMachinesList!=null){
            double cashCollected =0;
            for(int i=0 ; i< tripMachinesList.size();i++){
                cashCollected += tripMachinesList.get(i).getCashCollected();
            }
            tripsItem.setCashCollected(cashCollected);
        }

        writeDataToFirebase(tripsItem);
    }

    private void writeDataToFirebase(TripsItem tripsItem) {
        final ProgressDialog dialog = ProgressDialog.show(TripEdit.this, "",
                "Loading. Please wait...", true);
        FirebaseUtilClass.getDatabaseReference().child("Trip").child("Trips").child(tripsItem.getTripNumber()).setValue(tripsItem).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(TripEdit.this, "Updated", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                finish();

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TripEdit.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
                    drivers.add("No Driver");
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(TripEdit.this, android.R.layout.simple_spinner_item, drivers);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerDriver.setAdapter(adapter);

                // mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(TripEdit.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editTextDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void initializeRecyclerView() {
        recyclerViewMachines = findViewById(R.id.trip_machine_recyclerview);
        mAdapter = new TripMachineAdapter(tripMachinesList,this,tripsItem.getTripNumber());
        RecyclerView.LayoutManager mLayoutmanager =new LinearLayoutManager(this);
        recyclerViewMachines.setLayoutManager(mLayoutmanager);
        recyclerViewMachines.setItemAnimator(new DefaultItemAnimator());
        recyclerViewMachines.setAdapter(mAdapter);
    }

    private int getIndexFromSpinner(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }

        return 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                TripMachines tripMachines = (TripMachines) data.getExtras().getSerializable("machine");
                int machineNo = data.getExtras().getInt("machineNo");
                tripMachinesList.set(machineNo,tripMachines);
                tripsItem.setTripMachines(tripMachinesList);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
