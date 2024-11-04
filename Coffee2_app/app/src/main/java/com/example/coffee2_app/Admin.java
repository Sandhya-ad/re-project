package com.example.coffee2_app;



import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.util.Log;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;

public class Admin {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String EVENTS_COLLECTION = "events";
    private static final String PROFILES_COLLECTION = "profiles";
    private static final String IMAGES_COLLECTION = "images";
    private static final String FACILITIES_COLLECTION = "facilities";

    // Method to browse all events
    public void browseEvents(final FirebaseCallback<List<Event>> callback) {
        db.collection(EVENTS_COLLECTION).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Event> eventList = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        Event event = document.toObject(Event.class);
                        eventList.add(event);
                    }
                    callback.onCallback(eventList);
                } else {
                    Log.w("Admin", "Error getting events.", task.getException());
                    callback.onCallback(null);
                }
            }
        });
    }

    // Method to remove an event by its document ID
    public void removeEvent(String eventId, final FirebaseCallback<Boolean> callback) {
        db.collection(EVENTS_COLLECTION).document(eventId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                callback.onCallback(task.isSuccessful());
            }
        });
    }

    // Method to browse user profiles
    public void browseProfiles(final FirebaseCallback<List<Profile>> callback) {
        db.collection(PROFILES_COLLECTION).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Profile> profileList = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        Profile profile = document.toObject(Profile.class);
                        profileList.add(profile);
                    }
                    callback.onCallback(profileList);
                } else {
                    Log.w("Admin", "Error getting profiles.", task.getException());
                    callback.onCallback(null);
                }
            }
        });
    }

    // Method to remove a profile by its document ID
    public void removeProfile(String profileId, final FirebaseCallback<Boolean> callback) {
        db.collection(PROFILES_COLLECTION).document(profileId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                callback.onCallback(task.isSuccessful());
            }
        });
    }

    // Method to browse images
    public void browseImages(final FirebaseCallback<List<Image>> callback) {
        db.collection(IMAGES_COLLECTION).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Image> imageList = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        Image image = document.toObject(Image.class);
                        imageList.add(image);
                    }
                    callback.onCallback(imageList);
                } else {
                    Log.w("Admin", "Error getting images.", task.getException());
                    callback.onCallback(null);
                }
            }
        });
    }

    // Method to remove an image by its document ID
    public void removeImage(String imageId, final FirebaseCallback<Boolean> callback) {
        db.collection(IMAGES_COLLECTION).document(imageId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                callback.onCallback(task.isSuccessful());
            }
        });
    }

    // Method to remove facilities that violate app policies by document ID
    public void removeFacility(String facilityId, final FirebaseCallback<Boolean> callback) {
        db.collection(FACILITIES_COLLECTION).document(facilityId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                callback.onCallback(task.isSuccessful());
            }
        });
    }
}
