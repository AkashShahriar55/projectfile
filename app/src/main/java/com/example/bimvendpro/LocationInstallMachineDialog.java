package com.example.bimvendpro;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LocationInstallMachineDialog extends Dialog {

    private String code;
    private int noOfMachine;
    private String date;
    private Spinner machineSpinner;
    private CalendarView installDateCalenderView;
    private Button cancelButton, installButton;
    private ProgressBar spinnerLoading, machineInstallingProgressBar;
    List<String> machinecode = new ArrayList<>();
    private listener mListener ;

    public LocationInstallMachineDialog(Context context) {
        super(context);
    }

    public LocationInstallMachineDialog(Context context, String code,int noOfMachine) {
        super(context);
        this.code = code;
        this.noOfMachine = noOfMachine;
        this.mListener = (listener) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.location_install_machine);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        getWindow().setAttributes(params);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        init();
    }


    private void init() {
        machineSpinner = findViewById(R.id.loc_install_machine_spinner);
        cancelButton = findViewById(R.id.cancelButton);
        installButton = findViewById(R.id.installButton);
        spinnerLoading = findViewById(R.id.spinnerLoading);
        installDateCalenderView = findViewById(R.id.loc_install_machine_Calender);

        installButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedMachine = (String) machineSpinner.getSelectedItem();
                if(!selectedMachine.equals("No machine")){
                    String mCode = machinecode.get(getIndexFromSpinner(machineSpinner,selectedMachine));
                    writeDataToFirebase(mCode,date,new MachineInstall(code,date));
                }else{
                    Toast.makeText(getContext(),"Add machine to install",Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        SimpleDateFormat ss = new SimpleDateFormat("dd-MM-yyyy");
        Date cdate = new Date(installDateCalenderView.getDate());
        date= ss.format(cdate);
        Log.d("date of now",""+date);

        installDateCalenderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                StringBuilder strBld = new StringBuilder();
                strBld.append(dayOfMonth).append("-").append(month).append("-").append(year);
                date = String.valueOf(strBld);
                Log.d("date of now",""+date);
            }
        });

        loadMachinesToSpinner();
    }

    private void spinnerLoadingOn() {
        machineSpinner.setEnabled(false);
        spinnerLoading.setVisibility(View.VISIBLE);
        installButton.setEnabled(false);
    }

    private void spinnerLoadingOff() {
        machineSpinner.setEnabled(true);
        installButton.setEnabled(true);
        spinnerLoading.setVisibility(View.GONE);
    }

    public void writeDataToFirebase(final String machineCode, final String date, final MachineInstall item) {

        final DatabaseReference databaseReference = FirebaseUtilClass.getDatabaseReference();

        databaseReference.child("Location").child("Locations").child(code).child("machineInstall").child(machineCode).setValue(date).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                databaseReference.child("Machine").child("Items").child(machineCode).child("machineInstall").setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mListener.onMachineInstall();
                        dismiss();

                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadMachinesToSpinner() {
        spinnerLoadingOn();

        FirebaseUtilClass.getDatabaseReference().child("Machine").child("Items").orderByChild("code").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> machine = new ArrayList<>();
                boolean hasMachine = false;
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    Machine m = dsp.getValue(Machine.class); //add result into array list
                    if(m.getMachineInstall() == null){
                        hasMachine = true;
                        machinecode.add(m.getCode());
                        machine.add(m.getCode() + " | " + m.getName());
                    }

                }

                if(!hasMachine){
                    machine.add("No machine");
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, machine);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                machineSpinner.setAdapter(adapter);
                spinnerLoadingOff();
                // mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getIndexFromSpinner(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }

        return 0;
    }

    public interface listener{
        public void onMachineInstall();
    }
}
