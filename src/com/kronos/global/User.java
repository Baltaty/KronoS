 
package com.kronos.global;

 /**
 * @author TeamKronos
 * 
 * Version 1.0
 */
public class User {

    private String userName;
    private String fullName;
    private String email;
    private String password;

    public User(){

    }

    public User(String userName, String fullName, String email) {
        this.userName = userName;
        this.fullName = fullName;
        this.email = email;
    }

    public User(String userName, String fullName, String email, String password) {
        this.userName = userName;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User[userName = " +userName+ ", " +
                "fullName = " +fullName +", " +
                "email = " +email+ ", password = " + password +"]";
    }
}
