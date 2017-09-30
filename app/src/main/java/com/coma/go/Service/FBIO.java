package com.coma.go.Service;

import android.util.Log;

import com.coma.go.Custom.EventAdapter;
import com.coma.go.Model.Conversation;
import com.coma.go.Model.Event;
import com.coma.go.Model.User;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.coma.go.Misc.Constants.*;

/**
 * Created by Koma on 27.09.2017.
 */

public class FBIO {


    public static ArrayList<Conversation> getUserConversation(String uid){
        ArrayList<Conversation> arrayList = new ArrayList<>();


        return  arrayList;
    }

    public static void setConversation(){

    }

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

    public static ArrayList<Event> getMyEvents(String uid){
        final ArrayList<Event> eventList = new ArrayList<>();

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(FB_DIRECTORY_USERS);

            ref.child(uid).child("participation").addListenerForSingleValueEvent(//глобальный и постоянный прослушиватель всех данных marks
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                Event object = snapshot.getValue(Event.class);
                                eventList.add(object);
                                Log.v("received&added", object.toString());
                            }

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });


        return eventList;
    }

    public static ArrayList<String> getMyCids(String uid){
        final ArrayList<String> eventList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(FB_DIRECTORY_USERS);

        ref.child(uid).child("conversations").addListenerForSingleValueEvent(//глобальный и постоянный прослушиватель всех данных marks
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                            String string = snapshot.getValue(String.class);
                            eventList.add(string);
                            Log.v("received&added", string.toString());
                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });


        return eventList;
    }

    public static ArrayList<Event> getEvents(String category){
        final ArrayList<Event> eventList = new ArrayList<>();

        DatabaseReference ref = null;
        ref = FirebaseDatabase.getInstance().getReference(FB_DIRECTORY_EVENTS);

        ref.child(category).addListenerForSingleValueEvent(//глобальный и постоянный прослушиватель всех данных marks
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                            Event object = snapshot.getValue(Event.class);
                            eventList.add(object);
                            Log.v("received&added", object.toString());

                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });


        return eventList;
    }

    public static void deleteEvent(String uid, String category, final String key){
        DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference();


        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();


        Task deleteTaskGlobal = ref.child(FB_DIRECTORY_EVENTS).child(category).child(key).removeValue();




        ref.child(FB_DIRECTORY_USERS).addListenerForSingleValueEvent(//глобальный и постоянный прослушиватель всех данных marks
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //TODO make query deletion
                        //DatabaseReference refUser = FirebaseDatabase.getInstance().getReference().child(FB_DIRECTORY_USERS);

                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){//TODO delete only from participants
                            User object = snapshot.getValue(User.class);
                            String uid = object.userInfo.getUid();

                            for(Event event : object.participation)
                                if(event.getId().equals(key)){
                                    object.participation.remove(event);
                                    FBIO.createUserInfo(uid, object);
                                }

                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });

    }



}
