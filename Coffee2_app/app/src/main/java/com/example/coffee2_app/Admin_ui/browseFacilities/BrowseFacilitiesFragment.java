package com.example.coffee2_app.Admin_ui.browseFacilities;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coffee2_app.Admin_ui.browseFacilities.BrowseFacilitiesAdapter;
import com.example.coffee2_app.Admin_ui.browseProfiles.BrowseProfilesAdapter;
import com.example.coffee2_app.Facility;
import com.example.coffee2_app.User;
import com.example.coffee2_app.databinding.FragmentAdminFacilitiesBinding;
import com.example.coffee2_app.databinding.FragmentAdminProfilesBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BrowseFacilitiesFragment extends Fragment {
    private FirebaseFirestore db;

    private FragmentAdminFacilitiesBinding binding; // ViewBinding for fragment_admin_facilities.xml
    private BrowseFacilitiesAdapter facilitiesAdapter;
    private List<User> userList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Initialize view binding
        binding = FragmentAdminFacilitiesBinding.inflate(inflater, container, false);

        // Setup RecyclerView with binding
        binding.viewFacilityProfiles.setLayoutManager(new LinearLayoutManager(getContext()));

        userList = new ArrayList<>();
        facilitiesAdapter = new BrowseFacilitiesAdapter(getContext(), userList);
        binding.viewFacilityProfiles.setAdapter(facilitiesAdapter);
        binding.backButton.setOnClickListener(v -> getActivity().onBackPressed());

        db = FirebaseFirestore.getInstance();
        loadFacilities();

        return binding.getRoot();
    }

    private void loadFacilities() {
        db.collection("users")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    userList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        User user = document.toObject(User.class);
                        if (user.getEntrant() != null && user.getEntrant().getName() != null && user.getEntrant().getEmail() != null) {
                            userList.add(user);
                        }
                    }
                    facilitiesAdapter.notifyDataSetChanged();
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

