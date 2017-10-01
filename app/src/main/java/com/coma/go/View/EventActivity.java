package com.coma.go.View;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.coma.go.Model.Conversation;
import com.coma.go.Model.Event;
import com.coma.go.R;
import com.coma.go.Service.FBIO;
import com.coma.go.Service.Singleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class EventActivity extends AppCompatActivity {
    Singleton singleton = Singleton.getInstance();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        final Event event = (Event)getIntent().getSerializableExtra("clickedEvent");


        TextView textViewName = (TextView) findViewById(R.id.textView_conversation_name);
        TextView textViewDescription = (TextView) findViewById(R.id.textView_event_description);
        Button buttonWrite = (Button)  findViewById(R.id.button_write);
        Button buttonJoin = (Button) findViewById(R.id.button_join);



        textViewName.setText(event.getName());
        textViewDescription.setText("    " + event.getDescription());



        buttonWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

                final Task taskGetConversation = FBIO.getActualCid(singleton.user.userInfo.getUid(), event.getAuthor_id()).getTask();

                taskGetConversation.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        Conversation conversation = (Conversation) taskGetConversation.getResult();
                        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                        intent.putExtra("Conversation", conversation);
                        startActivity(intent);

                    }
                });

            }
        });

        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    singleton.user.getParticipation().add(event);
                }catch(NullPointerException npe){
                    Log.e("no part case", npe.toString());
                    singleton.user.participation = new ArrayList<Event>();
                    singleton.user.getParticipation().add(event);
                }

                FBIO.createUserInfo(singleton.user.userInfo.getUid(), singleton.user);
                Toast.makeText(EventActivity.this, "Вы учавствуете в этом мероприятии", Toast.LENGTH_SHORT).show();
                //TODO вы уже учавствуете в этом мероприятии!
            }
        });



    }
}
