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

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewEventActivity extends AppCompatActivity {

    Singleton singleton = Singleton.getInstance();
    @Bind(R.id.editText_name)
    EditText editTextName;
    @Bind(R.id.editText_description)
    EditText editTextDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        ButterKnife.bind(this);
       // editTextName = (EditText) findViewById(R.id.editText_name);
       // editTextDescription = (EditText) findViewById(R.id.editText_description);


        Button buttonAdd = (Button) findViewById(R.id.button_add_event);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String description = editTextDescription.getText().toString();

                editTextDescription.setText("");
                editTextName.setText("");



                String id = singleton.user.userInfo.getUid();

                Event event = new Event();
                event.setAuthor_id(id);
                event.setName(name);
                event.setDescription(description);
                event.getParticipants().add(id);


                selectCategory(event);
            }
        });
    }


    private void selectCategory(final Event event){
        final CharSequence categories[] = new CharSequence[] {"Public", "Job", "Friends"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a category");
        builder.setItems(categories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selected = categories[which].toString();




                event.setCategory(selected);

                String eventKey = FBIO.createEvent(event, selected);


                singleton.user.participation.add(event);

                FBIO.createUserInfo(singleton.user.userInfo.getUid(), singleton.user);

                Toast.makeText(NewEventActivity.this, "Добавлено!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        builder.show();

    }



}
