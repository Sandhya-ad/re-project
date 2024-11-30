package com.example.coffee2_app;

import android.graphics.Bitmap;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Event implements Serializable {
    private String posterImageURL; // Firebase URL or Storage path
    private String id;
    private String name;
    private String description;
    private String facilityId;
    private int maxEntries;
    private String imageID;
    private boolean collectGeo;
    private List<String> attendees;
    private List<String> waitingList;
    private List<String> waitlistEntrant;
    private List<String> cancelledEntrant;
    private List<String> invitedEntrant;
    private List<String> declinedEntrant;
    private List<String> finalEntrant;
    private String hashQrData;
    private Timestamp eventDate;
    private Timestamp drawDate;
    private String imageUrl;

    // No-argument constructor (Required for Firestore)
    public Event() {}

    // Constructor for an event without a maximum number of attendees
    public Event(String name, String facilityId, boolean collectGeo, Timestamp eventDate, Timestamp drawDate) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.attendees = new ArrayList<>();
        this.waitingList = new ArrayList<>();
        this.waitlistEntrant = new ArrayList<>();
        this.cancelledEntrant = new ArrayList<>();
        this.invitedEntrant = new ArrayList<>();
        this.declinedEntrant = new ArrayList<>();
        this.finalEntrant = new ArrayList<>();
        this.facilityId = facilityId;
        this.eventDate = eventDate;
        this.drawDate = drawDate;
        this.collectGeo = collectGeo;
        this.hashQrData = this.id;
        this.maxEntries = -1; // Indicates unlimited attendees
    }

    // Constructor for an event with a maximum number of attendees
    public Event(String name, String facilityId, int maxEntries, boolean collectGeo, Timestamp eventDate, Timestamp drawDate, String description) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.attendees = new ArrayList<>();
        this.waitingList = new ArrayList<>();
        this.waitlistEntrant = new ArrayList<>();
        this.cancelledEntrant = new ArrayList<>();
        this.invitedEntrant = new ArrayList<>();
        this.declinedEntrant = new ArrayList<>();
        this.finalEntrant = new ArrayList<>();
        this.facilityId = facilityId;
        this.eventDate = eventDate;
        this.drawDate = drawDate;
        this.collectGeo = collectGeo;
        this.hashQrData = this.id;
        this.maxEntries = maxEntries;
        this.description = description;
    }

    public Event(String name, String facilityId, int maxEntries, boolean collectGeo, Timestamp eventDate, Timestamp drawDate, String description, String posterImageID) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.attendees = new ArrayList<>();
        this.waitingList = new ArrayList<>();
        this.waitlistEntrant = new ArrayList<>();
        this.cancelledEntrant = new ArrayList<>();
        this.invitedEntrant = new ArrayList<>();
        this.declinedEntrant = new ArrayList<>();
        this.finalEntrant = new ArrayList<>();
        this.facilityId = facilityId;
        this.eventDate = eventDate;
        this.drawDate = drawDate;
        this.collectGeo = collectGeo;
        this.hashQrData = this.id;
        this.maxEntries = maxEntries;
        this.description = description;
        this.imageID = posterImageID;
    }

    /**
     * Adds an entrant to the waitlist.
     * @param entrantId Entrant ID to add.
     */
    public void addWaitlistEntrant(String entrantId) {
        if (!waitlistEntrant.contains(entrantId)) {
            waitlistEntrant.add(entrantId);
        }
    }

    /**
     * Adds an entrant to the cancelled list.
     * @param entrantId Entrant ID to add.
     */
    public void addCancelledEntrant(String entrantId) {
        if (!cancelledEntrant.contains(entrantId)) {
            cancelledEntrant.add(entrantId);
        }
    }

    /**
     * Adds an entrant to the invited list.
     * @param entrantId Entrant ID to add.
     */
    public void addInvitedEntrant(String entrantId) {
        if (!invitedEntrant.contains(entrantId)) {
            invitedEntrant.add(entrantId);
        }
    }

    /**
     * Adds an entrant to the declined list.
     * @param entrantId Entrant ID to add.
     */
    public void addDeclinedEntrant(String entrantId) {
        if (!declinedEntrant.contains(entrantId)) {
            declinedEntrant.add(entrantId);
        }
    }

    /**
     * Adds an entrant to the final list.
     * @param entrantId Entrant ID to add.
     */
    public void addFinalEntrant(String entrantId) {
        if (!finalEntrant.contains(entrantId)) {
            finalEntrant.add(entrantId);
        }
    }

    // Getters and Setters for all fields

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

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId= this.facilityId;
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

    public List<String> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<String> attendees) {
        this.attendees = attendees;
    }

    public List<String> getWaitingList() {
        return waitingList;
    }

    public void setWaitingList(List<String> waitingList) {
        this.waitingList = waitingList;
    }

    public List<String> getWaitlistEntrant() {
        return waitlistEntrant;
    }

    public void setWaitlistEntrant(List<String> waitlistEntrant) {
        this.waitlistEntrant = waitlistEntrant;
    }

    public List<String> getCancelledEntrant() {
        return cancelledEntrant;
    }

    public void setCancelledEntrant(List<String> cancelledEntrant) {
        this.cancelledEntrant = cancelledEntrant;
    }

    public List<String> getInvitedEntrant() {
        return invitedEntrant;
    }

    public void setInvitedEntrant(List<String> invitedEntrant) {
        this.invitedEntrant = invitedEntrant;
    }

    public List<String> getDeclinedEntrant() {
        return declinedEntrant;
    }

    public void setDeclinedEntrant(List<String> declinedEntrant) {
        this.declinedEntrant = declinedEntrant;
    }

    public List<String> getFinalEntrant() {
        return finalEntrant;
    }

    public void setFinalEntrant(List<String> finalEntrant) {
        this.finalEntrant = finalEntrant;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {return this.description;}

    /**
     * Uploads the event poster image to Firestore.
     *
     * @param imageID The event poster image as a Bitmap.
     */
    public void setImage(String imageID) {
        this.imageID = imageID;
    }

    public String getImageID() {
        return imageID;
    }

    public String getPosterImageURL() {
        return posterImageURL;
    }

    public void setPosterImageURL(String posterImageURL) {
        this.posterImageURL = posterImageURL;
    }

    public void addAttendee(String attendeeId) {
        if (!attendees.contains(attendeeId)) {
            attendees.add(attendeeId);
        }
    }
    public void removeAttendee(String attendeeId) {
        attendees.remove(attendeeId);
    }

}

