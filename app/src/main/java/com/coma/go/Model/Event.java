package com.coma.go.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Koma on 25.09.2017.
 */

public class Event extends WebInfo implements Serializable {

    String author_id;
    String photo_id;
    public ArrayList<String> participants = new ArrayList<>();
    String name;
    String description;
    String time;
    String category;

    //ArrayList<String> moderatorsList;


    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(String photo_id) {
        this.photo_id = photo_id;
    }

    public ArrayList<String> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<String> participants) {
        this.participants = participants;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
