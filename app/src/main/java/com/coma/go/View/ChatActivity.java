package com.coma.go.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.coma.go.Custom.MessageAdapter;
import com.coma.go.Model.Conversation;
import com.coma.go.Model.Event;
import com.coma.go.Model.Message;
import com.coma.go.R;
import com.coma.go.Service.Singleton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.coma.go.Misc.Constants.FB_DIRECTORY_CONVERSATIONS;

public class ChatActivity extends AppCompatActivity {

    Conversation conversation;
    String cid;
    @Bind(R.id.listView_message_history)
    ListView listView;
    @Bind(R.id.editText_message)
    EditText editTextMessage;
    @Bind(R.id.button_send)
    Button buttonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        ButterKnife.bind(this);


        try {
            conversation = (Conversation) getIntent().getSerializableExtra("Conversation");
            cid  = conversation.getCid();
        }catch(NullPointerException npe){
            Log.e("npe", npe.toString());
            conversation = new Conversation();
            cid = "SampleConv";
        }

        MessageAdapter chatAdapter = new MessageAdapter(this, conversation.getMessageHistory());
        listView.setAdapter(chatAdapter);


        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msgText = editTextMessage.getText().toString();
                editTextMessage.setText("");

                Message message = new Message();
                message.setMessage(msgText);
                message.setReaded(false);

                Singleton singleton = Singleton.getInstance();

                String uid = singleton.getUser().getId();
                message.setSender(uid);

                String s  = "date.2017";
                message.setTime(s);

                conversation.getMessageHistory().add(message);//может быть по времени рассинхрон.

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference(FB_DIRECTORY_CONVERSATIONS);

                ref.child(cid).setValue(conversation);



            }
        });
    }

    private void prepareSend(String msgText){


    }

/*

    public Conversation getConversation(String cid){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(FB_DIRECTORY_CONVERSATIONS);

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
    }*/


}
