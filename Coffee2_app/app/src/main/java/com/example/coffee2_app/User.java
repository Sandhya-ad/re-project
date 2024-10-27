package com.example.coffee2_app;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class User implements Serializable {
    //Does not have a profile picture yet

    private String userId; //Later set the userID to the deviceID
    // private String name; // Moving these into the classes themselves
    // private String email; // Moving these into the classes themselves
    // private Boolean isAdmin;//Instead of having a set of roles, we could just have isAdmin or not
    private Set<String> roles;  // Set of roles like "entrant", "organizer", "admin"

    // Constructor
    /*
    public User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.roles = new HashSet<>();
        this.roles.add("entrant");  // Default role is 'entrant'
    }*/

    // Constructor
    public User(String userId) {
        this.userId = userId;
        this.roles = new HashSet<>();
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void addRole(String role) {
        this.roles.add(role);
    }

    public void removeRole(String role) {
        this.roles.remove(role);
    }
}
