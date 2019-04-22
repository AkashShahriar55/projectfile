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
    private ProgressBar progressBar;

    List<CharSequence> locationNames = new ArrayList<>();
    List<LicationItem> locationItemList = new ArrayList<>();
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

                        String code = locationItemList.get(itemNo).getCode();
                        String name = locationItemList.get(itemNo).getLocation();
                        double latitude = locationItemList.get(itemNo).getLatitude();
                        double longitue = locationItemList.get(itemNo).getLongitude();
                        Toast.makeText(getContext(),"" + code + name + String.valueOf(latitude) + String.valueOf(longitue),Toast.LENGTH_SHORT).show();
                        listener.passData(code,name,latitude,longitue);

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        progressBar = view.findViewById(R.id.rut_progressbar_addlocation);

        listView = view.findViewById(R.id.rut_addLocation_listview);
        listAdapter = new ArrayAdapter<CharSequence>(getActivity(),android.R.layout.simple_list_item_1,locationNames);
        listView.setAdapter(listAdapter);

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
        void passData(String code, String name,double latitude,double longitude);
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
        progressBar.setVisibility(View.VISIBLE);
        FirebaseUtilClass.getDatabaseReference().child("Location").child("Locations").orderByChild("location").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                    locationItemList.add(dsp.getValue(LicationItem.class)); //add result into array list
                    locationNames.add(dsp.getValue(LicationItem.class).getLocation());

                }

                listAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
