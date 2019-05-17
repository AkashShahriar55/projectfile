package com.example.bimvendpro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MachineAdapter extends RecyclerView.Adapter<MachineAdapter.MachineViewHolder> implements Filterable {
    private List<Machine> itemList;
    private Context context;
    private List<Machine> itemListFiltered;
    private MainActivity mainActivity;


    public class MachineViewHolder extends RecyclerView.ViewHolder {
        private TextView codeTextView, locationTextView, totalCollectedTextView, modelTextView, lastVisitTextView, nameTextView, typeTextView, installedTextView, inServiceTextView,
                ventsPerDayTextView;
        private Machine item;
        private LinearLayout parent, expandedLayout;
        private ImageView machineImage;

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
            machineImage = view.findViewById(R.id.machineimage);
        }

        public Machine getItem() {
            return item;
        }

        public void setItem(Machine item) {
            this.item = item;
        }

    }

    public MachineAdapter(List<Machine> moviesList, Context context, MainActivity mainActivity) {
        this.itemList = moviesList;
        this.context = context;
        this.itemListFiltered = itemList;
        this.mainActivity = mainActivity;

    }

    @Override
    public MachineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.machine_layout, parent, false);

        return new MachineViewHolder(itemView);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    itemListFiltered = itemList;
                } else {
                    List<Machine> filteredList = new ArrayList<>();

                    for (Machine row : itemList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if(row.getMachineInstall()!=null){
                            if("installed".contains(charString.toLowerCase())){
                                filteredList.add(row);
                                continue;
                            }
                            if(row.getMachineInstall().getLocation().toLowerCase().contains(charString.toLowerCase())|| row.getMachineInstall().getInstallationDate().contains(charString.toLowerCase())){
                                filteredList.add(row);
                                continue;
                            }
                        }else {
                            if("not installed".contains(charString.toLowerCase())){
                                filteredList.add(row);
                                continue;
                            }
                        }
                        if (row.getCode().toLowerCase().contains(charString.toLowerCase())
                                || row.getName().toLowerCase().contains(charString.toLowerCase())
                                || row.getType().toLowerCase().contains(charString.toLowerCase())
                                || row.getNote().toLowerCase().contains(charString.toLowerCase())
                                || row.getModel().toLowerCase().contains(charString.toLowerCase())) {

                            filteredList.add(row);
                        }
                    }

                    itemListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = itemListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                itemListFiltered = (ArrayList<Machine>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public void onBindViewHolder(final MachineViewHolder holder, int position) {
        final Machine item = itemListFiltered.get(position);
        holder.codeTextView.setText(item.getCode());
        if (item.getMachineInstall() == null) {
            holder.locationTextView.setText("not installed");
            holder.installedTextView.setText("not installed");
        } else {
            holder.locationTextView.setText(item.getMachineInstall().getLocation());
            holder.installedTextView.setText(String.valueOf(item.getMachineInstall().getInstallationDate()));
        }

        holder.totalCollectedTextView.setText(String.valueOf(item.getTotalCollected()));
        holder.modelTextView.setText(item.getModel());
        if (item.getLastVisit() == null) {
            holder.lastVisitTextView.setText(String.valueOf("not set"));
        } else {
            holder.lastVisitTextView.setText(String.valueOf(item.getLastVisit()));
        }
        holder.nameTextView.setText(item.getName());
        holder.typeTextView.setText(item.getType());

        holder.inServiceTextView.setText(String.valueOf(item.getDaysInService()));
        holder.ventsPerDayTextView.setText(String.valueOf(item.getVendsPerDay()));


        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("uploads").child("Machine").child(item.getCode());


        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                holder.machineImage.setVisibility(View.VISIBLE);
                Uri downloadUrl = uri;

                Picasso.get().load(downloadUrl.toString()).into(holder.machineImage);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                holder.machineImage.setVisibility(View.GONE);
            }
        });


// ImageView in your Activity


        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                String[] colors = {"Install machine", "Edit Machine", "Edit Ingredients", "Notes"};

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(item.getName());
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                new MachineInstallDialogue(context, item.getCode(), item.getMachineInstall()).show();
                                break;
                            case 1:
                                Intent intent = new Intent(context, MachineAddActivity.class);
                                intent.putExtra("item", item);
                                context.startActivity(intent);

                                break;
                            case 2:
                                mainActivity.changeFragment(new MachineIngredientsFragment(), item.getName(), item.getCode(), MainActivity.INGREDIENTS);
                                break;
                            case 3:
                                new MachineNoteDialogue(context, item.getCode(), item.getNote()).show();
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
                int vb = holder.expandedLayout.getVisibility();
                if (vb == View.VISIBLE) {
                    holder.expandedLayout.setVisibility(View.GONE);
                } else {
                    holder.expandedLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        holder.setItem(item);
    }


    @Override
    public int getItemCount() {

        return itemListFiltered.size();
    }


}
