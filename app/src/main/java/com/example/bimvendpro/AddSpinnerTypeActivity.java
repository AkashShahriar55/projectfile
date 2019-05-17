package com.example.bimvendpro;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddSpinnerTypeActivity extends AppCompatActivity {
    private int type;
    public static int MACHINE = 1, PRODUCT = 2;
    private RecyclerView recyclerView;
    private List<String> itemList = new ArrayList<>();
    private SpinnerItemAdapter mAdapter;
    private Button addItemButton;
    private SwipeRefreshLayout refreshlayout;
    private EditText itemNameEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spinner_type);
        refreshlayout = findViewById(R.id.refreshingLayout);

        addItemButton = findViewById(R.id.addItemButton);
        itemNameEditText = findViewById(R.id.itemNameeditText);

        type = getIntent().getIntExtra("type", PRODUCT);

        if(type==PRODUCT){
            getSupportActionBar().setTitle("Edit product types");
        }else if(type==MACHINE){
            getSupportActionBar().setTitle("Edit machine types");
        }

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(itemNameEditText.getText().toString().trim())){
                    writeToFireBase(itemNameEditText.getText().toString());
                }

            }
        });
        hideKeyboard();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = findViewById(R.id.spinnerRecyclerView);

        mAdapter = new SpinnerItemAdapter(itemList, this, type);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        refreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                readDataFromFirebase();
            }
        });

        readDataFromFirebase();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void hideKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }


    }

    private void writeToFireBase(String str) {
        if (type == PRODUCT) {
            FirebaseUtilClass.getDatabaseReference().child("Inventory").child("Types").child(str).setValue(true);
        } else if (type == MACHINE) {
            FirebaseUtilClass.getDatabaseReference().child("Machine").child("Types").child(str).setValue(true);
        }
    }

    public void readDataFromFirebase() {
        refreshlayout.setRefreshing(true);
        if (type == PRODUCT) {
            FirebaseUtilClass.getDatabaseReference().child("Inventory").child("Types").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    itemList.clear();
                    for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                        itemList.add(dsp.getKey()); //add result into array list
                    }
                    mAdapter.notifyDataSetChanged();
                    refreshlayout.setRefreshing(false);

                    //   Toast.makeText(getContext(), "Changed something", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    refreshlayout.setRefreshing(false);
                }
            });
        } else if (type == MACHINE) {
            FirebaseUtilClass.getDatabaseReference().child("Machine").child("Types").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    itemList.clear();
                    for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                        itemList.add(dsp.getValue(String.class)); //add result into array list
                    }
                    mAdapter.notifyDataSetChanged();
                    refreshlayout.setRefreshing(false);

                    //   Toast.makeText(getContext(), "Changed something", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    refreshlayout.setRefreshing(false);
                }
            });
        }

    }
}
