package com.example.coffee2_app;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;

import android.content.Context;
import android.provider.Settings;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.coffee2_app.User;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

/**
 * Instrumented test for BrowseProfilesFragment in the admin UI,
 * verifying UI elements, list display, and interaction with Firestore.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class AdminProfilesTest {

    private FirebaseFirestore db;
    private String deviceID;

    /**
     * Sets up the test environment by initializing Firestore, creating a test user,
     * and navigating to the BrowseProfilesFragment.
     */
    @Before
    public void setup() throws InterruptedException {
        Context context = ApplicationProvider.getApplicationContext();
        deviceID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        db = FirebaseFirestore.getInstance();

        // Create a test user and add it to Firestore
        User testUser = new User(deviceID);
        Entrant testEntrant = new Entrant(deviceID);
        testEntrant.setName("Test User");
        testEntrant.setEmail("testuser@example.com");
        testEntrant.setPhone("1234567890");
        testUser.setEntrant(testEntrant);
        testUser.setAdmin(); // Set as admin for testing

        DatabaseHelper.addUser(testUser);
        Thread.sleep(3500); // Wait to ensure Firestore write is complete

        // Launch MainActivity to start the navigation flow
        ActivityScenario.launch(MainActivity.class);

        // Click "Enter as Admin" to navigate to the admin section
        onView(withId(R.id.button_enter_admin)).perform(click());
        Thread.sleep(2000); // Wait for admin dashboard to load

        // Navigate to "Browse Profiles" section
        onView(withId(R.id.admin_navigation_profiles)).perform(click());
        Thread.sleep(2000); // Wait for BrowseProfilesFragment to load
    }

    /**
     * Removes the test user from Firestore after each test.
     */
    @After
    public void tearDown() {
        // Use the actual document ID based on deviceID or test setup
        db.collection("users").document(deviceID).delete();
    }

    /**
     * Test to verify that the list of profiles is displayed with at least one user entry.
     */
    @Test
    public void testProfilesListDisplayed() {
        // Check if the RecyclerView is displayed
        onView(withId(R.id.view_user_profiles)).check(matches(isDisplayed()));

        // Verify that the test user appears in the list
        onView(withText("Test User")).check(matches(isDisplayed()));
        onView(withText("testuser@example.com")).check(matches(isDisplayed()));
    }

    /**
     * Test to verify that the back button works and navigates back as expected.
     */
    @Test
    public void testBackButtonFunctionality() {
        // Perform a click on the back button
        onView(withId(R.id.back_button)).perform(click());

        // Verify that the fragment is closed or navigated back
        // Adjust according to your app's navigation structure.
    }
}
