package com.example.bimvendpro;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Dashboard.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Dashboard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Dashboard extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Dashboard() {
        // Required empty public constructor
    }


    ViewPager viewPager;
    serviceAdapter adapter;
    List<serviceNotificationItem> services;
    List<Machine> machines = new ArrayList<>();
    double totalCollection = 0;
    List<PieEntry> pieEntries = new ArrayList<>();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Dashboard.
     */
    // TODO: Rename and change types and number of parameters
    public static Dashboard newInstance(String param1, String param2) {
        Dashboard fragment = new Dashboard();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item=menu.findItem(R.id.action_add);

            item.setVisible(false);

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }




    }

    @Override
    public void onAttach(Context context) {

        services = new ArrayList<>();
        services.add(new serviceNotificationItem("location 1"));
        services.add(new serviceNotificationItem("location 2"));
        services.add(new serviceNotificationItem("location 3"));
        services.add(new serviceNotificationItem("location 4"));

        adapter = new serviceAdapter(services,context);


        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        viewPager =  getView().findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        FirebaseUtilClass.getDatabaseReference().child("Machine").child("Items").orderByChild("totalCollected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                machines.clear();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                    machines.add(dsp.getValue(Machine.class)); //add result into array list
                    totalCollection += dsp.getValue(Machine.class).getTotalCollected();


                }
                Log.d("total", "createPieChart: " + totalCollection);

                double perc = 0.0;
                float totalPerc = (float) 0.0;
                int count = 0;
                for(int i = machines.size() - 1; i >= 0 ;i--){

                    perc = (machines.get(i).getTotalCollected()/totalCollection)*100;
                    totalPerc+= perc;

                    if(count == 5){
                        break;
                    }
                    pieEntries.add(new PieEntry((float) perc,machines.get(i).getName()));
                    count++;

                }

                if(count != 5){
                    pieEntries.add(new PieEntry(100-totalPerc,"other"));
                }


                createPieChart(view);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        createBarChart(view);


        super.onViewCreated(view, savedInstanceState);
    }

    private void createBarChart(View view) {
        List<BarEntry> barEntries = new ArrayList<>();




        barEntries.add(new BarEntry(0f, 30f));
        barEntries.add(new BarEntry(1f, 80f));
        barEntries.add(new BarEntry(2f, 60f));
        barEntries.add(new BarEntry(3f, 50f));
        barEntries.add(new BarEntry(4f, 70f));

        BarDataSet barDataSet = new BarDataSet(barEntries,"Revenue (Last 5 months)");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        BarData barData = new BarData(barDataSet);

        BarChart barChart = (BarChart)view.findViewById(R.id.revBarchart);
        barChart.setData(barData);
        barChart.invalidate();

    }

    private void createPieChart( View view) {


        PieDataSet pieDataSet = new PieDataSet(pieEntries,"Top Machines");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData = new PieData(pieDataSet);


        PieChart pieChart = (PieChart) view.findViewById(R.id.pieChart);
        pieChart.setData(pieData);
        Description description = new Description();
        description.setText("The top 5 machines");
        pieChart.setDescription(description);
        pieChart.setCenterTextColor(Color.WHITE);
        pieChart.invalidate();
    }

    @Override
    public void onDetach() {
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
}
