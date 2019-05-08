package com.example.bimvendpro;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MachineIngredientsAddDialogue extends Dialog {
    private MachineIngredients item;
    private Spinner ingredientsSpinner;
    private EditText canisterEditText;
    private EditText maxEditText;
    private String code;
    private Button addButton, deleteButton,cancelButton;
    private Boolean editDlg=false;
    private ProgressBar progressBar,spinnerProgressBar;

    public MachineIngredientsAddDialogue(Context context, String code) {
        super(context);
        this.code=code;
    }

    public MachineIngredientsAddDialogue(Context context, String code, MachineIngredients item) {
        super(context);
        this.code = code;
        this.item = item;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ingredients_add_dlg);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        getWindow().setAttributes(params);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        hideKeyboard();

        ingredientsSpinner = findViewById(R.id.ingredientSpinner);
        canisterEditText = findViewById(R.id.canisterEditText);
        maxEditText = findViewById(R.id.maxEditText);
        addButton=findViewById(R.id.addButton);
        deleteButton=findViewById(R.id.deleteButton);
        cancelButton=findViewById(R.id.cancelButton);
        progressBar=findViewById(R.id.loadingProgressBar);
        spinnerProgressBar=findViewById(R.id.spinnerLoading);

        ingredientsSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    hideKeyboard();
                    int canis=Integer.parseInt(canisterEditText.getText().toString());
                    int maxcap=Integer.parseInt(maxEditText.getText().toString());
                    String compositeStr=ingredientsSpinner.getSelectedItem().toString();
                    String codeWithBracket=compositeStr.split("\\[")[1];
                    String code=codeWithBracket.substring(0, codeWithBracket.length() - 1);
                    int index = compositeStr.lastIndexOf(" ");
                    String proname= compositeStr.substring(0, index);

                    writeDataToFirebase(new MachineIngredients(code,proname,canis,maxcap));



            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MachineIngredientsAddDialogue.this.dismiss();
            }
        });


        if (item != null) {  //dlg type edit
            canisterEditText.setText(String.valueOf(item.getCanister()));
            maxEditText.setText(String.valueOf(item.getMax()));
            //    machineType.setSelection(getIndexFromSpinner(productTypeSpinner, item.getProductType()));
            addButton.setText("Update");

            editDlg = true;
        }

        loadIngredientsToSpinner();
    }


    private void loadIngredientsToSpinner() {
        spinnerLoadingOn();

        FirebaseUtilClass.getDatabaseReference().child("Inventory").child("Items").orderByChild("productName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> loc = new ArrayList<>();

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                    InventoryItem l = dsp.getValue(InventoryItem.class); //add result into array list
                    loc.add(l.getProductName() +" ["+l.getCode()+"]");
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, loc);
                ;
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ingredientsSpinner.setAdapter(adapter);
                if(editDlg) {
                    ingredientsSpinner.setSelection(getIndexFromSpinner(ingredientsSpinner, item.getName() + " [" + item.getCode() + "]"));
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

    private void spinnerLoadingOn() {
        ingredientsSpinner.setEnabled(false);
        spinnerProgressBar.setVisibility(View.VISIBLE);
        addButton.setEnabled(false);
    }

    private void spinnerLoadingOff() {
        ingredientsSpinner.setEnabled(true);
        spinnerProgressBar.setVisibility(View.GONE);
        addButton.setEnabled(true);

    }

    private void installingOn() {
        ingredientsSpinner.setEnabled(false);
        addButton.setEnabled(false);
        cancelButton.setEnabled(false);

        progressBar.setVisibility(View.VISIBLE);
    }

    private void installingOff() {
       ingredientsSpinner.setEnabled(true);
        addButton.setEnabled(true);
        cancelButton.setEnabled(true);
        progressBar.setVisibility(View.GONE);
    }

    public void writeDataToFirebase(final MachineIngredients item) {
        installingOn();


        FirebaseUtilClass.getDatabaseReference().child("Machine").child("Items").child(code).child("machineIngredients").child(item.getCode()).setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    private void hideKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if(getCurrentFocus()!=null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }

    }
}
