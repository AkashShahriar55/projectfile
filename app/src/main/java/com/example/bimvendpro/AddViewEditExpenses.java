package com.example.bimvendpro;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.view.View.GONE;

public class AddViewEditExpenses extends AppCompatActivity {

    private EditText editTextDate,editTextAmount,editTextDescription,editTextPayee;
    private Spinner spinnerCategory,spinnerLocation;
    private ImageView imageViewReceipt;
    private LinearLayout linearLayoutAddImage;
    private Button buttonAdd,buttonCancel,buttonAddImage,buttonDeleteImage;
    final Calendar myCalendar = Calendar.getInstance();
    private boolean hasImage = false;

    private ExpenseItem expenseItem;

    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri mImageUri;
    private String categoryStr,dateStr,amountStr,descriptionStr,payeeStr,locationStr,imageUrlStr = "",mode,codeStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        setContentView(R.layout.activity_add_view_edit_expenses);

        editTextDate = findViewById(R.id.expense_edttxt_date);
        editTextAmount = findViewById(R.id.expense_edttxt_amount);
        editTextDescription = findViewById(R.id.expense_edttxt_description);
        editTextPayee = findViewById(R.id.expense_edttxt_payee);

        spinnerCategory = findViewById(R.id.expense_spnr_category);
        spinnerLocation = findViewById(R.id.expense_spnr_location);

        buttonAddImage = findViewById(R.id.expense_add_image);
        buttonAdd = findViewById(R.id.expense_add_button);
        buttonCancel = findViewById(R.id.expense_cancel_button);

        imageViewReceipt = findViewById(R.id.expenses_image_receipt);
        linearLayoutAddImage = findViewById(R.id.expense_add_image_layout);

*/

        buttonAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hasImage){
                    deleteImage();
                }
                else{
                    openFileChooser();
                }

            }
        });


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };


        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddViewEditExpenses.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        loadMachinesToSpinner();

        mode = (String) getIntent().getExtras().getSerializable("mode");

        if(mode.equals("add")){
            initializeAddMode();
        }
        else if(mode.equals("view")){
            initializeViewMode();
        }

    }

    private void initializeViewMode() {

        expenseItem = (ExpenseItem) getIntent().getExtras().getSerializable("item");
        buttonAdd.setText("Edit");
        buttonCancel.setText("Delete");
        linearLayoutAddImage.setVisibility(GONE);
        imageUrlStr = expenseItem.getAttachment();
        codeStr = expenseItem.getCode();

        if(!imageUrlStr.equals("")){
            hasImage = true;
            buttonAddImage.setText("delete");
            buttonAddImage.setTextColor(getResources().getColor(R.color.colorred));
            Picasso.get().load(imageUrlStr).into(imageViewReceipt);
        }
        else {
            hasImage = false;
            buttonAddImage.setText("add");
            buttonAddImage.setTextColor(getResources().getColor(R.color.colorhighlight2));
        }


        editTextPayee.setEnabled(false);
        editTextDescription.setEnabled(false);
        editTextAmount.setEnabled(false);
        editTextDate.setEnabled(false);
        editTextPayee.setHint("");
        editTextDescription.setHint("");
        editTextAmount.setHint("");
        editTextDate.setHint("");

        spinnerCategory.setEnabled(false);

        editTextPayee.setText(expenseItem.getPayee());
        editTextDescription.setText(expenseItem.getDrescription());
        editTextAmount.setText(expenseItem.getAmount());
        editTextDate.setText(expenseItem.getDate());
        spinnerCategory.setSelection(getIndexFromSpinner(spinnerCategory,expenseItem.getCatagory()));






        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeEditMode();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(AddViewEditExpenses.this)
                        .setMessage("Are you sure? This can't be undo.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                delete();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });

    }

    private void delete() {
        FirebaseUtilClass.getDatabaseReference().child("Expense").child("Expenses").child(expenseItem.getCode()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@android.support.annotation.NonNull Task<Void> task) {

                if(!expenseItem.getAttachment().equals("")){
                    StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(expenseItem.getAttachment());
                    imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            finish();
                        }
                    });
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@android.support.annotation.NonNull Exception e) {
            }
        });


    }

    private void initializeEditMode() {
        buttonAdd.setText("Done");
        buttonCancel.setText("Cancel");
        linearLayoutAddImage.setVisibility(View.VISIBLE);


        editTextPayee.setEnabled(true);
        editTextDescription.setEnabled(true);
        editTextAmount.setEnabled(true);
        editTextDate.setEnabled(true);
        editTextPayee.setHint("e.g. Hank");
        editTextDescription.setHint("e.g. 400$ for rent");
        editTextAmount.setHint("e.g. 12.25");
        editTextDate.setHint("dd-mm-yy");

        spinnerCategory.setEnabled(true);
        spinnerLocation.setEnabled(true);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(AddViewEditExpenses.this)
                        .setMessage("Are you sure? This will change your data.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tryWriteData();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();

            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(AddViewEditExpenses.this)
                        .setMessage("Are you sure? This can't be undo.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                initializeViewMode();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });

    }

    private void initializeAddMode() {
        buttonAdd.setText("Add");
        buttonCancel.setText("Cancel");

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(AddViewEditExpenses.this)
                        .setMessage("Are you sure? This can't be undo.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tryWriteData();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(AddViewEditExpenses.this)
                        .setMessage("Are you sure? Data will be lost.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();

            }
        });


    }

    private void tryWriteData() {
        boolean error = false;

        categoryStr = spinnerCategory.getSelectedItem().toString();

        dateStr = editTextDate.getText().toString();
        if (TextUtils.isEmpty(dateStr.trim())) {
            editTextDate.setError("Field mustn't be empty");
            editTextDate.requestFocus();
            error = true;
            return;
        }

        amountStr = editTextAmount.getText().toString();
        if (TextUtils.isEmpty(amountStr.trim())) {
            amountStr = "0.0";
        }

        descriptionStr = editTextDescription.getText().toString();

        payeeStr = editTextPayee.getText().toString();

        locationStr = spinnerLocation.getSelectedItem().toString();

        if(mode == "add"){
            DatabaseReference databaseReference = FirebaseUtilClass.getDatabaseReference().child("Expense").child("Expenses");
            codeStr = databaseReference.push().getKey();
        }



        if(!error){
            writeDataToFirebase(new ExpenseItem(dateStr,categoryStr,amountStr,payeeStr,locationStr,descriptionStr,imageUrlStr,codeStr));
        }


    }

    public void writeDataToFirebase(final ExpenseItem item) {
        final ProgressDialog dialog = ProgressDialog.show(AddViewEditExpenses.this, "",
                "Loading. Please wait...", true);
        FirebaseUtilClass.getDatabaseReference().child("Expense").child("Expenses").child(item.getCode()).setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AddViewEditExpenses.this, "Updated", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                finish();

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddViewEditExpenses.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void deleteImage() {
        StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrlStr);
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                imageViewReceipt.setImageResource(0);
                imageUrlStr = "";
                hasImage = false;
                buttonAddImage.setText("add");
                buttonAddImage.setTextColor(getResources().getColor(R.color.colorhighlight2));
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
            uploadFile(data.getData());
            hasImage = true;
            buttonAddImage.setText("delete");
            buttonAddImage.setTextColor(getResources().getColor(R.color.colorred));
        }
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editTextDate.setText(sdf.format(myCalendar.getTime()));

    }

    private void loadMachinesToSpinner() {
        spinnerLocation.setEnabled(false);

        FirebaseUtilClass.getDatabaseReference().child("Location").child("Locations").orderByChild("code").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> locations = new ArrayList<>();
                locations.add("none");
                boolean hasLocation = false;
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    Location m = dsp.getValue(Location.class); //add result into array list
                    if(m != null){
                        hasLocation = true;
                        locations.add(m.getLocation());
                    }
                }

                if(!hasLocation){
                    locations.add("No Location");
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddViewEditExpenses.this, android.R.layout.simple_spinner_item, locations);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerLocation.setAdapter(adapter);
                if(!mode.equals("view")){
                    spinnerLocation.setEnabled(true);
                }else{
                    spinnerLocation.setSelection(getIndexFromSpinner(spinnerLocation,expenseItem.getLocationName()));
                }

                // mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddViewEditExpenses.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(Uri imgUri) {
        final ProgressDialog dialog = ProgressDialog.show(AddViewEditExpenses.this, "",
                "Loading. Please wait...", true);
        dialog.show();
        if (imgUri != null) {
            final StorageReference fileReference = FirebaseStorage.getInstance().getReference("uploads").child("Expenses").child(System.currentTimeMillis()
                    + "." + getFileExtension(imgUri));
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


                            Toast.makeText(AddViewEditExpenses.this, "Upload successful", Toast.LENGTH_LONG).show();

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUrl = uri;
                                    imageUrlStr = downloadUrl.toString();
                                    Picasso.get().load(imageUrlStr).into(imageViewReceipt);
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddViewEditExpenses.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private int getIndexFromSpinner(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }

        return 0;
    }

}
