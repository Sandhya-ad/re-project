package com.example.coffee2_app;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

import android.content.Context;
import android.provider.Settings;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented tests for the EntrantProfileFragment to verify UI elements,
 * data saving, and interaction with Firebase Firestore.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class EntrantHomeActivityTest {

    private String deviceID;
    private FirebaseFirestore db;

    /**
     * Set up test environment by initializing Firestore, creating a test user, and navigating to EntrantMainActivity.
     * @throws InterruptedException if the thread is interrupted during sleep.
     */
    @Before
    public void setup() throws InterruptedException {
        // Retrieve the device ID to match app logic
        Context context = ApplicationProvider.getApplicationContext();
        deviceID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        db = FirebaseFirestore.getInstance();

        // Create a test user and add it to Firestore
        User testUser = new User(deviceID);
        Entrant testEntrant = new Entrant(deviceID);
        testEntrant.setName("Test User Main");
        testEntrant.setEmail("testuser@example.com");
        testEntrant.setPhone("1234567890");
        testUser.setEntrant(testEntrant);

        // Create a test event and add it to Firestore
        Event testEvent = new Event("testEvent1");

        // Create Timestamp objects for the dates shown in the image
        Timestamp drawDate = new Timestamp(2024, 11, 23, 4, 36, 0);
        Timestamp eventDate = new Timestamp(2024, 11, 30, 15, 11, 0);

        Event event = new Event(
                "uwhwb",
                "8af92a48112b9a69",
                true,
                "",
                eventDate,
                drawDate
        );

        DatabaseHelper.addUser(testUser);
        DatabaseHelper.addEvent(testUser);
        Thread.sleep(3000); // Wait to ensure Firestore write is complete

        // Launch MainActivity to start the navigation flow
        ActivityScenario.launch(MainActivity.class);

        // Step through the app: click "Enter as Entrant" and navigate to Profile
        onView(withId(R.id.button_enter_entrant)).perform(click());
        Thread.sleep(2000); // Wait for navigation to complete

        // Handle first-time setup if needed
        if (testEntrant.getName() == null) {
            onView(withId(R.id.input_name)).perform(typeText("Test User"));
            onView(withId(R.id.input_email)).perform(typeText("testuser@example.com"));
            onView(withId(R.id.save_button)).perform(click());
            Thread.sleep(2000); // Wait for profile setup completion
        }

        // Navigate to the Profile page using bottom navigation
        onView(withId(R.id.entrant_navigation_home)).perform(click());
        Thread.sleep(2000); // Wait for Profile fragment to load
    }

    /**
     * Tear down the test environment by removing the test user from Firestore after each test.
     * @throws InterruptedException if the thread is interrupted during sleep.
     */
    @After
    public void tearDown() throws InterruptedException {
        DatabaseHelper.deleteUser(deviceID);
        Thread.sleep(2000); // Wait to ensure user is deleted
    }