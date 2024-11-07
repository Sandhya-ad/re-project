package com.example.coffee2_app;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityButtonTest {


    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new ActivityScenarioRule<>(MainActivity.class);


    /**
     * Tests if the Entrant button is displayed and clickable.
     */
    @Test
    public void testEntrantButtonIsDisplayedAndClickable() throws InterruptedException {
        // Verify that the Entrant button is displayed
        onView(withId(R.id.button_enter_entrant)).check(matches(isDisplayed()));


        // Verify that the Entrant button is clickable
        onView(withId(R.id.button_enter_entrant)).check(matches(isClickable()));


        // Perform a click on the Entrant button
        onView(withId(R.id.button_enter_entrant)).perform(click());
    }


    /**
     * Tests if the Organizer button is displayed and clickable.
     */
    @Test
    public void testOrganizerButtonIsDisplayedAndClickable() throws InterruptedException {
        // Verify that the Organizer button is displayed
        onView(withId(R.id.button_enter_organizer)).check(matches(isDisplayed()));


        // Verify that the Organizer button is clickable
        onView(withId(R.id.button_enter_organizer)).check(matches(isClickable()));

    }


    /**
     * Tests if the Admin button is displayed and clickable.
     * Note: If the Admin button is sometimes hidden, you may need to add a condition in the test.
     */
    @Test
    public void testAdminButtonIsDisplayedAndClickable() {
        //Will fail if the user is not admin
        // Verify that the Admin button is displayed
        onView(withId(R.id.button_enter_admin)).check(matches(isDisplayed()));


        // Verify that the Admin button is clickable
        onView(withId(R.id.button_enter_admin)).check(matches(isClickable()));


        // Perform a click on the Admin button
        onView(withId(R.id.button_enter_admin)).perform(click());
    }
}

