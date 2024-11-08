package com.example.coffee2_app;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.intent.Intents.intended;

import android.util.Log;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.*;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import org.junit.Rule;
import org.junit.Test;


@LargeTest
public class OrganizerUITest {

    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new ActivityScenarioRule<MainActivity>(MainActivity.class);

    @Test
    public void testOrganizerHome() {
        Intents.init();

        // Need to sleep to properly finish loading program
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            Log.e("Debug", e.toString());
        }

        onView(withId(R.id.button_enter_organizer)).perform(click());

        // Check if intended activity was launched
        Intents.intended(hasComponent(OrganizerHomeActivity.class.getName()));

        Intents.release();
    }


    @Test
    public void testOrganizerProfile() {
        // Need to sleep to properly finish loading program
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            Log.e("Debug", e.toString());
        }

        onView(withId(R.id.button_enter_organizer)).perform(click());

        onView(withId(R.id.organizer_navigation_profile)).perform(click());

        // Check to see if the Organizer image shows, and thus the Fragment is switched.
        onView(withId(R.id.organizer_image)).check(matches(isDisplayed()));
    }

    @Test public void testProfileContextMenu() {
        // Need to sleep to properly finish loading program
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            Log.e("Debug", e.toString());
        }

        onView(withId(R.id.button_enter_organizer)).perform(click());

        onView(withId(R.id.organizer_navigation_profile)).perform(click());

        onView(withId(R.id.edit_profile_button)).perform(click());

        onView(withId(R.id.organizer_image)).perform(click());

        // Test to see if the context picker opens properly
        onView(withText("Remove Image")).check(matches(isDisplayed()));
    }

    @Test
    public void testOrganizerProfileEdits() {
        // Need to sleep to properly finish loading program
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            Log.e("Debug", e.toString());
        }

        onView(withId(R.id.button_enter_organizer)).perform(click());

        onView(withId(R.id.organizer_navigation_profile)).perform(click());

        // Check if the Save button is hidden
        onView(withId(R.id.save_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

        onView(withId(R.id.edit_profile_button)).perform(click());

        // Check to see if Save button is now visible
        onView(withId(R.id.save_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        onView(withId(R.id.organizer_name)).perform(clearText());
        onView(withId(R.id.organizer_name)).perform(ViewActions.typeText("Test"));

        onView(withId(R.id.organizer_email)).perform(clearText());
        onView(withId(R.id.organizer_email)).perform(ViewActions.typeText("test@email.com"));

        onView(withId(R.id.organizer_address)).perform(clearText());
        onView(withId(R.id.organizer_address)).perform(ViewActions.typeText("Address 123 Street"));

        onView(withId(R.id.save_button)).perform(click());

        onView(withText("Test")).check(matches(isDisplayed()));
        onView(withText("test@email.com")).check(matches(isDisplayed()));
        onView(withText("Address 123 Street")).check(matches(isDisplayed()));
    }

    @Test
    public void testOrganizerAddEvent() {
        // Need to sleep to properly finish loading program
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            Log.e("Debug", e.toString());
        }

        onView(withId(R.id.button_enter_organizer)).perform(click());

        onView(withId(R.id.organizer_navigation_addevent)).perform(click());

        // Check to see if an Add Event element shows, and thus the Fragment is switched.
        onView(withId(R.id.date_button)).check(matches(isDisplayed()));
    }

    @Test
    public void testDatePicker() {
        // Need to sleep to properly finish loading program
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            Log.e("Debug", e.toString());
        }

        onView(withId(R.id.button_enter_organizer)).perform(click());

        onView(withId(R.id.organizer_navigation_addevent)).perform(click());

        onView(withId(R.id.date_button)).perform(click());

        // Test to see if date picker properly opens
        onView(withText("OK")).check(matches(isDisplayed()));

    }

    @Test
    public void testBackButton() {
        // Need to sleep to properly finish loading program
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            Log.e("Debug", e.toString());
        }

        onView(withId(R.id.button_enter_organizer)).perform(click());

        onView(withId(R.id.back_button)).perform(click());

        // Check to see if we are sent back to main activity.
        onView(withId(R.id.button_enter_organizer)).check(matches(isDisplayed()));
    }

    /* NOT IMPLEMENTED YET
        @Test
        public void testMyEvents() {
            // Need to sleep to properly finish loading program
            try {
             Thread.sleep(1000);
            } catch (Exception e) {
                 Log.e("Debug", e.toString());
            }

            onView(withId(R.id.button_enter_organizer)).perform(click());

            onView(withId(R.id.organizer_navigation_profile)).perform(click());

            onView(withId(R.id.organizer_navigation_home)).perform(click());

            // Check to see if an RecyclerView shows, and thus the Fragment is switched.
            onView(withId(R.id.view_event_list)).check(matches(isDisplayed()));
        }
     */

}
