import database.Account;
import database.Transaction;
import database.User;
import database.jdbc;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HomeTest {

    @Test
    void testTransactionMatchesSearch() {
        Transaction transaction = new Transaction(1, 1, "Groceries", 50.0, "2024-02-07", 0);
        User user = new User(1, "example@example.com", "password123", "user123");
        ArrayList<Account> accounts = new ArrayList<>();
        Home home = new Home(user, accounts);

        // Test when search text matches
        assertTrue(home.transactionMatchesSearch(transaction, "groceries"));

        // Test when search text does not match
        assertFalse(home.transactionMatchesSearch(transaction, "shopping"));
    }

//    @Test
//    void testUpdateTransactionList() {
//        User user = new User(1, "example@example.com", "password123", "user123");
//        ArrayList<Transaction> transactions = new ArrayList<>();
//        transactions.add(new Transaction(1, 1, "Groceries", 50.0, "2024-02-07", 0));
//        transactions.add(new Transaction(2, 1, "Salary", 2000.0, "2024-02-01", 1));
//        Account account = new Account(1, 1, "Savings", 0.0);
//        account.addTransaction(transactions.get(0));
//        account.addTransaction(transactions.get(1));
//        ArrayList<Account> accounts = new ArrayList<>();
//        accounts.add(account);
//
//        Home home = new Home(user, accounts);
//        home.updateTransactionList(account);
//        int x = home.transactionListModel.getSize();
//        account.removeTransaction(transactions.get(0));
//        account.removeTransaction(transactions.get(1));
//        jdbc.deleteTransaction(transactions.get(0));
//        jdbc.deleteTransaction(transactions.get(1));
//
//        assertEquals(2, x);
//    }

    @Test
    void testDateLabelFormatter() throws ParseException {
        Home.DateLabelFormatter formatter = new Home.DateLabelFormatter();
        assertEquals("2024-02-07", formatter.valueToString(formatter.stringToValue("2024-02-07")));
    }
}
