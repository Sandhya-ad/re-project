package com.example.coffee2_app;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Organizer implements Serializable {

    private ArrayList<Event> events;
    private String name;
    private String address;
    private String email;
    private String userID;
    private String imageID;

    /**
     * Constructor class for the Organization
     * @param userID
     */
    public Organizer(String userID) {
        this.userID = userID;
        this.events = new ArrayList<>();
    }
    public Organizer() {
        this.events = new ArrayList<>();
    }

    /**
     * Setter for Organizer Name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for Organizer Name
     * @return Name of the Organizer
     */
    public String getName() { return this.name; }

    /**
     * Setter for Organizer Address
     * @param address
     */
    // TODO: Set this as a real geolocation possibly?
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Getter for Organizer Address
     * @return Address
     */
    public String getAddress() { return this.address; }

    /**
     * Setter for Email
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter for Email address
     * @return Email
     */
    public String getEmail() { return this.email; }

    /**
     * Adds event into the Organizer's Event List
     * @param event
     */
    public void addEvent(Event event) { events.add(event); }

    /**
     * Returns an ArrayList of the Organizer's Events
     * @return ArrayList of Events
     */
    public ArrayList<Event> events() {
        return events;
    }

    /**
     * Getter for UserID
     * @return UserID
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Turns a Bitmap into an ID and stores it in the Firebase
     * @param bitmap
     */
    public void setImage(Bitmap bitmap) {
        ByteArrayOutputStream converter = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, converter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String encodedBmp = Base64.getEncoder().encodeToString(converter.toByteArray());
            if (encodedBmp != null) {
                this.imageID = UUID.nameUUIDFromBytes(encodedBmp.getBytes()).toString();
                Log.d("ProfilePhoto", this.imageID);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> data = new HashMap<>();
                data.put("imageData", encodedBmp);
                db.collection("images").document(imageID).set(data);
            }
            else {
                System.err.println("Could not upload image.");
            }
        }
        else {
            Log.d("ImageTest", "Permission Error");
        }
    }

    /**
     * Returns
     * @return ImageID to retrieve in Firestore
     */
    public String getImageID() {
        return this.imageID;
    }


    /*
    public void sync() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        HashMap<String, Object> map = (HashMap<String, Object>) document.getData().get("organizer");
                        name = map.get("name").toString();
                        email = map.get("email").toString();
                        address = map.get("address").toString();
                    }
                    else {
                        Log.d("FirestoreCheck", "Document does not exist!");
                    }
                }
                else {
                    Log.d("FirestoreCheck", String.valueOf(task.getException()));
                }
            }
        });
    }
    */

}
