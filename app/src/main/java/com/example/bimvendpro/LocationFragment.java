package com.example.bimvendpro;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LocationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerViewLocation;
    private List<Location> itemList = new ArrayList<>();
    private LocationItemAdapter mAdapter;


    ImageView addButtonImage;
    SearchView searchViewLocation;
    private SwipeRefreshLayout refreshlayout;

    private OnFragmentInteractionListener mListener;

    public LocationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocationFragment newInstance(String param1, String param2) {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        Log.d("lifecycle","onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("lifecycle","onCreateView");
        return inflater.inflate(R.layout.fragment_location, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d("lifecycle","onViewCreated");

        refreshlayout=view.findViewById(R.id.refreshingLayout);
        refreshlayout.setRefreshing(true);
        refreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                readDataFromFirebase();
            }
        });



        searchViewLocation = view.findViewById(R.id.loc_search);
        searchViewLocation.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });

        initializeRecyclerView();
        readDataFromFirebase();



        super.onViewCreated(view, savedInstanceState);
    }

    private void updateNextVisit() {

        for(int i =0; i<itemList.size();i++){
            Log.d("date", "updateNextVisit: " + itemList.get(i).getNextVisit());
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
            Date Date = null;
            Calendar nextDate = Calendar.getInstance();
            try {
                Date = sdf.parse(itemList.get(i).getNextVisit());
                nextDate.setTime(Date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Calendar presentDate = Calendar.getInstance();
            Log.d("date", "updateNextVisit: " + nextDate);
            if (presentDate.after(nextDate)) {
                int interVal = itemList.get(i).getIntervalDay();
                nextDate.add(Calendar.DAY_OF_MONTH, interVal);
                DateFormat df = new SimpleDateFormat("dd-MM-yy");
                String nextVisit = df.format(nextDate.getTime());
                Log.d("date", "updateNextVisit: " + nextVisit);
                FirebaseUtilClass.getDatabaseReference().child("Location").child("Locations").child(itemList.get(i).getCode()).child("nextVisit").setValue(nextVisit);
            }
        }

    }

    private void initializeRecyclerView() {
        recyclerViewLocation = getView().findViewById(R.id.locationRecyclerView);

        mAdapter = new LocationItemAdapter(itemList,getContext());
        RecyclerView.LayoutManager mLayoutmanager =new LinearLayoutManager(getContext());
        recyclerViewLocation.setLayoutManager(mLayoutmanager);
        recyclerViewLocation.setItemAnimator(new DefaultItemAnimator());
        recyclerViewLocation.setAdapter(mAdapter);
    }






    @Override
    public void onDetach() {
        Log.d("lifecycle","onDetach");
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void readDataFromFirebase() {
        FirebaseUtilClass.getDatabaseReference().child("Location").child("Locations").orderByChild("location").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemList.clear();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    Log.d("error","here is a error" + dsp.getValue(Location.class).getLocation());
                    itemList.add(dsp.getValue(Location.class)); //add result into array list
                }
                mAdapter.notifyDataSetChanged();
                refreshlayout.setRefreshing(false);
                updateNextVisit();
                //   Toast.makeText(getContext(), "Changed something", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                refreshlayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }



}
