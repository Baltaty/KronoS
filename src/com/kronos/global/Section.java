 
package com.kronos.global;

 /**
 * @author TeamKronos
 * 
 * Version 1.0
 */
public class Section {

    private boolean logged;
    private String userLogged;

    public Section() {
    }

    public Section(boolean logged, String userLogged) {
        this.logged = logged;
        this.userLogged = userLogged;
    }

    public String getUserLogged() {
        return userLogged;
    }

    public void setUserLogged(String userLogged) {
        this.userLogged = userLogged;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    @Override
    public String toString() {
        return "Section[logged = " + logged + ", userLogged = " + userLogged + "]";
    }
}