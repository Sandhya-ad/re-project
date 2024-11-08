package com.example.coffee2_app.Admin_ui.browseFacilities;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.coffee2_app.Facility;
import com.example.coffee2_app.R;

import java.util.List;

import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

public class BrowseFacilitiesAdapter extends RecyclerView.Adapter<BrowseFacilitiesAdapter.FacilityViewHolder> {

    private final Context context;
    private final List<Facility> facilityList;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public BrowseFacilitiesAdapter(Context context, List<Facility> facilityList) {
        this.context = context;
        this.facilityList = facilityList;
    }

    @NonNull
    @Override
    public FacilityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
        return new FacilityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FacilityViewHolder holder, int position) {
        Facility facility = facilityList.get(position);
        Glide.with(context).load(facility.getImageUrl()).into(holder.imageView);

        // Set up the delete button click listener
        holder.deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Facility")
                    .setMessage("Are you sure you want to delete this facility?")
                    .setPositiveButton("Yes", (dialog, which) -> deleteFacility(facility.getId(), position))
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    private void deleteFacility(String facilityId, int position) {
        // Delete facility from Firestore
        db.collection("facilities").document(facilityId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    facilityList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Facility removed successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(context, "Failed to remove facility", Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return facilityList.size();
    }

    public static class FacilityViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageButton deleteButton;

        public FacilityViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.event_image);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }

}
