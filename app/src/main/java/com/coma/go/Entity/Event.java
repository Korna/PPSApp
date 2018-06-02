package com.coma.go.Entity;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Koma on 25.09.2017.
 */

public class Event implements Serializable{

   // String idAuthor;

    String _id;
    Profile authorId;
    String name;
    String text;
    String category;
    double longitude;
    double latitude;
    ArrayList<String> members;
    private String image;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }

    public String getImage() {
        return image;
    }

    public Profile getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Profile authorId) {
        this.authorId = authorId;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
