package com.example.bimvendpro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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

import java.util.List;

public class LocationMachineAdapter extends RecyclerView.Adapter<LocationMachineAdapter.LocationMachineViewHolder> {

    private List<MachineInstall> itemList;
    private Context context;
    private String lcode;
    private int noOfMachine;
    public notifydata notifydata;
    private Machine machine;

    public LocationMachineAdapter(List<MachineInstall> itemList, Context context,String lcode,int noOfMachine) {
        this.itemList = itemList;
        this.context = context;
        this.lcode = lcode;
        this.noOfMachine = noOfMachine;
        notifydata = (LocationMachineAdapter.notifydata) context;
    }

    @NonNull
    @Override
    public LocationMachineAdapter.LocationMachineViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.location_install_machine_item,viewGroup,false);

        return new LocationMachineViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationMachineViewHolder locationMachineViewHolder, int i) {
        final MachineInstall item = itemList.get(i);

        locationMachineViewHolder.textViewCode.setText(String.valueOf(item.getLocation()));
        locationMachineViewHolder.textViewDate.setText(String.valueOf(item.getInstallationDate()));
        locationMachineViewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setMessage("Are you sure? Your Data will be Change")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final DatabaseReference databaseReference = FirebaseUtilClass.getDatabaseReference();
                                databaseReference.child("Machine").child("Items").child(item.getLocation()).child("machineInstall").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@android.support.annotation.NonNull Task<Void> task) {
                                        databaseReference.child("Location").child("Locations").child(lcode).child("machineInstall").child(item.getLocation()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@android.support.annotation.NonNull Task<Void> task) {
                                                itemList.remove(item);
                                                notifyDataSetChanged();
                                                notifydata.onNoOfMachineDelete();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@android.support.annotation.NonNull Exception e) {
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@android.support.annotation.NonNull Exception e) {
                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();

            }
        });
        locationMachineViewHolder.setItem(item);
    }

    private void deleteMachine() {


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class LocationMachineViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewCode,textViewDate;
        private ImageView deleteButton;
        private MachineInstall item;
        public LocationMachineViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCode = itemView.findViewById(R.id.installed_machine_code);
            textViewDate = itemView.findViewById(R.id.installed_machine_date);
            deleteButton = itemView.findViewById(R.id.installed_machine_delete);

        }

        public MachineInstall getItem() {
            return item;
        }

        public void setItem(MachineInstall item) {
            this.item = item;
        }
    }

    public interface notifydata{
        void onNoOfMachineDelete();
    }
}
