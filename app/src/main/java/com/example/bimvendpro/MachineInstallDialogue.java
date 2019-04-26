package com.example.bimvendpro;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MachineInstallDialogue extends Dialog {
    private MachineInstall item;
    private String code;
    private Spinner machineType;
    private EditText installDateEditText;
    private CalendarView installDateCalenderView;
    private Button cancelButton, installButton;
    private ProgressBar spinnerLoading, machineInstallingProgressBar;
    private boolean editDlg = false;

    public MachineInstallDialogue(Context context) {
        super(context);
    }

    public MachineInstallDialogue(Context context, String code, MachineInstall item) {
        super(context);
        this.code = code;
        this.item = item;
    }

    //todo: partial complete
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.machine_install_dlg);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        getWindow().setAttributes(params);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        init();

        if (item != null) {  //dlg type edit
            if(item.getInstallationDate()==null){
                installDateEditText.setText("not set");
            }else {
                installDateEditText.setText(item.getInstallationDate().toString());
            }
            //    machineType.setSelection(getIndexFromSpinner(productTypeSpinner, item.getProductType()));
            installButton.setText("Update");

            editDlg = true;
        }

        loadLocationsToSpinner();

    }

    private void init() {
        machineType = findViewById(R.id.machineTypeSpinner);
        installDateEditText = findViewById(R.id.installDateEditText);
        cancelButton = findViewById(R.id.cancelButton);
        installButton = findViewById(R.id.installButton);
        spinnerLoading = findViewById(R.id.spinnerLoading);
        machineInstallingProgressBar = findViewById(R.id.installingProgressBar);
        installDateCalenderView = findViewById(R.id.installDateCalender);

        installButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: validate & update data to firebase
            }
        });

        installDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SimpleDateFormat format = new SimpleDateFormat("dd-mm-yyyy");
                    Date date = format.parse(installDateEditText.getText().toString());
                    installDateCalenderView.setDate(date.getTime());

                } catch (ParseException e) {
                    SimpleDateFormat format = new SimpleDateFormat("dd-mm-yyyy", Locale.getDefault());

                    String str = format.format(new Date());
                    installDateEditText.setText(str);
                    //installDateCalenderView.setDate(date.getTime());
                }
                installDateCalenderView.setVisibility(View.VISIBLE);
            }
        });

        installDateCalenderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                StringBuilder strBld = new StringBuilder();
                strBld.append(dayOfMonth).append("-").append(month).append("-").append(year);
                installDateEditText.setText(strBld.toString());
                installDateCalenderView.setVisibility(View.GONE);
            }
        });
    }

    private void spinnerLoadingOn() {
        machineType.setEnabled(false);
        spinnerLoading.setVisibility(View.VISIBLE);
        installButton.setEnabled(false);
    }

    private void spinnerLoadingOff() {
        machineType.setEnabled(true);
        installButton.setEnabled(true);
        spinnerLoading.setVisibility(View.GONE);
    }

    private void installingOn() {
        machineType.setEnabled(false);
        installButton.setEnabled(false);
        cancelButton.setEnabled(false);
        installDateCalenderView.setVisibility(View.GONE);
        machineInstallingProgressBar.setVisibility(View.VISIBLE);
    }

    private void installingOff() {
        machineType.setEnabled(true);
        installButton.setEnabled(true);
        cancelButton.setEnabled(true);
        machineInstallingProgressBar.setVisibility(View.GONE);
    }


    private void loadLocationsToSpinner() {
        spinnerLoadingOn();

        FirebaseUtilClass.getDatabaseReference().child("LocationFragment").child("Locations").orderByChild("code").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> loc = new ArrayList<>();

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                    Location l = dsp.getValue(Location.class); //add result into array list
                    loc.add(l.getCode());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, loc);
                ;
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                machineType.setAdapter(adapter);
                machineType.setSelection(getIndexFromSpinner(machineType, item.getLocation()));
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
}
