package com.example.bimvendpro;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MachineAddActivity extends AppCompatActivity {
    private Machine item;
    private EditText codeEditText, machineNameEditText, modelEditText,lastMeterRatingsEditText;
    private Spinner machineTypeSpinner;
    private Button cancelButton, deleteButton, addButton;
    private ProgressBar progressBar, spinnerLoading;
    private Boolean editDlg = false;
    private ImageView addImage;
    private CheckBox hasGovtStickerCheckBox;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private boolean hasImage = false;
    private String imageUrlStr = "";
    private Toolbar toolbar;
    private ImageView addType;

    public MachineAddActivity() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }


    private void hideKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure to delete the item?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }


    private void init() {
        spinnerLoading = findViewById(R.id.spinnerLoading);
        codeEditText = findViewById(R.id.codeEditText);
        machineNameEditText = findViewById(R.id.machineNameEditTExt);
        machineTypeSpinner = findViewById(R.id.machineTypeSpinner);
        modelEditText = findViewById(R.id.modelEditText);
        cancelButton = findViewById(R.id.cancelButton);
        deleteButton = findViewById(R.id.deleteButton);
        addButton = findViewById(R.id.addButton);
        progressBar = findViewById(R.id.loadingProgressBar);
        addImage = findViewById(R.id.machineImageAdd);
        addType = findViewById(R.id.addType);
        lastMeterRatingsEditText = findViewById(R.id.lastMeterEditText);
        hasGovtStickerCheckBox =findViewById(R.id.hasGovtStickerCheckBox);

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();


                finish();


            }
        });

        addType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MachineAddActivity.this, AddSpinnerTypeActivity.class);
                intent.putExtra("type", AddSpinnerTypeActivity.MACHINE);
                MachineAddActivity.this.startActivity(intent);
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

        loadProductsToSpinner();
    }



    private void loadProductsToSpinner() {
        spinnerLoadingOn();

        FirebaseUtilClass.getDatabaseReference().child("Machine").child("Types").orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> loc = new ArrayList<>();


                for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                    String l = dsp.getKey(); //add result into array list
                    loc.add(l);

                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MachineAddActivity.this, android.R.layout.simple_spinner_item, loc);
                ;
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                machineTypeSpinner.setAdapter(adapter);
                if (editDlg) {
                    machineTypeSpinner.setSelection(getIndexFromSpinner(machineTypeSpinner, item.getType()));
                }
                spinnerLoadingOff();
                // mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MachineAddActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                spinnerLoadingOff();
            }
        });
    }

    private void spinnerLoadingOn() {
       machineTypeSpinner.setEnabled(false);
       machineTypeSpinner.setVisibility(View.VISIBLE);
        addButton.setEnabled(false);
    }

    private void spinnerLoadingOff() {
        machineTypeSpinner.setEnabled(true);
        spinnerLoading.setVisibility(View.GONE);
        addButton.setEnabled(true);

    }
    private void deleteItem() {
        loading();
        FirebaseUtilClass.getDatabaseReference().child("Machine").child("Items").child(item.getCode()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@android.support.annotation.NonNull Task<Void> task) {

                if (item.getMachineInstall() != null) {

                    FirebaseUtilClass.getDatabaseReference().child("Location").child("Locations").child(item.getMachineInstall().getLocation()).child("machineInstall").child(item.getCode()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@android.support.annotation.NonNull Task<Void> task) {
                            notLoading();
                            MachineAddActivity.this.finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@android.support.annotation.NonNull Exception e) {
                            notLoading();
                            Toast.makeText(MachineAddActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@android.support.annotation.NonNull Exception e) {
                notLoading();
                Toast.makeText(MachineAddActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            hasImage = true;
            Picasso.get().load(mImageUri).into(addImage);
            //addImage.setImageURI(mImageUri);
            //    buttonAddImage.setText("delete");
            //   buttonAddImage.setTextColor(getResources().getColor(R.color.colorred));
        }
    }


    private void uploadFile(Uri imgUri, String code) {
        final ProgressDialog dialog = ProgressDialog.show(MachineAddActivity.this, "",
                "Loading. Please wait...", true);
        dialog.show();
        if (imgUri != null) {
            final StorageReference fileReference = FirebaseStorage.getInstance().getReference("uploads").child("Machine").child(code);
            UploadTask mUploadTask;
            mUploadTask = (UploadTask) fileReference.putFile(imgUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                }
                            }, 500);


                            Toast.makeText(MachineAddActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
                            finish();
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUrl = uri;
                                    imageUrlStr = downloadUrl.toString();
                                    Picasso.get().load(imageUrlStr).into(addImage);

                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MachineAddActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            dialog.setMessage("Uploading Image: " + progress + "%");
                        }
                    });

        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_machine_add);


        hideKeyboard();
        init();


        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            item = (Machine) intent.getExtras().getSerializable("item");
            getSupportActionBar().setTitle(item.getName());
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        if (item != null) {  //dlg type edit
            codeEditText.setText(item.getCode());
            machineNameEditText.setText(String.valueOf(item.getName()));
            modelEditText.setText(String.valueOf(item.getModel()));
            lastMeterRatingsEditText.setText(String.valueOf(item.getLastMeterReadings()));
            machineTypeSpinner.setSelection(getIndexFromSpinner(machineTypeSpinner, item.getType()));
            addButton.setText("Update");
            deleteButton.setVisibility(View.VISIBLE);
            editDlg = true;
            codeEditText.setEnabled(false);
            hasGovtStickerCheckBox.setChecked(item.isHasSticker());
        }

        if (item != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("uploads").child("Machine").child(item.getCode());


            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    Uri downloadUrl = uri;

                    Picasso.get().load(downloadUrl.toString()).into(addImage);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });


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


        String codeStr = codeEditText.getText().toString();
        String machineNameStr = machineNameEditText.getText().toString();
        String machineModelStr = modelEditText.getText().toString();
        String lastMeterReadingStr = lastMeterRatingsEditText.getText().toString();
        String machineType;
        boolean hasSticker = hasGovtStickerCheckBox.isChecked();
        if(machineTypeSpinner.getSelectedItem()==null || TextUtils.isEmpty(machineTypeSpinner.getSelectedItem().toString().trim())){
            Toast.makeText(this,"Please add a machine type first",Toast.LENGTH_SHORT).show();
            return;
        } else {
            machineType=machineTypeSpinner.getSelectedItem().toString();
        }


        if (TextUtils.isEmpty(codeStr.trim())) {
            codeEditText.setError("Field mustn't be empty");
            codeEditText.requestFocus();

            return;
        }

        if (TextUtils.isEmpty(machineNameStr.trim())) {
            machineNameEditText.setError("Field mustn't be empty");
            machineNameEditText.requestFocus();

            return;
        }

        if (TextUtils.isEmpty(machineModelStr.trim())) {
            modelEditText.setError("Field mustn't be empty");
            modelEditText.requestFocus();

            return;
        }

        int lastMeterReadings;
        try {
            lastMeterReadings = Integer.parseInt(lastMeterReadingStr);
        }catch (NumberFormatException e){
            lastMeterRatingsEditText.setError("Please Give Valid Input");
            lastMeterRatingsEditText.requestFocus();
            return;
        }

        if (editDlg) {
            item.setName(machineNameStr);
            item.setModel(machineModelStr);
            item.setType(machineType);
            item.setLastMeterReadings(lastMeterReadings);
            item.setHasSticker(hasSticker);
            writeDataToFirebase(item);
        } else {
            writeDataToFirebase(new Machine(codeStr, machineNameStr, machineModelStr, machineType,lastMeterReadings,hasSticker));
        }


    }

    public void writeDataToFirebase(final Machine item) {
        loading();


        FirebaseUtilClass.getDatabaseReference().child("Machine").child("Items").child(item.getCode()).setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MachineAddActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                notLoading();
                if (mImageUri != null) {
                    uploadFile(mImageUri, item.getCode());
                } else {
                    finish();
                }

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MachineAddActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                notLoading();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
