
package com.example.bimvendpro;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class TripAddtrips extends AppCompatActivity implements TripAddLocationDialog.NoticeDialogListener,TripLocationAdapter.onDeleteClick {


    private RecyclerView recyclerViewAddLocation;
    private TripLocationAdapter mAdapter;
    private Spinner spinnerDriver;
    private EditText editTextDate,editTextDescription;
    private Button buttonAddLocation,buttonAdd,buttonCancel;
    private TextView textViewDummy;
    private List<Location> locationList = new ArrayList<>();
    private List<Machine> machineList = new ArrayList<>();
    private List<TripMachineProduct> tripMachineProducts = new ArrayList<>();
    private List<TripMachines> tripMachines = new ArrayList<>();
    private List<MachineIngredients> machineIngredients = new ArrayList<>();

    private static final String ALLOWED_CHARACTERS ="0123456789abcde";

    private int noOfLocation = 0;
    private int noOfMachine = 0;

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

        buttonAdd = findViewById(R.id.trip_button_add);
        buttonCancel = findViewById(R.id.trip_button_cancel);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryWriteData();
            }
        });

        initializeRecyclerView();

    }

    private static String getTripNumber(){
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(8);

        for(int i=0;i<8;++i){
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));

        }

        return sb.toString();
    }

    private void tryWriteData() {
        tripMachines = new ArrayList<>();
        tripMachineProducts = new ArrayList<>();
        machineIngredients = new ArrayList<>();
        boolean error = false;
        if(!machineList.isEmpty()){

            for(int i =0 ; i< machineList.size();i++){
                machineIngredients = new ArrayList<>();
                String name = machineList.get(i).getName();
                String location = machineList.get(i).getMachineInstall().getLocation();
                String type = machineList.get(i).getType();
                String machineCode = machineList.get(i).getCode();
                Log.d("data", "machine count: " + i);
                if(machineList.get(i).getMachineIngredients()!=null){
                    machineIngredients = machineList.get(i).getMachineIngredientsToList();
                    for(int j =1;j<machineIngredients.size();j++){
                        Log.d("data", "product count: " + machineIngredients.size() + " " + j);
                        String productName = machineIngredients.get(j).getName();
                        int lastCount = machineIngredients.get(j).getLastCount();
                        String productCode = machineIngredients.get(j).getCode();
                        tripMachineProducts.add(new TripMachineProduct(productName,lastCount,productCode));
                    }
                }

                tripMachines.add(new TripMachines(name,location,type,tripMachineProducts,machineCode));
            }
        }

        String driverName = spinnerDriver.getSelectedItem().toString();

        String tripDate = editTextDate.getText().toString();
        if(TextUtils.isEmpty(tripDate.trim())) {
            editTextDate.setError("Field mustn't be empty");
            editTextDate.requestFocus();
            error = true;
            return;
        }

        String tripDescription = editTextDescription.getText().toString();

        String tripNumber = getTripNumber();



        if(!error){
            writeDataToFirebase(new TripsItem(driverName,tripDate,tripDescription,noOfLocation,noOfMachine,tripNumber,tripMachines));
        }
    }

    private void writeDataToFirebase(TripsItem item) {
        final ProgressDialog dialog = ProgressDialog.show(TripAddtrips.this, "",
                "Loading. Please wait...", true);
        FirebaseUtilClass.getDatabaseReference().child("Trip").child("Trips").child(item.getTripNumber()).setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(TripAddtrips.this, "Updated", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                finish();

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TripAddtrips.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
                    drivers.add("No Driver");
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
            noOfLocation += 1;
            locationList.add(location);
            if(location.getMachines() != null){
                noOfMachine += location.getNoOfMachines();
                machineList.addAll(location.getMachines());

            }
            Log.d("no of machine", "deleteClicked: " + machineList.size());
            textViewDummy.setVisibility(View.GONE);
        }

        if(location.getMachines() != null){
            Log.d("machine info", "machine size on passData: "+location.getMachines().size());
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
        locationList.remove(itemPass);
        if(itemPass.getMachines() != null){
            machineList.removeAll(itemPass.getMachines());
            Log.d("no of machine", "deleteClicked: " + machineList.size());
        }

        mAdapter.notifyDataSetChanged();
    }
}
