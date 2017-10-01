package com.coma.go.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Koma on 26.09.2017.
 */

public class Conversation extends WebInfo implements Serializable{
    String name;
    ArrayList<String> admins;
    ArrayList<String> participants;//including admins
    ArrayList<Message> messageHistory = new ArrayList<>();
    String cid;
    public Conversation(){

    }

    public ArrayList<String> getAdmins() {
        return admins;
    }

    public void setAdmins(ArrayList<String> admins) {
        this.admins = admins;
    }

    public ArrayList<String> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<String> participants) {
        this.participants = participants;
    }

    public ArrayList<Message> getMessageHistory() {
        return messageHistory;
    }

    public void setMessageHistory(ArrayList<Message> messageHistory) {
        this.messageHistory = messageHistory;
    }

    public String getName() {
        return name;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCid() {
        return cid;
    }

    public void setName(String name) {
        this.name = name;
    }
}
