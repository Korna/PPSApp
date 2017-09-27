package com.coma.go.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.coma.go.Model.Event;
import com.coma.go.R;
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
                String description = editTextDescription.toString();

                editTextDescription.setText("");
                editTextName.setText("");

                Singleton singleton = Singleton.getInstance();
                String id = singleton.user.getId();




                Event event = new Event();
                event.setId(id);
                event.setName(name);
                event.setDescription(description);

                ArrayList<String> list = new ArrayList<>();
                list.add(id);
                event.setParticipants(list);



                Toast.makeText(NewEventActivity.this, "Добавлено!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
