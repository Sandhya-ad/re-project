package com.example.coffee2_app;

// File: Entrant.java
import java.util.ArrayList;
public class Entrant extends User {
    private final ArrayList<String> signedUpEvents;  // List of events the entrant signed up for

    private String name;
    private String email;

    public Entrant(String userId, String name, String email) {
        super(userId);  // Call User constructor
        this.name = name;
        this.email = email;
        this.signedUpEvents = new ArrayList<>();  // Initialize an empty list of events
        this.getRoles().add("entrant");  // Automatically assign the 'entrant' role
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Method to add a signed-up event
    public void addSignedUpEvent(String event) {
        signedUpEvents.add(event);
        System.out.println(this.getName() + " signed up for event: " + event);
    }

    // Method to retrieve the signed-up events list
    public ArrayList<String> getSignedUpEvents() {
        return signedUpEvents;
    }

}
