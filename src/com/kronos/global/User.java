package com.kronos.global;

/**
 * @author TeamKronos
 * @version 1.0
 */
public class User {

    private String userName;
    private String fullName;
    private String email;
    private String password;

    /**
     * Constructor.
     */
    public User() {

    }

    /**
     * Constructor.
     * @param userName username
     * @param fullName full name
     * @param email email
     */
    public User(String userName, String fullName, String email) {
        this.userName = userName;
        this.fullName = fullName;
        this.email = email;
    }

    /**
     * Constructor.
     * @param userName username
     * @param fullName full name
     * @param email email
     * @param password password
     */
    public User(String userName, String fullName, String email, String password) {
        this.userName = userName;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }

    /**
     * Gets the full name.
     * @return full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the full name
     * @param fullName full name
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Gets the email.
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email.
     * @param email email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the username.
     * @return username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the username.
     * @param userName username
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets password.
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     * @param password password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Converts the credentials to a single string.
     * @return full credentials
     */
    @Override
    public String toString() {
        return "User[userName = " + userName + ", " +
                "fullName = " + fullName + ", " +
                "email = " + email + ", password = " + password + "]";
    }
}
