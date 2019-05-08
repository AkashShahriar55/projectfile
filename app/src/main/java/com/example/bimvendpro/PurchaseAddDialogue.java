package com.example.bimvendpro;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PurchaseAddDialogue extends Dialog {
    private PurchaseClass item;
    private EditText supplierEditText, purchaseDateEditText;
    private CalendarView purchaseDateCalenderView;
    private Button cancelButton, saveButton, deleteButton;
    private Boolean editDlg;
    private ProgressBar progressBar;

    public PurchaseAddDialogue(Context context) {
        super(context);
    }

    public PurchaseAddDialogue(Context context, PurchaseClass item) {
        super(context);
        this.item = item;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.purchase_add_dlg);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        getWindow().setAttributes(params);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        init();

        hideKeyboard();

        if (item != null) {  //dlg type edit
            deleteButton.setVisibility(View.VISIBLE);
            if (item.getPurchaseDate() == null) {
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

                String str = format.format(new Date());
                purchaseDateEditText.setText(str);
            } else {
                purchaseDateEditText.setText(item.getPurchaseDate());
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

                    Date d = dateFormat.parse(purchaseDateEditText.getText().toString());
                    purchaseDateCalenderView.setDate(d.getTime());
                    purchaseDateCalenderView.setVisibility(View.VISIBLE);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            //    machineType.setSelection(getIndexFromSpinner(productTypeSpinner, item.getProductType()));
            saveButton.setText("Update");

            editDlg = true;
        }
    }

    private void init() {
        supplierEditText = findViewById(R.id.supplieEditText);
        purchaseDateCalenderView = findViewById(R.id.purchaseDateCalender);
        purchaseDateEditText = findViewById(R.id.purchaseDateEditText);
        cancelButton = findViewById(R.id.cancelButton);
        saveButton = findViewById(R.id.saveButton);
        progressBar=findViewById(R.id.savingProgressBar);
        deleteButton=findViewById(R.id.deleteButton);

        purchaseDateCalenderView.setVisibility(View.GONE);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                writeDataToFirebase(new PurchaseClass(supplierEditText.getText().toString(), purchaseDateEditText.getText().toString()));
            }
        });

        purchaseDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                purchaseDateCalenderView.setVisibility(View.VISIBLE);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    Date d = dateFormat.parse(purchaseDateEditText.getText().toString());
                    purchaseDateCalenderView.setDate(d.getTime());
                    //purchaseDateCalenderView.setVisibility(View.VISIBLE);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });

        purchaseDateCalenderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                hideKeyboard();
                StringBuilder strBld = new StringBuilder();
                strBld.append(String.format("%02d", dayOfMonth)).append("-").append(String.format("%02d", month + 1)).append("-").append(year);
                purchaseDateEditText.setText(strBld.toString());
                purchaseDateCalenderView.setVisibility(View.GONE);
                //installDateCalenderView.setVisibility(View.GONE);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                dismiss();
            }
        });
    }

    private void installingOn() {
        purchaseDateEditText.setEnabled(false);
        supplierEditText.setEnabled(false);
        cancelButton.setEnabled(false);
        saveButton.setEnabled(false);
        deleteButton.setEnabled(false);
        purchaseDateCalenderView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void installingOff() {
        purchaseDateEditText.setEnabled(true);
        supplierEditText.setEnabled(true);
        cancelButton.setEnabled(true);
        saveButton.setEnabled(true);
        deleteButton.setEnabled(true);
        purchaseDateCalenderView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    public void writeDataToFirebase(final PurchaseClass item) {
        installingOn();


        FirebaseUtilClass.getDatabaseReference().child("Purchases").child("Items").push().setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
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

//Show
       // imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
    }

}
