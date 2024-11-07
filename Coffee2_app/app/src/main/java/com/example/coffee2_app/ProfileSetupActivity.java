package com.example.coffee2_app;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileSetupActivity extends AppCompatActivity {
    Entrant newEntrant;

    private FirebaseFirestore db;
    private String deviceID;
    private EditText nameInput, emailInput, phoneInput;
    private CheckBox adminNotifCheckbox, organizerNotifCheckbox;
    private ImageView profilePhoto;
    private Uri profilePhotoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_setup);

        db = FirebaseFirestore.getInstance();

        // Initialize fields
        profilePhoto = findViewById(R.id.profile_photo);
        Button selectPhotoButton = findViewById(R.id.select_photo_button);
        nameInput = findViewById(R.id.input_name);
        emailInput = findViewById(R.id.input_email);
        phoneInput = findViewById(R.id.input_phone);
        Button saveButton = findViewById(R.id.save_button);
        adminNotifCheckbox = findViewById(R.id.checkbox_admin_notif);
        organizerNotifCheckbox = findViewById(R.id.checkbox_organizer_notif);

        // Retrieve entrant and device ID
        Intent intent = getIntent();
        newEntrant = (Entrant) intent.getSerializableExtra("entrant");
        deviceID = newEntrant.getUserId();

        // Back button functionality
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish()); // Finishes the activity to return to the previous screen

        // Set up photo selection
        selectPhotoButton.setOnClickListener(v -> selectPhoto());

        // Save data
        saveButton.setOnClickListener(v -> saveProfileData());
    }

    // Photo picker launcher
    private final ActivityResultLauncher<Intent> photoPickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    profilePhotoUri = result.getData().getData();
                    profilePhoto.setImageURI(profilePhotoUri);
                }
            }
    );

    private void selectPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        photoPickerLauncher.launch(intent);
    }

    private void saveProfileData() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        boolean receiveAdminNotif = adminNotifCheckbox.isChecked();
        boolean receiveOrganizerNotif = organizerNotifCheckbox.isChecked();

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Name and email are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate and set profile data
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!phone.isEmpty() && !phone.matches("\\d{10}")) {
            Toast.makeText(this, "Phone number must be exactly 10 digits", Toast.LENGTH_SHORT).show();
            return;
        }

        newEntrant.setName(name);
        newEntrant.setEmail(email);
        if (!phone.isEmpty()) {
            newEntrant.setPhone(phone);
        }
        newEntrant.setAdminNotification(receiveAdminNotif);
        newEntrant.setOrganizerNotification(receiveOrganizerNotif);
        DatabaseHelper.updateEntrant(newEntrant);

        redirectToHome(newEntrant);
    }

    private void redirectToHome(Entrant newEntrant) {
        Intent intent = new Intent(ProfileSetupActivity.this, EntrantHomeActivity.class);
        intent.putExtra("entrant", newEntrant);
        intent.putExtra("deviceID", deviceID);
        startActivity(intent);
        finish();
    }

    // Override onBackPressed to ensure consistent back-navigation behavior
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish(); // Finishes the activity to go back to the previous screen
    }
}
