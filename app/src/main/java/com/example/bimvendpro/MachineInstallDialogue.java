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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MachineInstallDialogue extends Dialog {
    private MachineInstall item;
    private String code;
    private Spinner machineType;
    private EditText installDateEditText;
    private CalendarView installDateCalenderView;
    private Button cancelButton, installButton, uninstallButton;
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
            if (item.getInstallationDate() == null) {
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

                String str = format.format(new Date());
                installDateEditText.setText(str);
            } else {
                installDateEditText.setText(item.getInstallationDate());
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

                    Date d = dateFormat.parse(installDateEditText.getText().toString());
                    installDateCalenderView.setDate(d.getTime());

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            //    machineType.setSelection(getIndexFromSpinner(productTypeSpinner, item.getProductType()));
            installButton.setText("Update");
            uninstallButton.setVisibility(View.VISIBLE);
            machineType.setEnabled(false);
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
        uninstallButton = findViewById(R.id.uninstallButton);
        installButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                writeDataToFirebase(new MachineInstall(machineType.getSelectedItem().toString(), installDateEditText.getText().toString()));
            }
        });

        installDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                installDateCalenderView.setVisibility(View.VISIBLE);
                installDateEditText.setVisibility(View.GONE);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    Date d = dateFormat.parse(installDateEditText.getText().toString());
                    installDateCalenderView.setDate(d.getTime());

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });

        installDateCalenderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                installDateCalenderView.setVisibility(View.GONE);
                installDateEditText.setVisibility(View.VISIBLE);
                StringBuilder strBld = new StringBuilder();
                strBld.append(String.format("%02d", dayOfMonth)).append("-").append(String.format("%02d", month + 1)).append("-").append(year);
                installDateEditText.setText(strBld.toString());
                //installDateCalenderView.setVisibility(View.GONE);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        installDateCalenderView.setVisibility(View.GONE);


        uninstallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                installingOn();
                FirebaseUtilClass.getDatabaseReference().child("Machine").child("Items").child(code).child("machineInstall").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@android.support.annotation.NonNull Task<Void> task) {


                        FirebaseUtilClass.getDatabaseReference().child("Location").child("Locations").child(item.getLocation()).child("machineInstall").child(code).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@android.support.annotation.NonNull Task<Void> task) {
                                installingOff();
                                dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@android.support.annotation.NonNull Exception e) {
                                installingOff();
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@android.support.annotation.NonNull Exception e) {
                        installingOff();
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void spinnerLoadingOn() {
        machineType.setEnabled(false);
        spinnerLoading.setVisibility(View.VISIBLE);
        installButton.setEnabled(false);
    }

    private void spinnerLoadingOff() {
        if (!editDlg) {
            machineType.setEnabled(true);
        }
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
        if (!editDlg) {
            machineType.setEnabled(true);
        }
        installButton.setEnabled(true);
        cancelButton.setEnabled(true);
        machineInstallingProgressBar.setVisibility(View.GONE);
    }


    public void writeDataToFirebase(final MachineInstall item) {
        installingOn();


        FirebaseUtilClass.getDatabaseReference().child("Machine").child("Items").child(code).child("machineInstall").setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {


                FirebaseUtilClass.getDatabaseReference().child("Location").child("Locations").child(item.getLocation()).child("machineInstall").child(code).setValue(item.getInstallationDate()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
                        installingOff();
                        dismiss();

                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        installingOff();
                    }
                });

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                installingOff();
            }
        });

    }

    private void loadLocationsToSpinner() {
        spinnerLoadingOn();

        FirebaseUtilClass.getDatabaseReference().child("Location").child("Locations").orderByChild("code").addListenerForSingleValueEvent(new ValueEventListener() {
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
                if(editDlg) {
                    machineType.setSelection(getIndexFromSpinner(machineType, item.getLocation()));
                }
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
