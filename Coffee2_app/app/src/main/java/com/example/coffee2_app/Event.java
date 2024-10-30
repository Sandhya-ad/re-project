package com.example.coffee2_app;

import com.google.firebase.Timestamp;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Event implements Serializable {
    private String id;
    private String name;
    private User organizer;
    private int maxEntries;
    private boolean collectGeo;
    private List<User> attendees;
    private List<User> waitingList;
    private String hashQrData;
    private Timestamp eventDate;
    private Timestamp drawDate;

    // No-argument constructor (Required for Firestore)
    public Event() {}

    //Constructor if no maxAttendees
    public Event(String id, String name, User organizer, boolean collectGeo, String hashQrData, Timestamp eventDate, Timestamp drawDate) {
        this.id = id;
        this.name = name;
        this.attendees = new ArrayList<>();
        this.waitingList = new ArrayList<>();
        this.organizer = organizer;
        this.eventDate= eventDate;
        this.drawDate = drawDate;
        this.collectGeo = collectGeo;
        this.hashQrData = hashQrData;
        this.maxEntries = -1; // Indicator for infinite max attendees
    }

    // Constructor if maxAttendees
    public Event(String id, String name, User organizer, int maxEntries, boolean collectGeo, String hashQrData, Timestamp eventDate, Timestamp drawDate) {
        this.id = id;
        this.name = name;
        this.attendees = new ArrayList<>();
        this.waitingList = new ArrayList<>();
        this.organizer = organizer;
        this.eventDate= eventDate;
        this.drawDate = drawDate;
        this.collectGeo = collectGeo;
        this.hashQrData = hashQrData;
        this.maxEntries = maxEntries;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOrganizer() {
        return organizer;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }

    public int getMaxEntries() {
        return maxEntries;
    }

    public void setMaxEntries(int maxEntries) {
        this.maxEntries = maxEntries;
    }

    public boolean isCollectGeo() {
        return collectGeo;
    }

    public void setCollectGeo(boolean collectGeo) {
        this.collectGeo = collectGeo;
    }

    public List<User> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<User> attendees) {
        this.attendees = attendees;
    }

    public List<User> getWaitingList() {
        return waitingList;
    }

    public void setWaitingList(List<User> waitingList) {
        this.waitingList = waitingList;
    }

    public String getHashQrData() {
        return hashQrData;
    }

    public void setHashQrData(String hashQrData) {
        this.hashQrData = hashQrData;
    }

    public Timestamp getEventDate() {
        return eventDate;
    }

    public void setEventDate(Timestamp eventDate) {
        this.eventDate = eventDate;
    }

    public Timestamp getDrawDate() {
        return drawDate;
    }

    public void setDrawDate(Timestamp drawDate) {
        this.drawDate = drawDate;
    }
}
