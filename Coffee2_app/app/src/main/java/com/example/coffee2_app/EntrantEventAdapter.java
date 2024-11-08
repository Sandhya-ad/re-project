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


/**
 * Adapter class for binding event data to a RecyclerView.
 * Handles the display of events, their statuses, and user interactions.
 */
public class EntrantEventAdapter extends RecyclerView.Adapter<EntrantEventAdapter.EventViewHolder> {
    private List<Event> events;
    private FragmentManager currentFragment;
    private Context context;
    private List<String> status;
    private FirebaseFirestore db;
    private String userID;
    public String status_str;


    /**
     * Constructor for the EntrantEventAdapter.
     *
     * @param events List of events to display in the RecyclerView.
     * @param context The context of the application.
     * @param currentFragment FragmentManager used to handle fragments in the UI.
     * @param status List of statuses for the events.
     * @param userID The ID of the current user interacting with the events.
     */
    public EntrantEventAdapter(List<Event> events, Context context, FragmentManager currentFragment,List<String> status, String userID) {
        this.userID = userID;
        this.events = events;
        this.context = context;
        this.currentFragment = currentFragment;
        this.status = status;
    }

    /**
     * Called to create a new ViewHolder to represent an event.
     *
     * @param parent The parent ViewGroup into which the new View will be added.
     * @param viewType The view type for the new ViewHolder.
     * @return A new EventViewHolder object.
     */
    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entrant_signedup_events, parent, false);
        return new EventViewHolder(view);
    }

    /**
     * Binds data from a specific event to its corresponding views in the ViewHolder.
     * Handles setting the event's name, date, and status with the connection to the Firestore database.
     *
     * @param holder The ViewHolder for the event.
     * @param position The position of the event in the list.
     */
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

} else if (Objects.equals(status_str, "waitlisted")){
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Event Status")
                        .setMessage("Do you want to be waitlisted for this event?")
                        .setCancelable(true)
                        // Handle Yes
                        .setPositiveButton("Yes", null)  // Initially set to null
                        // Handle No
                        .setNegativeButton("No, I don't", null);  // Initially set to null

                AlertDialog dialog = builder.create();

                dialog.setOnShowListener(dialogInterface -> {
                    // Get button references
                    Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                    // Style buttons
                    positiveButton.setTextColor(context.getResources().getColor(R.color.black));
                    negativeButton.setTextColor(context.getResources().getColor(android.R.color.black));

                    negativeButton.setOnClickListener(view -> {
                        db.collection("users").document(userID).collection("events").document(event.getId())
                                .update("status", "cancelled")
                                .addOnSuccessListener(aVoid -> {
                                    status_str = "cancelled";
                                    holder.eventStatusTextView.setText("cancelled");

                                    dialog.dismiss();
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("Firestore", "Error updating document", e);
                                    dialog.dismiss();
                                });
                    });
                });

                dialog.show();
            } else if ((Objects.equals(status_str, "not-chosen")) || (Objects.equals(status_str, "cancelled"))){
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                String message;
                if (Objects.equals(status_str, "not-chosen")){
                    message = "You were not chosen. Do you want to be waitlisted for this event?";
                }
                else{
                    message = "You're cancelled now, do you want to be waitlisted?";
                }
                builder.setTitle("Event Status")
                        .setMessage(message)
                        .setCancelable(true)  // Allows dialog to be cancelable
                        // Handle Yes
                        .setPositiveButton("Yes", null)  // Initially set to null
                        // Handle Don't
                        .setNegativeButton("No, I don't", null);  // Initially set to null

                AlertDialog dialog = builder.create();

                dialog.setOnShowListener(dialogInterface -> {
                    Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                    // Style buttons
                    positiveButton.setTextColor(context.getResources().getColor(R.color.black));
                    negativeButton.setTextColor(context.getResources().getColor(android.R.color.black));

                    // Set click listeners for buttons
                    positiveButton.setOnClickListener(view -> {
                        db.collection("users").document(userID).collection("events").document(event.getId())
                                .update("status", "waitlisted")
                                .addOnSuccessListener(aVoid -> {
                                    // Warn for geolocation
                                    if (event.isCollectGeo()) {
                                        AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                                        builder2.setTitle("Action Required")
                                                .setMessage("Organizer collects your geolocation. Continue?")
                                                .setCancelable(true)  // Allows dialog to be cancelable
                                                // Handle Yes
                                                .setPositiveButton("Yes", null)  // Initially set to null
                                                // Handle No
                                                .setNegativeButton("No", null);  // Initially set to null

                                        AlertDialog dialog2 = builder2.create();

                                        // Set the button click listeners after warning dialog is created
                                        dialog2.setOnShowListener(dialogInterface2 -> {
                                            Button positiveButton2 = dialog2.getButton(AlertDialog.BUTTON_POSITIVE);
                                            Button negativeButton2 = dialog2.getButton(AlertDialog.BUTTON_NEGATIVE);

                                            // Style buttons for dialog2
                                            positiveButton2.setTextColor(context.getResources().getColor(R.color.black));
                                            negativeButton2.setTextColor(context.getResources().getColor(android.R.color.black));

                                            // Set click listeners for buttons in dialog2
                                            positiveButton2.setOnClickListener(view2 -> {
                                                db.collection("users").document(userID).collection("events").document(event.getId())
                                                        .update("status", "waitlisted")
                                                        .addOnSuccessListener(aVoid2 -> {
                                                            status_str = "waitlisted";
                                                            holder.eventStatusTextView.setText("waitlisted");
                                                            dialog2.dismiss();  // Close dialog2
                                                        })
                                                        .addOnFailureListener(e -> {
                                                            Log.e("Firestore", "Error updating document", e);
                                                            dialog2.dismiss();  // Close dialog2
                                                        });
                                            });

                                            negativeButton2.setOnClickListener(view2 -> {
                                                status_str = "cancelled";
                                                holder.eventStatusTextView.setText("cancelled");

                                                dialog2.dismiss();
                                            });
                                        });
                                        dialog2.show();
                                    }
                                    dialog.dismiss();
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("Firestore", "Error updating document", e);
                                    dialog.dismiss();
                                });
                    });

                    negativeButton.setOnClickListener(view -> {
                        db.collection("users").document(userID).collection("events").document(event.getId())
                                .update("status", "cancelled")
                                .addOnSuccessListener(aVoid -> {
                                    status_str = "cancelled";
                                    holder.eventStatusTextView.setText("cancelled");

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
            //Navigation controller to replace fragment
            //NavController navController = Navigation.findNavController((Activity) context, R.id.nav_host_fragment_activity_main);
            //navController.navigate(R.id.nav_host_fragment_activity_main, args);
        });
    }

    /**
     * Returns the total number of events in the list.
     *
     * @return The number of events in the list.
     */
    @Override
    public int getItemCount() {
        return events.size();
    }

    /**
     * ViewHolder class for holding references to the UI elements of an event.
     * This helps in efficiently reusing views in the RecyclerView.
     */
    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView eventDateTextView;
        TextView eventStatusTextView;

        /**
         * Constructor for the EventViewHolder.
         * It initializes the references to the event UI elements.
         *
         * @param itemView The view representing the event item.
         */
        EventViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.event_title);
            eventDateTextView = itemView.findViewById(R.id.event_date);
            eventStatusTextView = itemView.findViewById(R.id.event_status);
        }
    }
}
