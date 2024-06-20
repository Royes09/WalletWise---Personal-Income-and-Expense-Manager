package database;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class CryptoTest {

    @Test
    public void testEncryptDecrypt() {
        // Create a Crypto instance
        Crypto crypto = new Crypto();

        // Test encryption and decryption for a sample string
        String originalString = "Hello, world!";
        String encryptedString = crypto.encrypt(originalString);
        assertNotNull(encryptedString);
        assertNotEquals(originalString, encryptedString);

        String decryptedString = crypto.decrypt(encryptedString);
        assertNotNull(decryptedString);
        assertEquals(originalString, decryptedString);
    }

    @Test
    public void testEncryptDecryptEmptyString() {
        // Create a Crypto instance
        Crypto crypto = new Crypto();

        // Test encryption and decryption for an empty string
        String originalString = "";
        String encryptedString = crypto.encrypt(originalString);
        assertNotNull(encryptedString);

        String decryptedString = crypto.decrypt(encryptedString);
        assertNotNull(decryptedString);
        assertEquals(originalString, decryptedString);
    }

    @Test
    public void testEncryptDecryptNullString() {
        // Create a Crypto instance
        Crypto crypto = new Crypto();

        // Test encryption and decryption for a null string
        String originalString = null;
        String encryptedString = crypto.encrypt(originalString);
        assertNull(encryptedString); // Expecting null for encryption

        String decryptedString = crypto.decrypt(encryptedString);
        assertNull(decryptedString); // Expecting null for decryption
    }
}
