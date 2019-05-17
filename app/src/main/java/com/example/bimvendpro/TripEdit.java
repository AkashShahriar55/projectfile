package com.example.bimvendpro;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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

    private List<TripMachineProduct> tempProducts = new ArrayList<>();

    private Spinner spinnerDriver;
    private EditText editTextDate,editTextDescription,editTextName;
    private ImageButton imageButton;

    private Button buttonUpdate,buttonDelete,buttonPost;
    private PopupWindow popupWindow;

    final Calendar myCalendar = Calendar.getInstance();

    boolean flag = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trip);

        tripsItem = (TripsItem) getIntent().getExtras().getSerializable("item");
        if(tripsItem.getTripMachines() != null){
            tripMachinesList = tripsItem.getTripMachines();
            initializeRecyclerView();
        }

        hideKeyboard();

        TripEdit.this.setTitle("Trip Number - "+ tripsItem.getTripNumber());

        spinnerDriver = findViewById(R.id.trip_driver_name);
        loadMachinesToSpinner();

        editTextName = findViewById(R.id.trip_name);
        imageButton = findViewById(R.id.trip_get_location);

        editTextDate = findViewById(R.id.trip_date);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupWindow popUp = popupWindowsort();
                popUp.showAsDropDown(view, -10, 0); // show popup like dropdown list
            }
        });

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
                hideKeyboard();
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

    private PopupWindow popupWindowsort() {

        // initialize a pop up window type
        popupWindow = new PopupWindow(this);

        final ArrayList<String> sortList = new ArrayList<String>();

        FirebaseUtilClass.getDatabaseReference().child("Location").child("Locations").orderByChild("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    Location m = dsp.getValue(Location.class); //add result into array list
                    sortList.add(m.getLocation());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(TripEdit.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,
                sortList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                // Get the current item from ListView
                View view = super.getView(position,convertView,parent);

                // Get the Layout Parameters for ListView Current Item View
                ViewGroup.LayoutParams params = view.getLayoutParams();

                // Set the height of the Item View
                params.height = 100;
                view.setLayoutParams(params);

                return view;
            }
        };
        // the drop down list is a list view
        final ListView listViewSort = new ListView(this);

        // set our adapter and pass our pop up window contents
        listViewSort.setAdapter(adapter);

        // set on item selected
        listViewSort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                editTextName.setText(adapterView.getItemAtPosition(i).toString());
                popupWindow.dismiss();
            }
        });

        // some other visual settings for popup window
        popupWindow.setFocusable(true);
        popupWindow.setWidth(500);
        // popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        // set the listview as popup content
        popupWindow.setContentView(listViewSort);

        return popupWindow;
    }

    private void hideKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }


    }

    private void init() {

        if(tripsItem.getStatus().equals("posted")){
            editTextDescription.setEnabled(false);
            editTextDate.setEnabled(false);
            spinnerDriver.setEnabled(false);
            buttonPost.setVisibility(View.GONE);
            buttonUpdate.setVisibility(View.GONE);
        }

        editTextDate.setText(tripsItem.getTripDate());
        editTextDescription.setText(tripsItem.getTripDescription());
        editTextName.setText(tripsItem.getTripName());


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(TripEdit.this)
                        .setMessage("Are you sure? Data will be change.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tryWriteData();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();

            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(TripEdit.this)
                        .setMessage("Are you sure? Data will be lost.")
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
        });

        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(TripEdit.this)
                        .setMessage("Are you sure? It's can not be undone.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                postResults();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();

            }
        });
    }

    private void postResults() {

        tempProducts = new ArrayList<>();
        tryWriteData();
        if(tripMachinesList != null){
            for(int i = 0 ; i<tripMachinesList.size();i++){
                flag = false;
                String mcode = tripMachinesList.get(i).getCode();
                String lcode = tripMachinesList.get(i).getLocation();
                final double cashCollectd = tripMachinesList.get(i).getCashCollected();

                FirebaseUtilClass.getDatabaseReference().child("Machine").child("Items").child(mcode).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        double totalCollected = Double.parseDouble(dataSnapshot.child("totalCollected").getValue().toString());
                        totalCollected += cashCollectd;
                        FirebaseUtilClass.getDatabaseReference().child("Machine").child("Items").child(dataSnapshot.getKey()).child("totalCollected").setValue(totalCollected);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(TripEdit.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                FirebaseUtilClass.getDatabaseReference().child("Location").child("Locations").child(lcode).child("lastVisit").setValue(tripsItem.getTripDate());



                for(int j = 0; j<tripMachinesList.get(i).getTripMachineProducts().size();j++){
                    flag = false;
                    TripMachineProduct machineProduct = tripMachinesList.get(i).getTripMachineProducts().get(j);
                    final String pcode = machineProduct.getCode();
                    final int pnewCount = machineProduct.getNewCount();

                    FirebaseUtilClass.getDatabaseReference().child("Machine").child("Items").child(mcode).child("machineIngredients").child(pcode).child("lastCount").setValue(pnewCount);

                    boolean flag = false;
                    for(int k =0 ; k<tempProducts.size();k++){
                        if(machineProduct.getCode().equals(tempProducts.get(k).getCode())){
                            flag = true;
                            int sold = tempProducts.get(k).getSoldOrWin();
                            int filled = tempProducts.get(k).getFilled();

                            tempProducts.get(k).setFilled(filled + machineProduct.getFilled());
                            tempProducts.get(k).setSoldOrWin(sold+machineProduct.getSoldOrWin());

                        }
                    }

                    if(!flag){
                        tempProducts.add(machineProduct);
                    }



                }
            }

            FirebaseUtilClass.getDatabaseReference().child("Trip").child("Trips").child(tripsItem.getTripNumber()).child("status").setValue("posted");
        }

        for(int i = 0; i<tempProducts.size();i++){
            final String pcode = tempProducts.get(i).getCode();
            final int psold = tempProducts.get(i).getSoldOrWin();
            final int pfilled = tempProducts.get(i).getFilled();

            FirebaseUtilClass.getDatabaseReference().child("Inventory").child("Items").child(pcode).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    InventoryItem inventoryItem= dataSnapshot.getValue(InventoryItem.class);
                    int inwarehouse = inventoryItem.getInWarehouse();
                    int instock = inventoryItem.getInStock();
                    int inmachine = inventoryItem.getInMachine();
                    FirebaseUtilClass.getDatabaseReference().child("Inventory").child("Items").child(pcode).child("inMachine").setValue(inmachine - psold + pfilled);
                    FirebaseUtilClass.getDatabaseReference().child("Inventory").child("Items").child(pcode).child("inWarehouse").setValue(inwarehouse - pfilled);
                    FirebaseUtilClass.getDatabaseReference().child("Inventory").child("Items").child(pcode).child("inStock").setValue(instock - psold);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(TripEdit.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void delete() {
        FirebaseUtilClass.getDatabaseReference().child("Trip").child("Trips").child(tripsItem.getTripNumber()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
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

        String tripName = editTextName.getText().toString();

        tripsItem.setDriverName(driverName);
        tripsItem.setTripDescription(tripDescription);
        tripsItem.setTripName(tripName);

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
                spinnerDriver.setSelection(getIndexFromSpinner(spinnerDriver,tripsItem.getDriverName()));

                if(tripsItem.getStatus().equals("posted")){
                    spinnerDriver.setEnabled(false);
                }

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
        mAdapter = new TripMachineAdapter(tripMachinesList,this,tripsItem.getTripNumber(),tripsItem.getStatus());
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
                mAdapter.notifyDataSetChanged();
                tripsItem.setTripMachines(tripMachinesList);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
