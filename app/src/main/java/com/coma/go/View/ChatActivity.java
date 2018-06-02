package com.coma.go.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.coma.go.Custom.Adapters.EventAdapter;
import com.coma.go.Custom.Adapters.MessageAdapter;
import com.coma.go.Entity.Dialog;
import com.coma.go.Entity.Event;
import com.coma.go.Entity.Message;
import com.coma.go.Entity.Profile;
import com.coma.go.Misc.App;
import com.coma.go.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import retrofit2.Response;

import static com.coma.go.View.EventActivity.EVENT;
import static com.coma.go.View.UserProfileActivity.PROFILE;

public class ChatActivity extends AppCompatActivity {
    String TAG = "ChatActivity";



    @BindView(R.id.listView_message_history)
    RecyclerView recyclerView;

    @BindView(R.id.editText_message)
    EditText editTextMessage;
    @BindView(R.id.button_send)
    Button buttonSend;
    @BindView(R.id.textView_dialog_name)
    TextView textView_dialog_name;

    String dialogId;
    Profile profile;

    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swipeRefreshLayout;

    public static String DIALOG_ID = "Dialog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        dialogId = getIntent().getStringExtra(DIALOG_ID);

        profile = (Profile) getIntent().getSerializableExtra(PROFILE);
        if(profile != null)
            getSupportActionBar().setTitle(profile.getUsername());
        else
            getSupportActionBar().setTitle("Dialog");

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setupRecycler();
        swipeRefreshLayout.setOnRefreshListener(this::refreshList);

       // textView_dialog_name.setText(profile.getUsername());
        buttonSend.setOnClickListener(view -> {
            Message message = new Message();
            message.setText(editTextMessage.getText().toString());
            message.setDialogId(dialogId);
            sendMessage(message);
        });
    }

    private void refreshList() {
        eventAdapter.removeAll();
        getList(dialogId);
    }

    public void scrollToBottom(){
        recyclerView.scrollToPosition(0);
    }
    MessageAdapter eventAdapter;
    private void setupRecycler(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        eventAdapter = new MessageAdapter(new ArrayList<>(), R.layout.row_message_income, (view, event) -> {

           // Intent intent = new Intent(this, EventActivity.class);

            //intent.putExtra(EVENT, event);
           // startActivity(intent);

        });
        recyclerView.setAdapter(eventAdapter);
        getList(dialogId);
    }
    @SuppressLint("CheckResult")
    private void sendMessage(Message message){

        App.getApp().getComponent().userApi().sendMessage(message)
                .subscribeOn(App.getApp().getNetworkScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::subscribeOnMessageSent, this::error);
    }
    private void subscribeOnMessageSent(Response<Message> listResponse) {

        if(listResponse.isSuccessful()) {
            Message message = listResponse.body();

            setupMessage(message, profile);

            eventAdapter.add(message, 0);
            editTextMessage.setText("");
            scrollToBottom();
        }
        else
            Log.d(TAG, listResponse.message());

    }

    private Message setupMessage(Message message, Profile profile){
        if(profile != null)
            if(message.getSenderId().equals(profile.get_id()))
                message.setSenderId(profile.getUsername());
            else
                message.setSenderId("You");
        else
            message.setSenderId("   ");
        return message;
    }

    @SuppressLint("CheckResult")
    private void getList(String dialogId){
        swipeRefreshLayout.setRefreshing(true);
        App.getApp().getComponent().userApi()
                .getMessages(dialogId)
                .subscribeOn(App.getApp().getNetworkScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::subscribe,
                        this::error);
    }

    private void subscribe(Response<List<Message>> listResponse) {
        if(listResponse.isSuccessful()) {
            List<Message> list = listResponse.body();

            for(Message message : list)
                setupMessage(message, profile);

            eventAdapter.addItems(list);
            scrollToBottom();
        }
        else
            Log.d(TAG, listResponse.message());
        swipeRefreshLayout.setRefreshing(false);
    }


    private void error(Throwable throwable) {
        // sendErrorToView(new Exception(throwable.getMessage()));
        Log.d(TAG, throwable.getMessage());
        swipeRefreshLayout.setRefreshing(false);
    }


}
