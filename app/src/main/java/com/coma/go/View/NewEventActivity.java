package com.coma.go.View;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.coma.go.Model.Event;
import com.coma.go.R;
import com.coma.go.Service.FBIO;
import com.coma.go.Service.Singleton;

import java.util.ArrayList;

public class NewEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        final EditText editTextName = (EditText) findViewById(R.id.editText_name);
        final EditText editTextDescription = (EditText) findViewById(R.id.editText_description);


        Button buttonAdd = (Button) findViewById(R.id.button_add_event);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String description = editTextDescription.getText().toString();

                editTextDescription.setText("");
                editTextName.setText("");


                Singleton singleton = Singleton.getInstance();
                String id = singleton.user.userInfo.getUid();

                Event event = new Event();
                event.setAuthor_id(id);
                event.setName(name);
                event.setDescription(description);

                //ArrayList<String> list = new ArrayList<>();

                event.participants.add(id);
                event.participants.add(id);


                selectCategory(event);


            }
        });
    }


    private void selectCategory(final Event event){
        final CharSequence colors[] = new CharSequence[] {"Public", "Job", "Friends"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a color");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selected = colors[which].toString();




                event.setCategory(selected);

                String eventKey = FBIO.createEvent(event, selected);



                Singleton singleton = Singleton.getInstance();
                singleton.user.participation.add(event);

                FBIO.createUserInfo(singleton.user.userInfo.getUid(), singleton.user);

                Toast.makeText(NewEventActivity.this, "Добавлено!", Toast.LENGTH_SHORT).show();
                finish();



            }
        });
        builder.show();

    }



}
