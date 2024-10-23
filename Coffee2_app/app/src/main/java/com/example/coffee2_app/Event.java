package com.example.coffee2_app;

import android.content.Intent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Event implements Serializable {
    private String id;
    private String name;
    private User organizer;
    private int maxAttendees;
    private List<User> attendees;
    private List<User> waitingList;
    private String hashQrData;
    private String startDate;
    private String endDate;

    //Constructor if no maxAttendees
    public Event(String id, String name, User organizer,String startDate, String endDate,String hashQrData) {
        this.id = id;
        this.name = name;
        this.attendees = new ArrayList<>();
        this.waitingList = new ArrayList<>();
        this.organizer = organizer;
        this.startDate= startDate;
        this.endDate = endDate;
        this.hashQrData = hashQrData;
    }


    // Constructor if maxAttendees
    public Event(String id, String name, User organizer, String startDate, String endDate, String hashQrData,Integer maxAttendees) {
        this.id = id;
        this.name = name;
        this.attendees = new ArrayList<>();
        this.waitingList = new ArrayList<>();
        this.organizer = organizer;
        this.startDate= startDate;
        this.endDate = endDate;
        this.hashQrData = hashQrData;
        this.maxAttendees = maxAttendees;
    }

    // Getter methods (accessible to all)
    public String getId() {
        return id;
    }

    public String getHashQrData(){
        return hashQrData;
    }

    public String getName() {
        return name;
    }
    public User getOrganizer(){
        return organizer;
    }
    public int getMaxAttendees() {
        return maxAttendees;
    }

    public String getStartDate(){
        return startDate;
    }

    public String getEndDate(){
        return endDate;
    }
    public List<User> getAttendees() {
        return attendees;
    }
    public List<User> getWaitingList() {
        return waitingList;
    }
}
