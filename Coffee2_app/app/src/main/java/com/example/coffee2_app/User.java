package com.example.coffee2_app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private boolean isAdmin;
    private String userId;
    private List<String> roles;  // List of roles like "entrant", "organizer", "admin"
    private Entrant entrant;
    private Organizer organizer;

    // Constructor
    public User(String userId) {
        this.userId = userId;
        this.roles = new ArrayList<>();
        this.isAdmin = false; //default is false
        addRole("entrant"); // Default role
    }
    public User() {
        this.roles = new ArrayList<>();
    }


    // Role-checking methods
    public boolean hasEntrantRole() {
        return roles.contains("entrant");
    }

    public boolean hasOrganizerRole() {
        return roles.contains("organizer");
    }

    // Entrant-specific access
    public Entrant getEntrant() {
        return hasEntrantRole() ? entrant : null;
    }
    public void setAdmin(){
        isAdmin = true;
    }
    public boolean getisAdmin(){
        return isAdmin;
    }

    // Organizer-specific access
    public Organizer getOrganizer() {
        return hasOrganizerRole() ? organizer : null;
    }

    // Getters and Setters for common fields
    public String getUserId() { return userId; }
    public List<String> getRoles() { return roles; }

    // Role management methods
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
