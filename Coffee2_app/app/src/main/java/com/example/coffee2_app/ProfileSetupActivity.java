package com.example.coffee2_app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
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
    private EditText nameInput, emailInput, addressInput, phoneInput;
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
        addressInput = findViewById(R.id.input_address);
        phoneInput = findViewById(R.id.input_phone);
        Button saveButton = findViewById(R.id.save_button);
        Intent intent = getIntent();
        newEntrant = (Entrant) intent.getSerializableExtra("entrant");
        deviceID = newEntrant.getUserId();

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
        String address = addressInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Name and email are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create Entrant instance with all data
        newEntrant.setName(name);
        newEntrant.setEmail(email);
        if (phone!=null){
            newEntrant.setPhone(phone);
        }
        if (address!=null) {
            newEntrant.setAddress(address);
        }

        //newEntrant.setPhotoUri(profilePhotoUri != null ? profilePhotoUri.toString() : null); // Optional photo URI
        redirectToHome(newEntrant);
    }

    private void redirectToHome(Entrant newEntrant) {
        Intent intent = new Intent(ProfileSetupActivity.this, EntrantHomeActivity.class);
        intent.putExtra("entrant", newEntrant);
        intent.putExtra("deviceID", deviceID);
        startActivity(intent);
        finish();
    }
}
