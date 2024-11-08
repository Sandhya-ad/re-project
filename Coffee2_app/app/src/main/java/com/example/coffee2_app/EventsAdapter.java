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
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffee2_app.Organizer_ui.myevents.EventDetailsFragment;
import com.example.coffee2_app.Organizer_ui.myevents.MyEventsFragment;

import java.util.Date;
import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventViewHolder> {
    private List<Event> events;
    private FragmentManager currentFragment;
    private Context context;

    /**
     * Constructor for EventsAdapter
     * @param events
     * @param currentFragment
     */
    public EventsAdapter(List<Event> events, Context context, FragmentManager currentFragment) {
        this.events = events;
        this.context = context;
        this.currentFragment = currentFragment;
    }



    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entrant_signedup_events, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = events.get(position);
        holder.nameTextView.setText(event.getName());
        //holder.entriesTextView.setText(event.getMaxEntries() > 0 ? String.valueOf(event.getMaxEntries()) : "Unlimited");

        // Format and set the event date
        if (event.getEventDate() != null) {
            Date eventDate = event.getEventDate().toDate();
            String formattedEventDate = DateFormat.format("MM/dd/yyyy", eventDate).toString();
            holder.eventDateTextView.setText(formattedEventDate);
        } else {
            holder.eventDateTextView.setText("N/A");
        }

        // Format and set the draw date
//        if (event.getDrawDate() != null) {
//            Date drawDate = event.getDrawDate().toDate();
//            String formattedDrawDate = DateFormat.format("MM/dd/yyyy", drawDate).toString();
//            holder.drawDateTextView.setText(formattedDrawDate);
//        } else {
//            holder.drawDateTextView.setText("N/A");
//        }

        // Set the click listener to navigate to EventDetailsFragment with the event ID
        holder.itemView.setOnClickListener(v -> {
            EventDetailsFragment detailsFragment = new EventDetailsFragment();

            // Bundle event ID to pass to the fragment
            Bundle args = new Bundle();
            args.putString("id", event.getId());

            // Navigation controller to replace fragment
            //NavController navController = Navigation.findNavController((Activity) context, R.id.nav_host_fragment_activity_main);
            //navController.navigate(R.id.nav_host_fragment_activity_main, args);
        });
    }

    /**
     * Returns event count
     * @return Event count
     */
    @Override
    public int getItemCount() {
        return events.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView eventDateTextView;
        TextView drawDateTextView;
        TextView entriesTextView;

        EventViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.event_title);
            eventDateTextView = itemView.findViewById(R.id.event_date);
            //drawDateTextView = itemView.findViewById(R.id.event_draw_date);
            //entriesTextView = itemView.findViewById(R.id.event_entries);
        }
    }
}
