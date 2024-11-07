package com.example.coffee2_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.coffee2_app.databinding.AdminMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Activity for the Admin home screen, providing navigation and access to various
 * admin functions such as managing events, profiles, images, and facilities.
 */
public class AdminHomeActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private AdminMainBinding binding;
    private Admin admin;
    private String deviceID;

    /**
     * Initializes the activity, sets up the binding, Firestore, and bottom navigation.
     *
     * @param savedInstanceState Bundle containing the activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate layout using view binding
        binding = AdminMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firestore instance
        db = FirebaseFirestore.getInstance();
        Log.d("FirestoreCheck", "Firestore is Initialized");

        // Set up bottom navigation view
        BottomNavigationView navView = findViewById(R.id.admin_nav_view);
        Intent intent = getIntent();

        // Retrieve Admin and Device ID from Intent
        deviceID = intent.getStringExtra("deviceID");

        // Set up navigation for Admin sections
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.admin_navigation_events,
                R.id.admin_navigation_profiles,
                R.id.admin_navigation_images,
                R.id.admin_navigation_facilities)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.adminNavView, navController);
    }
}



// import android.os.Bundle;
// import androidx.appcompat.app.AppCompatActivity;
// import androidx.fragment.app.Fragment;
// import androidx.recyclerview.widget.RecyclerView;
// import androidx.recyclerview.widget.LinearLayoutManager;
// import java.util.List;
// import android.widget.Toast;

// public class AdminHomeActivity extends AppCompatActivity {

//     private Admin admin;
//     private RecyclerView recyclerView;


//     @Override
//     protected void onCreate(Bundle savedInstanceState) {
//         super.onCreate(savedInstanceState);


//         binding = AdminMainBinding.inflate(getLayoutInflater());
//         setContentView(binding.getRoot());

//         db = FirebaseFirestore.getInstance();
//         Log.d("FirestoreCheck", "Firestore is Initialized");

//         BottomNavigationView navView = findViewById(R.id.admin_nav_view);
//         Intent intent = getIntent();

//         // Retrieve Admin and Device ID from Intent
//         deviceID = intent.getStringExtra("deviceID");

//         // Set up navigation for Admin sections
//         AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                 R.id.admin_navigation_events,
//                 R.id.admin_navigation_profiles,
//                 R.id.admin_navigation_images,
//                 R.id.admin_navigation_facilities)
//                 .build();

//         NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//         NavigationUI.setupWithNavController(binding.adminNavView, navController);

// =======
//         setContentView(R.layout.admin_main);

//         admin = new Admin();

//         // Initialize RecyclerView for browsing events (example)
//         recyclerView = findViewById(R.id.recycler_view);
//         recyclerView.setLayoutManager(new LinearLayoutManager(this));

//         loadEvents();
//     }

//     private void loadEvents() {
//         admin.browseEvents(new Admin.OnDataFetchedListener<Event>() {
//             @Override
//             public void onDataFetched(List<Event> events) {
//                 Fragment currentFragment = null;
//                 EventsAdapter adapter = new EventsAdapter(events, null );
//                 recyclerView.setAdapter(adapter);
//             }

//             @Override
//             public void onError(Exception e) {
//                 Toast.makeText(AdminHomeActivity.this, "Failed to load events", Toast.LENGTH_SHORT).show();
//             }
//         });
//     }

//     // Similar methods can be added for loading profiles, images, and facilities
//     private void loadProfiles() {
//         admin.browseProfiles(new Admin.OnDataFetchedListener<Profile>() {
//             @Override
//             public void onDataFetched(List<Profile> profiles) {
//                 ProfileAdapter adapter = new ProfileAdapter(profiles);
//                 recyclerView.setAdapter(adapter);
//             }

//             @Override
//             public void onError(Exception e) {
//                 Toast.makeText(AdminHomeActivity.this, "Failed to load profiles", Toast.LENGTH_SHORT).show();
//             }
//         });
//     }

//     // Method for deleting an event, for example
//     private void deleteEvent(String eventId) {
//         admin.removeEvent(eventId);
//         loadEvents(); // Reload events after deletion

//     }
// }
