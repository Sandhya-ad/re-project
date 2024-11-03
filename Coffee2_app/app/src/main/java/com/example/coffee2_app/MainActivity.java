package com.example.coffee2_app;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

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

        // Initially hide the Admin button until we confirm user getisAdmin status
        buttonAdmin.setVisibility(View.GONE);

        if (deviceID != null) {
            checkAndAddUser(deviceID, newUser -> {
                // Enable buttons only after currentUser is fully initialized
                currentUser = newUser;
                setupButtonListeners(buttonEntrant, buttonOrganizer, buttonAdmin);
                Log.e("Admin", currentUser.getisAdmin() + "");
                if (currentUser.getisAdmin()) {
                    buttonAdmin.setVisibility(View.VISIBLE);
                }
            });
        } else {
            Log.e("Firestore", "Device ID is null, cannot proceed");
        }
    }

    private void setupButtonListeners(Button buttonEntrant, Button buttonOrganizer, Button buttonAdmin) {
        buttonEntrant.setOnClickListener(v -> {
            if (currentUser != null && currentUser.getEntrant().getName() == null){
                Intent intent = new Intent(MainActivity.this, ProfileSetupActivity.class);
                intent.putExtra("entrant", currentUser.getEntrant());
                startActivity(intent);
            }
            else if (currentUser != null && currentUser.getEntrant().getName() != null) {
                Intent intent = new Intent(MainActivity.this, EntrantHomeActivity.class);
                intent.putExtra("entrant", currentUser.getEntrant());
                startActivity(intent);
            } else {
                Log.e("Firestore", "Entrant role not found for current user");
            }
        });

        buttonOrganizer.setOnClickListener(v -> {
            //currentUser.addRole("organizer");
            if(currentUser.getOrganizer() == null) {Log.e("test", "null");}
            Intent intent = new Intent(MainActivity.this, OrganizerHomeActivity.class);
            intent.putExtra("organizer", currentUser.getOrganizer());
            intent.putExtra("deviceID", deviceID);
            startActivity(intent);
        });
    }

    private void checkAndAddUser(String deviceID, UserCallback callback) {
        DocumentReference docRef = db.collection("users").document(deviceID);

        // Force fetch from Firestore server to ensure the latest data
        docRef.get(Source.SERVER).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    User user = document.toObject(User.class);
                    Log.d("Firestore", "User loaded: " + user);

                    // Debug log for getisAdmin to verify its value
                    Log.d("AdminCheck", "User loaded with getisAdmin: " + user.getisAdmin());

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


    public void updateEntrantInFirestore() {
        Entrant entrant = currentUser.getEntrant();
        if (deviceID != null && entrant != null) {
            db.collection("users")
                    .document(deviceID)  // Use Device ID as the document ID in "users"
                    .set(entrant)
                    .addOnSuccessListener(aVoid -> Log.d("Firestore", "Entrant updated successfully in users"))
                    .addOnFailureListener(e -> Log.e("Firestore", "Error updating entrant in users", e));
        } else {
            Log.w("Firestore", "Device ID or Entrant data is null, cannot update Firestore.");
        }
    }
    interface UserCallback {
        void onUserLoaded(User user);
    }
}
