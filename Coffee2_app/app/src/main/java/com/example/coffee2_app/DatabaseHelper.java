package com.example.coffee2_app;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Source;

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
     * @param organizer
     */
    public static void updateOrganizer(Organizer organizer) {
        if (organizer.getUserID() == null) {
            System.err.println("User ID is null, cannot update entrant.");
            return;
        }
        db.collection("users").document(organizer.getUserID())
                .update("organizer", organizer);
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

    public static void addEvent(User testUser) {
    }
}
