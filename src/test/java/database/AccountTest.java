package database;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class AccountTest{
    private Account account;

    @BeforeEach
    public void setUp() {
        // Initialize a sample account before each test
        account = new Account(1, 1001, "Savings", 1000.0);
    }

    @Test
    public void testGetNextTransactionID() {
        // Test when there are no transactions
        assertEquals(1, account.getNextTransactionID());

        // Add a transaction and test the next ID
        account.addTransaction(new Transaction(1, account.getIdaccounts(), "Deposit", 500.0, "2024-02-06", 0));
        assertEquals(2, account.getNextTransactionID());
    }

    @Test
    public void testAddTransaction() {
        // Check if the transaction list is empty initially
        assertTrue(account.getTransactions().isEmpty());

        // Add a transaction
        Transaction transaction = new Transaction(1, account.getIdaccounts(), "Withdrawal", 200.0, "2024-02-05", 1);
        account.addTransaction(transaction);

        // Check if the transaction list contains the added transaction
        ArrayList<Transaction> transactions = account.getTransactions();
        assertEquals(1, transactions.size());
        assertTrue(transactions.contains(transaction));
    }

    @Test
    public void testRemoveTransaction() {
        // Add transactions
        Transaction transaction1 = new Transaction(1, account.getIdaccounts(), "Withdrawal", 200.0, "2024-02-05", 1);
        Transaction transaction2 = new Transaction(2, account.getIdaccounts(), "Deposit", 500.0, "2024-02-06", 0);
        account.addTransaction(transaction1);
        account.addTransaction(transaction2);

        // Remove a transaction
        account.removeTransaction(transaction1);

        // Check if the transaction list no longer contains the removed transaction
        ArrayList<Transaction> transactions = account.getTransactions();
        assertEquals(1, transactions.size());
        assertFalse(transactions.contains(transaction1));
    }

    @Test
    public void testUpdateTransaction() {
        // Add a transaction
        Transaction transaction = new Transaction(1, account.getIdaccounts(), "Withdrawal", 200.0, "2024-02-05", 1);
        account.addTransaction(transaction);

        // Update the transaction
        account.updateTransaction(transaction, "Deposit", 500.0, "2024-02-06", 0);

        // Check if the transaction details are updated
        ArrayList<Transaction> transactions = account.getTransactions();
        assertEquals("Deposit", transactions.get(0).getName());
        assertEquals(500.0, transactions.get(0).getAmount());
        assertEquals("2024-02-06", transactions.get(0).getDate());
        assertEquals(0, transactions.get(0).getType());
    }
}
