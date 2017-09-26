package com.coma.go.Model;

import java.util.ArrayList;

/**
 * Created by Koma on 26.09.2017.
 */

public class Conversation extends WebInfo{
    ArrayList<String> admins;
    ArrayList<String> participants;//including admins
    ArrayList<Message> messageHistory = new ArrayList<>();

}
