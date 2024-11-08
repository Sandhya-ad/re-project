package com.example.coffee2_app.Entrant_ui.profile;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.coffee2_app.DatabaseHelper;
import com.example.coffee2_app.Entrant;
import com.example.coffee2_app.EntrantHomeActivity;
import com.example.coffee2_app.R;
import com.example.coffee2_app.databinding.FragmentEntrantProfileBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.auth.FirebaseAuth;




/**
 * Fragment for managing and displaying the profile of an Entrant.
 * Allows editing profile details, changing profile picture, and updating notification preferences.
 */
public class ProfileFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;


    private Entrant entrant;
    private FragmentEntrantProfileBinding binding;
    private boolean isEditing = false;
    private FirebaseFirestore db;


    ActivityResultLauncher<Intent> imagePickLauncher;
    Uri selectedImageUri;
    ImageView profilePic;


    /**
     * Sets the profile picture of an ImageView using a specified image URI.
     *
     * @param context   The context used to load the image.
     * @param imageUri  The URI of the image to be set.
     * @param imageView The ImageView to display the profile picture.
     */
    public static void setProfilePic(Context context, Uri imageUri, ImageView imageView){
        Glide.with(context).load(imageUri).apply(RequestOptions.circleCropTransform()).into(imageView);
    }


    /**
     * Retrieves the Firebase UID of the current user.
     *
     * @return The Firebase UID of the current user.
     */
    public static String currentUserId(){
        return FirebaseAuth.getInstance().getUid();
    }


    /**
     * Provides a Firebase Storage reference for the current user's profile picture.
     *
     * @return A StorageReference pointing to the user's profile picture.
     */
    public static StorageReference getCurrentProfilePicStorageRef(){
        return FirebaseStorage.getInstance().getReference().child("profile_pic")
                .child(currentUserId());
    }


    /**
     * Provides a Firestore document reference for the current user's details.
     *
     * @return A DocumentReference pointing to the user's Firestore document.
     */
    public static DocumentReference currentUserDetails(){
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId());
    }


    /**
     * Initializes the fragment and sets up the image picker result launcher.
     *
     * @param savedInstanceState Bundle containing the fragment's saved state
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data!=null && data.getData()!=null){
                            selectedImageUri = data.getData();
                            setProfilePic(getContext(), selectedImageUri, profilePic);
                        }
                    }
                });
    }


    /**
     * Initializes the fragment's UI, retrieves Entrant data from the parent activity, and sets up event listeners.
     *
     * @param inflater           LayoutInflater to inflate views in the fragment
     * @param container          Parent view to contain the fragment's UI
     * @param savedInstanceState Bundle containing the fragment's saved state
     * @return The root view of the fragment
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEntrantProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        // Initialize Firestore
        db = FirebaseFirestore.getInstance();


        EntrantHomeActivity activity = (EntrantHomeActivity) getActivity();
        if (activity != null) {
            entrant = activity.getEntrant();  // Get the Entrant instance
        }


        if (entrant != null) {
            displayEntrantDetails();  // Populate fields with Entrant data
        } else {
            Toast.makeText(getActivity(), "Error: Entrant data is missing.", Toast.LENGTH_SHORT).show();
        }


        // Set up Save Button functionality
        binding.editProfileButton.setVisibility(View.GONE);  // Hide by default
        binding.editProfileButton.setOnClickListener(view -> {
            if (entrant != null) {
                saveProfileData();
                toggleEditMode();  // Exit edit mode
            }
        });


        binding.editProfileIcon.setOnClickListener(view -> toggleEditMode());
        binding.backButton.setOnClickListener(v -> getActivity().onBackPressed());


        // Profile picture click listener
        binding.profilePicture.setOnClickListener(view -> {
//            if (isEditing) {
//                ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512, 512)
//                        .createIntent(intent -> {
//                            imagePickLauncher.launch(intent);
//                            return null;
//                        });
//            }
            if (isEditing) openImagePicker();
        });


        // Notification settings button listener
        binding.notificationSettingsButton.setOnClickListener(view -> showNotificationSettingsDialog());


        return root;
    }


    /**
     * Displays the Entrant's details in the UI components.
     */
    private void displayEntrantDetails() {
        binding.entrantName.setText(entrant.getName());
        binding.entrantPhone.setText(entrant.getPhone());
        binding.entrantEmail.setText(entrant.getEmail());
    }


    /**
     * Toggles the edit mode for the profile, enabling or disabling input fields
     * and adjusting the visibility of buttons accordingly.
     */
    private void toggleEditMode() {
        isEditing = !isEditing;
        binding.editProfileButton.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        binding.editProfileIcon.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        binding.notificationSettingsButton.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        setEditMode(isEditing);
    }


    /**
     * Enables or disables editing for the profile fields.
     *
     * @param enabled True to enable editing, false to disable it.
     */
    private void setEditMode(boolean enabled) {
        binding.entrantName.setEnabled(enabled);
        binding.entrantEmail.setEnabled(enabled);
        binding.entrantPhone.setEnabled(enabled);


        int bgColor = enabled ? getResources().getColor(android.R.color.background_light) : getResources().getColor(android.R.color.transparent);
        binding.entrantName.setBackgroundColor(bgColor);
        binding.entrantEmail.setBackgroundColor(bgColor);
        binding.entrantPhone.setBackgroundColor(bgColor);
    }


    /**
     * Launches an image picker to select a profile picture for the Entrant.
     */
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    /**
     * Displays a dialog to manage notification preferences, allowing the Entrant to select
     * whether to receive notifications from admins or organizers.
     */
    private void showNotificationSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_notification_settings, null);
        builder.setView(dialogView);


        CheckBox adminCheckBox = dialogView.findViewById(R.id.checkbox_admin);
        CheckBox organizerCheckBox = dialogView.findViewById(R.id.checkbox_organizer);


        // Set checkbox states based on entrant's notification preferences
        if (entrant != null) {
            adminCheckBox.setChecked(entrant.getAdminNotification());
            organizerCheckBox.setChecked(entrant.getOrganizerNotification());
        }


        builder.setPositiveButton("Save", (dialog, which) -> {
            boolean receiveFromAdmin = adminCheckBox.isChecked();
            entrant.setAdminNotification(receiveFromAdmin);
            boolean receiveFromOrganizer = organizerCheckBox.isChecked();
            entrant.setOrganizerNotification(receiveFromOrganizer);
            Toast.makeText(getActivity(), "Notification preferences saved.", Toast.LENGTH_SHORT).show();
        });


        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dlg -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(android.R.color.black));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(android.R.color.black));
        });
        dialog.show();
    }


    /**
     * Updates the Entrant's profile data in Firestore and provides feedback on the operation's success or failure.
     */
    void updateToFirestore(){
        if (entrant != null) {
            currentUserDetails().set(entrant)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Update failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getContext(), "Entrant data is missing", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Loads the Entrant's data from Firebase Storage and Firestore, including the profile picture.
     */
    void getUserData() {
        // Load profile picture from Firebase Storage
        getCurrentProfilePicStorageRef().getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Uri uri = task.getResult();
                        setProfilePic(getContext(), uri, binding.profilePicture); // updated to use binding.profilePicture
                    } else {
                        Toast.makeText(getContext(), "Failed to load profile picture.", Toast.LENGTH_SHORT).show();
                    }
                });


        // Load user data from Firestore
        currentUserDetails().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                entrant = task.getResult().toObject(Entrant.class); // Retrieve Entrant data
                if (entrant != null) {
                    binding.entrantName.setText(entrant.getName()); // Use Entrant's data to populate fields
                    binding.entrantPhone.setText(entrant.getPhone());
                    binding.entrantEmail.setText(entrant.getEmail());
                }
            } else {
                Toast.makeText(getContext(), "Failed to load user data.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * Saves the updated profile data for the Entrant, including validation for email and phone.
     * Updates the Entrant in Firestore if validation is successful.
     */
    private void saveProfileData() {
        String originalName = entrant.getName();
        String originalEmail = entrant.getEmail();
        String originalPhone = entrant.getPhone();


        String name = binding.entrantName.getText().toString().trim();
        String email = binding.entrantEmail.getText().toString().trim();
        String phone = binding.entrantPhone.getText().toString().trim();


        // Validate email format
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getActivity(), "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            resetUI(originalName, originalEmail, originalPhone);
            return;
        }


        // Validate phone number (must be exactly 10 digits)
        if (!phone.isEmpty() && !phone.matches("\\d{10}")) {
            Toast.makeText(getActivity(), "Phone number must be exactly 10 digits", Toast.LENGTH_SHORT).show();
            resetUI(originalName, originalEmail, originalPhone);
            return;
        }


        // Update Entrant instance and Firestore
        entrant.setName(name);
        entrant.setEmail(email);
        entrant.setPhone(phone);
        DatabaseHelper.updateEntrant(entrant);


        if(selectedImageUri != null) {
            getCurrentProfilePicStorageRef().putFile(selectedImageUri)
                    .addOnCompleteListener(task -> {
                        updateToFirestore();
                    });
        }else{
            updateToFirestore();
        }
    }


    /**
     * Resets the UI fields to their original values if validation fails.
     *
     * @param name  Original name value
     * @param email Original email value
     * @param phone Original phone value
     */
    private void resetUI(String name, String email, String phone) {
        // Revert the UI to original values if validation fails
        binding.entrantName.setText(name);
        binding.entrantEmail.setText(email);
        binding.entrantPhone.setText(phone);
    }


    /**
     * Clears the binding reference when the view is destroyed to prevent memory leaks.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
