package com.coma.go.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.coma.go.Custom.EventAdapter;
import com.coma.go.Model.Event;
import com.coma.go.R;

import java.util.ArrayList;

public class EventListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        ListView listView = (ListView) findViewById(R.id.listView_events);
        EventAdapter eventAdapter = new EventAdapter(this, new ArrayList<Event>(), "My");

        listView.setAdapter(eventAdapter);
    }
}
