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

public class BrowseProfilesFragment extends Fragment {

    private FirebaseFirestore db;
    private BrowseProfilesAdapter profilesAdapter;
    private List<User> usersList;
    private FragmentAdminProfilesBinding binding; // Binding object

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Initialize view binding
        binding = FragmentAdminProfilesBinding.inflate(inflater, container, false);

        // Setup RecyclerView with binding
        binding.viewEventList.setLayoutManager(new LinearLayoutManager(getContext()));

        usersList = new ArrayList<>();
        profilesAdapter = new BrowseProfilesAdapter(getContext(), usersList);
        binding.viewEventList.setAdapter(profilesAdapter);
        binding.backButton.setOnClickListener(v -> getActivity().onBackPressed());

        db = FirebaseFirestore.getInstance();
        loadProfiles();

        return binding.getRoot();
    }

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Avoid memory leaks
    }
}
