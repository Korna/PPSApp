package com.coma.go.Entity;

import java.io.Serializable;

/**
 * Created by Koma on 25.09.2017.
 */

public class Profile implements Serializable {
    String _id;
    String username;
    String email;
    String city;
    String description;
    String image;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Profile(String username, String city, String description, String image) {
        this.username = username;
        this.city = city;
        this.description = description;
        this.image = image;
    }

    public String getImage() {
        return image;
    }



    public String get_id() {
        return _id;
    }

    public void set_id(String id) {
        this._id = id;
    }
}
