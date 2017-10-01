package com.coma.go.Model;

import java.io.Serializable;

/**
 * Created by Koma on 26.09.2017.
 */

public class Message implements Serializable{

    private String sender;
    private String message;
    private String time;
    private boolean readed;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isReaded() {
        return readed;
    }

    public void setReaded(boolean readed) {
        this.readed = readed;
    }
}
