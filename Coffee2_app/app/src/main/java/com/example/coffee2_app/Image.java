package com.example.coffee2_app;

// Image.java
public class Image {
    private String id;
    private String url;
    private String description;

    // No-argument constructor required for Firestore
    public Image() {}

    public Image(String id, String url, String description) {
        this.id = id;
        this.url = url;
        this.description = description;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
