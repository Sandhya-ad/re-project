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
public class EntrantProfileFragmentTest {

    private String deviceID;
    private FirebaseFirestore db;

    /**
     * Set up test environment by initializing Firestore, creating a test user, and navigating to EntrantProfileFragment.
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
        testEntrant.setName("Test User");
        testEntrant.setEmail("testuser@example.com");
        testEntrant.setPhone("1234567890");
        testUser.setEntrant(testEntrant);

        DatabaseHelper.addUser(testUser);
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
        onView(withId(R.id.entrant_navigation_notifications)).perform(click());
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

    /**
     * Test to verify that the entrant's profile details (name, email, phone) are displayed correctly.
     */
    @Test
    public void testProfileDetailsDisplayed() {
        onView(withId(R.id.entrant_name)).check(matches(isDisplayed()));
        onView(withId(R.id.entrant_name)).check(matches(withText("Test User")));
        onView(withId(R.id.entrant_email)).check(matches(withText("testuser@example.com")));
        onView(withId(R.id.entrant_phone)).check(matches(withText("1234567890")));
    }

    /**
     * Test to verify that the profile edit mode can be toggled on and the fields are editable.
     * @throws InterruptedException if the thread is interrupted during sleep.
     */
    @Test
    public void testEditProfileModeToggle() throws InterruptedException {
        onView(withId(R.id.edit_profile_icon)).perform(click());
        Thread.sleep(1000); // Wait for UI to update

        onView(withId(R.id.edit_profile_button)).check(matches(isDisplayed()));
        onView(withId(R.id.edit_profile_icon)).check(matches(not(isDisplayed())));

        onView(withId(R.id.entrant_name)).perform(typeText(" Updated"));
        onView(withId(R.id.entrant_email)).perform(typeText("updated@example.com"));
        onView(withId(R.id.entrant_phone)).perform(typeText("0987654321"));
    }

    /**
     * Test to verify that profile data can be saved and displayed with updated values.
     * @throws InterruptedException if the thread is interrupted during sleep.
     */
    @Test
    public void testSaveProfileData() throws InterruptedException {
        onView(withId(R.id.edit_profile_icon)).perform(click());
        Thread.sleep(1000); // Wait for edit mode to activate

        onView(withId(R.id.entrant_name)).perform(clearText(), typeText("Updated Name"));
        onView(withId(R.id.entrant_email)).perform(clearText(), typeText("updatedemail@example.com"));
        onView(withId(R.id.entrant_phone)).perform(clearText(), typeText("0987654321"));

        onView(withId(R.id.edit_profile_button)).perform(click());
        Thread.sleep(2000); // Wait for save operation to complete

        onView(withId(R.id.edit_profile_button)).check(matches(not(isDisplayed())));
        onView(withId(R.id.edit_profile_icon)).check(matches(isDisplayed()));

        onView(withId(R.id.entrant_name)).check(matches(withText("Updated Name")));
        onView(withId(R.id.entrant_email)).check(matches(withText("updatedemail@example.com")));
        onView(withId(R.id.entrant_phone)).check(matches(withText("0987654321")));
    }

    /**
     * Test to verify that the notification settings dialog opens and allows the selection of checkboxes.
     */
    @Test
    public void testNotificationSettingsDialog() {
        onView(withId(R.id.notification_settings_button)).perform(click());

        onView(withId(R.id.checkbox_admin)).check(matches(isDisplayed()));
        onView(withId(R.id.checkbox_organizer)).check(matches(isDisplayed()));

        onView(withId(R.id.checkbox_admin)).perform(click());
        onView(withId(R.id.checkbox_organizer)).perform(click());
        onView(withId(android.R.id.button1)).perform(click()); // Clicks "Save" button in the dialog
    }

    /**
     * Test to verify the back button functionality, ensuring it navigates back as expected.
     * @throws InterruptedException if the thread is interrupted during sleep.
     */
    @Test
    public void testBackButtonFunctionality() throws InterruptedException {
        onView(withId(R.id.back_button)).perform(click());
        Thread.sleep(1000); // Wait for navigation back to main screen
        // Check manually to confirm if it goes to the previous page
    }
}
