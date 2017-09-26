package com.coma.go.Model;

import java.util.ArrayList;

/**
 * Created by Koma on 26.09.2017.
 */

public class User extends WebInfo {
    ArrayList<Conversation> conversations = new ArrayList<>();
    UserInfo userInfo = new UserInfo();
}
