package database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User(1, "example@example.com", "password123", "user123");
    }

    @Test
    public void testConstructor() {
        assertNotNull(user);
    }

    @Test
    public void testGetters() {
        assertEquals(1, user.getUser_id());
        assertEquals("example@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals("user123", user.getNickname());
    }

    @Test
    public void testSetters() {
        user.setUser_id(2);
        user.setEmail("new@example.com");
        user.setPassword("newPassword");
        user.setNickname("newUser");

        assertEquals(2, user.getUser_id());
        assertEquals("new@example.com", user.getEmail());
        assertEquals("newPassword", user.getPassword());
        assertEquals("newUser", user.getNickname());
    }

}
