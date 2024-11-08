package com.example.coffee2_app.Admin_ui.browseEvents;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.example.coffee2_app.Event;
import com.example.coffee2_app.databinding.FragmentAdminEventsBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class BrowseEventsFragment extends Fragment {

    private FragmentAdminEventsBinding binding; // Assuming view binding is enabled
    private FirebaseFirestore db;
    private BrowseEventsAdapter eventsAdapter;
    private List<Event> eventsList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdminEventsBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Setup GridView
        GridView gridView = binding.eventGrid;
        eventsList = new ArrayList<>();
        eventsAdapter = new BrowseEventsAdapter(getContext(), eventsList);
        gridView.setAdapter(eventsAdapter);

        // Load events from Firestore
        loadEvents();

        // Back button functionality
        binding.backButton.setOnClickListener(v -> getActivity().onBackPressed());

        return rootView;
    }

    private void loadEvents() {
        db.collection("events").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                eventsList.clear();
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        Event event = document.toObject(Event.class);
                        eventsList.add(event);
                    }
                    eventsAdapter.notifyDataSetChanged();
                }
            } else {
                Toast.makeText(getContext(), "Failed to load events", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Prevent memory leaks
    }
}

