package com.coma.go.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.coma.go.Custom.Adapters.DialogsAdapter;
import com.coma.go.Custom.Adapters.EventAdapter;
import com.coma.go.Entity.Event;
import com.coma.go.Misc.App;
import com.coma.go.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.coma.go.View.EventActivity.EVENT;

public class ConversationsActivity extends AppCompatActivity {


    DialogsAdapter dialogsAdapter;
    @BindView(R.id.recyclerview_events)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);
        ButterKnife.bind(this);


        setupRecycler();
        //App.getApp().getComponent().userApi().getDialogs()
    }

    private void setupRecycler(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        dialogsAdapter = new DialogsAdapter(new ArrayList<>(), R.layout.row_conversation, (view, o) -> {
            Event event = (Event) o;

          //  Intent intent = new Intent(this, EventActivity.class);
//
          //  intent.putExtra(EVENT, event);
          //  startActivity(intent);

        });
        recyclerView.setAdapter(dialogsAdapter);
    }

}
