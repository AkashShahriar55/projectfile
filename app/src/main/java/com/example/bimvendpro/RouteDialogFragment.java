package com.example.bimvendpro;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RouteDialogFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private ListView listView;
    private NoticeDialogListener listener;

    List<CharSequence> locationNames = new ArrayList<>();
    List<Location> locationItemList = new ArrayList<>();
    ArrayAdapter<CharSequence> listAdapter;
    private int itemNo;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.route_add_location_layout, null);

        builder.setView(view)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        listener.passData(locationItemList.get(itemNo));

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        listView = view.findViewById(R.id.rut_addLocation_listview);
        listAdapter = new ArrayAdapter<CharSequence>(getActivity(),android.R.layout.simple_list_item_1,locationNames);
        listView.setAdapter(listAdapter);
        listView.setSelection(0);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listView.setSelection(position);
                itemNo = position;

            }
        });

        return builder.create();
    }


    public interface NoticeDialogListener{
        void passData(Location location);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {

        }
        catch (ClassCastException e){
            throw new ClassCastException(context.toString() +
                    "must implement NoticeDialogListener");
        }

        listener = (NoticeDialogListener) context;



        populateListFromDatabase();
    }

    private void populateListFromDatabase() {

        FirebaseUtilClass.getDatabaseReference().child("Location").child("Locations").orderByChild("location").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    Log.d("error","here is a error" + dsp.getValue(Location.class).getLocation());

                    locationItemList.add(dsp.getValue(Location.class)); //add result into array list
                    locationNames.add(dsp.getValue(Location.class).getLocation());
                }
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
