package com.coma.go.Entity;

import java.io.Serializable;

/**
 * Created by Koma on 26.09.2017.
 */

public class Message implements Serializable{
    String _id;
    String dialogId;
    String senderId;


    String text;
    String timeSent;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDialogId() {
        return dialogId;
    }

    public void setDialogId(String dialogId) {
        this.dialogId = dialogId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(String timeSent) {
        this.timeSent = timeSent;
    }
}
