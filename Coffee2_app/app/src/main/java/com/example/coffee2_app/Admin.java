package com.example.coffee2_app;


import com.example.coffee2_app.Facility;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.List;
import android.util.Log;

public class Admin {
    private static final String TAG = "Admin";
    private FirebaseFirestore db;

    public Admin() {
        db = FirebaseFirestore.getInstance();
    }

    // Interface for data fetch callbacks
    public interface OnDataFetchedListener<T> {
        void onDataFetched(List<T> data);
        void onError(Exception e);
    }

    // Browse Events
    public void browseEvents(OnDataFetchedListener<Event> listener) {
        CollectionReference eventsRef = db.collection("events");
        eventsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Event> events = task.getResult().toObjects(Event.class);
                listener.onDataFetched(events);
            } else {
                listener.onError(task.getException());
            }
        });
    }

    // Browse Profiles
    public void browseProfiles(OnDataFetchedListener<Profile> listener) {
        CollectionReference profilesRef = db.collection("profiles");
        profilesRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Profile> profiles = task.getResult().toObjects(Profile.class);
                listener.onDataFetched(profiles);
            } else {
                listener.onError(task.getException());
            }
        });
    }

    // Browse Images
    public void browseImages(OnDataFetchedListener<Image> listener) {
        CollectionReference imagesRef = db.collection("images");
        imagesRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Image> images = task.getResult().toObjects(Image.class);
                listener.onDataFetched(images);
            } else {
                listener.onError(task.getException());
            }
        });
    }

    // Browse Facilities
    public void browseFacilities(OnDataFetchedListener<Facility> listener) {
        CollectionReference facilitiesRef = db.collection("facilities");
        facilitiesRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Facility> facilities = task.getResult().toObjects(Facility.class);
                listener.onDataFetched(facilities);
            } else {
                listener.onError(task.getException());
            }
        });
    }

    // Remove Event
    public void removeEvent(String eventId) {
        db.collection("events").document(eventId).delete()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Event successfully deleted!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error deleting event", e));
    }

    // Remove Profile
    public void removeProfile(String profileId) {
        db.collection("profiles").document(profileId).delete()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Profile successfully deleted!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error deleting profile", e));
    }

    // Remove Image
    public void removeImage(String imageId) {
        db.collection("images").document(imageId).delete()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Image successfully deleted!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error deleting image", e));
    }

    // Remove Facility
    public void removeFacility(String facilityId) {
        db.collection("facilities").document(facilityId).delete()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Facility successfully deleted!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error deleting facility", e));
    }
}
