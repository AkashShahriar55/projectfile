package com.example.bimvendpro;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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

import java.util.ArrayList;
import java.util.List;

public class PurchaseProductAddDialogue extends Dialog {
    private PurchaseProductClass item;
    private String sentPushId;
    private Spinner productNameSpinner;
    private ProgressBar spinnerLoading, savingProgressBar;
    private EditText casesPurchasedEditText, unitPerCaseEditText, costPerCaseEditText, noOfUnitEditText, unitCostEditText, totalCostEditText;
    private Button saveButton, cancelButton, deleteButton;
    private Boolean editDlg = false;
    private List<Integer> defaultUnitPerCase = new ArrayList<>();

    public PurchaseProductAddDialogue(Context context, String sentPushId) {
        super(context);
        this.sentPushId = sentPushId;
    }

    public PurchaseProductAddDialogue(Context context, String sentPushId, PurchaseProductClass item) {
        super(context);
        this.sentPushId = sentPushId;
        this.item = item;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.product_add_dlg);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        getWindow().setAttributes(params);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        hideKeyboard();

        productNameSpinner = findViewById(R.id.productNameSpinner);
        casesPurchasedEditText = findViewById(R.id.casesPur);
        unitPerCaseEditText = findViewById(R.id.unitPerCase);
        costPerCaseEditText = findViewById(R.id.costPerCase);
        unitCostEditText = findViewById(R.id.unitCost);
        noOfUnitEditText = findViewById(R.id.noOfUnit);
        totalCostEditText = findViewById(R.id.totalCost);


        deleteButton = findViewById(R.id.deleteButton);
        saveButton = findViewById(R.id.saveButton);
        cancelButton = findViewById(R.id.cancelButton);
        spinnerLoading = findViewById(R.id.spinnerLoading);
        savingProgressBar = findViewById(R.id.savingProgressBar);


        setCancelable(false);
        productNameSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });

        productNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                unitPerCaseEditText.setText(String.valueOf(defaultUnitPerCase.get(productNameSpinner.getSelectedItemPosition())));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeyboard();
                Integer casesPur, unitPerCase;
                Double costPerCase;
                try {
                    casesPur = Integer.parseInt(casesPurchasedEditText.getText().toString());
                } catch (Exception e) {
                    casesPurchasedEditText.setError("Invalid input");
                    return;
                }

                try {
                    unitPerCase = Integer.parseInt(unitPerCaseEditText.getText().toString());
                } catch (Exception e) {
                    unitPerCaseEditText.setError("Invalid input");
                    return;
                }

                try {
                    costPerCase = Double.parseDouble(costPerCaseEditText.getText().toString());
                } catch (Exception e) {
                    costPerCaseEditText.setError("Invalid input");
                    return;
                }


                String compositeStr = productNameSpinner.getSelectedItem().toString();
                String codeWithBracket = compositeStr.split("\\[")[1];
                String code = codeWithBracket.substring(0, codeWithBracket.length() - 1);
                int index = compositeStr.lastIndexOf(" ");
                String proname = compositeStr.substring(0, index);

                Integer unitPurchased = Integer.parseInt(noOfUnitEditText.getText().toString());
                Double unitCost = Double.parseDouble(unitCostEditText.getText().toString());
                Double totlCost = Double.parseDouble(totalCostEditText.getText().toString());
                String pushId;
                if (editDlg) {
                    pushId = item.getPushId();
                } else {
                    pushId = FirebaseUtilClass.getDatabaseReference().child("Purchases").child("Items").child(sentPushId).child("purchaseProducts").push().getKey();
                }

                writeDataToFirebase(new PurchaseProductClass(pushId, code, proname, casesPur, unitPerCase, costPerCase, unitPurchased, unitCost, totlCost));


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
                confirmDeleteDlg();
            }
        });


        if (item != null) {  //dlg type edit
            casesPurchasedEditText.setText(String.valueOf(item.getCasesPurchased()));
            unitPerCaseEditText.setText(String.valueOf(item.getUnitPerCase()));
            costPerCaseEditText.setText(String.valueOf(item.getCostPerCase()));
            noOfUnitEditText.setText(String.valueOf(item.getUnitPurchased()));
            unitCostEditText.setText(String.valueOf(item.getUnitCost()));
            totalCostEditText.setText(String.valueOf(item.getTotalCost()));
            deleteButton.setVisibility(View.VISIBLE);
            //    machineType.setSelection(getIndexFromSpinner(productTypeSpinner, item.getProductType()));
            saveButton.setText("Update");

            editDlg = true;
        }

        loadProductsToSpinner();


        TextWatcher updateEditTexts = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Integer casesPur, unitPerCase;
                Double costPerCase;
                try {
                    casesPur = Integer.parseInt(casesPurchasedEditText.getText().toString());
                } catch (Exception e) {

                    return;
                }

                try {
                    unitPerCase = Integer.parseInt(unitPerCaseEditText.getText().toString());
                } catch (Exception e) {

                    return;
                }

                try {
                    costPerCase = Double.parseDouble(costPerCaseEditText.getText().toString());
                } catch (Exception e) {

                    return;
                }
                unitCostEditText.setText(String.format("%.2f", costPerCase / unitPerCase));
                noOfUnitEditText.setText(String.valueOf(casesPur * unitPerCase));
                totalCostEditText.setText(String.format("%.2f", casesPur * unitPerCase * costPerCase / unitPerCase));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        casesPurchasedEditText.addTextChangedListener(updateEditTexts);
        unitPerCaseEditText.addTextChangedListener(updateEditTexts);
        costPerCaseEditText.addTextChangedListener(updateEditTexts);
    }

    private void deleteDataFromFirebase() {
        savingOn();


        FirebaseUtilClass.getDatabaseReference().child("Purchases").child("Items").child(sentPushId).child("purchaseProducts").child(item.getPushId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                FirebaseUtilClass.getDatabaseReference().child("Purchases").child("Items").child(sentPushId).child("productNo").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Integer proNo = Integer.valueOf(dataSnapshot.getValue().toString());
                        proNo--;
                        dataSnapshot.getRef().setValue(proNo);

                        FirebaseUtilClass.getDatabaseReference().child("Purchases").child("Items").child(sentPushId).child("totalCost").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Double totCost = Double.valueOf(dataSnapshot.getValue().toString());
                                totCost -= item.getTotalCost();
                                dataSnapshot.getRef().setValue(totCost);


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                savingOff();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        savingOff();
                    }
                });

                Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
                savingOff();
                dismiss();

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                savingOff();
            }
        });
    }

    private void confirmDeleteDlg() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        deleteDataFromFirebase();
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

    private void hideKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }

    }

    private void spinnerLoadingOn() {
        productNameSpinner.setEnabled(false);
        spinnerLoading.setVisibility(View.VISIBLE);
        saveButton.setEnabled(false);
    }

    private void spinnerLoadingOff() {
        productNameSpinner.setEnabled(true);
        spinnerLoading.setVisibility(View.GONE);
        saveButton.setEnabled(true);

    }

    private void savingOn() {
        productNameSpinner.setEnabled(false);
        saveButton.setEnabled(false);
        cancelButton.setEnabled(false);

        savingProgressBar.setVisibility(View.VISIBLE);
    }

    private void savingOff() {
        productNameSpinner.setEnabled(true);
        saveButton.setEnabled(true);
        cancelButton.setEnabled(true);
        savingProgressBar.setVisibility(View.GONE);
    }


    private int getIndexFromSpinner(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }

        return 0;
    }


    private void loadProductsToSpinner() {
        spinnerLoadingOn();

        FirebaseUtilClass.getDatabaseReference().child("Inventory").child("Items").orderByChild("productName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> loc = new ArrayList<>();
                defaultUnitPerCase.clear();

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                    InventoryItem l = dsp.getValue(InventoryItem.class); //add result into array list
                    loc.add(l.getProductName() + " [" + l.getCode() + "]");
                    defaultUnitPerCase.add(l.getUnitPerCase());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, loc);
                ;
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                productNameSpinner.setAdapter(adapter);
                if (editDlg) {
                    productNameSpinner.setSelection(getIndexFromSpinner(productNameSpinner, item.getProductName() + " [" + item.getProductCode() + "]"));
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

    public void writeDataToFirebase(final PurchaseProductClass item) {
        savingOn();


        FirebaseUtilClass.getDatabaseReference().child("Purchases").child("Items").child(sentPushId).child("purchaseProducts").child(item.getPushId()).setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                FirebaseUtilClass.getDatabaseReference().child("Purchases").child("Items").child(sentPushId).child("productNo").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Integer proNo = Integer.valueOf(dataSnapshot.getValue().toString());
                        proNo++;
                        dataSnapshot.getRef().setValue(proNo);

                        FirebaseUtilClass.getDatabaseReference().child("Purchases").child("Items").child(sentPushId).child("totalCost").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Double totCost = Double.valueOf(dataSnapshot.getValue().toString());
                                totCost += item.getTotalCost();
                                dataSnapshot.getRef().setValue(totCost);


                                FirebaseUtilClass.getDatabaseReference().child("Inventory").child("Items").child(item.getProductCode()).child("lastCost").setValue(item.getUnitCost()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
                                        savingOff();
                                        dismiss();
                                    }
                                });

                                FirebaseUtilClass.getDatabaseReference().child("Inventory").child("Items").child(item.getProductCode()).child("inWarehouse").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        final Integer onInternetInWarehouse = Integer.valueOf(dataSnapshot.getValue().toString());
                                        Integer oldInWarehouse = 0;
                                        if (editDlg) {
                                            oldInWarehouse = PurchaseProductAddDialogue.this.item.getUnitPurchased();
                                        }
                                        final Integer newInWarehouse = item.getUnitPurchased();
                                        final Integer changedInWarehouse = newInWarehouse - oldInWarehouse;

                                        dataSnapshot.getRef().setValue(onInternetInWarehouse + changedInWarehouse);


                                        FirebaseUtilClass.getDatabaseReference().child("Inventory").child("Items").child(item.getProductCode()).child("inStock").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                Integer inStock = Integer.valueOf(dataSnapshot.getValue().toString());


                                                dataSnapshot.getRef().setValue(inStock + changedInWarehouse);

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                                //   installingOff();
                                            }
                                        });

                                        //  Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
                                        //  installingOff();
                                        //  dismiss();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                        //installingOff();
                                    }
                                });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                savingOff();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        savingOff();
                    }
                });


            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                savingOff();
            }
        });

    }
}
