package com.example.coffee2_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;


import com.example.coffee2_app.Organizer_ui.myevents.EventDetailsFragment;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class EntrantEventAdapter extends RecyclerView.Adapter<EntrantEventAdapter.EventViewHolder> {
    private List<Event> events;
    private FragmentManager currentFragment;
    private Context context;
    private List<String> status;
    private FirebaseFirestore db;
    private String userID;
    public String status_str;



    public EntrantEventAdapter(List<Event> events, Context context, FragmentManager currentFragment,List<String> status, String userID) {
        this.userID = userID;
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
        status_str = status.get(position);
        holder.nameTextView.setText(event.getName());

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


        holder.itemView.setOnClickListener(v -> {
            db = FirebaseFirestore.getInstance();

            if(Objects.equals(status_str, "chosen")){
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Event Confirmation")
                        .setMessage("Do you want to accept or decline this event?")
                        .setCancelable(true)  // Allows dialog to be cancelable
                        // Handle Accept
                        .setPositiveButton("Accept", null)  // Initially set to null
                        // Handle Decline
                        .setNegativeButton("Decline", null);  // Initially set to null

                AlertDialog dialog = builder.create();

                // Set the button click listeners after dialog is created
                dialog.setOnShowListener(dialogInterface -> {
                    // Get button references
                    Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                    // Style buttons
                    positiveButton.setTextColor(context.getResources().getColor(R.color.black));
                    negativeButton.setTextColor(context.getResources().getColor(android.R.color.black));

                    // Set click listeners for buttons
                    positiveButton.setOnClickListener(view -> {
                        db.collection("users").document(userID).collection("events").document(event.getId())
                                .update("status", "accepted")
                                .addOnSuccessListener(aVoid -> {
                                    status_str = "accepted";
                                    holder.eventStatusTextView.setText("accepted");
                                    dialog.dismiss();
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("Firestore", "Error updating document", e);
                                    dialog.dismiss();
                                });
                    });

                    negativeButton.setOnClickListener(view -> {
                        db.collection("users").document(userID).collection("events").document(event.getId())
                                .update("status", "declined")
                                .addOnSuccessListener(aVoid -> {
                                    status_str = "declined";
                                    holder.eventStatusTextView.setText("declined");

                                    dialog.dismiss();
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("Firestore", "Error updating document", e);
                                    dialog.dismiss();
                                });
                    });
                });

                dialog.show();

}
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

        EventViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.event_title);
            eventDateTextView = itemView.findViewById(R.id.event_date);
            eventStatusTextView = itemView.findViewById(R.id.event_status);
        }
    }
}
