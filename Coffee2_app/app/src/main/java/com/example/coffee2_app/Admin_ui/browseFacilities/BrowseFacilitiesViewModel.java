package com.example.coffee2_app.Admin_ui.browseFacilities;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.coffee2_app.Facility;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class BrowseFacilitiesViewModel extends ViewModel {

    private final MutableLiveData<List<Facility>> facilitiesLiveData = new MutableLiveData<>();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public BrowseFacilitiesViewModel() {
        loadFacilities();
    }

    public LiveData<List<Facility>> getFacilities() {
        return facilitiesLiveData;
    }

    private void loadFacilities() {
        db.collection("facilities").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Facility> facilityList = new ArrayList<>();
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        Facility facility = document.toObject(Facility.class);
                        facilityList.add(facility);
                    }
                }
                facilitiesLiveData.setValue(facilityList);
            }
        });
    }
}

