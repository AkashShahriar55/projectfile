package com.example.bimvendpro;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AddItemDialogue extends Dialog {
    private EditText codeEditText, inMachineEditText, inStockEditText, inWareHouseEditText, lastCostEditText, productNameEditText, unitPerCaseEditText;
    private Spinner productTypeSpinner;
    private Button addButton, cancelButton;
    private InventoryItem item;

    public AddItemDialogue(@NonNull Context context) {
        super(context);
    }

    public AddItemDialogue(@NonNull Context context, InventoryItem item) {

        super(context);
    }

    public AddItemDialogue(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected AddItemDialogue(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    private void init() {


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryWriteData();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
       // setContentView(R.layout.custom_dialog);
            //TODO: set content


        init();

        if(item!=null){  //editing dilg
            codeEditText.setText(item.getCode());
            inMachineEditText.setText(String.valueOf(item.getInMachine()));
            inStockEditText.setText(String.valueOf(item.getInStock()));
            inWareHouseEditText.setText(String.valueOf(item.getInWarehouse()));
            lastCostEditText.setText(String.valueOf(item.getLastCost()));
            productNameEditText.setText(item.getProductName());
            unitPerCaseEditText.setText(String.valueOf(item.getUnitPerCase()));
            productTypeSpinner.setSelection(getIndexFromSpinner(productTypeSpinner, item.getProductType()));
            addButton.setText("Update");
        }


    }

    private int getIndexFromSpinner(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }

    private void tryWriteData() {

        boolean error = false;
        String codeStr = codeEditText.getText().toString();
        int inMachineInt = 0;
        int inStockInt = 0;
        int inWareHouseInt = 0;
        float lastCostFloat = 0;
        int unitPerCaseInt = 0;

        String productNameStr = productNameEditText.getText().toString();
        if ( TextUtils.isEmpty(productNameStr.trim())){
            productNameEditText.setError("input valid product name");
            error = true;
            return;
        }

        String productTypeStr = productTypeSpinner.getSelectedItem().toString();

        try {
            inMachineInt = Integer.parseInt(inMachineEditText.getText().toString());
        } catch (NumberFormatException e) {
            inMachineEditText.setError("input a valid integer");
            //Toast.makeText(getContext(), "In machine ", Toast.LENGTH_SHORT).show();
            error = true;
        }

        try {
            inStockInt = Integer.parseInt(inStockEditText.getText().toString());
        } catch (NumberFormatException e) {
            inStockEditText.setError("input a valid integer");
            error = true;
        }

        try {
            inWareHouseInt = Integer.parseInt(inWareHouseEditText.getText().toString());
        } catch (NumberFormatException e) {
            inWareHouseEditText.setError("input a valid integer");
            error = true;
        }


        try {
            lastCostFloat = Float.parseFloat(lastCostEditText.getText().toString());
        } catch (NumberFormatException e) {
            lastCostEditText.setError("input a valid decimal number");
            error = true;
        }

        try {
            unitPerCaseInt = Integer.parseInt(unitPerCaseEditText.getText().toString());
        } catch (NumberFormatException e) {
            unitPerCaseEditText.setError("input a valid integer");
            error = true;
        }



        if (!error)
            writeDataToFirebase(new InventoryItem(codeStr, productNameStr, productTypeStr, inStockInt, inWareHouseInt, inMachineInt, unitPerCaseInt, lastCostFloat));

    }

    private boolean validData() {

        return true;
    }

    public void writeDataToFirebase(final InventoryItem item) {


        FirebaseUtilClass.getDatabaseReference().child("Inventory").setValue(item.getCode()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@android.support.annotation.NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    FirebaseUtilClass.getDatabaseReference().child("Inventory").child(item.getCode()).setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@android.support.annotation.NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
                                dismiss();
                            } else {
                                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
