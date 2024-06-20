package database;

import java.util.ArrayList;
/**
 * The Account class represents a bank account with associated transactions.
 */
public class Account {
    private int idaccounts;
    private int user_id;
    private String name;
    private double value;
    private ArrayList<Transaction> transactions; // Add the ArrayList of transactions
    /**
     * Constructs an Account object with the specified parameters.
     *
     * @param idaccounts The unique identifier for the account
     * @param user_id The user ID associated with the account
     * @param name The name of the account
     * @param value The initial balance of the account
     */
    public Account(int idaccounts, int user_id, String name, double value) {
        this.idaccounts = idaccounts;
        this.user_id = user_id;
        this.name = name;
        this.value = value;
        this.transactions = new ArrayList<>(); // Initialize the ArrayList
    }
    /**
     * Gets the ID of the account.
     *
     * @return The ID of the account
     */
    public int getIdaccounts() {
        return idaccounts;
    }

    /**
     * Sets the ID of the account.
     *
     * @param idaccounts The ID to set for the account
     */
    public void setIdaccounts(int idaccounts) {
        this.idaccounts = idaccounts;
    }

    /**
     * Gets the user ID associated with the account.
     *
     * @return The user ID associated with the account
     */
    public int getUser_id() {
        return user_id;
    }

    /**
     * Sets the user ID associated with the account.
     *
     * @param user_id The user ID to set for the account
     */
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    /**
     * Gets the name of the account.
     *
     * @return The name of the account
     */
    public String getName() {
        return name;
    }
    /**
     * Sets the name of the account.
     *
     * @param name The name to set for the account
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the current balance of the account.
     *
     * @return The current balance of the account
     */
    public double getValue() {
        return value;
    }

    /**
     * Sets the current balance of the account.
     *
     * @param value The balance to set for the account
     */
    public void setValue(double value) {
        this.value = value;
    }

    public ArrayList<Transaction> getTransactions() {
        this.transactions = jdbc.getTransactionsByAccountID(idaccounts);
        return transactions;
    }
    /**
     * Adds a new transaction to the list of transactions associated with the account.
     *
     * @param newTransaction The transaction to add
     */
    public void addTransaction(Transaction newTransaction) {
        transactions.add(newTransaction); // Add the new transaction to the ArrayList
        jdbc.insertTransaction(newTransaction);
    }

    /**
     * Removes a transaction from the list of transactions associated with the account.
     *
     * @param transaction The transaction to remove
     */

    public void removeTransaction(Transaction transaction) {
        transactions.remove(transaction);
        jdbc.deleteTransaction(transaction);
    }

    /**
     * Updates the details of a transaction in the list of transactions associated with the account.
     *
     * @param transaction The transaction to update
     * @param name The new name for the transaction
     * @param amount The new amount for the transaction
     * @param date The new date for the transaction
     * @param type The new type for the transaction
     */
    public void updateTransaction(Transaction transaction, String name, double amount, String date, int type) {
        transaction.setName(name);
        transaction.setAmount(amount);
        transaction.setDate(date);
        transaction.setType(type);
        jdbc.updateTransaction(transaction);
    }
    /**
     * Gets the ID for the next transaction.
     *
     * @return The ID for the next transaction
     */

    public int getNextTransactionID() {
        int transactionID;
        if (!transactions.isEmpty()) {
            transactionID = transactions.get(transactions.size() - 1).getIdtransactions() + 1;
        } else {
            // If the list is empty, set transactionID to 1 or another appropriate value
            transactionID = 1;
        }
        return transactionID;
    }
}
