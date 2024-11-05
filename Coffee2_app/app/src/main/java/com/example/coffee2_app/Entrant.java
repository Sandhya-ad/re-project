package com.example.coffee2_app;

// File: Entrant.java
import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;
import com.google.firebase.firestore.FirebaseFirestore;
/**
 * The Entrant class represents a user participating in events within the application.
 * It stores user details such as name, email, user ID, phone number, and notification
 * preferences, as well as a list of events the user has signed up for. It includes
 * methods to update user information in Firebase Firestore.
 *
 * <p>This class implements Serializable and marks the Firebase Firestore instance as
 * transient to avoid serialization issues.</p>
 */
public class Entrant implements Serializable {

    /**
     * List of events the entrant has signed up for.
     */
    private final ArrayList<String> signedUpEvents;

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

    /**
     * A transient Firebase Firestore instance for database operations.
     */
    private transient FirebaseFirestore db;

    /**
     * Constructor for Entrant with a FirebaseFirestore instance.
     *
     * @param userId The ID of the user.
     * @param name   The name of the user.
     * @param email  The email of the user.
     * @param db     The FirebaseFirestore instance for database operations.
     */
    public Entrant(String userId, String name, String email, FirebaseFirestore db) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.signedUpEvents = new ArrayList<>();
        this.db = db;
    }

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
        this.signedUpEvents = new ArrayList<>();
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
        this.signedUpEvents = new ArrayList<>();
    }

    /**
     * Default constructor for Entrant.
     */
    public Entrant() {
        this.signedUpEvents = new ArrayList<>();
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
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (userId != null) {
            db.collection("users").document(userId)
                    .update("entrant.name", name)
                    .addOnSuccessListener(aVoid -> System.out.println("Entrant name updated in Firestore successfully."))
                    .addOnFailureListener(e -> System.err.println("Error updating Entrant name in Firestore: " + e.getMessage()));
        } else {
            System.err.println("Firestore instance or user ID is null, cannot update name.");
        }
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
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (userId != null) {
            db.collection("users").document(userId)
                    .update("entrant.phone", phone)
                    .addOnSuccessListener(aVoid -> System.out.println("Entrant phone updated in Firestore successfully."))
                    .addOnFailureListener(e -> System.err.println("Error updating Entrant phone in Firestore: " + e.getMessage()));
        } else {
            System.err.println("Firestore instance or user ID is null, cannot update phone.");
        }
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
        if (userId != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(userId)
                    .update("entrant.adminNotification", this.adminNotification);
        }
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
        if (userId != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(userId)
                    .update("entrant.organizerNotification", this.organizerNotification);
        }
    }

    /**
     * Adds an event to the list of signed-up events.
     *
     * @param event The event to add.
     */
    public void addSignedUpEvent(String event) {
        signedUpEvents.add(event);
        System.out.println(this.getName() + " signed up for event: " + event);
    }

    /**
     * Retrieves the list of events the entrant has signed up for.
     *
     * @return An ArrayList of signed-up events.
     */
    public ArrayList<String> getSignedUpEvents() {
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
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (userId != null) {
            db.collection("users").document(userId)
                    .update("entrant.email", email)
                    .addOnSuccessListener(aVoid -> System.out.println("Entrant email updated in Firestore successfully."))
                    .addOnFailureListener(e -> System.err.println("Error updating Entrant email in Firestore: " + e.getMessage()));
        } else {
            System.err.println("Firestore instance or user ID is null, cannot update email.");
        }
    }
}
