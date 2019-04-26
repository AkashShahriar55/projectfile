package com.example.bimvendpro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MachineAdapter extends RecyclerView.Adapter<MachineAdapter.MachineViewHolder> {
    private List<Machine> itemList;
    private Context context;

    public class MachineViewHolder extends RecyclerView.ViewHolder {
        private TextView codeTextView, locationTextView, totalCollectedTextView, modelTextView, lastVisitTextView, nameTextView, typeTextView, installedTextView, inServiceTextView,
                ventsPerDayTextView;
        private Machine item;
        private LinearLayout parent, expandedLayout;

        public MachineViewHolder(View view) {
            super(view);
            codeTextView = view.findViewById(R.id.machineCode);
            locationTextView = view.findViewById(R.id.machineLocation);
            totalCollectedTextView = view.findViewById(R.id.totalCollected);
            modelTextView = view.findViewById(R.id.machineModel);
            lastVisitTextView = view.findViewById(R.id.lastVisitingDate);
            nameTextView = view.findViewById(R.id.machineName);
            typeTextView = view.findViewById(R.id.machineType);
            installedTextView = view.findViewById(R.id.machineInstallDate);
            inServiceTextView = view.findViewById(R.id.daysInService);
            ventsPerDayTextView = view.findViewById(R.id.ventsPerDay);
            parent = view.findViewById(R.id.parent);
            expandedLayout = view.findViewById(R.id.expandedLayout);
        }

        public Machine getItem() {
            return item;
        }

        public void setItem(Machine item) {
            this.item = item;
        }

    }
    public MachineAdapter(List<Machine> moviesList, Context context) {
        this.itemList = moviesList;
        this.context=context;
    }
    @Override
    public MachineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.machine_layout, parent, false);

        return new MachineViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MachineViewHolder holder, int position) {
        final Machine item = itemList.get(position);
        holder.codeTextView.setText(item.getCode());
        holder.locationTextView.setText(item.getMachineInstall().getLocation());
        holder.totalCollectedTextView.setText(String.valueOf(item.getTotalCollected()));
        holder.modelTextView.setText(item.getModel());
        if(item.getLastVisit()==null){
            holder.lastVisitTextView.setText(String.valueOf("not set"));
        }else {
            holder.lastVisitTextView.setText(String.valueOf(item.getLastVisit()));
        }
        holder.nameTextView.setText(item.getName());
        holder.typeTextView.setText(item.getType());
        holder.installedTextView.setText(String.valueOf(item.getMachineInstall().getInstallationDate()));
        holder.inServiceTextView.setText(String.valueOf(item.getDaysInService()));
        holder.ventsPerDayTextView.setText(String.valueOf(item.getVendsPerDay()));
        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                String[] colors = {"Install machine", "Edit"};

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(item.getName());
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                new MachineInstallDialogue(context,item.getCode(),item.getMachineInstall()).show();
                                break;
                            case 1:
                                new MachineAddDialogue(context,item).show();
                                break;
                        }
                    }
                });
                builder.show();

                return false;
            }
        });
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int vb= holder.expandedLayout.getVisibility();
                if(vb==View.VISIBLE){
                    holder.expandedLayout.setVisibility(View.GONE);
                }else {
                    holder.expandedLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        holder.setItem(item);
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
