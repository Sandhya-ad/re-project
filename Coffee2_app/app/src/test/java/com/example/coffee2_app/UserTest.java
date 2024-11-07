package com.example.coffee2_app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the User class.
 */
public class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("user123");
    }

    @Test
    public void testInitialRoles() {
        // Default roles should include "entrant" and "organizer"
        assertTrue(user.getRoles().contains("entrant"), "User should initially have entrant role");
        assertTrue(user.getRoles().contains("organizer"), "User should initially have organizer role");
    }

    @Test
    public void testAddRole() {
        user.addRole("admin");
        assertTrue(user.getRoles().contains("admin"), "User should have admin role after addition");
    }

    @Test
    public void testRemoveRole() {
        user.addRole("organizer");
        user.removeRole("organizer");
        assertFalse(user.getRoles().contains("organizer"), "User should not have organizer role after removal");
    }

    @Test
    public void testHasEntrantRole() {
        assertTrue(user.hasEntrantRole(), "User should have entrant role");
        user.removeRole("entrant");
        assertFalse(user.hasEntrantRole(), "User should not have entrant role after removal");
    }

    @Test
    public void testHasOrganizerRole() {
        assertTrue(user.hasOrganizerRole(), "User should have organizer role");
        user.removeRole("organizer");
        assertFalse(user.hasOrganizerRole(), "User should not have organizer role after removal");
    }

    @Test
    public void testSetAndCheckAdminStatus() {
        assertFalse(user.getIsAdmin(), "User should not be an admin initially");
        user.setAdmin();
        assertTrue(user.getIsAdmin(), "User should have admin privileges after calling setAdmin");
    }

    @Test
    public void testAddAndRemoveEntrantRole() {
        user.addRole("entrant");
        assertNotNull(user.getEntrant(), "User's Entrant should be set after adding entrant role");

        user.removeRole("entrant");
        assertNull(user.getEntrant(), "User's Entrant should be null after removing entrant role");
    }

    @Test
    public void testAddAndRemoveOrganizerRole() {
        user.addRole("organizer");
        assertNotNull(user.getOrganizer(), "User's Organizer should be set after adding organizer role");

        user.removeRole("organizer");
        assertNull(user.getOrganizer(), "User's Organizer should be null after removing organizer role");
    }

    @Test
    public void testGetUserId() {
        assertEquals("user123", user.getUserId(), "User ID should match the initialized ID");
    }

    @Test
    public void testGetRoles() {
        List<String> roles = user.getRoles();
        assertEquals(2, roles.size(), "User should have 2 roles initially (entrant and organizer)");
        assertTrue(roles.contains("entrant") && roles.contains("organizer"),
                "User should initially have entrant and organizer roles");
    }

    @Test
    public void testSetEntrant() {
        Entrant entrant = new Entrant("user123", "Test Entrant", "entrant@example.com");
        user.setEntrant(entrant);
        assertEquals(entrant, user.getEntrant(), "Entrant should match the set Entrant instance");
    }
}
