package com.example.coffee2_app;

import android.graphics.Bitmap;

import com.google.firebase.Timestamp;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Event implements Serializable {
    private String id;
    private String name;
    private String organizerID;
    private int maxEntries;
    private boolean collectGeo;
    private List<User> attendees;
    private List<User> waitingList;
    private String hashQrData;
    private Timestamp eventDate;
    private Timestamp drawDate;
    private Bitmap QRCode;
    private String imageUrl;

    // No-argument constructor (Required for Firestore)
    public Event() {}

    //Constructor if no maxAttendees
    public Event(String name, String organizerID, boolean collectGeo, String hashQrData, Timestamp eventDate, Timestamp drawDate) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.attendees = new ArrayList<>();
        this.waitingList = new ArrayList<>();
        this.organizerID = organizerID;
        this.eventDate= eventDate;
        this.drawDate = drawDate;
        this.collectGeo = collectGeo;
        this.hashQrData = hashQrData;
        this.maxEntries = -1; // Indicator for infinite max attendees
    }

    // Constructor if maxAttendees
    public Event(String name, String organizerID, int maxEntries, boolean collectGeo, String hashQrData, Timestamp eventDate, Timestamp drawDate) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.attendees = new ArrayList<>();
        this.waitingList = new ArrayList<>();
        this.organizerID = organizerID;
        this.eventDate= eventDate;
        this.drawDate = drawDate;
        this.collectGeo = collectGeo;
        this.hashQrData = hashQrData;
        this.maxEntries = maxEntries;
    }
    //constructor to check the events for Entrants
    public Event(String sampleEvent) {
        this.id = UUID.randomUUID().toString();
        this.name = sampleEvent;
        this.attendees = new ArrayList<>();
        this.waitingList = new ArrayList<>();
    }

    /**
     * Gets UUID of Event
     * @return Event ID
     */
    public String getId() {
        return id;
    }

    /**
     * Setter method for ID, if needed
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter method for Name
     * @return Name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter method for Name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter method for Organizer owner
     * @return Organizer
     */
    public String getOrganizer() {
        return organizerID;
    }

    /**
     * Setter method for Organizer owner
     * @param organizer
     */
    public void setOrganizer(String organizer) {
        this.organizerID = organizerID;
    }

    /**
     * Getter method for max entries
     * @return Event Max Entries
     */
    public int getMaxEntries() {
        return maxEntries;
    }

    /**
     * Setter method for max entries
     * @param maxEntries
     */
    public void setMaxEntries(int maxEntries) {
        this.maxEntries = maxEntries;
    }

    /**
     * Getter method for Geo Checkbox
     * @return Boolean
     */
    public boolean isCollectGeo() {
        return collectGeo;
    }

    /**
     * Setter method for Geo Checkbox
     * @param collectGeo
     */
    public void setCollectGeo(boolean collectGeo) {
        this.collectGeo = collectGeo;
    }

    /**
     * Getter method for total attendees
     * @return Attendee total
     */
    public List<User> getAttendees() {
        return attendees;
    }

    /**
     * Setter method for total attendees
     * @param attendees
     */
    public void setAttendees(List<User> attendees) {
        this.attendees = attendees;
    }

    /**
     * Getter method for waitlist
     * @return Waitlist
     */
    public List<User> getWaitingList() {
        return waitingList;
    }

    /**
     * Setter method for waitlist
     * @param waitingList
     */
    public void setWaitingList(List<User> waitingList) {
        this.waitingList = waitingList;
    }

    /**
     * Getter method for QR Code Data
     * @return QR Code Data
     */
    public String getHashQrData() {
        return hashQrData;
    }

    /**
     * Setter method for QR Code Data
     * @param hashQrData
     */
    public void setHashQrData(String hashQrData) {
        this.hashQrData = hashQrData;
    }

    /**
     * Getter method for event date
     * @return Event Date
     */
    public Timestamp getEventDate() {
        return eventDate;
    }

    /**
     * Setter method for event date
     * @param eventDate
     */
    public void setEventDate(Timestamp eventDate) {
        this.eventDate = eventDate;
    }

    /**
     * Getter method for draw date
     * @return Draw Date
     */
    public Timestamp getDrawDate() {
        return drawDate;
    }

    /**
     * Setter method for draw date
     * @param drawDate
     */
    public void setDrawDate(Timestamp drawDate) {
        this.drawDate = drawDate;
    }


    private void generateQRCode() {
        BarcodeEncoder QRGenerator = new BarcodeEncoder();

        try {
            this.QRCode = QRGenerator.encodeBitmap(this.id, BarcodeFormat.QR_CODE, 400, 400);
        }
        catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * Returns the hashed QR Code containing the Event's UUID.
     * @return QR Code of Event UUID
     */
    public Bitmap getQRCode() {
        if (this.QRCode == null) {
            generateQRCode();
        }
        return this.QRCode;
    }

    // Getter and Setter
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
