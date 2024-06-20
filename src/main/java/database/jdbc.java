package database;

import java.sql.*;
import java.util.ArrayList;

/**
 * The jdbc class provides methods to interact with the database.
 */
public class jdbc {
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/walletwise";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASS = "cosmin";

    /**
     * Method to create shema and tables if they don't exist
     */
    public static void createSchemaIfNotExist() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/", DB_USERNAME, DB_PASS);

            // Check if the database exists
            PreparedStatement checkSchemaStatement = connection.prepareStatement("SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = ?");
            checkSchemaStatement.setString(1, "walletwise");
            ResultSet resultSet = checkSchemaStatement.executeQuery();

            if (!resultSet.next()) {
                // Create the database if it doesn't exist
                PreparedStatement createSchemaStatement = connection.prepareStatement("CREATE DATABASE walletwise");
                createSchemaStatement.executeUpdate();
            }

            // Switch to the 'walletwise' database
            connection.setCatalog("walletwise");

            // Check if the 'accounts' table exists
            PreparedStatement checkAccountsTableStatement = connection.prepareStatement("SHOW TABLES LIKE 'accounts'");
            resultSet = checkAccountsTableStatement.executeQuery();

            if (!resultSet.next()) {
                // Create the 'accounts' table if it doesn't exist
                PreparedStatement createAccountsTableStatement = connection.prepareStatement(
                        "CREATE TABLE accounts (" +
                                "idaccounts INT NOT NULL AUTO_INCREMENT, " +
                                "user_id INT NOT NULL, " +
                                "name VARCHAR(45) NOT NULL, " +
                                "value DECIMAL(10,2) UNSIGNED ZEROFILL NOT NULL, " +
                                "PRIMARY KEY (idaccounts)" +
                                ")"
                );
                createAccountsTableStatement.executeUpdate();
            }

            // Check if the 'users' table exists
            PreparedStatement checkUsersTableStatement = connection.prepareStatement("SHOW TABLES LIKE 'users'");
            resultSet = checkUsersTableStatement.executeQuery();

            if (!resultSet.next()) {
                // Create the 'users' table if it doesn't exist
                PreparedStatement createUsersTableStatement = connection.prepareStatement(
                        "CREATE TABLE users (" +
                                "user_id INT NOT NULL AUTO_INCREMENT, " +
                                "email VARCHAR(45) NOT NULL, " +
                                "password VARCHAR(45) NOT NULL, " +
                                "nickname VARCHAR(45) NOT NULL, " +
                                "PRIMARY KEY (user_id)" +
                                ")"
                );
                createUsersTableStatement.executeUpdate();
            }

            // Check if the 'transactions' table exists
            PreparedStatement checkTransactionsTableStatement = connection.prepareStatement("SHOW TABLES LIKE 'transactions'");
            resultSet = checkTransactionsTableStatement.executeQuery();

            if (!resultSet.next()) {
                // Create the 'transactions' table if it doesn't exist
                PreparedStatement createTransactionsTableStatement = connection.prepareStatement(
                        "CREATE TABLE transactions (" +
                                "idtransactions INT NOT NULL AUTO_INCREMENT, " +
                                "account_id INT NOT NULL, " +
                                "name VARCHAR(45) NOT NULL, " +
                                "amount DECIMAL(10,2) NOT NULL, " +
                                "date DATE NOT NULL, " +
                                "type INT NOT NULL, " +
                                "PRIMARY KEY (idtransactions)" +
                                ")"
                );
                createTransactionsTableStatement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    /**
     * Validates user login credentials.
     *
     * @param email    The user's email
     * @param password The user's password
     * @return The User object if login is successful, otherwise null
     */
    public static User validateLogin(String email, String password) {
        Crypto crypto = new Crypto();
        String encrypted = crypto.encrypt(password);
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users WHERE email = ? AND password = ?"
            );
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, encrypted);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                String nickname = resultSet.getString("nickname");
                if (!hasDefaultAccount(userId)) {
                    createDefaultAccount(userId, nickname);
                }
                return new User(userId, email, password, nickname);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Checks if a user has a default account.
     *
     * @param userId The user ID
     * @return true if the user has a default account, otherwise false
     */
    private static boolean hasDefaultAccount(int userId) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM accounts WHERE user_id = ?"
            );
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Creates a default account for a user.
     *
     * @param userId   The user ID
     * @param nickname The user's nickname
     */
    private static void createDefaultAccount(int userId, String nickname) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO accounts(user_id,name,value) VALUES(?,?,0)"
            );
            preparedStatement.setInt(1, userId);
            String accout_name = "Current accout for " + nickname;
            System.out.println(accout_name);
            preparedStatement.setString(2, accout_name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if an email already exists in the database.
     *
     * @param email The email to check
     * @return true if the email exists, otherwise false
     */
    private static boolean checkEmail(String email) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users WHERE email = ?"
            );
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Checks if a nickname already exists in the database.
     *
     * @param nickname The nickname to check
     * @return true if the nickname exists, otherwise false
     */
    private static boolean checkNickname(String nickname) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users WHERE nickname = ?"
            );
            preparedStatement.setString(1, nickname);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Registers a new user.
     *
     * @param nickname The user's nickname
     * @param email    The user's email
     * @param password The user's password
     * @return 1 for successful registration, 2 if nickname already exists, 3 if email already exists, 0 for failure
     */
    public static int register(String nickname, String email, String password) {
        Crypto crypto = new Crypto();
        String encrypted = crypto.encrypt(password);
        boolean valid = false;
        try {
            if (!checkEmail(email) && !checkNickname(nickname)) {
                Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);

                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO users(email, password, nickname)" +
                                "VALUES(?, ?, ?)"
                );
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, encrypted);
                preparedStatement.setString(3, nickname);

                preparedStatement.executeUpdate();
                return 1;
            } else if (checkNickname(nickname)) {
                return 2;
            } else {
                return 3;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Retrieves accounts associated with a user.
     *
     * @param userid The user ID
     * @return An ArrayList of Account objects associated with the user
     */
    public static ArrayList<Account> getAccountsByUserId(int userid) {
        ArrayList<Account> accounts = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM accounts WHERE user_id = ?"
            );
            preparedStatement.setInt(1, userid);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int accountId = resultSet.getInt("idaccounts");
                String accountName = resultSet.getString("name");
                double value = resultSet.getDouble("value");

                Account account = new Account(accountId, userid, accountName, value);

                accounts.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    /**
     * Retrieves transactions associated with an account.
     *
     * @param id The account ID
     * @return An ArrayList of Transaction objects associated with the account
     */
    public static ArrayList<Transaction> getTransactionsByAccountID(int id) {
        ArrayList<Transaction> transactions = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM transactions WHERE account_id = ? ORDER BY date"
            );
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int transactionId = resultSet.getInt("idtransactions");
                String name = resultSet.getString("name");
                double value = resultSet.getDouble("amount");
                String date = resultSet.getString("date");
                int type = resultSet.getInt("type");
                Transaction account = new Transaction(transactionId, id, name, value, date, type);

                transactions.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    /**
     * Inserts a new transaction into the database.
     *
     * @param transaction The transaction to insert
     */
    public static void insertTransaction(Transaction transaction) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO transactions (account_id, name, amount, date, type)" +
                            "VALUES (?, ?, ?, ?, ?)"
            );
            preparedStatement.setInt(1, transaction.getAccount_id());
            preparedStatement.setString(2, transaction.getName());
            preparedStatement.setDouble(3, transaction.getAmount());
            preparedStatement.setString(4, transaction.getDate());
            preparedStatement.setInt(5, transaction.getType());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
    }

    /**
     * Deletes a transaction from the database.
     *
     * @param transaction The transaction to delete
     */
    public static void deleteTransaction(Transaction transaction) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM transactions WHERE idtransactions=?"
            );
            preparedStatement.setInt(1, transaction.getIdtransactions());
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Transaction deleted successfully.");
            } else {
                System.out.println("Transaction not found or could not be deleted.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
    }

    /**
     * Deletes an account from the database.
     *
     * @param accountId The ID of the account to delete
     */
    public static void deleteAccount(int accountId) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);
            PreparedStatement preparedStatementDeleteTransactions = connection.prepareStatement(
                    "DELETE FROM transactions WHERE account_id=?"
            );
            preparedStatementDeleteTransactions.setInt(1, accountId);
            int rowsAffectedT = preparedStatementDeleteTransactions.executeUpdate();

            PreparedStatement preparedStatementDeleteAccount = connection.prepareStatement(
                    "DELETE FROM accounts WHERE idaccounts=?"
            );
            preparedStatementDeleteAccount.setInt(1, accountId);
            int rowsAffectedA = preparedStatementDeleteAccount.executeUpdate();

            if (rowsAffectedT > 0) {
                System.out.println("Transactions deleted successfully.");
            } else {
                System.out.println("Transactions not found or could not be deleted.");
            }
            if (rowsAffectedA > 0) {
                System.out.println("Account deleted successfully.");
            } else {
                System.out.println("Account not found or could not be deleted.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
    }

    /**
     * Updates a transaction in the database.
     *
     * @param transaction The transaction to update
     */
    public static void updateTransaction(Transaction transaction) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE transactions SET name=?, amount=?, date=?, type=? WHERE idtransactions= ?"
            );
            preparedStatement.setString(1, transaction.getName());
            preparedStatement.setDouble(2, transaction.getAmount());
            preparedStatement.setString(3, transaction.getDate());
            preparedStatement.setInt(4, transaction.getType());
            preparedStatement.setInt(5, transaction.getIdtransactions());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Transaction updated successfully.");
            } else {
                System.out.println("Transaction not found or could not be updated.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
    }

    /**
     * Adds a new account for a user.
     *
     * @param userId      The user ID
     * @param accountName The name of the new account
     * @return The newly added Account object, or null if the addition fails
     */
    public static Account addNewAccount(int userId, String accountName) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO accounts(user_id,name,value) VALUES(?,?,0)"
            );
            PreparedStatement retrieveStatement = connection.prepareStatement(
                    "SELECT * FROM accounts WHERE user_id = ? AND name = ?"
            );
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, accountName);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                // Retrieve the newly added account
                retrieveStatement.setInt(1, userId);
                retrieveStatement.setString(2, accountName);
                ResultSet resultSet = retrieveStatement.executeQuery();

                if (resultSet.next()) {
                    int idaccounts = resultSet.getInt("idaccounts");
                    double value = resultSet.getDouble("value");
                    return new Account(idaccounts, userId, accountName, value);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}