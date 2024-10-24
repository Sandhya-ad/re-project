package com.example.coffee2_app;

// File: Entrant.java
import java.util.ArrayList;
public class Entrant extends User {
    private final ArrayList<String> signedUpEvents;  // List of events the entrant signed up for

    public Entrant(String userId, String name, String email) {
        super(userId, name, email);  // Call User constructor
        this.signedUpEvents = new ArrayList<>();  // Initialize an empty list of events
        this.getRoles().add("entrant");  // Automatically assign the 'entrant' role
    }

    // Method to add a signed-up event
    public void addSignedUpEvent(String event) {
        signedUpEvents.add(event);
        System.out.println(getName() + " signed up for event: " + event);
    }

    // Method to retrieve the signed-up events list
    public ArrayList<String> getSignedUpEvents() {
        return signedUpEvents;
    }

}
