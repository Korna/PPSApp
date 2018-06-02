package com.coma.go.View.User;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListView;

import com.coma.go.Custom.Adapters.EventAdapter;
import com.coma.go.Entity.Event;
import com.coma.go.Misc.App;
import com.coma.go.R;
import com.coma.go.View.EventActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import retrofit2.Response;

import static com.coma.go.View.EventActivity.EVENT;

public class MyEventActivity extends AppCompatActivity {
    String TAG = "MyEventActivity";

    @BindView(R.id.recyclerview_events)
    RecyclerView recyclerView;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        ButterKnife.bind(this);


        swipeRefreshLayout.setOnRefreshListener(this::refreshList);
        setupRecycler();
        getList();
    }

    private void refreshList() {
        eventAdapter.removeAll();
        getList();
    }

    EventAdapter eventAdapter;
    private void setupRecycler(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        eventAdapter = new EventAdapter(new ArrayList<>(), R.layout.row_event, (view, event) -> {

            Intent intent = new Intent(this, EventActivity.class);

            intent.putExtra(EVENT, event);
            startActivity(intent);

        });
        recyclerView.setAdapter(eventAdapter);
    }

    @SuppressLint("CheckResult")
    private void getList(){
        swipeRefreshLayout.setRefreshing(true);
        App.getApp().getComponent().userApi().getMyEvents()
                .subscribeOn(App.getApp().getNetworkScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::subscribe,
                        this::error);

    }

    private void subscribe(Response<List<Event>> listResponse) {
        if(listResponse.isSuccessful())
            eventAdapter.addItems(listResponse.body());
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
