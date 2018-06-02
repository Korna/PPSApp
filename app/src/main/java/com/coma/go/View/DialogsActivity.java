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

import com.coma.go.Custom.Adapters.DialogsAdapter;
import com.coma.go.Custom.Adapters.EventAdapter;
import com.coma.go.Entity.Dialog;
import com.coma.go.Entity.Event;
import com.coma.go.Entity.Message;
import com.coma.go.Misc.App;
import com.coma.go.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import retrofit2.Response;

import static com.coma.go.View.ChatActivity.DIALOG_ID;
import static com.coma.go.View.EventActivity.EVENT;
import static com.coma.go.View.UserProfileActivity.PROFILE;

public class DialogsActivity extends AppCompatActivity {

    String TAG = "DialogsActivity";

    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swipeRefreshLayout;

    DialogsAdapter dialogsAdapter;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);
        ButterKnife.bind(this);


        setupRecycler();
        //App.getApp().getComponent().userApi().getDialogs()
        swipeRefreshLayout.setOnRefreshListener(this::getList);
        getList();
    }

    private void setupRecycler(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        dialogsAdapter = new DialogsAdapter(new ArrayList<>(), R.layout.row_conversation, (view, o) -> {
            o.setName("Dialog");
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra(DIALOG_ID, o.get_id());

            //intent.putExtra(PROFILE, profile);
            startActivity(intent);
          //  Intent intent = new Intent(this, EventActivity.class);

          //  intent.putExtra(EVENT, event);
          //  startActivity(intent);

        });
        recyclerView.setAdapter(dialogsAdapter);
    }

    @SuppressLint("CheckResult")
    private void getList(){
        swipeRefreshLayout.setRefreshing(true);
        App.getApp().getComponent().userApi()
                .getDialogs()
                .subscribeOn(App.getApp().getNetworkScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::subscribe,
                        this::error);
    }

    private void subscribe(Response<List<Dialog>> listResponse) {
        if(listResponse.isSuccessful()) {
            dialogsAdapter.addItems(listResponse.body());
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
