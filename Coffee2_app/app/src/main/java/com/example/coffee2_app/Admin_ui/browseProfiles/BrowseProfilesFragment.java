package com.example.coffee2_app.Admin_ui.browseProfiles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.coffee2_app.R;
import com.example.coffee2_app.User;
import com.example.coffee2_app.databinding.FragmentAdminProfilesBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

/**
 * Fragment to display a list of user profiles for the admin.
 */
public class BrowseProfilesFragment extends Fragment {

    private FirebaseFirestore db;
    private BrowseProfilesAdapter profilesAdapter;
    private List<User> usersList;
    private FragmentAdminProfilesBinding binding; // Binding object

    /**
     * Inflates the layout for this fragment and initializes the view binding,
     * RecyclerView, adapter, and data source.
     *
     * @param inflater           LayoutInflater object to inflate views in the fragment
     * @param container          Parent view to contain the fragment's UI
     * @param savedInstanceState Bundle object containing the fragment's saved state
     * @return Root view of the binding for this fragment
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Initialize view binding
        binding = FragmentAdminProfilesBinding.inflate(inflater, container, false);

        // Setup RecyclerView with binding
        binding.viewUserProfiles.setLayoutManager(new LinearLayoutManager(getContext()));

        usersList = new ArrayList<>();
        profilesAdapter = new BrowseProfilesAdapter(getContext(), usersList);
        binding.viewUserProfiles.setAdapter(profilesAdapter);
        binding.backButton.setOnClickListener(v -> getActivity().onBackPressed());

        db = FirebaseFirestore.getInstance();
        loadProfiles();

        return binding.getRoot();
    }

    /**
     * Loads user profiles from Firestore and populates the users list.
     * Only adds users with non-null Entrant details to the list.
     */
    private void loadProfiles() {
        db.collection("users")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    usersList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        User user = document.toObject(User.class);
                        if (user.getEntrant() != null && user.getEntrant().getName() != null && user.getEntrant().getEmail() != null) {
                            usersList.add(user);
                        }
                    }
                    profilesAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                });
    }

    /**
     * Releases binding resources when the view is destroyed to avoid memory leaks.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Avoid memory leaks
    }
}
