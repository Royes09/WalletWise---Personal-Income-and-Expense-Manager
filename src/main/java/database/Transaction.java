package database;

/**
 * The Transaction class represents a financial transaction within an account.
 */
public class Transaction {
    private int idtransactions;
    private int account_id;
    private String name;
    private double amount;
    private String date;
    private int type;

    /**
     * Constructs a new Transaction instance with the specified attributes.
     *
     * @param idtransactions The ID of the transaction.
     * @param account_id     The ID of the account associated with the transaction.
     * @param name           The name or description of the transaction.
     * @param amount         The amount of money involved in the transaction.
     * @param date           The date of the transaction.
     * @param type           The type of the transaction (e.g., income or expense).
     */
    public Transaction(int idtransactions, int account_id, String name, double amount, String date, int type) {
        this.idtransactions = idtransactions;
        this.account_id = account_id;
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.type = type;
    }

    /**
     * Gets the ID of the transaction.
     *
     * @return The ID of the transaction.
     */
    public int getIdtransactions() {
        return idtransactions;
    }

    /**
     * Sets the ID of the transaction.
     *
     * @param idtransactions The ID of the transaction.
     */
    public void setIdtransactions(int idtransactions) {
        this.idtransactions = idtransactions;
    }

    /**
     * Gets the ID of the account associated with the transaction.
     *
     * @return The ID of the account.
     */
    public int getAccount_id() {
        return account_id;
    }

    /**
     * Sets the ID of the account associated with the transaction.
     *
     * @param account_id The ID of the account.
     */
    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    /**
     * Gets the amount of money involved in the transaction.
     *
     * @return The amount of money.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Sets the amount of money involved in the transaction.
     *
     * @param amount The amount of money.
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Gets the date of the transaction.
     *
     * @return The date of the transaction.
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date of the transaction.
     *
     * @param date The date of the transaction.
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Gets the type of the transaction.
     *
     * @return The type of the transaction.
     */
    public int getType() {
        return type;
    }

    /**
     * Sets the type of the transaction.
     *
     * @param type The type of the transaction.
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * Gets the name or description of the transaction.
     *
     * @return The name or description of the transaction.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name or description of the transaction.
     *
     * @param name The name or description of the transaction.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the amount of money involved in the transaction.
     *
     * @return The amount of money involved in the transaction.
     */
    public double getValue() {
        return amount;
    }

    /**
     * Gets the description of the transaction.
     *
     * @return The description of the transaction.
     */
    public String getDescription() {
        return "Transaction{" +
                "idtransactions=\n" + idtransactions +
                ", account_id=" + account_id +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", date='" + date + '\'' +
                ", type=" + type +
                '}';
    }

}
