package com.coma.go.Model;

import java.util.ArrayList;

/**
 * Created by Koma on 26.09.2017.
 */

public class User extends WebInfo {
    ArrayList<Conversation> conversations = new ArrayList<>();
    public UserInfo userInfo = new UserInfo();
    ArrayList<Event> participation = new ArrayList<>();
    public User(){

    }


    public User(UserInfo userInfo) {
        this.userInfo = userInfo;
        setId(userInfo.getUid());
    }

    public ArrayList<Event> getParticipation() {
        return participation;
    }
}
