package com.coma.go.Service;

import android.support.annotation.NonNull;
import android.util.Log;

import com.coma.go.Custom.EventAdapter;
import com.coma.go.Model.Conversation;
import com.coma.go.Model.Event;
import com.coma.go.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.coma.go.Misc.Constants.*;

/**
 * Created by Koma on 27.09.2017.
 */

public class FBIO {

    /**  создание аккаунта   */
    public static User createUserInfo(String uid, User user){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(FB_DIRECTORY_USERS);
        ref.child(uid).setValue(user);
        return user;
    }
    /** создание ивента в категории */
    public static String createEvent(Event event, String categoryName){//можно доп категории ввести в виде .child(CATEGORY_NAME)
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(FB_DIRECTORY_EVENTS);
        String key = ref.push().getKey();

        event.setId(key);
        ref.child(categoryName).child(key).setValue(event);
        return key;
    }

    public static Conversation createConversation(ArrayList<String> participants
            //, ArrayList<String> admins
    ){
        Conversation conversation = new Conversation();

        DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference(FB_DIRECTORY_CONVERSATIONS);
        String key = ref.push().getKey();

        conversation.setCid(key);
        //conversation.setId(key);
        conversation.setParticipants(participants);
        ref.child(key).setValue(conversation);


        ref = FirebaseDatabase.getInstance().getReference(FB_DIRECTORY_USERS);
        for(String participant : participants){
            ref.child(participant).child(FB_DIRECTORY_CONVERSATIONS).child(key).setValue(conversation.getCid());
        }

        Log.d("Created", " Conversation");

        return conversation;
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



    static Conversation createdConversation = null;
    static boolean found = false;

    /**  пытается найти диалог. вызывает создание нового, если не найдено */
    public static TaskCompletionSource<Conversation> getActualCid(final String senderUid, final String getterUid){//для случая диалога 1vs1
        final TaskCompletionSource<Conversation> taskCompletionSource = new TaskCompletionSource<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(FB_DIRECTORY_USERS).child(senderUid).child(FB_DIRECTORY_CONVERSATIONS);//ишем у себя диалог

        ref.addListenerForSingleValueEvent(//глобальный и постоянный прослушиватель всех данных marks
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<String> listOfCids = new ArrayList<String>();

                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                            String cid = snapshot.getValue(String.class);
                            listOfCids.add(cid);

                            /*
                            *

                            Task task = getConversationByCid(cid).getTask();//TODO здесь мы получаем лишь uid



                            task.addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    conversation = (Conversation) task.getResult();

                                    for(String participants : conversation.getParticipants()){
                                        if(getterUid.equals(participants) && conversation.getParticipants().size() == 1){
                                            found = true;
                                            break;
                                        }
                                    }

                                    if(!found){
                                        ArrayList<String> list = new ArrayList<>();
                                        list.add(senderUid);
                                        list.add(getterUid);
                                        conversation = FBIO.createConversation(list);
                                    }

                                    taskCompletionSource.setResult(conversation);

                                }

                            });
                            * */
                        }

                        final Task taskConversationList = getConversationsByCids(listOfCids).getTask();

                        taskConversationList.addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {

                                ArrayList<Conversation> arrayList = (ArrayList<Conversation>) taskConversationList.getResult();
                                for(Conversation conversation : arrayList){

                                    for(String participants : conversation.getParticipants()){
                                        if(getterUid.equals(participants) && conversation.getParticipants().size() == 2){
                                            Log.d("found", "value");
                                            found = true;
                                            createdConversation = conversation;
                                            break;
                                        }
                                    }

                                }

                                if(!found){
                                    ArrayList<String> list = new ArrayList<>();
                                    list.add(senderUid);
                                    list.add(getterUid);
                                    createdConversation = FBIO.createConversation(list);
                                }
                                taskCompletionSource.setResult(createdConversation);

                            }
                        });

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });

        return taskCompletionSource;
    }

    public static TaskCompletionSource<ArrayList<Conversation>> getConversationsByCids(final ArrayList<String> listOfCids){
        final TaskCompletionSource<ArrayList<Conversation>> taskCompletionSource = new TaskCompletionSource<>();
        final ArrayList<Conversation> listConversations = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(FB_DIRECTORY_CONVERSATIONS);

        ref.addListenerForSingleValueEvent(//глобальный и постоянный прослушиватель всех данных marks
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){//находим все диалоги

                            Conversation conversation = snapshot.getValue(Conversation.class);
                            if(conversation == null)
                                Log.e("conversation", "is null");
                            else
                                for(String string : listOfCids){//проверяем, в каких из них мы учавствуем
                                    String cid = conversation.getCid();
                                    if(cid.equals(string))
                                        listConversations.add(conversation);
                                }


                        }

                        taskCompletionSource.setResult(listConversations);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });


        return taskCompletionSource;
    }


    public static TaskCompletionSource<Conversation> getConversationByCid(String cid){
        final TaskCompletionSource<Conversation> taskCompletionSource = new TaskCompletionSource<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(FB_DIRECTORY_CONVERSATIONS);

        ref.child(cid).addListenerForSingleValueEvent(//глобальный и постоянный прослушиватель всех данных marks
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Conversation conversation = dataSnapshot.getValue(Conversation.class);
                        taskCompletionSource.setResult(conversation);

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });

        return taskCompletionSource;
    }


    public static TaskCompletionSource<ArrayList<String>> getMyCids(String uid){
        final TaskCompletionSource<ArrayList<String>> arrayListTaskCompletionSource = new TaskCompletionSource<>();

        final ArrayList<String> eventList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(FB_DIRECTORY_USERS);

        ref.child(uid).child(FB_DIRECTORY_CONVERSATIONS).addListenerForSingleValueEvent(//глобальный и постоянный прослушиватель всех данных marks
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                            String string = snapshot.getValue(String.class);
                            eventList.add(string);
                            Log.v("received&added", string.toString());
                        }
                        arrayListTaskCompletionSource.setResult(eventList);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });


        return arrayListTaskCompletionSource;
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
