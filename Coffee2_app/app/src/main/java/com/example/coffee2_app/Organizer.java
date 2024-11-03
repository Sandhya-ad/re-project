package com.example.coffee2_app;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Organizer implements Serializable {

    private ArrayList<Event> events;
    private String name;
    private String address;
    private String email;
    private String userID;

    /**
     * Constructor class for the Organization
     * @param userID
     */
    public Organizer(String userID) {
        this.userID = userID;
        this.events = new ArrayList<>();
    }
    public Organizer() {
        this.events = new ArrayList<>();
    }

    /**
     * Setter for Organizer Name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
        if (userID != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(userID)
                    .update("organizer.name", name) // Nested path if Entrant is a nested object
                    .addOnSuccessListener(aVoid -> {
                        System.out.println("Facility name updated in Firestore successfully.");
                    })
                    .addOnFailureListener(e -> {
                        System.err.println("Error updating Facility name in Firestore: " + e.getMessage());
                    });
        }
        else {
            System.err.println("Firestore instance or user ID is null, cannot update.");
        }
    }

    /**
     * Getter for Organizer Name
     * @return Name of the Organizer
     */
    public String getName() { return this.name; }

    /**
     * Setter for Organizer Address
     * @param address
     */
    // TODO: Set this as a real geolocation possibly?
    public void setAddress(String address) {
        this.address = address;
        if (userID != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(userID)
                    .update("organizer.address", address) // Nested path if Entrant is a nested object
                    .addOnSuccessListener(aVoid -> {
                        System.out.println("Facility address updated in Firestore successfully.");
                    })
                    .addOnFailureListener(e -> {
                        System.err.println("Error updating Facility address in Firestore: " + e.getMessage());
                    });
        }
        else {
            System.err.println("Firestore instance or user ID is null, cannot update.");
        }
    }

    /**
     * Getter for Organizer Address
     * @return Address
     */
    public String getAddress() { return this.address; }

    /**
     * Setter for Email
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
        if (userID != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(userID)
                    .update("organizer.email", email) // Nested path if Entrant is a nested object
                    .addOnSuccessListener(aVoid -> {
                        System.out.println("Facility email updated in Firestore successfully.");
                    })
                    .addOnFailureListener(e -> {
                        System.err.println("Error updating Facility email in Firestore: " + e.getMessage());
                    });
        }
        else {
            System.err.println("Firestore instance or user ID is null, cannot update.");
        }
    }

    /**
     * Getter for Email address
     * @return Email
     */
    public String getEmail() { return this.email; }

    /**
     * Adds event into the Organizer's Event List
     * @param event
     */
    public void addEvent(Event event) { events.add(event); }

    /**
     * Returns an ArrayList of the Organizer's Events
     * @return ArrayList of Events
     */
    public ArrayList<Event> events() {
        return events;
    }

    /**
     * Getter for UserID
     * @return UserID
     */
    public String getUserID() {
        return userID;
    }

    /*
    public void sync() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        HashMap<String, Object> map = (HashMap<String, Object>) document.getData().get("organizer");
                        name = map.get("name").toString();
                        email = map.get("email").toString();
                        address = map.get("address").toString();
                    }
                    else {
                        Log.d("FirestoreCheck", "Document does not exist!");
                    }
                }
                else {
                    Log.d("FirestoreCheck", String.valueOf(task.getException()));
                }
            }
        });
    }
    */

}
