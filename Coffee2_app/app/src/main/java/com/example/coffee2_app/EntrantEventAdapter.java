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

import org.w3c.dom.Text;

import java.util.Date;
import java.util.List;

public class EntrantEventAdapter extends RecyclerView.Adapter<EntrantEventAdapter.EventViewHolder> {
    private List<Event> events;
    private FragmentManager currentFragment;
    private Context context;
    private List<String> status;

    public EntrantEventAdapter(List<Event> events, Context context, FragmentManager currentFragment,List<String> status) {
        this.events = events;
        this.context = context;
        this.currentFragment = currentFragment;
        this.status = status;
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
        String status_str = status.get(position);
        holder.nameTextView.setText(event.getName());
        //holder.entriesTextView.setText(event.getMaxEntries() > 0 ? String.valueOf(event.getMaxEntries()) : "Unlimited");

        if (status_str != null) {
            holder.eventStatusTextView.setText(status_str);
        } else {
            holder.eventStatusTextView.setText("N/A");
        }
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


    @Override
    public int getItemCount() {
        return events.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView eventDateTextView;
        TextView eventStatusTextView;
        TextView drawDateTextView;
        TextView entriesTextView;

        EventViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.event_title);
            eventDateTextView = itemView.findViewById(R.id.event_date);
            eventStatusTextView = itemView.findViewById(R.id.event_status);
            //drawDateTextView = itemView.findViewById(R.id.event_draw_date);
            //entriesTextView = itemView.findViewById(R.id.event_entries);
        }
    }
}
