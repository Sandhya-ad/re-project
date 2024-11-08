package com.example.coffee2_app.Admin_ui.browseFacilities;

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

public class BrowseFacilitiesAdapter extends RecyclerView.Adapter<BrowseFacilitiesAdapter.FacilityViewHolder> {

    private final Context context;
    private final List<Facility> facilityList;

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
        Glide.with(context).load(facility.getImageUrl()).into(holder.imageView); // Ensure `imageUrl` is correct
    }

    @Override
    public int getItemCount() {
        return facilityList.size();
    }

    public static class FacilityViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public FacilityViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.event_image);
        }
    }
}

