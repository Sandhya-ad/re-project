package com.example.coffee2_app.Admin_ui.browseProfiles;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.example.coffee2_app.R;
import com.example.coffee2_app.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class BrowseProfilesAdapter extends RecyclerView.Adapter<BrowseProfilesAdapter.ProfileViewHolder> {

    private final Context context;
    private final List<User> userList;
    private final FirebaseFirestore db;

    public BrowseProfilesAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
        this.db = FirebaseFirestore.getInstance(); // Initialize Firestore
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_profile_entry, parent, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        User user = userList.get(position);

        // Ensure user has non-null entrant with name and email
        if (user.getEntrant() != null) {
            holder.profileName.setText(user.getEntrant().getName() != null ? user.getEntrant().getName() : "No Name");
            holder.profileEmail.setText(user.getEntrant().getEmail() != null ? user.getEntrant().getEmail() : "No Email");
        }

        // Set up Delete Button functionality with confirmation dialog
        holder.deleteButton.setOnClickListener(v -> showDeleteConfirmationDialog(user, position));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

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
            // Set button colors to black
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            positiveButton.setTextColor(Color.BLACK);
            negativeButton.setTextColor(Color.BLACK);
        });

        dialog.show();
    }

    private void deleteUser(User user, int position) {
        if (user.getEntrant() != null) {
            // Set Entrant to null in Firestore
            db.collection("users")
                    .document(user.getUserId())
                    .update("entrant", null)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("DeleteUser", "Entrant role set to null successfully.");

                        // Check if user has no other roles
                        if (user.getOrganizer() == null && !user.getIsAdmin()) {
                            // Delete the entire user document if no other roles
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
                            // If user still has roles, just update the Entrant to null
                            user.setEntrant(null); // Ensure User has a setter for Entrant
                            userList.remove(position); // Remove from list immediately
                            notifyItemRemoved(position); // Notify adapter of item removal
                        }
                    })
                    .addOnFailureListener(e -> Log.e("DeleteUser", "Failed to set entrant to null", e));
        }
    }

    public static class ProfileViewHolder extends RecyclerView.ViewHolder {

        TextView profileName;
        TextView profileEmail;
        ImageButton deleteButton;

        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            profileName = itemView.findViewById(R.id.profile_name);
            profileEmail = itemView.findViewById(R.id.profile_email);
            deleteButton = itemView.findViewById(R.id.delete_button); // Ensure this ID matches the delete button in XML
        }
    }
}
