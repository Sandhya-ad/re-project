package com.example.coffee2_app;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

/**
 * Helper class to handle Firestore database operations related to `User` and `Entrant` objects.
 */
public class DatabaseHelper {

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Updates the Entrant object in Firestore under the specified user's document.
     *
     * @param entrant The Entrant object to update in Firestore.
     *                Requires a non-null User ID within the Entrant object.
     */
    public static void updateEntrant(Entrant entrant) {
        if (entrant.getUserId() == null) {
            System.err.println("User ID is null, cannot update entrant.");
            return;
        }
        db.collection("users").document(entrant.getUserId())
                .update("entrant", entrant)
                .addOnSuccessListener(aVoid -> System.out.println("Entrant updated in Firestore successfully."))
                .addOnFailureListener(e -> System.err.println("Error updating Entrant in Firestore: " + e.getMessage()));
       }
  
    /**
     * Updates the User object in Firestore, setting the entire object in the database.
     *
     * @param user The User object to update in Firestore.
     *             Requires a non-null User ID within the User object.
     */
    public static void updateUser(User user) {
        if (user.getUserId() == null) {
            System.err.println("User ID is null, cannot update user.");
            return;
        }

        db.collection("users").document(user.getUserId())
                .set(user) // Directly sets the entire User object
                .addOnSuccessListener(aVoid -> System.out.println("User updated in Firestore successfully."))
                .addOnFailureListener(e -> System.err.println("Error updating User in Firestore: " + e.getMessage()));
    }

    /**
     * Method for updating organizer
     * @param facility
     */
    public static void updateFacility(Facility facility) {
        if (facility.getUserID() == null) {
            System.err.println("User ID is null, cannot update entrant.");
            return;
        }
        db.collection("users").document(facility.getUserID())
                .update("facility", facility);
    }

     /* Adds a new User object to Firestore.
     *
     * @param user The User object to add to Firestore.
     */
    public static void addUser(User user) {
        if (user.getUserId() == null) {
            System.err.println("User ID is null, cannot add user.");
            return;
        }

        db.collection("users").document(user.getUserId())
                .set(user)
                .addOnSuccessListener(aVoid -> System.out.println("User added to Firestore successfully."))
                .addOnFailureListener(e -> System.err.println("Error adding User to Firestore: " + e.getMessage()));
    }

    /**
     * Deletes a user from Firestore.
     *
     * @param userId The ID of the user to delete from Firestore.
     */
    public static void deleteUser(String userId) {
        if (userId == null) {
            System.err.println("User ID is null, cannot delete user.");
            return;
        }

        db.collection("users").document(userId)
                .delete()
                .addOnSuccessListener(aVoid -> System.out.println("User deleted from Firestore successfully."))
                .addOnFailureListener(e -> System.err.println("Error deleting User from Firestore: " + e.getMessage()));
    }

    /**
     * Adds a new event to Firestore and updates the associated Facility's event list.
     *
     * @param event The map containing event details to add to Firestore.
     * @param facility  The Facility object to update with the new event ID.
     */
    public static void addEvent(Event event, Facility facility) {
        if (facility == null || event == null) {
            System.err.println("Facility or Event is null. Cannot add event.");
            return;
        }

        // Add the Event to Firestore
        db.collection("events")
                .add(event)
                .addOnSuccessListener(documentReference -> {
                    String eventID = documentReference.getId(); // Retrieve the generated Event ID
                    System.out.println("Event added to Firestore with ID: " + eventID);

                    // Update Event object with the generated ID
                    event.setId(eventID);

                    // Update the Event in Firestore with the ID
                    db.collection("events").document(eventID)
                            .set(event)
                            .addOnSuccessListener(aVoid -> System.out.println("Event ID updated in Firestore successfully."))
                            .addOnFailureListener(e -> System.err.println("Error updating Event ID in Firestore: " + e.getMessage()));

                    // Update the Facility's event list
                    facility.addEvent(eventID); // Adds the event ID to the Facility object
                    updateFacility(facility);  // Persist the Facility update in Firestore
                })
                .addOnFailureListener(e -> System.err.println("Error adding Event to Firestore: " + e.getMessage()));
    }

}
