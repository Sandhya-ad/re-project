package com.example.coffee2_app;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;

public class Organizer implements Serializable {

    private ArrayList<Event> events;
    private String name;
    private String address;
    private String email;
    private String amenities;
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
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (userID != null) {
            db.collection("users").document(userID)
                    .update("facilities.name", name) // Nested path if Entrant is a nested object
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
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (userID != null) {
            db.collection("users").document(userID)
                    .update("facilities.address", address) // Nested path if Entrant is a nested object
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
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (userID != null) {
            db.collection("users").document(userID)
                    .update("facilities.email", email) // Nested path if Entrant is a nested object
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
     * Setter for Amenities
     * @param amenities
     */
    public void setAmenities(String amenities) {
        this.amenities = amenities;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (userID != null) {
            db.collection("users").document(userID)
                    .update("facilities.amenities", amenities) // Nested path if Entrant is a nested object
                    .addOnSuccessListener(aVoid -> {
                        System.out.println("Facility amenities updated in Firestore successfully.");
                    })
                    .addOnFailureListener(e -> {
                        System.err.println("Error updating Facility amenities in Firestore: " + e.getMessage());
                    });
        }
        else {
            System.err.println("Firestore instance or user ID is null, cannot update.");
        }
    }

    /**
     * Getter for Amenities
     * @return Amenities
     */
    public String getAmenities() {
        return this.amenities;
    }

    /**
     * Getter for UserID
     * @return UserID
     */
    public String getUserID() {
        return userID;
    }

}
