package com.example.bimvendpro;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.view.View.GONE;

public class AddViewEditDrivers extends AppCompatActivity implements DriverImagesAdapter.onDeleteClick {

    private EditText editTextName,editTextAddress,editTextId,editTextSsn,editTextWorkingHour,editTextPassword;
    private TextView textViewMode;
    private LinearLayout linearLayoutAddImage;
    private Button addButton,deleteButton,addImageButton;
    private DriverItem driverItem;
    private List<DriverImages> driverImages = new ArrayList<>();
    private ViewPager viewPager;
    private DriverImagesAdapter driverImagesAdapter;

    private String mode;
    String nameStr,addressStr,idStr,ssnStr,whStr,passStr;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;

    private boolean hasImage = false;

    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";

    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_view_edit_drivers);

        editTextName = findViewById(R.id.driver_edttxt_name);
        editTextAddress = findViewById(R.id.driver_edttxt_address);
        editTextId = findViewById(R.id.driver_edttxt_id);
        editTextSsn = findViewById(R.id.driver_edttxt_ssn);
        editTextWorkingHour = findViewById(R.id.driver_edttxt_wh);
        editTextPassword = findViewById(R.id.driver_edttxt_pass);

        addButton = findViewById(R.id.driver_button_add);
        deleteButton = findViewById(R.id.driver_button_cancel);
        addImageButton = findViewById(R.id.driver_add_image);

        textViewMode = findViewById(R.id.driver_view_mode);
        linearLayoutAddImage = findViewById(R.id.driver_add_image_layout);


        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });





        mode = (String) getIntent().getExtras().getSerializable("mode");

        if(mode.equals("add")){
            initializeAddMode();
        }
        else if(mode.equals("view")){
            driverItem = (DriverItem) getIntent().getExtras().getSerializable("item");
            if(driverItem.getDriverImagesList() != null){
                driverImages = driverItem.getDriverImagesList();
            }
            initializeViewMode();
        }




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


        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void initializePagerView() {
        driverImagesAdapter = new DriverImagesAdapter(driverImages,this,this,mode);
        viewPager = findViewById(R.id.viewPagerDriver);
        viewPager.setAdapter(driverImagesAdapter);
    }

    private void initializeAddMode() {
        initializePagerView();
        editTextPassword.setText(getRandomString(7));
        editTextWorkingHour.setText("00-00");
        textViewMode.setText("Add mode");

        addButton.setText("Add");
        deleteButton.setText("Cancel");

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(AddViewEditDrivers.this)
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

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(AddViewEditDrivers.this)
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



    private static String getRandomString(final int sizeOfPasswordString){
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfPasswordString);

        for(int i=0;i<sizeOfPasswordString;++i){
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));

        }

        return sb.toString();
    }




    private void initializeViewMode() {


        textViewMode.setText("View Mode");

        addButton.setText("Edit");
        deleteButton.setText("Delete");
        linearLayoutAddImage.setVisibility(GONE);


        editTextName.setEnabled(false);
        editTextAddress.setEnabled(false);
        editTextId.setEnabled(false);
        editTextSsn.setEnabled(false);
        editTextWorkingHour.setEnabled(false);
        editTextPassword.setEnabled(false);

        editTextName.setText(driverItem.getName());
        editTextAddress.setText(driverItem.getAddress());
        editTextId.setText(driverItem.getIdNo());
        editTextSsn.setText(driverItem.getSidNo());
        editTextWorkingHour.setText(driverItem.getWorkingHour());
        editTextPassword.setText(driverItem.getPassword());

        if(driverItem.getDriverImagesList() != null){
            driverImages = driverItem.getDriverImagesList();
        }
        initializePagerView();


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeEditMode();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(AddViewEditDrivers.this)
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

    private void initializeEditMode() {

        mode = "edit";
        textViewMode.setText("Edit Mode");

        addButton.setText("Done");
        deleteButton.setText("Cancel");
        linearLayoutAddImage.setVisibility(View.VISIBLE);


        editTextName.setEnabled(true);
        editTextAddress.setEnabled(true);
        editTextId.setEnabled(true);
        editTextSsn.setEnabled(true);
        editTextWorkingHour.setEnabled(true);
        editTextPassword.setEnabled(true);

        initializePagerView();


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(AddViewEditDrivers.this)
                        .setMessage("Are you sure? This can't be undone.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tryWriteData();
                                mode = "view";
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

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(AddViewEditDrivers.this)
                        .setMessage("Are you sure? Data will be lost.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mode = "view";
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

    private void getImages() {

    }

    private void delete() {
        FirebaseUtilClass.getDatabaseReference().child("Driver").child("Drivers").child(driverItem.getIdNo()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@android.support.annotation.NonNull Task<Void> task) {

                if(!driverImages.isEmpty()){
                    for(int i =0 ; i< driverImages.size();i++){
                        StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(driverImages.get(i).getImgUrl());
                        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                finish();
                            }
                        });
                    }
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@android.support.annotation.NonNull Exception e) {
            }
        });
    }


    private void tryWriteData() {
        boolean error =  false;

        nameStr = editTextName.getText().toString();
        if (TextUtils.isEmpty(nameStr.trim())) {
            editTextName.setError("Field mustn't be empty");
            editTextName.requestFocus();
            error = true;
            return;
        }

        addressStr = editTextAddress.getText().toString();

        idStr = editTextId.getText().toString();
        if (TextUtils.isEmpty(idStr.trim())) {
            editTextId.setError("Field mustn't be empty");
            editTextId.requestFocus();
            error = true;
            return;
        }

        ssnStr = editTextSsn.getText().toString();
        if (TextUtils.isEmpty(ssnStr.trim())) {
            editTextSsn.setError("Field mustn't be empty");
            editTextSsn.requestFocus();
            error = true;
            return;
        }

        passStr = editTextPassword.getText().toString();
        if (TextUtils.isEmpty(passStr.trim())) {
            editTextPassword.setError("Field mustn't be empty");
            editTextPassword.requestFocus();
            error = true;
            return;
        }

        whStr = editTextWorkingHour.getText().toString();

        if(!TextUtils.isEmpty(whStr.trim()) && !whStr.matches("\\d{2}-\\d{2}")){
            editTextWorkingHour.setError("Please give a valid input");
            editTextWorkingHour.requestFocus();
            error = true;
            return;
        }


        if(!error){
            writeDataToFirebase(new DriverItem(nameStr,addressStr,ssnStr,idStr,whStr,passStr,driverImages));


        }
    }


    private void uploadFile(Uri imgUri) {
        final ProgressDialog dialog = ProgressDialog.show(AddViewEditDrivers.this, "",
                "Loading. Please wait...", true);
        dialog.show();
        if (imgUri != null) {
            final StorageReference fileReference = FirebaseStorage.getInstance().getReference("uploads").child("Drivers").child(System.currentTimeMillis()
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


                            Toast.makeText(AddViewEditDrivers.this, "Upload successful", Toast.LENGTH_LONG).show();

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUrl = uri;
                                    driverImages.add(new DriverImages(downloadUrl.toString()));
                                    driverImagesAdapter.notifyDataSetChanged();
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddViewEditDrivers.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            dialog.setMessage("Uploading Image "+ (float) progress);
                        }
                    });

        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }


    public void writeDataToFirebase(final DriverItem item) {
        final ProgressDialog dialog = ProgressDialog.show(AddViewEditDrivers.this, "",
                "Loading. Please wait...", true);
        FirebaseUtilClass.getDatabaseReference().child("Driver").child("Drivers").child(item.getIdNo()).setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AddViewEditDrivers.this, "Updated", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                finish();

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddViewEditDrivers.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onDeleteClick(final int pos) {


        StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(driverImages.get(pos).getImgUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                driverImages.remove(pos);

                driverImagesAdapter.notifyDataSetChanged();
            }
        });


    }
}
