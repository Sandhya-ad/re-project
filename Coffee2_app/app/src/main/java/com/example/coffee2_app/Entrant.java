package com.example.coffee2_app;

// File: Entrant.java
import java.io.Serializable;
import java.util.ArrayList;
import com.google.firebase.firestore.FirebaseFirestore;
public class Entrant implements Serializable {
    private final ArrayList<String> signedUpEvents;  // List of events the entrant signed up for
    private String address;

    private String name;
    private String email;
    private String userId;
    private String phone;

    public Entrant(String userId, String name,String email,String address) {
        this.address = address;
        this.name = name;
        this.userId = userId;
        this.signedUpEvents = new ArrayList<>();  // Initialize an empty list of events
    }
    public Entrant(String userId, String name,String email) {
        this.address = address;
        this.name = name;
        this.userId = userId;
        this.signedUpEvents = new ArrayList<>();  // Initialize an empty list of events
    }
    public Entrant(String userId){
        this.userId = userId;
        this.signedUpEvents = new ArrayList<>();  // Initialize an empty list of events
    }

    public Entrant() {
        this.signedUpEvents = new ArrayList<>();
    }

    public String getName() {
        return name;
    }
    public String getUserId() {
        return userId;
    }
    public String getAddress() {
        return address;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (userId != null) {
            db.collection("users").document(userId)
                    .update("entrant.phone",phone) // Nested path if Entrant is a nested object
                    .addOnSuccessListener(aVoid -> {
                        System.out.println("Entrant phone updated in Firestore successfully.");
                    })
                    .addOnFailureListener(e -> {
                        System.err.println("Error updating Entrant phone in Firestore: " + e.getMessage());
                    });
        } else {
            System.err.println("Firestore instance or user ID is null, cannot update name.");
        }
    }

    public void setName(String name) {
        this.name = name;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (userId != null) {
            db.collection("users").document(userId)
                    .update("entrant.name", name) // Nested path if Entrant is a nested object
                    .addOnSuccessListener(aVoid -> {
                        System.out.println("Entrant name updated in Firestore successfully.");
                    })
                    .addOnFailureListener(e -> {
                        System.err.println("Error updating Entrant name in Firestore: " + e.getMessage());
                    });
        } else {
            System.err.println("Firestore instance or user ID is null, cannot update name.");
        }
    }
    public void setAddress(String address) {
        this.address = address;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (userId != null) {
            db.collection("users").document(userId)
                    .update("entrant.address", address) // Nested path if Entrant is a nested object
                    .addOnSuccessListener(aVoid -> {
                        System.out.println("Entrant address updated in Firestore successfully.");
                    })
                    .addOnFailureListener(e -> {
                        System.err.println("Error updating Entrant address in Firestore: " + e.getMessage());
                    });
        } else {
            System.err.println("Firestore instance or user ID is null, cannot update name.");
        }

    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (userId != null) {
            db.collection("users").document(userId)
                    .update("entrant.email", email) // Nested path if Entrant is a nested object
                    .addOnSuccessListener(aVoid -> {
                        System.out.println("Entrant email updated in Firestore successfully.");
                    })
                    .addOnFailureListener(e -> {
                        System.err.println("Error updating Entrant email in Firestore: " + e.getMessage());
                    });
        } else {
            System.err.println("Firestore instance or user ID is null, cannot update name.");
        }
    }

    // Method to add a signed-up event
    public void addSignedUpEvent(String event) {
        signedUpEvents.add(event);
        System.out.println(this.getName() + " signed up for event: " + event);
    }

    // Method to retrieve the signed-up events list
    public ArrayList<String> getSignedUpEvents() {
        return signedUpEvents;
    }

}
