package com.example.coffee2_app;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Helper class to handle Firestore database operations related to `User` and `Entrant` objects.
 */
public class DatabaseHelper {

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Constructor to initialize the Firestore instance.
     */
    public DatabaseHelper() {
        this.db = FirebaseFirestore.getInstance();
    }

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
                .update("entrant", entrant);
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
}
