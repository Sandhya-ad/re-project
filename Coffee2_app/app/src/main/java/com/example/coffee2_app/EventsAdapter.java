package com.example.coffee2_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffee2_app.Organizer_ui.myevents.EventDetailsFragment;
//import com.example.coffee2_app.Organizer_ui.myevents.MyEventsFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * RecyclerView Adapter for displaying a list of events
 * Each entry includes event name, date and address
 */
public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventViewHolder> {

    private List<Event> events;
    private Fragment currentFragment;
    private Context context;

    /**
     * Constructor for EventsAdapter
     * @param events
     * @param currentFragment
     */
    public EventsAdapter(List<Event> events, Fragment currentFragment) {
        this.events = events;
        this.context = context;
        this.currentFragment = currentFragment;
    }

    /**
     * Inflates layout for each item and returns ViewHolder
     *
     * @param parent   The parent ViewGroup
     * @param viewType The view type of the new View
     * @return A new instance of EventViewHolder containing item layout
     */
    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.organizer_event_entry, parent, false);
        return new EventViewHolder(view);
    }


    /**
     * Binds event data to the ViewHolder for each item in the list
     *
     * @param holder   ViewHolder for binding
     * @param position Position of item in the adapter
     */
    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = events.get(position);
        holder.nameTextView.setText(event.getName());
        holder.addressTextView.setText(event.getOrganizer());
        holder.entriesTextView.setText(event.getMaxEntries() > 0 ? String.valueOf(event.getMaxEntries()) : "Unlimited");

        // Format and set the event date
        if (event.getEventDate() != null) {
            Date eventDate = event.getEventDate().toDate();
            String formattedEventDate = DateFormat.format("MM/dd/yyyy", eventDate).toString();
            holder.eventDateTextView.setText(formattedEventDate);
        } else {
            holder.eventDateTextView.setText("N/A");
        }

        // Set the click listener for the item
        holder.itemView.setOnClickListener(view -> {

            EventDetailsFragment eventDetailsFragment = new EventDetailsFragment();

            Bundle args = new Bundle();
            args.putString("name", event.getName());
            args.putString("eventID", event.getOrganizer());
            args.putInt("maxEntries", event.getMaxEntries());
            args.putBoolean("collectGeo", event.isCollectGeo());
            args.putString("hashQRData", event.getHashQrData());
            Date date = event.getEventDate().toDate();
            args.putString("eventDate", new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).format(date));

            eventDetailsFragment.setArguments(args);

            // Get context from the ViewHolder's itemView
            Context context = holder.itemView.getContext();

            // Replace current fragment with EventDetailsFragment
            FragmentTransaction transaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
            transaction.replace(android.R.id.content, eventDetailsFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }
  /**
        //Format and set the draw date
        if (event.getDrawDate() != null) {
            Date drawDate = event.getDrawDate().toDate();
            String formattedDrawDate = DateFormat.format("MM/dd/yyyy", drawDate).toString();
            holder.drawDateTextView.setText(formattedDrawDate);
        } else {
            holder.drawDateTextView.setText("N/A");
        }

        // Set the click listener to navigate to EventDetailsFragment with the event ID
        holder.itemView.setOnClickListener(v -> {
            EventDetailsFragment detailsFragment = new EventDetailsFragment();

            // Bundle event ID to pass to the fragment
            Bundle args = new Bundle();
            args.putString("id", event.getId());

            // Navigation controller to replace fragment
            NavController navController = Navigation.findNavController((Activity) context, R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.nav_host_fragment_activity_main, args);
        });
    }*/

    /**
     * Returns total number of items in the list
     *
     * @return The size of the name list
     */
    @Override
    public int getItemCount() {
        return events.size();
    }

    /**
     * ViewHolder class for holding references to the views for each event item
     */
    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView addressTextView;
        TextView eventDateTextView;

        EventViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.event_name);
            addressTextView = itemView.findViewById(R.id.event_address);
            eventDateTextView = itemView.findViewById(R.id.event_date);
        }
    }
}