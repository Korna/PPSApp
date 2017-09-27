package com.coma.go.Service;

import com.coma.go.Model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.coma.go.Misc.Constants.FB_DIRECTORY_USERS;

/**
 * Created by Koma on 27.09.2017.
 */

public class FBIO {

    public static User createUserInfo(String uid, User user){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(FB_DIRECTORY_USERS);
        ref.child(uid).setValue(user);
        return user;
    }

}
