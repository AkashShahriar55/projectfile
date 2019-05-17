package com.example.bimvendpro;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.List;


public class InventoryItemAddDialogue extends Dialog {
    private EditText codeEditText, inMachineEditText, inStockEditText, inWareHouseEditText, productNameEditText, unitPerCaseEditText;
    //   private TextView messageTextView;
    private Spinner productTypeSpinner;
    private ProgressBar progressBar, spinnerLoading;
    private Button addButton, cancelButton, deleteButton;
    private InventoryItem item;
    private boolean editDlg = false;
    private ImageView addType;


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

    private void spinnerLoadingOn() {
        productTypeSpinner.setEnabled(false);
        spinnerLoading.setVisibility(View.VISIBLE);
        addButton.setEnabled(false);
    }

    private void spinnerLoadingOff() {
        productTypeSpinner.setEnabled(true);
        spinnerLoading.setVisibility(View.GONE);
        addButton.setEnabled(true);

    }


    private void init() {
        hideKeyboard();
        codeEditText = findViewById(R.id.codeEditText);
        inMachineEditText = findViewById(R.id.inMachineEditText);
        inStockEditText = findViewById(R.id.inStockEditText);
        inWareHouseEditText = findViewById(R.id.inWarehouseEditText);
        spinnerLoading = findViewById(R.id.spinnerLoading);
        productTypeSpinner = findViewById(R.id.productTypeSpinner);
        productNameEditText = findViewById(R.id.productNameEditTExt);
        unitPerCaseEditText = findViewById(R.id.unitPerCaseEditText);
        addButton = findViewById(R.id.addButton);
        cancelButton = findViewById(R.id.cancelButton);
        progressBar = findViewById(R.id.loadingProgressBar);
        deleteButton = findViewById(R.id.deleteButton);
        addType = findViewById(R.id.addType);

        addType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddSpinnerTypeActivity.class);
                intent.putExtra("type", AddSpinnerTypeActivity.PRODUCT);
                getContext().startActivity(intent);
            }
        });


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

        setCancelable(false);

        loadProductsToSpinner();
    }

    private void hideKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }


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


    private void loadProductsToSpinner() {
        spinnerLoadingOn();

        FirebaseUtilClass.getDatabaseReference().child("Inventory").child("Types").orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> loc = new ArrayList<>();


                for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                    String l = dsp.getKey(); //add result into array list
                    loc.add(l);

                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, loc);
                ;
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                productTypeSpinner.setAdapter(adapter);
                if (editDlg) {
                    productTypeSpinner.setSelection(getIndexFromSpinner(productTypeSpinner, item.getProductName()));
                }
                spinnerLoadingOff();
                // mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                spinnerLoadingOff();
            }
        });
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
        setContentView(R.layout.dialogue_inventory_add_item);


        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        getWindow().setAttributes(params);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        hideKeyboard();

        init();

        if (item != null) {  //dlg type edit
            codeEditText.setText(item.getCode());
            inMachineEditText.setText(String.valueOf(item.getInMachine()));
            inStockEditText.setText(String.valueOf(item.getInStock()));
            inWareHouseEditText.setText(String.valueOf(item.getInWarehouse()));

            productNameEditText.setText(item.getProductName());
            unitPerCaseEditText.setText(String.valueOf(item.getUnitPerCase()));
            inStockEditText.setVisibility(View.VISIBLE);
            inMachineEditText.setVisibility(View.VISIBLE);
            productTypeSpinner.setSelection(getIndexFromSpinner(productTypeSpinner, item.getProductType()));
            addButton.setText("Update");
            deleteButton.setVisibility(View.VISIBLE);
            editDlg = true;
            codeEditText.setEnabled(false);
        }
        inWareHouseEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editDlg) {
                    try {
                        Integer inMach = Integer.valueOf(inMachineEditText.getText().toString());
                        Integer inWare = Integer.valueOf(inWareHouseEditText.getText().toString());
                        Integer inStock = (item.getInMachine()) + (inWare);
                        inStockEditText.setText(String.valueOf(inStock));
                    } catch (Exception e) {
                        inStockEditText.setText(inMachineEditText.getText());
                    }


                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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


    private void tryWriteData() {

        boolean error = false;
        String codeStr = codeEditText.getText().toString();
        int inMachineInt = 0;
        int inStockInt = 0;
        int inWareHouseInt = 0;
        float lastCostFloat = 0;
        Integer unitPerCaseInt = 0;


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

        String productTypeStr;
        if (productTypeSpinner.getSelectedItem() == null || TextUtils.isEmpty(productTypeSpinner.getSelectedItem().toString().trim())) {
            Toast.makeText(getContext(), "Please add a product type first", Toast.LENGTH_SHORT).show();
            return;
        } else {
            productTypeStr = productTypeSpinner.getSelectedItem().toString();
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
            inStockInt = Integer.parseInt(inStockEditText.getText().toString());
        } catch (NumberFormatException e) {
            inStockInt = inWareHouseInt;
        }
        try {
            inMachineInt = Integer.parseInt(inMachineEditText.getText().toString());
        } catch (NumberFormatException e) {
            inMachineInt = 0;
        }

        try {
            unitPerCaseInt = Integer.parseInt(unitPerCaseEditText.getText().toString());
        } catch (NumberFormatException e) {
            unitPerCaseEditText.setError("input a valid input");
            unitPerCaseEditText.requestFocus();
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
    public void dismiss() {

        super.dismiss();
    }
}
