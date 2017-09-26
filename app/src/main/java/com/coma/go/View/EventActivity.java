package com.coma.go.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.coma.go.Model.Event;
import com.coma.go.R;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        final Event event = (Event)getIntent().getSerializableExtra("clickedEvent");


        TextView textViewName = (TextView) findViewById(R.id.textView_event_name);
        TextView textViewDescription = (TextView) findViewById(R.id.textView_event_description);
        Button buttonWrite = (Button)  findViewById(R.id.button_write);
        Button buttonJoin = (Button) findViewById(R.id.button_join);



        textViewName.setText(event.getName());
        textViewDescription.setText("    " + event.getDescription());

        buttonWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("clickedEvent", event);
                startActivity(intent);
            }
        });



    }
}
