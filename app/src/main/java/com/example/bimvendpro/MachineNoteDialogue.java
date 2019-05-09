package com.example.bimvendpro;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class MachineNoteDialogue extends Dialog {
    private String code;
    private ProgressBar progressBar;
    private String prevNote;
    private Boolean editDlg=false;

    public MachineNoteDialogue(Context context, String code) {
        super(context);
        this.code = code;
    }


    public MachineNoteDialogue(Context context, String code, String prevNote) {
        super(context);
        this.code = code;
        this.prevNote=prevNote;
    }

    private Button saveButton, cancelButton;
    private EditText note;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.machine_note);
        saveButton = findViewById(R.id.saveButton);
        note = findViewById(R.id.noteText);
        progressBar = findViewById(R.id.loadingNote);
        cancelButton=findViewById(R.id.cancelButton);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        getWindow().setAttributes(params);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        hideKeyboard();

        setCancelable(false);

        if(prevNote!=null){
            editDlg=true;
            saveButton.setText("Update");
            note.setText(prevNote);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                writeDataToFirebase(note.getText().toString());
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

    private void loading() {
        progressBar.setVisibility(View.VISIBLE);
        saveButton.setEnabled(false);
        note.setEnabled(false);
    }

    private void notLoading() {
        progressBar.setVisibility(View.GONE);
        saveButton.setEnabled(true);
        note.setEnabled(true);
    }

    public void writeDataToFirebase(String data) {
        loading();


        FirebaseUtilClass.getDatabaseReference().child("Machine").child("Items").child(code).child("note").setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    private void hideKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if(getCurrentFocus()!=null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

}
