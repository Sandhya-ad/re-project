package com.example.coffee2_app;

import java.io.Serializable;
import java.util.ArrayList;

public class Organizer implements Serializable {

    private ArrayList<Event> events;
    private String name;
    private String address;
    private String email;

    /**
     * Constructor class for the Organization
     * @param userID
     */
    public Organizer(String userID) {
        this.events = new ArrayList<>();
    }
    public Organizer() {
        this.events = new ArrayList<>();
    }

    /**
     * Setter for Organizer Name
     * @param name
     */
    public void setName(String name) { this.name = name; }

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
    public void setAddress(String address) { this.address = address; }

    /**
     * Getter for Organizer Address
     * @return Address
     */
    public String getAddress() { return this.address; }

    /**
     * Setter for Email
     * @param email
     */
    public void setEmail(String email) { this.email = email; }

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

}
