package com.coma.go.Entity;

import java.io.Serializable;

/**
 * Created by Koma on 26.09.2017.
 */

public class Message extends WebInfo implements Serializable{

    String text;
    String time;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
