package com.example.bimvendpro;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;



public class InventoryItemAddDialogue extends Dialog {
    private EditText codeEditText, inMachineEditText, inStockEditText, inWareHouseEditText, lastCostEditText, productNameEditText, unitPerCaseEditText;
    //   private TextView messageTextView;
    private Spinner productTypeSpinner;
    private ProgressBar progressBar;
    private Button addButton, cancelButton, deleteButton;
    private InventoryItem item;
    private boolean editDlg=false;


    public InventoryItemAddDialogue(@NonNull Context context) {
        super(context);

    }

    public InventoryItemAddDialogue(@NonNull Context context, InventoryItem item) {
        super(context);
        this.item = item;

    }

    public InventoryItemAddDialogue(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected InventoryItemAddDialogue(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    private void init() {
        hideKeyboard();
        codeEditText = findViewById(R.id.codeEditText);
        inMachineEditText = findViewById(R.id.inMachineEditText);
        inStockEditText = findViewById(R.id.inStockEditText);
        inWareHouseEditText = findViewById(R.id.inWarehouseEditText);
        lastCostEditText = findViewById(R.id.lastCostEditText);
        productTypeSpinner = findViewById(R.id.productTypeSpinner);
        productNameEditText = findViewById(R.id.productNameEditTExt);
        unitPerCaseEditText = findViewById(R.id.unitPerCaseEditText);
        addButton = findViewById(R.id.addButton);
        cancelButton = findViewById(R.id.cancelButton);
        progressBar = findViewById(R.id.loadingProgressBar);
        deleteButton = findViewById(R.id.deleteButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                tryWriteData();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                if(isEdited()){
                    confirmCancelDlg();

                } else {

                    dismiss();
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                confirmDeleteDlg();
            }
        });

        setCancelable(false);
    }

    private void hideKeyboard(){
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

    private void confirmCancelDlg() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        InventoryItemAddDialogue.this.dismiss();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Some item edited, are you sure to cancel?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private void deleteItem() {
        loading();
        FirebaseUtilClass.getDatabaseReference().child("Inventory").child("Items").child(item.getCode()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@android.support.annotation.NonNull Task<Void> task) {
                notLoading();
                InventoryItemAddDialogue.this.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@android.support.annotation.NonNull Exception e) {
                notLoading();
            }
        });
    }

    private void loading() { //busy state, updating data
        progressBar.setVisibility(View.VISIBLE);
        codeEditText.setEnabled(false);
        inMachineEditText.setEnabled(false);
        inStockEditText.setEnabled(false);
        inWareHouseEditText.setEnabled(false);
        lastCostEditText.setEnabled(false);
        productTypeSpinner.setEnabled(false);
        productNameEditText.setEnabled(false);
        unitPerCaseEditText.setEnabled(false);
        addButton.setEnabled(false);
        cancelButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    private void notLoading() { //normal state, data are now editable
        progressBar.setVisibility(View.GONE);
        codeEditText.setEnabled(true);
        inMachineEditText.setEnabled(true);
        inStockEditText.setEnabled(true);
        inWareHouseEditText.setEnabled(true);
        lastCostEditText.setEnabled(true);
        productTypeSpinner.setEnabled(true);
        productNameEditText.setEnabled(true);
        unitPerCaseEditText.setEnabled(true);
        addButton.setEnabled(true);
        cancelButton.setEnabled(true);
        deleteButton.setEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_item_dialogue);


        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        getWindow().setAttributes(params);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE );

        init();

        if (item != null) {  //dlg type edit
            codeEditText.setText(item.getCode());
            inMachineEditText.setText(String.valueOf(item.getInMachine()));
            inStockEditText.setText(String.valueOf(item.getInStock()));
            inWareHouseEditText.setText(String.valueOf(item.getInWarehouse()));
            lastCostEditText.setText(String.valueOf(item.getLastCost()));
            productNameEditText.setText(item.getProductName());
            unitPerCaseEditText.setText(String.valueOf(item.getUnitPerCase()));
            productTypeSpinner.setSelection(getIndexFromSpinner(productTypeSpinner, item.getProductType()));
            addButton.setText("Update");
            deleteButton.setVisibility(View.VISIBLE);
            editDlg=true;
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

    private boolean isEdited() {

        String codeStr = codeEditText.getText().toString();
        String productNameStr = productNameEditText.getText().toString();
        String productTypeStr = productTypeSpinner.getSelectedItem().toString();
        int inMachineInt = -1;
        int inStockInt = -1;
        int inWareHouseInt = -1;
        float lastCostFloat = -1;
        int unitPerCaseInt = -1;
        try {
            inStockInt = Integer.parseInt(inStockEditText.getText().toString());
        } catch (NumberFormatException e) {

        }
        try {
            inWareHouseInt = Integer.parseInt(inWareHouseEditText.getText().toString());
        } catch (NumberFormatException e) {

        }
        try {
            inMachineInt = Integer.parseInt(inMachineEditText.getText().toString());
        } catch (NumberFormatException e) {

        }
        try {
            unitPerCaseInt = Integer.parseInt(unitPerCaseEditText.getText().toString());
        } catch (NumberFormatException e) {

        }
        try {
            lastCostFloat = Float.parseFloat(lastCostEditText.getText().toString());
        } catch (NumberFormatException e) {

        }
        if (item!=null){
            if(codeStr.equals(item.getCode()) && productNameStr.equals(item.getProductName()) && productTypeStr.equals(item.getProductType())
            && inMachineInt==item.getInMachine() && inStockInt==item.getInStock() && inWareHouseInt==item.getInWarehouse() && lastCostFloat==item.getLastCost()
            && unitPerCaseInt==item.getUnitPerCase()){
                return false;
            }else
                return true;
        } else {
            if(!TextUtils.isEmpty(codeStr.trim()) || !TextUtils.isEmpty(productNameStr.trim())
            || inMachineInt!=-1 || inWareHouseInt!=-1 || lastCostFloat!=-1 || unitPerCaseInt!=-1){
                return true;
            }else
                return false;
        }

    }

    private void tryWriteData() {

        boolean error = false;
        String codeStr = codeEditText.getText().toString();
        int inMachineInt = 0;
        int inStockInt = 0;
        int inWareHouseInt = 0;
        float lastCostFloat = 0;
        int unitPerCaseInt = 0;


        if (TextUtils.isEmpty(codeStr.trim())) {
            codeEditText.setError("Field mustn't be empty");
            codeEditText.requestFocus();
            error = true;
            return;
        }

        String productNameStr = productNameEditText.getText().toString();
        if (TextUtils.isEmpty(productNameStr.trim())) {
            productNameEditText.setError("input valid product name");
            productNameEditText.requestFocus();
            error = true;
            return;
        }

        String productTypeStr = productTypeSpinner.getSelectedItem().toString();

        try {
            inStockInt = Integer.parseInt(inStockEditText.getText().toString());
        } catch (NumberFormatException e) {
            inStockEditText.setError("input a valid integer");
            inStockEditText.requestFocus();
            error = true;
            return;
        }

        try {
            inWareHouseInt = Integer.parseInt(inWareHouseEditText.getText().toString());
        } catch (NumberFormatException e) {
            inWareHouseEditText.setError("input a valid integer");
            inWareHouseEditText.requestFocus();
            error = true;
            return;
        }

        try {
            inMachineInt = Integer.parseInt(inMachineEditText.getText().toString());
        } catch (NumberFormatException e) {
            inMachineEditText.setError("input a valid integer");
            inMachineEditText.requestFocus();
            //Toast.makeText(getContext(), "In machine ", Toast.LENGTH_SHORT).show();
            error = true;
            return;
        }

        try {
            unitPerCaseInt = Integer.parseInt(unitPerCaseEditText.getText().toString());
        } catch (NumberFormatException e) {
            unitPerCaseEditText.setError("input a valid integer");
            unitPerCaseEditText.requestFocus();
            error = true;
            return;
        }


        try {
            lastCostFloat = Float.parseFloat(lastCostEditText.getText().toString());
        } catch (NumberFormatException e) {

            lastCostEditText.setError("input a valid decimal number");
            lastCostEditText.requestFocus();
            error = true;
            return;
        }


        if (!error)
            writeDataToFirebase(new InventoryItem(codeStr, productNameStr, productTypeStr, inStockInt, inWareHouseInt, inMachineInt, unitPerCaseInt, lastCostFloat));

    }

    public void writeDataToFirebase(final InventoryItem item) {
        loading();


        FirebaseUtilClass.getDatabaseReference().child("Inventory").child("Items").child(item.getCode()).setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    @Override
    public void dismiss(){

        super.dismiss();
    }
}
