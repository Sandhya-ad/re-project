package com.example.coffee2_app;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.UUID;

public class OrganizerTest {

    private Organizer testOrganizer() { return new Organizer("testUser"); }
    private Event testEvent() { return new Event(); }

    @Test
    public void testUserID() {
        Organizer org = testOrganizer();
        assertEquals(org.getUserID(), "testUser");
    }

    @Test
    public void testName() {
        Organizer org = testOrganizer();
        org.setName("John 123");
        assertEquals(org.getName(), "John 123");
    }

    @Test
    public void testEmail() {
        Organizer org = testOrganizer();
        org.setEmail("john@test.com");
        assertEquals(org.getEmail(), "john@test.com");
    }

    @Test
    public void testAddress() {
        Organizer org = testOrganizer();
        org.setAddress("123 Test Street");
        assertEquals(org.getAddress(), "123 Test Street");
    }

    /*
    Methods are not implemented, so the tests are commented out

    @Test
    public void testAddEvent() {
        Organizer org = testsOrganizer();
        Event event = testEvent();
        org.addEvent(event);
        assertTrue(org.getEvents().contains(event));
    }

    @Test
    public void testRemoveEvent() {
        Organizer org = testOrganizer();
        Event event = testEvent();
        org.addEvent(event);
        org.removeEvent(event);
        assertFalse(org.getEvents().contains(event));
    }

     */

}
