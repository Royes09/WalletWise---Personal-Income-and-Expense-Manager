package database;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TransactionTest {

    @Test
    public void testTransactionInitialization() {
        // Create a new Transaction object
        Transaction transaction = new Transaction(1, 1001, "Groceries", 50.0, "2024-02-06", 1);

        // Verify the attributes are set correctly
        assertEquals(1, transaction.getIdtransactions());
        assertEquals(1001, transaction.getAccount_id());
        assertEquals("Groceries", transaction.getName());
        assertEquals(50.0, transaction.getAmount());
        assertEquals("2024-02-06", transaction.getDate());
        assertEquals(1, transaction.getType());
    }

    @Test
    public void testSettersAndGetters() {
        // Create a new Transaction object
        Transaction transaction = new Transaction(1, 1001, "Groceries", 50.0, "2024-02-06", 1);

        // Test setters and getters
        transaction.setIdtransactions(2);
        assertEquals(2, transaction.getIdtransactions());

        transaction.setAccount_id(1002);
        assertEquals(1002, transaction.getAccount_id());

        transaction.setName("Clothing");
        assertEquals("Clothing", transaction.getName());

        transaction.setAmount(75.0);
        assertEquals(75.0, transaction.getAmount());

        transaction.setDate("2024-02-07");
        assertEquals("2024-02-07", transaction.getDate());

        transaction.setType(2);
        assertEquals(2, transaction.getType());
    }

    @Test
    public void testDescription() {
        // Create a new Transaction object
        Transaction transaction = new Transaction(1, 1001, "Groceries", 50.0, "2024-02-06", 1);

        // Test the getDescription method
        String expectedDescription = "Transaction{idtransactions=1, account_id=1001, name='Groceries', amount=50.0, date='2024-02-06', type=1}";
        assertEquals(expectedDescription, transaction.getDescription());
    }

    @Test
    public void testGetValue() {
        // Create a new Transaction object
        Transaction transaction = new Transaction(1, 1001, "Groceries", 50.0, "2024-02-06", 1);

        // Test the getValue method
        assertEquals(50.0, transaction.getValue());
    }
}
