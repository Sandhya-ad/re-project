package com.example.coffee2_app;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The Entrant class represents a user participating in events within the application.
 * It stores user details such as name, email, user ID, phone number, and notification
 * preferences, as well as a list of events the user has signed up for. It includes
 * methods to update user information in Firebase Firestore.
 *
 * <p>This class implements Serializable and marks the Firebase
 */
public class Entrant implements Serializable {

    /**
     * List of events the entrant has signed up for.
     */
    private final ArrayList<Event> signedUpEvents;
    /**
     * List of events the entrant has waitlisted.
     */
    private final ArrayList<Event> waitListEvents;

    /**
     * The name of the entrant.
     */
    private String name;
    private Bitmap profilePicture;

    /**
     * The email of the entrant.
     */
    private String email;

    /**
     * The unique user ID of the entrant.
     */
    private String userId;

    /**
     * The phone number of the entrant.
     */
    private String phone;

    /**
     * Notification preference for admin notifications.
     */
    private Boolean adminNotification;

    /**
     * Notification preference for organizer notifications.
     */
    private Boolean organizerNotification;

    private String imageID;

    /**
     * Constructor for Entrant with basic user details.
     *
     * @param userId The ID of the user.
     * @param name   The name of the user.
     * @param email  The email of the user.
     */
    public Entrant(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.signedUpEvents = new ArrayList<>();
        this.waitListEvents = new ArrayList<>();
    }

    /**
     * Constructor for Entrant with basic user details.
     *
     * @param userId The ID of the user.
     * @param name   The name of the user.
     * @param email  The email of the user.
     * @param profilePicture   User's profilepicture
     */
    public Entrant(String userId, String name, String email, Bitmap profilePicture) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.signedUpEvents = new ArrayList<Event>();
        this.waitListEvents = new ArrayList<Event>();
        this.profilePicture = profilePicture;
    }

    /**
     * Constructor for Entrant with only a user ID.
     *
     * @param userId The ID of the user.
     */
    public Entrant(String userId) {
        this.userId = userId;
        this.adminNotification = false;
        this.organizerNotification = false;
        this.signedUpEvents = new ArrayList<Event>();
        this.waitListEvents = new ArrayList<Event>();
    }

    /**
     * Default constructor for Entrant.
     */
    public Entrant() {
        this.signedUpEvents = new ArrayList<>();
        this.waitListEvents = new ArrayList<>();
    }

    // Getter and Setter methods

    /**
     * Retrieves the entrant's name.
     *
     * @return The name of the entrant.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the entrant's name and updates it in Firestore if a user ID is available.
     *
     * @param name The new name of the entrant.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the entrant's user ID.
     *
     * @return The user ID of the entrant.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Gets the User's profile picture bitmap
     * @return the bitmap image of the progile picture
     */

    public Bitmap getProfilePicture() {
        return profilePicture;
    }

    /**
     * Retrieves the entrant's phone number.
     *
     * @return The phone number of the entrant.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the entrant's phone number and updates it in Firestore if a user ID is available.
     *
     * @param phone The new phone number of the entrant.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Retrieves the entrant's admin notification preference.
     *
     * @return True if admin notifications are enabled, false otherwise.
     */
    public Boolean getAdminNotification() {
        return adminNotification;
    }

    /**
     * Sets the entrant's admin notification preference and updates it in Firestore if a user ID is available.
     *
     * @param adminNotification The new admin notification preference.
     */
    public void setAdminNotification(Boolean adminNotification) {
        this.adminNotification = adminNotification;
    }

    /**
     * Retrieves the entrant's organizer notification preference.
     *
     * @return True if organizer notifications are enabled, false otherwise.
     */
    public Boolean getOrganizerNotification() {
        return organizerNotification;
    }

    /**
     * Sets the entrant's organizer notification preference and updates it in Firestore if a user ID is available.
     *
     * @param organizerNotification The new organizer notification preference.
     */
    public void setOrganizerNotification(Boolean organizerNotification) {
        this.organizerNotification = organizerNotification;
    }

    /**
     * Adds an event to the list of signed-up events.
     *
     * @param event The event to add.
     */
    public void addSignedUpEvent(Event event) {
        signedUpEvents.add(event);
        System.out.println(this.getName() + " signed up for event: " + event);
    }
    /**
     * Adds an event to the list of waitlisted events.
     *
     * @param event The event to add.
     */
    public void addWaitListedEvent(Event event) {
        waitListEvents.add(event);
        System.out.println(this.getName() + " signed up for event: " + event);
    }
    /**
     * Removes an event from the list of signed-up events.
     *
     * @param event The event to remove.
     */
    public void removeWaitListedEvent(Event event) {
        waitListEvents.remove(event);
    }
    /**
     * Retrieves the list of events the entrant has waitlisted for.
     *
     * @return An ArrayList of waitlisted events.
     */
    public ArrayList<Event> getWaitListedEvents() {
        return waitListEvents;
    }
    /**
     * Retrieves the list of events the entrant has signed up for.
     *
     * @return An ArrayList of signed-up events.
     */
    public ArrayList<Event> getSignedUpEvents() {
        return signedUpEvents;
    }

    /**
     * Retrieves the email of the entrant.
     *
     * @return The email of the entrant.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the entrant's email and updates it in Firestore if a user ID is available.
     *
     * @param email The new email of the entrant.
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * Turns a Bitmap into an ID and stores it in the Firebase
     * @param bitmap
     */
    public void setImage(Bitmap bitmap) {
        ByteArrayOutputStream converter = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, converter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String encodedBmp = Base64.getEncoder().encodeToString(converter.toByteArray());
            if (encodedBmp != null) {
                this.imageID = UUID.nameUUIDFromBytes(encodedBmp.getBytes()).toString();
                Log.d("ProfilePhoto", this.imageID);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> data = new HashMap<>();
                data.put("imageData", encodedBmp);
                db.collection("images").document(imageID).set(data);
            }
            else {
                System.err.println("Could not upload image.");
            }
        }
        else {
            Log.d("ImageTest", "Permission Error");
        }
    }
    public String getImageID() {
        return this.imageID;
    }
}
