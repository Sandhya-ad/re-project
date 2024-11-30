package com.example.coffee2_app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Entrant class.
 */
class EntrantTest {

    /**
     * Helper method to initialize an Entrant instance with sample data.
     * @return Entrant object with sample data.
     */
    private Entrant mockEntrant() {
        return new Entrant("12345", "Test User", "test@example.com");
    }

    /**
     * Test the setName and getName methods to ensure Entrant's name can be set and retrieved correctly.
     */
    @Test
    void testSetAndGetName() {
        Entrant entrant = mockEntrant();
        entrant.setName("New Name");
        assertEquals("New Name", entrant.getName(), "Entrant's name should match the updated name");
    }

    /**
     * Test the setEmail and getEmail methods to ensure Entrant's email can be set and retrieved correctly.
     */
    @Test
    void testSetAndGetEmail() {
        Entrant entrant = mockEntrant();
        entrant.setEmail("new_email@example.com");
        assertEquals("new_email@example.com", entrant.getEmail(), "Entrant's email should match the updated email");
    }

    /**
     * Test the setPhone and getPhone methods to ensure Entrant's phone number can be set and retrieved correctly.
     */
    @Test
    void testSetAndGetPhone() {
        Entrant entrant = mockEntrant();
        entrant.setPhone("1234567890");
        assertEquals("1234567890", entrant.getPhone(), "Entrant's phone number should match the updated phone number");
    }

    /**
     * Test the setAdminNotification and getAdminNotification methods to verify
     * the admin notification preference can be set and retrieved.
     */
    @Test
    void testSetAndGetAdminNotification() {
        Entrant entrant = mockEntrant();
        entrant.setAdminNotification(true);
        assertTrue(entrant.getAdminNotification(), "Admin notification should be true after setting it to true");

        entrant.setAdminNotification(false);
        assertFalse(entrant.getAdminNotification(), "Admin notification should be false after setting it to false");
    }

    /**
     * Test the setOrganizerNotification and getOrganizerNotification methods to verify
     * the organizer notification preference can be set and retrieved.
     */
    @Test
    void testSetAndGetOrganizerNotification() {
        Entrant entrant = mockEntrant();
        entrant.setOrganizerNotification(true);
        assertTrue(entrant.getOrganizerNotification(), "Organizer notification should be true after setting it to true");

        entrant.setOrganizerNotification(false);
        assertFalse(entrant.getOrganizerNotification(), "Organizer notification should be false after setting it to false");
    }

}
