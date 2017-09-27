package com.coma.go.Service;

import com.coma.go.Model.Event;
import com.coma.go.Model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.coma.go.Misc.Constants.*;

/**
 * Created by Koma on 27.09.2017.
 */

public class FBIO {

    public static User createUserInfo(String uid, User user){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(FB_DIRECTORY_USERS);
        ref.child(uid).setValue(user);
        return user;
    }

    public static String createEvent(Event event, String categoryName){//можно доп категории ввести в виде .child(CATEGORY_NAME)
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(FB_DIRECTORY_EVENTS);
        String key = ref.push().getKey();

        event.setId(key);
        ref.child(categoryName).child(key).setValue(event);
        return key;
    }

}
