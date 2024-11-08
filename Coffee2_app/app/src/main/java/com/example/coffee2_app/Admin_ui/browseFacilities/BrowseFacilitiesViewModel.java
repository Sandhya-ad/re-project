package com.example.coffee2_app.Admin_ui.browseFacilities;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BrowseFacilitiesViewModel extends ViewModel {
    private final MutableLiveData<List<Map<String, Object>>> facilities;

    public BrowseFacilitiesViewModel() {
        facilities = new MutableLiveData<>();
        loadFacilities();
    }

    public LiveData<List<Map<String, Object>>> getFacilities() {
        return facilities;
    }

    private void loadFacilities() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Map<String, Object>> facilityData = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> organizer = document.get("organizer", Map.class);
                            if (organizer != null) {
                                facilityData.add(organizer);
                            }
                        }
                        facilities.setValue(facilityData);
                    }
                });
    }
}
