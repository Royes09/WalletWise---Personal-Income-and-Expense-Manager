package database;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

public class jdbcTest {

    // Define test constants
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "testpassword";
    private static final String TEST_NICKNAME = "testuser";
    private ArrayList<Integer> createdAccounts = new ArrayList<>();
    private ArrayList<Integer> createdTransactions = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        // Clean up before each test
        createdAccounts.clear();
        createdTransactions.clear();
    }

    @AfterEach
    public void tearDown() {
        // Clean up after each test
        for (int accountId : createdAccounts) {
            jdbc.deleteAccount(accountId);
        }
        for (int transactionId : createdTransactions) {
            jdbc.deleteTransaction(new Transaction(transactionId, 0, "", 0, "", 0));
        }
    }

    @Test
    public void testValidateLogin() {
        // Test valid login
        User user = jdbc.validateLogin(TEST_EMAIL, TEST_PASSWORD);
        assertNotNull(user);
        assertEquals(TEST_EMAIL, user.getEmail());

        // Test invalid login
        user = jdbc.validateLogin("invalid@example.com", "invalidpassword");
        assertNull(user);
    }

    @Test
    public void testRegister() {
        // Test successful registration
        int registrationStatus = jdbc.register(TEST_NICKNAME, "newuser@example.com", "newpassword");
        assertEquals(1, registrationStatus);

        // Test registration with existing nickname
        registrationStatus = jdbc.register(TEST_NICKNAME, "newuser2@example.com", "newpassword2");
        assertEquals(2, registrationStatus);

        // Test registration with existing email
        registrationStatus = jdbc.register("newuser2", TEST_EMAIL, "newpassword3");
        assertEquals(3, registrationStatus);
    }

    @Test
    public void testGetAccountsByUserId() {
        // Assuming there's a user with ID 1 in the database
        ArrayList<Account> accounts = jdbc.getAccountsByUserId(1);
        assertNotNull(accounts);
        assertFalse(accounts.isEmpty());
        for (Account account : accounts) {
            createdAccounts.add(account.getIdaccounts());
        }
    }

    @Test
    public void testGetTransactionsByAccountID() {
        // Assuming there's an account with ID 1 in the database
        ArrayList<Transaction> transactions = jdbc.getTransactionsByAccountID(1);
        assertNotNull(transactions);
        assertFalse(transactions.isEmpty());
        for (Transaction transaction : transactions) {
            createdTransactions.add(transaction.getIdtransactions());
        }
    }

    @Test
    public void testInsertAndDeleteTransaction() {
        // Create a test transaction
        Transaction testTransaction = new Transaction(1, 1, "Test Transaction", 100.0, "2024-02-06", 1);

        // Insert the transaction
        jdbc.insertTransaction(testTransaction);
        createdTransactions.add(testTransaction.getIdtransactions());

        // Retrieve the transaction from the database
        ArrayList<Transaction> transactions = jdbc.getTransactionsByAccountID(1);
        assertTrue(transactions.contains(testTransaction));

        // Delete the transaction
        jdbc.deleteTransaction(testTransaction);
        createdTransactions.remove((Integer) testTransaction.getIdtransactions());

        // Check if the transaction is deleted
        transactions = jdbc.getTransactionsByAccountID(1);
        assertFalse(transactions.contains(testTransaction));
    }

    @Test
    public void testAddNewAccount() {
        // Assuming there's a user with ID 1 in the database
        Account newAccount = jdbc.addNewAccount(1, "New Account");
        assertNotNull(newAccount);
        assertEquals("New Account", newAccount.getName());
        createdAccounts.add(newAccount.getIdaccounts());
    }
}
