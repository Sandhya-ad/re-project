package com.example.coffee2_app;



import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.List;
import android.widget.Toast;

public class AdminHomeActivity extends AppCompatActivity {

    private Admin admin;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_main);

        admin = new Admin();

        // Initialize RecyclerView for browsing events (example)
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadEvents();
    }

    private void loadEvents() {
        admin.browseEvents(new Admin.OnDataFetchedListener<Event>() {
            @Override
            public void onDataFetched(List<Event> events) {
                Fragment currentFragment = null;
                EventsAdapter adapter = new EventsAdapter(events, null );
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(AdminHomeActivity.this, "Failed to load events", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Similar methods can be added for loading profiles, images, and facilities
    private void loadProfiles() {
        admin.browseProfiles(new Admin.OnDataFetchedListener<Profile>() {
            @Override
            public void onDataFetched(List<Profile> profiles) {
                ProfileAdapter adapter = new ProfileAdapter(profiles);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(AdminHomeActivity.this, "Failed to load profiles", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method for deleting an event, for example
    private void deleteEvent(String eventId) {
        admin.removeEvent(eventId);
        loadEvents(); // Reload events after deletion
    }
}
