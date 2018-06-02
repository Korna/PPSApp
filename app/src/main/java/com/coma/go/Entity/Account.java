package com.coma.go.Entity;

import java.io.Serializable;

/**
 * Created by Koma on 26.09.2017.
 */

public class Account extends WebInfo implements Serializable{
    String email;
    String password;
    boolean admin;
    boolean access;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isAccess() {
        return access;
    }

    public void setAccess(boolean access) {
        this.access = access;
    }
}
