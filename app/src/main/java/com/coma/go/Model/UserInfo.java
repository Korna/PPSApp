package com.coma.go.Model;

import java.io.Serializable;

/**
 * Created by Koma on 26.09.2017.
 */

public class UserInfo implements Serializable{
    String uid;
    String nickname;
    String photo;
    String description;
    String city;
    public UserInfo(){}

    public UserInfo(String uid, String nickname, String photo, String description, String city) {
        this.uid = uid;
        this.nickname = nickname;
        this.photo = photo;
        this.description = description;
        this.city = city;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
