package com.example.bimvendpro;


import android.telecom.Call;
import android.widget.TextView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.io.IOException;
import java.util.logging.Handler;

import javax.security.auth.callback.Callback;


public class FirebaseUtilClass {
    private static DatabaseReference databaseReference;
    private static FirebaseDatabase database;
    public final int WRITE_SUCCESSFUL=1,FAILED_SAME_CODE=2;



    public static DatabaseReference getDatabaseReference() {
        if (databaseReference == null) {
            getDatabase();
            databaseReference = database.getReference();
        }
        return databaseReference;
    }

    public static FirebaseDatabase getDatabase() {
        if (database == null) {

            database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
        }
        return database;
    }


}

