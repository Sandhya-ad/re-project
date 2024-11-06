package com.example.coffee2_app;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The User class represents a user in the system with specific roles and access to
 * either Entrant or Organizer functionality based on assigned roles. It includes
 * methods for managing user roles and accessing role-specific data.
 *
 * <p>This class implements Serializable to allow for object serialization.</p>
 */
public class User implements Serializable {

    /**
     * Indicates whether the user has admin privileges.
     */
    private boolean isAdmin;

    /**
     * The unique identifier for the user.
     */
    private String userId;

    /**
     * A list of roles assigned to the user, such as "entrant", "organizer", or "admin".
     */
    private List<String> roles;

    /**
     * The Entrant object associated with the user if they have the entrant role.
     */
    private Entrant entrant;

    /**
     * The Organizer object associated with the user if they have the organizer role.
     */
    private Organizer organizer;

    /**
     * Constructor to initialize a User with a specific user ID. By default, assigns
     * the "entrant" and "organizer" roles.
     *
     * @param userId The ID of the user.
     */
    public User(String userId) {
        this.userId = userId;
        this.roles = new ArrayList<>();
        this.isAdmin = false; // default to non-admin
        addRole("entrant"); // Default roles
        addRole("organizer");
    }

    /**
     * Default constructor for creating a User instance without setting a user ID.
     */
    public User() {
        this.roles = new ArrayList<>();
    }

    // Role-checking methods

    /**
     * Checks if the user has the "entrant" role.
     *
     * @return True if the user has the entrant role, false otherwise.
     */
    public boolean hasEntrantRole() {
        return roles.contains("entrant");
    }

    /**
     * Checks if the user has the "organizer" role.
     *
     * @return True if the user has the organizer role, false otherwise.
     */
    public boolean hasOrganizerRole() {
        return roles.contains("organizer");
    }

    // Entrant-specific access

    /**
     * Retrieves the Entrant object associated with the user if they have the entrant role.
     *
     * @return The Entrant object if the user has the entrant role, null otherwise.
     */
    public Entrant getEntrant() {
        return hasEntrantRole() ? entrant : null;
    }

    // Admin management methods

    /**
     * Grants admin privileges to the user.
     */
    public void setAdmin() {
        isAdmin = true;
    }

    /**
     * Checks if the user has admin privileges.
     *
     * @return True if the user is an admin, false otherwise.
     */
    public boolean getIsAdmin() {
        return isAdmin;
    }

    // Organizer-specific access

    /**
     * Retrieves the Organizer object associated with the user if they have the organizer role.
     *
     * @return The Organizer object if the user has the organizer role, null otherwise.
     */
    public Organizer getOrganizer() {
        return hasOrganizerRole() ? organizer : null;
    }

    // Getters and Setters for common fields

    /**
     * Retrieves the user ID.
     *
     * @return The user ID.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Retrieves the list of roles assigned to the user.
     *
     * @return A list of roles.
     */
    public List<String> getRoles() {
        return roles;
    }

    // Role management methods

    /**
     * Adds a role to the user if it is not already present. If the role is "entrant" or
     * "organizer," it creates an Entrant or Organizer object respectively.
     *
     * @param role The role to add.
     */
    public void addRole(String role) {
        if (!roles.contains(role)) {  // Only add if the role isn't already present
            roles.add(role);
            if (role.equals("entrant")) {
                entrant = new Entrant(userId);
            } else if (role.equals("organizer")) {
                organizer = new Organizer(userId);
            }
        }
    }

    public void setEntrant(Entrant entrant) {
        this.entrant = entrant;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (userId != null) {
            db.collection("users").document(userId)
                    .update("entrant", entrant)
                    .addOnSuccessListener(aVoid -> System.out.println("Entrant name updated in Firestore successfully."))
                    .addOnFailureListener(e -> System.err.println("Error updating Entrant name in Firestore: " + e.getMessage()));
        } else {
            System.err.println("Firestore instance or user ID is null, cannot update name.");
        }
    }

    /**
     * Removes a role from the user if it is present. If the role is "entrant" or
     * "organizer," it nullifies the Entrant or Organizer object respectively.
     *
     * @param role The role to remove.
     */
    public void removeRole(String role) {
        if (roles.remove(role)) {  // Only remove if the role was present
            if (role.equals("entrant")) {
                entrant = null;
            } else if (role.equals("organizer")) {
                organizer = null;
            }
        }
    }
}
