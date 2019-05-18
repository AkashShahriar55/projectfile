package com.example.bimvendpro;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.Mac;

public class TripAddLocationDialog extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private Spinner spinnerAddLocation;
    private TripAddLocationDialog.NoticeDialogListener listener;

    List<String> locationNames = new ArrayList<>();
    List<Location> locationItemList = new ArrayList<>();
    List<Machine> machineList = new ArrayList<>();
    TextView textViewName,textViewAddress,textViewNoOfMachine;
    ArrayAdapter<CharSequence> listAdapter;
    private int itemNo;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.trip_add_location_dialog, null);

        builder.setView(view)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {


                        Log.d("machine info", "machine size on add: "+ machineList.size());
                        listener.passData(locationItemList.get(itemNo));

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        spinnerAddLocation = view.findViewById(R.id.Trips_add_location_spinner);

        textViewName = view.findViewById(R.id.trip_add_location_name);
        textViewAddress = view.findViewById(R.id.trip_add_location_address);
        textViewNoOfMachine = view.findViewById(R.id.trip_add_location_noOfMachine);

        spinnerAddLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textViewName.setText(locationItemList.get(position).getLocation());
                textViewAddress.setText(locationItemList.get(position).getAddress());
                textViewNoOfMachine.setText(String.valueOf(locationItemList.get(position).getNoOfMachines()));
                itemNo = position;
                machineList = new ArrayList<>();
                FirebaseUtilClass.getDatabaseReference().child("Location").child("Locations").child(locationItemList.get(itemNo).getCode()).child("machineInstall").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            //Log.d("machine code", "onDataChange machine code: "+dsp.getKey());
                            FirebaseUtilClass.getDatabaseReference().child("Machine").child("Items").child(dsp.getKey()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    //Log.d("machine code", "machine code onDataChange: "+ dataSnapshot.getValue(Machine.class).getCode());
                                    if(dataSnapshot != null){
                                        machineList.add(dataSnapshot.getValue(Machine.class));
                                        locationItemList.get(itemNo).setMachines(machineList);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            });
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return builder.create();
    }


    public interface NoticeDialogListener{
        void passData(Location location);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);



        listener = (TripAddLocationDialog.NoticeDialogListener) context;



        populateListFromDatabase();
    }

    private void populateListFromDatabase() {

        FirebaseUtilClass.getDatabaseReference().child("Location").child("Locations").orderByChild("location").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                    locationItemList.add(dsp.getValue(Location.class)); //add result into array list
                    locationNames.add(dsp.getValue(Location.class).getLocation());


                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, locationNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerAddLocation.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
