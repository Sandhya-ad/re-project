package com.example.coffee2_app;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.coffee2_app.Organizer_ui.myevents.EventDetailsFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * RecyclerView Adapter for displaying a list of events.
 * Each entry includes event name, date, description, and an image.
 */
public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventViewHolder> {

    private final List<Event> events; // List of events
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final Context context; // Context for navigation

    /**
     * Constructor for EventsAdapter.
     *
     * @param events  List of events to display.
     * @param context The context for navigation and access.
     */
    public EventsAdapter(List<Event> events, Context context) {
        this.events = events;
        this.context = context;
    }

    /**
     * Inflates layout for each item and returns a ViewHolder.
     *
     * @param parent   The parent ViewGroup.
     * @param viewType The view type of the new View.
     * @return A new instance of EventViewHolder containing item layout.
     */
    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.organizer_event_entry, parent, false);
        return new EventViewHolder(view);
    }

    /**
     * Binds event data to the ViewHolder for each item in the list.
     *
     * @param holder   ViewHolder for binding.
     * @param position Position of the item in the adapter.
     */
    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = events.get(position);

        // Set event name
        holder.nameTextView.setText(event.getName());

        // Set event description
        holder.descriptionTextView.setText(event.getDescription() != null ? event.getDescription() : "No description available");

        // Format and set the event date
        if (event.getEventDate() != null) {
            Date eventDate = event.getEventDate().toDate();
            String formattedEventDate = DateFormat.format("MM/dd/yyyy", eventDate).toString();
            holder.eventDateTextView.setText(formattedEventDate);
        } else {
            holder.eventDateTextView.setText("N/A");
        }

        // Fetch and set the poster image
        String posterImageID = event.getImageID(); // Get the poster ID from the Event object
        if (posterImageID != null && !posterImageID.isEmpty()) {
            fetchEventPosterImage(posterImageID, holder.eventImageView); // Load the image dynamically
        } else {
            holder.eventImageView.setImageResource(R.drawable.ic_event_placeholder); // Fallback image
        }

        // Set the click listener for the item
        holder.itemView.setOnClickListener(view -> {
            EventDetailsFragment eventDetailsFragment = new EventDetailsFragment();

            // Pass event details to EventDetailsFragment via Bundle
            Bundle args = new Bundle();
            args.putString("name", event.getName());
            args.putString("eventID", event.getId());
            args.putInt("maxEntries", event.getMaxEntries());
            args.putBoolean("collectGeo", event.isCollectGeo());
            args.putString("posterImageID", event.getImageID());
            args.putString("hashQRData", event.getHashQrData());

            if (event.getEventDate() != null) {
                Date date = event.getEventDate().toDate();
                args.putString("eventDate", new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).format(date));
            }

            eventDetailsFragment.setArguments(args);

            // Replace current fragment with EventDetailsFragment
            FragmentTransaction transaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
            transaction.replace(android.R.id.content, eventDetailsFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }


    /**
     * Returns the total number of items in the list.
     *
     * @return The size of the events list.
     */
    @Override
    public int getItemCount() {
        return events.size();
    }

    /**
     * ViewHolder class for holding references to the views for each event item.
     */
    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView descriptionTextView;
        TextView eventDateTextView;
        ImageView eventImageView;

        EventViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.event_name);
            descriptionTextView = itemView.findViewById(R.id.event_description);
            eventDateTextView = itemView.findViewById(R.id.event_date);
            eventImageView = itemView.findViewById(R.id.event_image); // ImageView for event poster
        }
    }
    private void fetchEventPosterImage(String posterImageID, ImageView imageView) {
        db.collection("images").document(posterImageID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        String encodedImage = task.getResult().getString("imageData"); // Get the Base64 string
                        if (encodedImage != null) {
                            // Decode the Base64 encoded image
                            byte[] decodedBytes = android.util.Base64.decode(encodedImage, android.util.Base64.DEFAULT);
                            Bitmap posterBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

                            // Set the image in the ImageView
                            imageView.setImageBitmap(posterBitmap);
                        } else {
                            Log.e("MyEventsFragment", "Image data is null for ID: " + posterImageID);
                            imageView.setImageResource(R.drawable.ic_event_placeholder); // Fallback image
                        }
                    } else {
                        Log.e("MyEventsFragment", "Error fetching poster image for ID: " + posterImageID, task.getException());
                        imageView.setImageResource(R.drawable.ic_event_placeholder); // Fallback image
                    }
                });
    }

}
