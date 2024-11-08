package com.example.coffee2_app.Admin_ui.browseEvents;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.coffee2_app.Event;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class BrowseEventsViewModel extends ViewModel {

    private final MutableLiveData<List<Event>> eventsLiveData = new MutableLiveData<>();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public BrowseEventsViewModel() {
        loadEvents();
    }

    public LiveData<List<Event>> getEvents() {
        return eventsLiveData;
    }

    private void loadEvents() {
        db.collection("events").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Event> eventList = new ArrayList<>();
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        Event event = document.toObject(Event.class);
                        eventList.add(event);
                    }
                }
                eventsLiveData.setValue(eventList);
            }
        });
    }
}
