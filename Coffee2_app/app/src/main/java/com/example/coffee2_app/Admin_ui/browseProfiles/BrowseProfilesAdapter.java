package com.example.coffee2_app.Admin_ui.browseProfiles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffee2_app.DatabaseHelper;
import com.example.coffee2_app.ImageGenerator;
import com.example.coffee2_app.R;
import com.example.coffee2_app.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

/**
 * Adapter class for managing and displaying user profiles in a RecyclerView.
 * Each profile allows viewing the user's name, email, and includes a delete option.
 * Provides a delete confirmation dialog to prevent accidental deletions.
 */
public class BrowseProfilesAdapter extends RecyclerView.Adapter<BrowseProfilesAdapter.ProfileViewHolder> {

    private final Context context;
    private final List<User> userList;
    private final FirebaseFirestore db;

    /**
     * Constructor to initialize the adapter with a context and a list of users.
     *
     * @param context  The context of the activity or fragment.
     * @param userList The list of User objects to be displayed.
     */
    public BrowseProfilesAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
        this.db = FirebaseFirestore.getInstance(); // Initialize Firestore
    }

    /**
     * Creates and returns a ProfileViewHolder that holds the view for each user profile item.
     *
     * @param parent   The parent view group.
     * @param viewType The view type of the new view.
     * @return A ProfileViewHolder that holds the view for each item.
     */
    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_profile_entry, parent, false);
        return new ProfileViewHolder(view);
    }

    /**
     * Binds user data to each item in the RecyclerView and sets up the delete button listener.
     *
     * @param holder   The ProfileViewHolder to bind data to.
     * @param position The position of the item in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        User user = userList.get(position);

        // Set profile name and email, handling null values
        if (user.getEntrant() != null) {
            holder.profileName.setText(user.getEntrant().getName() != null ? user.getEntrant().getName() : "No Name");
            holder.profileEmail.setText(user.getEntrant().getEmail() != null ? user.getEntrant().getEmail() : "No Email");

            // Set profile image if available
            Bitmap profileImage = user.getEntrant().getProfilePicture();
            if (profileImage != null) {
                holder.profileImage.setImageBitmap(profileImage);
            } else {
                // Optionally, set a placeholder image if no profile picture is available
                ImageGenerator gen = new ImageGenerator(user.getEntrant().getName());
                holder.profileImage.setImageBitmap(gen.getImg()); // Replace with your placeholder image
            }
        }

        // Set up Delete Button functionality with confirmation dialog
        holder.deleteButton.setOnClickListener(v -> showDeleteConfirmationDialog(user, position));
    }

    /**
     * Returns the total number of items in the RecyclerView.
     *
     * @return The size of the user list.
     */
    @Override
    public int getItemCount() {
        return userList.size();
    }

    /**
     * Displays a confirmation dialog before deleting a user profile.
     *
     * @param user     The user to be deleted.
     * @param position The position of the user in the RecyclerView.
     */
    private void showDeleteConfirmationDialog(User user, int position) {
        String entrantName = user.getEntrant() != null && user.getEntrant().getName() != null
                ? user.getEntrant().getName()
                : "this profile";

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete " + entrantName + "?")
                .setPositiveButton("Delete", (dialogInterface, which) -> deleteUser(user, position))
                .setNegativeButton("Cancel", (dialogInterface, which) -> dialogInterface.dismiss())
                .create();

        dialog.setOnShowListener(dialogInterface -> {
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            positiveButton.setTextColor(Color.BLACK);
            negativeButton.setTextColor(Color.BLACK);
        });

        dialog.show();
    }

    /**
     * Deletes a user profile from Firestore and updates the RecyclerView.
     *
     * @param user     The user to delete.
     * @param position The position of the user in the RecyclerView.
     */
    private void deleteUser(User user, int position) {
        if (user.getEntrant() != null) {
            db.collection("users")
                    .document(user.getUserId())
                    .update("entrant", null)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("DeleteUser", "Entrant role set to null successfully.");

                        // Check if user has no other roles
                        if (user.getOrganizer() == null && !user.getIsAdmin()) {
                            db.collection("users")
                                    .document(user.getUserId())
                                    .delete()
                                    .addOnSuccessListener(success -> {
                                        Log.d("DeleteUser", "User document deleted as no other roles exist.");
                                        userList.remove(position);
                                        notifyItemRemoved(position);
                                    })
                                    .addOnFailureListener(e -> Log.e("DeleteUser", "Failed to delete user document", e));
                        } else {
                            user.setEntrant(null);
                            userList.remove(position);
                            notifyItemRemoved(position);
                            DatabaseHelper.updateUser(user);
                        }
                    })
                    .addOnFailureListener(e -> Log.e("DeleteUser", "Failed to set entrant to null", e));
        }
    }

    /**
     * ViewHolder class to hold and manage each user profile item in the RecyclerView.
     */
    public static class ProfileViewHolder extends RecyclerView.ViewHolder {

        TextView profileName;
        TextView profileEmail;
        ImageButton deleteButton;
        ImageView profileImage; // ImageView for profile picture

        /**
         * Constructor for initializing the ProfileViewHolder with views from the layout.
         *
         * @param itemView The root view of the profile item layout.
         */
        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            profileName = itemView.findViewById(R.id.profile_name);
            profileEmail = itemView.findViewById(R.id.profile_email);
            deleteButton = itemView.findViewById(R.id.delete_button);
            profileImage = itemView.findViewById(R.id.profile_image); // Ensure this ID matches the ImageView in XML
        }
    }
}
