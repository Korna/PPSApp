package com.coma.go.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.coma.go.Custom.Adapters.EventAdapter;
import com.coma.go.Entity.Event;
import com.coma.go.Misc.App;
import com.coma.go.Misc.SignViewModel;
import com.coma.go.R;
import com.coma.go.View.User.CreatedEventActivity;
import com.coma.go.View.User.MyEventActivity;
import com.coma.go.View.User.MyProfileActivity;
import com.coma.go.View.User.NewEventActivity;
import com.coma.go.View.User.OptionsActivity;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import retrofit2.Response;

import static com.coma.go.View.EventActivity.EVENT;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private String TAG = "MainActivity";

    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.recyclerview_events)
    RecyclerView recyclerView;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        swipeRefreshLayout.setOnRefreshListener(this::refreshList);
        loadGui();
        FirebaseMessaging.getInstance().subscribeToTopic("notes");


        setupRecycler();
        getList();
    }

    private void refreshList() {
        eventAdapter.removeAll();
        getList();
    }

    private void loadGui(){
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        fab.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), NewEventActivity.class);
            startActivity(intent);
        });

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
        App.getApp().getComponent().webApi().getNotes()
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



    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // добавляет панель верхнюю
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent = null;
        switch(id){
            case R.id.nav_dialogs:
                intent = new Intent(MainActivity.this, DialogsActivity.class);
                break;
            case R.id.nav_quit:
                SignViewModel.saveSession("", this);
                SignViewModel.saveCredi("", "", this);
                finish();
                intent = new Intent(MainActivity.this, LoginActivity.class);
                break;
            case R.id.nav_slideshow:
                intent = new Intent(MainActivity.this, CreatedEventActivity.class);
                break;
            case R.id.nav_events:
                intent = new Intent(MainActivity.this, MyEventActivity.class);
                break;
            case R.id.nav_profile:
                intent = new Intent(MainActivity.this, MyProfileActivity.class);
                break;
            case R.id.nav_options:
                intent = new Intent(MainActivity.this, OptionsActivity.class);
                break;
        }
        if(intent != null)
            startActivity(intent);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
