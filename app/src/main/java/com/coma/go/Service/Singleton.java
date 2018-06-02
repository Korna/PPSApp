package com.coma.go.Service;

import com.coma.go.Entity.Account;

/**
 * Created by Koma on 26.09.2017.
 */

public class Singleton {
    private static final Singleton ourInstance = new Singleton();

    public static Singleton getInstance() {
        return ourInstance;
    }

    private Singleton() {
    }

    private Account user;
    public void setUser(Account user){
        this.user =  user;
    }

    public Account getUser() {
        return user;
    }
}
