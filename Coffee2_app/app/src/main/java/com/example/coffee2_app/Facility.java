package com.example.coffee2_app;

import java.util.Map;

public class Facility {
    private String id;
    private String name;
    private String description;
    private String imageUrl;

    // Constructor
    public Facility(Map<String, Object> data) {
        this.name = (String) data.get("name");
        this.id = (String) data.get("id");
        this.imageUrl = (String) data.get("imageUrl");
        // Initialize other fields from the map
    }
    public Facility(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    // Default constructor for Firebase
    public Facility() {}

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
