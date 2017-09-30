package com.coma.go.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.coma.go.Custom.ConversationAdapter;
import com.coma.go.Custom.EventAdapter;
import com.coma.go.Model.Conversation;
import com.coma.go.Model.Event;
import com.coma.go.R;
import com.coma.go.Service.FBIO;
import com.coma.go.Service.Singleton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.coma.go.Misc.Constants.FB_DIRECTORY_CONVERSATIONS;
import static com.coma.go.Misc.Constants.FB_DIRECTORY_USERS;

public class ConversationsActivity extends AppCompatActivity {


    ArrayList<String> cidList = null;

    ConversationAdapter conversationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);
        Singleton singleton = Singleton.getInstance();

        cidList = FBIO.getMyCids(singleton.user.userInfo.getUid());


        conversationAdapter = new ConversationAdapter(this, getMyConversations(singleton.user.userInfo.getUid()));

        ListView listView = (ListView) findViewById(R.id.listView_conversations);
        listView.setAdapter(conversationAdapter);

    }

    public ArrayList<Conversation> getMyConversations(String uid){//можно создать таблицу title of COnversation для view(меньше трафика)
        final ArrayList<Conversation> listConversations = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(FB_DIRECTORY_CONVERSATIONS);

        for(String string : cidList){

            ref.child(string).addListenerForSingleValueEvent(//глобальный и постоянный прослушиватель всех данных marks
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                                Conversation conversation = dataSnapshot.getValue(Conversation.class);

                                listConversations.add(conversation);

                                conversationAdapter.notifyDataSetChanged();




                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });


        }


        return listConversations;
    }



    public ArrayList<Conversation> getConversationList(String uid){
        final ArrayList<Conversation> listConversations = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(FB_DIRECTORY_USERS);

        ref.addListenerForSingleValueEvent(//глобальный и постоянный прослушиватель всех данных marks
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {



                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                            //Conversation conversation = dataSnapshot.getValue(Conversation.class);
                            Conversation conversation = snapshot.getValue(Conversation.class);

                            listConversations.add(conversation);

                            conversationAdapter.notifyDataSetChanged();
                        }



                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });

        return listConversations;
    }

}
