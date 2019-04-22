package com.example.bimvendpro;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MachineAddDialogue extends Dialog {
    private Machine item;
    private EditText codeEditText, machineNameEditText, modelEditText;
    private Spinner machineTypeSpinner;
    private Button cancelButton, deleteButton, addButton;
    private ProgressBar progressBar;
    private Boolean editDlg;


    public MachineAddDialogue(Context context) {
        super(context);

    }

    public MachineAddDialogue(Context context, Machine item) {
        super(context);
        this.item = item;

    }

    private void hideKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void confirmDeleteDlg() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        deleteItem();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Are you sure to delete the item?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }


    private void init() {
        codeEditText = findViewById(R.id.codeEditText);
        machineNameEditText = findViewById(R.id.machineNameEditTExt);
        machineTypeSpinner = findViewById(R.id.machineTypeSpinner);
        modelEditText = findViewById(R.id.modelEditText);
        cancelButton = findViewById(R.id.cancelButton);
        deleteButton = findViewById(R.id.deleteButton);
        addButton = findViewById(R.id.addButton);
        progressBar = findViewById(R.id.loadingProgressBar);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();


                dismiss();

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                confirmDeleteDlg();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                tryWriteData();
            }
        });
        setCancelable(false);
    }


    private void deleteItem() {
        loading();
        FirebaseUtilClass.getDatabaseReference().child("Inventory").child("Items").child(item.getCode()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@android.support.annotation.NonNull Task<Void> task) {
                notLoading();
                MachineAddDialogue.this.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@android.support.annotation.NonNull Exception e) {
                notLoading();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.machine_add_dlg);


        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        getWindow().setAttributes(params);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        init();

        if (item != null) {  //dlg type edit
            codeEditText.setText(item.getCode());
            machineNameEditText.setText(String.valueOf(item.getName()));
            modelEditText.setText(String.valueOf(item.getModel()));

            machineTypeSpinner.setSelection(getIndexFromSpinner(machineTypeSpinner, item.getType()));
            addButton.setText("Update");
            deleteButton.setVisibility(View.VISIBLE);
            editDlg = true;
            codeEditText.setEnabled(false);
        }


    }

    private int getIndexFromSpinner(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }

        return 0;
    }

    private void loading() {
        progressBar.setVisibility(View.VISIBLE);
        modelEditText.setEnabled(false);
        machineNameEditText.setEnabled(false);
        machineTypeSpinner.setEnabled(false);
        addButton.setEnabled(false);
        deleteButton.setEnabled(false);
        cancelButton.setEnabled(false);
    }

    private void notLoading() {
        progressBar.setVisibility(View.GONE);
        modelEditText.setEnabled(true);
        machineNameEditText.setEnabled(true);
        machineTypeSpinner.setEnabled(true);
        addButton.setEnabled(true);
        deleteButton.setEnabled(true);
    }

    private void tryWriteData() {

        boolean error = false;
        String codeStr = codeEditText.getText().toString();
        String machineNameStr = machineNameEditText.getText().toString();
        String machineModelStr = modelEditText.getText().toString();
        String machineType = machineTypeSpinner.getSelectedItem().toString();


        if (TextUtils.isEmpty(codeStr.trim())) {
            codeEditText.setError("Field mustn't be empty");
            codeEditText.requestFocus();
            error = true;
            return;
        }

        if (TextUtils.isEmpty(machineNameStr.trim())) {
            machineNameEditText.setError("Field mustn't be empty");
            machineNameEditText.requestFocus();
            error = true;
            return;
        }

        if (TextUtils.isEmpty(machineModelStr.trim())) {
            modelEditText.setError("Field mustn't be empty");
            modelEditText.requestFocus();
            error = true;
            return;
        }


        if (!error)
            writeDataToFirebase(new Machine(codeStr, machineNameStr, machineModelStr, machineType));

    }

    public void writeDataToFirebase(final Machine item) {
        loading();


        FirebaseUtilClass.getDatabaseReference().child("Machine").child("Items").child(item.getCode()).setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
                notLoading();
                dismiss();

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                notLoading();
            }
        });

    }
}
