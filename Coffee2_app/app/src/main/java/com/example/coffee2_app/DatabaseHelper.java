package com.example.coffee2_app;

import com.google.firebase.firestore.FirebaseFirestore;

public class DatabaseHelper {

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public DatabaseHelper() {
        this.db = FirebaseFirestore.getInstance();
    }

    /**
     * Updates the entire Entrant object in Firestore.
     *
     * @param entrant The Entrant object to update in Firestore.
     */
    public static void updateEntrant(Entrant entrant) {
        if (entrant.getUserId() == null) {
            System.err.println("User ID is null, cannot update entrant.");
            return;
        }
        db.collection("users").document(entrant.getUserId())
                .update("entrant",entrant);
       }

    /**
     * Updates User on Firebase
     * @param user
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


}
