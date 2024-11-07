package com.example.coffee2_app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EntrantTest {

    // Helper method to initialize an Entrant with sample data
    private Entrant mockEntrant() {
        return new Entrant("12345", "Test User", "test@example.com");
    }

    @Test
    void testSetAndGetName() {
        Entrant entrant = mockEntrant();
        entrant.setName("New Name");
        assertEquals("New Name", entrant.getName());
    }

    @Test
    void testSetAndGetEmail() {
        Entrant entrant = mockEntrant();
        entrant.setEmail("new_email@example.com");
        assertEquals("new_email@example.com", entrant.getEmail());
    }

    @Test
    void testSetAndGetPhone() {
        Entrant entrant = mockEntrant();
        entrant.setPhone("1234567890");
        assertEquals("1234567890", entrant.getPhone());
    }

    @Test
    void testSetAndGetAdminNotification() {
        Entrant entrant = mockEntrant();
        entrant.setAdminNotification(true);
        assertTrue(entrant.getAdminNotification());

        entrant.setAdminNotification(false);
        assertFalse(entrant.getAdminNotification());
    }

    @Test
    void testSetAndGetOrganizerNotification() {
        Entrant entrant = mockEntrant();
        entrant.setOrganizerNotification(true);
        assertTrue(entrant.getOrganizerNotification());

        entrant.setOrganizerNotification(false);
        assertFalse(entrant.getOrganizerNotification());
    }
//
//    @Test
//    void testAddSignedUpEvent() {
//        Entrant entrant = mockEntrant();
//        Event event = new Event("Sample Event"); // Assuming Event has a constructor taking a name
//        entrant.addSignedUpEvent(event);
//        assertTrue(entrant.getSignedUpEvents().contains(event));
//    }
//
//    @Test
//    void testAddWaitListedEvent() {
//        Entrant entrant = mockEntrant();
//        Event event = new Event("Sample Waitlisted Event");
//        entrant.addWaitListedEvent(event);
//        assertTrue(entrant.getWaitListedEvents().contains(event));
//    }
//
//    @Test
//    void testRemoveWaitListedEvent() {
//        Entrant entrant = mockEntrant();
//        Event event = new Event("Sample Waitlisted Event");
//        entrant.addWaitListedEvent(event);
//        entrant.removeWaitListedEvent(event);
//        assertFalse(entrant.getWaitListedEvents().contains(event));
//    }
//
//    @Test
//    void testAddEventException() {
//        Entrant entrant = mockEntrant();
//        Event event = new Event("Duplicate Event");
//
//        // Add event once
//        entrant.addSignedUpEvent(event);
//
//        // Try to add the same event again and expect an exception (assuming the logic throws one)
//        assertThrows(IllegalArgumentException.class, () -> {
//            entrant.addSignedUpEvent(event);
//        });
//    }
//
//    @Test
//    void testRemoveEventException() {
//        Entrant entrant = mockEntrant();
//        Event event = new Event("Non-existent Event");
//
//        // Try to remove an event not in the list and expect an exception
//        assertThrows(IllegalArgumentException.class, () -> {
//            entrant.removeWaitListedEvent(event);
//        });
//    }
}
