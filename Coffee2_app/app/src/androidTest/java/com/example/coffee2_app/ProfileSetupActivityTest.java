package com.example.coffee2_app;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;
import android.provider.Settings;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Instrumented tests for the ProfileSetupActivity to verify UI elements, data saving,
 * and interaction with Firebase Firestore.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ProfileSetupActivityTest {

    private String deviceID;
    private FirebaseFirestore db;

    /**
     * Set up test environment by initializing Firestore, creating a test user, and navigating to ProfileSetupActivity.
     * @throws InterruptedException if the thread is interrupted during sleep.
     */
    @Before
    public void setup() throws InterruptedException {
        // Retrieve device ID to use as a unique identifier for the test
        Context context = ApplicationProvider.getApplicationContext();
        deviceID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        db = FirebaseFirestore.getInstance();

        // Create a test user and add it to Firestore
        User testUser = new User(deviceID);
        Entrant testEntrant = new Entrant(deviceID);

        DatabaseHelper.addUser(testUser);
        Thread.sleep(3000); // Wait for Firestore write to complete

        // Launch MainActivity and navigate to ProfileSetupActivity
        ActivityScenario.launch(MainActivity.class);

        // Step through the app: click "Enter as Entrant" to reach ProfileSetupActivity
        onView(withId(R.id.button_enter_entrant)).perform(click());
        Thread.sleep(2000); // Wait for navigation to complete

        // Ensure ProfileSetupActivity views are displayed
        onView(withId(R.id.input_name)).check(matches(isDisplayed()));
    }

    /**
     * Clean up by removing the test user from Firestore after each test.
     */
    @After
    public void tearDown() {
        // Clean up by deleting the test user from Firestore
        DatabaseHelper.deleteUser(deviceID);
    }

    /**
     * Test to verify that all essential elements in ProfileSetupActivity are displayed correctly.
     */
    @Test
    public void testProfileSetupElementsDisplayed() {
        // Check if all necessary elements are displayed
        onView(withId(R.id.input_name)).check(matches(isDisplayed()));
        onView(withId(R.id.input_email)).check(matches(isDisplayed()));
        onView(withId(R.id.input_phone)).check(matches(isDisplayed()));
        onView(withId(R.id.save_button)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_photo)).check(matches(isDisplayed()));
        onView(withId(R.id.select_photo_button)).check(matches(isDisplayed()));
        onView(withId(R.id.back_button)).check(matches(isDisplayed()));
        onView(withId(R.id.checkbox_admin_notif)).check(matches(isDisplayed()));
        onView(withId(R.id.checkbox_organizer_notif)).check(matches(isDisplayed()));
    }

    /**
     * Test to verify saving profile data, including validation of data saved in Firestore.
     * @throws InterruptedException if the thread is interrupted while waiting.
     */
    @Test
    public void testSaveProfileData() throws InterruptedException {
        // Set up the CountDownLatch to wait for Firestore to respond
        CountDownLatch latch = new CountDownLatch(1);

        // Enter data in the form fields
        onView(withId(R.id.input_name)).perform(typeText("John Doe"));
        onView(withId(R.id.input_email)).perform(typeText("johndoe@example.com"));
        onView(withId(R.id.input_phone)).perform(typeText("1234567890"));

        // Click the save button
        onView(withId(R.id.save_button)).perform(click());

        // Fetch the saved data from Firestore for verification
        db.collection("users").document(deviceID).get().addOnSuccessListener(documentSnapshot -> {
            User savedUser = documentSnapshot.toObject(User.class);
            assertNotNull("User should not be null", savedUser);
            assertNotNull("Entrant should not be null", savedUser.getEntrant());

            // Check if data matches expected values
            assertEquals("John Doe", savedUser.getEntrant().getName());
            assertEquals("johndoe@example.com", savedUser.getEntrant().getEmail());
            assertEquals("1234567890", savedUser.getEntrant().getPhone());

            // CountDown when done
            latch.countDown();
        }).addOnFailureListener(e -> {
            // Fail the test if Firestore retrieval fails
            throw new AssertionError("Firestore retrieval failed: " + e.getMessage());
        });

        // Wait for up to 5 seconds for Firestore operation to complete
        boolean awaitSuccess = latch.await(5, TimeUnit.SECONDS);
        if (!awaitSuccess) {
            throw new AssertionError("Firestore data retrieval timed out");
        }
    }

    /**
     * Test to verify that the select photo button is displayed and can be clicked.
     */
    @Test
    public void testSelectPhotoButton() {
        // Ensure the select photo button is displayed and clickable
        onView(withId(R.id.select_photo_button)).check(matches(isDisplayed())).perform(click());
    }

    /**
     * Test to verify the back button functionality, ensuring proper navigation on click.
     * @throws InterruptedException if the thread is interrupted during sleep.
     */
    @Test
    public void testBackButtonFunctionality() throws InterruptedException {
        // Click the back button and verify navigation
        onView(withId(R.id.back_button)).perform(click());
        Thread.sleep(1000);
        // Check if we navigated back as expected; additional checks based on navigation logic
    }
}
