package com.coma.go.View;

import android.icu.text.SimpleDateFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.coma.go.Custom.ChatAdapter;
import com.coma.go.Model.Conversation;
import com.coma.go.Model.Message;
import com.coma.go.R;
import com.coma.go.Service.Singleton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.coma.go.Misc.Constants.FB_DIRECTORY_MESSAGES;
import static com.coma.go.Misc.Constants.FB_DIRECTORY_USERS;

public class ChatActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        final EditText editTextMessage = (EditText) findViewById(R.id.editText_message);
        ListView listView = (ListView) findViewById(R.id.listView_message_history);
        ArrayList<Message> messageList = new ArrayList<>();

        ChatAdapter chatAdapter = new ChatAdapter(this, messageList);
        listView.setAdapter(chatAdapter);



        Button buttonSend = (Button) findViewById(R.id.button_send);


        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msgText = editTextMessage.getText().toString();
                editTextMessage.setText("");

                Message message = new Message();
                message.setMessage(msgText);
                message.setReaded(false);

                Singleton singleton = Singleton.getInstance();
                String uid = singleton.user.getId();
                message.setSender(uid);

                String s  = "date.2017";

                message.setTime(s);

                String cid = "conversation1";

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference(FB_DIRECTORY_MESSAGES);

                ref.child(cid).setValue(message);



            }
        });
    }
    Conversation conversation;
    public Conversation getConversation(String cid){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(FB_DIRECTORY_MESSAGES);

        ref.child(cid).addListenerForSingleValueEvent(//глобальный и постоянный прослушиватель всех данных marks
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Conversation conversation = dataSnapshot.getValue(Conversation.class);


                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });

        return conversation;
    }


}
