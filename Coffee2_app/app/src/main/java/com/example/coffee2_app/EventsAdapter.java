package com.example.coffee2_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffee2_app.Organizer_ui.myevents.EventDetailsFragment;

import java.util.Date;
import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventViewHolder> {
    private List<Event> events;
    private Fragment currentFragment;

    public EventsAdapter(List<Event> events, Fragment currentFragment) {
        this.events = events;
        this.currentFragment = currentFragment;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.organizer_event_entry, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = events.get(position);
        holder.nameTextView.setText(event.getName());
        holder.entriesTextView.setText(String.valueOf(event.getMaxEntries()));

        // Format and set the event date
        if (event.getEventDate() != null) {
            Date eventDate = event.getEventDate().toDate();
            String formattedEventDate = DateFormat.format("MM/dd/yyyy", eventDate).toString();
            holder.eventDateTextView.setText(formattedEventDate);
        } else {
            holder.eventDateTextView.setText("N/A");
        }
        if (event.getDrawDate() != null) {
            Date drawDate = event.getDrawDate().toDate();
            String formattedDrawDate = DateFormat.format("MM/dd/yyyy", drawDate).toString();
            holder.drawDateTextView.setText(formattedDrawDate);
        } else {
            holder.drawDateTextView.setText("N/A");
        }

        // Set the click listener for the item
        holder.itemView.setOnClickListener(v -> {
            // Replace the current fragment with EventDetailsFragment
            EventDetailsFragment detailsFragment = new EventDetailsFragment();

            // Passing information to EventDetailsFragment
            Bundle args = new Bundle();
            args.putString("id", event.getId());
            detailsFragment.setArguments(args);

            currentFragment.getActivity().getSupportFragmentManager().beginTransaction()
                   // .replace(R.id.fragment_container, detailsFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

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
            nameTextView = itemView.findViewById(R.id.event_name);
            eventDateTextView = itemView.findViewById(R.id.event_date);
            drawDateTextView = itemView.findViewById(R.id.event_draw_date);
            entriesTextView = itemView.findViewById(R.id.event_entries);
        }
    }
}
