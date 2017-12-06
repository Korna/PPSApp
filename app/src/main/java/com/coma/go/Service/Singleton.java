package com.coma.go.Service;

import com.coma.go.Model.User;

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

    private User user;
    public void setUser(User user){
        this.user =  user;
    }

    public User getUser() {
        return user;
    }
}
