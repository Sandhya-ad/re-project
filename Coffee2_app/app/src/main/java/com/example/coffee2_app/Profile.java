package com.example.coffee2_app;

// Profile.java
public class Profile {
    private String id;
    private String name;
    private String email;

    /**
     * Firestore Constructor for Profile
     */
    // No-argument constructor required for Firestore
    public Profile() {}

    /**
     * Profile Constructor
     * @param id
     * @param name
     * @param email
     */
    public Profile(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // Getters and setters

    /**
     * Getter method for ID
     * @return Profile ID
     */
    public String getId() {
        return id;
    }

    /**
     * Setter method for ID
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter ID for Name
     * @return Profile Name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter ID for Name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter method for Email
     * @return Email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter method for Email
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
