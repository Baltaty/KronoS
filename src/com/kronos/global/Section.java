 
package com.kronos.global;

 /**
 * @author TeamKronos
 * @version 1.0
  *
 */
public class Section {

    private boolean logged;
    private String userLogged;

     /**
      * Constructor.
      */
    public Section() {
    }

     /**
      *
      * @param logged
      * @param userLogged
      */
    public Section(boolean logged, String userLogged) {
        this.logged = logged;
        this.userLogged = userLogged;
    }

     /**
      *
      * @return
      */
    public String getUserLogged() {
        return userLogged;
    }

     /**
      *
      * @param userLogged
      */
    public void setUserLogged(String userLogged) {
        this.userLogged = userLogged;
    }

     /**
      *
      * @return
      */
    public boolean isLogged() {
        return logged;
    }

     /**
      *
      * @param logged
      */
    public void setLogged(boolean logged) {
        this.logged = logged;
    }

     /**
      *
      * @return
      */
    @Override
    public String toString() {
        return "Section[logged = " + logged + ", userLogged = " + userLogged + "]";
    }
}