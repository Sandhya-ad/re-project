package com.example.coffee2_app.Admin_ui.browseEvents;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.example.coffee2_app.Event;
import com.example.coffee2_app.R;

import java.util.List;

import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;

public class BrowseEventsAdapter extends RecyclerView.Adapter<BrowseEventsAdapter.EventViewHolder> {

    private final Context context;
    private final List<Event> eventList;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public BrowseEventsAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        Glide.with(context).load(event.getImageUrl()).into(holder.imageView);

        // Set up the delete button click listener
        holder.deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Event")
                    .setMessage("Are you sure you want to delete this event?")
                    .setPositiveButton("Yes", (dialog, which) -> deleteEvent(event.getId(), position))
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    private void deleteEvent(String eventId, int position) {
        // Delete event from Firestore
        db.collection("events").document(eventId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    eventList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Event removed successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(context, "Failed to remove event", Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageButton deleteButton;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.event_image);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}
