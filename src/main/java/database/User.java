package database;

/**
 * The User class represents a user in the system with basic information such as user ID, email, password, and nickname.
 */
public class User {

    /**
     * The unique identifier for the user.
     */
    private int user_id;

    /**
     * The email address of the user.
     */
    private String email;

    /**
     * The encrypted password of the user.
     */
    private String password;

    /**
     * The nickname chosen by the user.
     */
    private String nickname;

    /**
     * Constructs a new User with the specified attributes.
     *
     * @param user_id  The unique identifier for the user.
     * @param email    The email address of the user.
     * @param password The encrypted password of the user.
     * @param nickname The nickname chosen by the user.
     */
    public User(int user_id, String email, String password, String nickname) {
        this.user_id = user_id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    /**
     * Gets the unique identifier of the user.
     *
     * @return The user ID.
     */
    public int getUser_id() {
        return user_id;
    }

    /**
     * Sets the user ID.
     *
     * @param user_id The new user ID.
     */
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    /**
     * Gets the email address of the user.
     *
     * @return The user's email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email The new email address.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the encrypted password of the user.
     *
     * @return The encrypted password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the encrypted password of the user.
     *
     * @param password The new encrypted password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the nickname chosen by the user.
     *
     * @return The user's nickname.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Sets the nickname chosen by the user.
     *
     * @param nickname The new nickname.
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    /**
     * Adds a new account for the user with the given account name.
     *
     * @param accountName The name of the new account.
     * @return The newly created Account object.
     */
    public Account addNewAccount(String accountName) {
        Account newAccount = jdbc.addNewAccount(user_id, accountName);
        return newAccount;
    }
    /**
     * Deletes the account with the specified ID.
     *
     * @param accountId The ID of the account to be deleted.
     */
    public void deleteAccount(int accountId) {
        jdbc.deleteAccount(accountId);
    }
}
