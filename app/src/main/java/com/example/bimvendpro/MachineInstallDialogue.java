package com.example.bimvendpro;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public class MachineInstallDialogue extends Dialog {
    private MachineInstall item;
    private Spinner machineType;
    private EditText installDateEditText;
    private CalendarView installCalenderView;
    private Button cancelButton, installButton;
    private ProgressBar spinnerLoading;
    private boolean editDlg=false;
    public MachineInstallDialogue( Context context) {
        super(context);
    }
    public MachineInstallDialogue( Context context, MachineInstall item) {
        super(context);
        this.item=item;
    }

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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE );

        init();

        if (item != null) {  //dlg type edit
           installDateEditText.setText(item.getDate().toString());

        //    machineType.setSelection(getIndexFromSpinner(productTypeSpinner, item.getProductType()));
            installButton.setText("Update");

            editDlg=true;
        }

        loadLocationsToSpinner();

    }

    private void init(){
        machineType=findViewById(R.id.machineTypeSpinner);
        installDateEditText=findViewById(R.id.installDateEditText);
        cancelButton=findViewById(R.id.cancelButton);
        installButton=findViewById(R.id.installButton);
        spinnerLoading=findViewById(R.id.spinnerLoading);

    }

    private void spinnerLoadingOn(){
        machineType.setEnabled(false);
        spinnerLoading.setVisibility(View.VISIBLE);
        installButton.setEnabled(false);
    }
    private void spinnerLoadingOff(){
        machineType.setEnabled(true);
        installButton.setEnabled(true);
        spinnerLoading.setVisibility(View.GONE);
    }

    private void loadLocationsToSpinner(){

        FirebaseUtilClass.getDatabaseReference().child("Location").child("Locations").orderByChild("code").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                    itemList.add(dsp.getValue(Machine.class)); //add result into array list
                }

                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
