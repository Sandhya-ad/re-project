package com.example.coffee2_app.Admin_ui.browseFacilities;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.coffee2_app.Facility;
import com.example.coffee2_app.databinding.FragmentAdminFacilitiesBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BrowseFacilitiesFragment extends Fragment {

    private FragmentAdminFacilitiesBinding binding; // ViewBinding for fragment_admin_facilities.xml
    private BrowseFacilitiesAdapter facilitiesAdapter;
    private List<Facility> facilityList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdminFacilitiesBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        // Initialize ViewModel
        BrowseFacilitiesViewModel viewModel = new ViewModelProvider(this).get(BrowseFacilitiesViewModel.class);

        // Setup RecyclerView with GridLayoutManager
        RecyclerView recyclerView = binding.viewEventList;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3)); // Display in a 3-column grid

        facilityList = new ArrayList<>();
        facilitiesAdapter = new BrowseFacilitiesAdapter(getContext(), facilityList);
        recyclerView.setAdapter(facilitiesAdapter);

        // Observe LiveData from ViewModel
        viewModel.getFacilities().observe(getViewLifecycleOwner(), facilities -> {
            if (facilities != null) {
                facilityList.clear();
                for (Map<String, Object> facilityData : facilities) {
                    // Assuming Facility has a constructor that takes a Map
                    Facility facility = new Facility(facilityData);
                    facilityList.add(facility);
                }
                facilitiesAdapter.notifyDataSetChanged();
            }
        });

        // Back button functionality
        binding.backButton.setOnClickListener(v -> getActivity().onBackPressed());

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Avoid memory leaks
    }
}

