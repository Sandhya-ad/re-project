package com.example.coffee2_app;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

/**
 * Main activity that allows users to enter the app as Entrant, Organizer, or Admin.
 * It verifies the user's status and provides navigation based on the assigned role.
 */
public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private String deviceID;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        Button buttonEntrant = findViewById(R.id.button_enter_entrant);
        Button buttonOrganizer = findViewById(R.id.button_enter_organizer);
        Button buttonAdmin = findViewById(R.id.button_enter_admin);

        // Initially hide the Admin button until we confirm user's getIsAdmin status
        buttonAdmin.setVisibility(View.GONE);

        if (deviceID != null) {
            checkAndAddUser(deviceID, newUser -> {
                currentUser = newUser;
                setupButtonListeners(buttonEntrant, buttonOrganizer, buttonAdmin);

                // Display admin button if user is an admin
                if (currentUser.getIsAdmin()) {
                    buttonAdmin.setVisibility(View.VISIBLE);
                }
            });
        } else {
            Log.e("Firestore", "Device ID is null, cannot proceed");
        }
    }

    private void setupButtonListeners(Button buttonEntrant, Button buttonOrganizer, Button buttonAdmin) {
        buttonEntrant.setOnClickListener(v -> {
            checkIsDeleted(deviceID, isDeleted -> {
                if (isDeleted) {
                    // Entrant profile was deleted
                    Toast.makeText(this,
                            "Sorry, your profile was deleted by the admin.",
                            Toast.LENGTH_LONG).show();
                    Toast.makeText(this,
                            "Please enter with another available role.",
                            Toast.LENGTH_LONG).show();
                    buttonEntrant.setVisibility(View.GONE);
                } else {
                    // Check Entrant Profile
                    checkAndAddUser(deviceID, newUser -> {
                        currentUser = newUser;

                        if (currentUser.getEntrant() != null && currentUser.getEntrant().getName() == null) {
                            // Redirect to profile setup if name is missing
                            Intent intent = new Intent(MainActivity.this, ProfileSetupActivity.class);
                            intent.putExtra("entrant", currentUser.getEntrant());
                            startActivity(intent);
                        } else if (currentUser.getEntrant() != null) {
                            // Entrant exists and has a name
                            Intent intent = new Intent(MainActivity.this, EntrantHomeActivity.class);
                            intent.putExtra("entrant", currentUser.getEntrant());
                            startActivity(intent);
                        } else {
                            Log.e("Firestore", "Entrant role not found for current user");
                        }
                    });
                }
            });
        });

        buttonOrganizer.setOnClickListener(v -> {
            // Check Organizer Profile on button click
            checkAndAddUser(deviceID, newUser -> {
                currentUser = newUser;

                if (currentUser.getFacility() != null) {
                    if (currentUser.getFacility().getName() == null || currentUser.getFacility().getAddress() == null) {
                        // Redirect to profile setup if name or address is missing
                        Intent intent = new Intent(MainActivity.this, OrganizerProfileSetupActivity.class);
                        intent.putExtra("organizer", currentUser.getFacility());
                        startActivity(intent);
                    } else {
                        // Organizer exists and has name and address
                        Intent intent = new Intent(MainActivity.this, OrganizerHomeActivity.class);
                        intent.putExtra("organizer", currentUser.getFacility());
                        intent.putExtra("deviceID", deviceID);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, "Organizer profile not found. Please contact support.", Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", "Organizer role not found for current user");
                }
            });
        });

        buttonAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AdminHomeActivity.class);
            intent.putExtra("deviceID", deviceID);
            startActivity(intent);
        });
    }

    private void checkAndAddUser(String deviceID, UserCallback callback) {
        DocumentReference docRef = db.collection("users").document(deviceID);

        // Fetch from Firestore server to ensure the latest data
        docRef.get(Source.SERVER).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    User user = document.toObject(User.class);
                    Log.d("Firestore", "User loaded: " + user);

                    callback.onUserLoaded(user);
                } else {
                    Log.d("Firestore", "User does not exist. Creating new user.");
                    User newUser = new User(deviceID);
                    db.collection("users").document(deviceID).set(newUser)
                            .addOnSuccessListener(aVoid -> {
                                Log.d("Firestore", "New user added");
                                callback.onUserLoaded(newUser);
                            })
                            .addOnFailureListener(e -> Log.e("Firestore", "Failed to add user", e));
                }
            } else {
                Log.e("Firestore", "Error fetching user", task.getException());
            }
        });
    }

    private void checkIsDeleted(String deviceID, DeletionCallback callback) {
        DocumentReference docRef = db.collection("users").document(deviceID);

        docRef.get().addOnSuccessListener(document -> {
            boolean isDeleted = false;
            if (document.exists()) {
                User user = document.toObject(User.class);
                if (user != null && user.getEntrant() == null) {
                    isDeleted = true;
                }
            }
            callback.onDeletionChecked(isDeleted);
        }).addOnFailureListener(e -> {
            Log.e("FirestoreError", "Error checking if user is deleted", e);
            Toast.makeText(this, "Error checking profile. Try again later.", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    interface UserCallback {
        void onUserLoaded(User user);
    }

    interface DeletionCallback {
        void onDeletionChecked(boolean isDeleted);
    }
}
