package com.coma.go.Entity;

import java.io.Serializable;

/**
 * Created by Koma on 26.09.2017.
 */

public class Dialog extends WebInfo implements Serializable{
    String name;
    String userId;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
