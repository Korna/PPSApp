package com.coma.go.View.User;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.coma.go.Entity.Options;
import com.coma.go.Entity.Profile;
import com.coma.go.Misc.App;
import com.coma.go.Misc.SignViewModel;
import com.coma.go.R;
import com.coma.go.Utils.ViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import retrofit2.Response;

public class OptionsActivity extends AppCompatActivity {
    @BindView(R.id.switch_receiveFcm)
    Switch switch_fcm;
    @BindView(R.id.button_refresh)
    Button button_refresh;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        ButterKnife.bind(this);

        App.getApp().getComponent().userApi().getOptions()
                .subscribeOn(App.getApp().getNetworkScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::subscribe,
                        this::error);

        switch_fcm.setOnCheckedChangeListener((compoundButton, b) -> checked(b));

        button_refresh.setOnClickListener(view -> {
            refreshFcm();
        });
    }



    private void subscribe(Response<Options> profileResponse) {
        if(profileResponse.isSuccessful())
            loadProfileToUi(profileResponse.body());
        else
            ViewUtils.showToast(this, "Cant load profile");
    }

    private void loadProfileToUi(Options body) {
        switch_fcm.setChecked(body.isReceiveFcm());
    }

    private void error(Throwable throwable) {
        ViewUtils.showToast(this, throwable.getMessage());
        Log.d("OptionsActivity", throwable.getMessage());
    }


    @SuppressLint("CheckResult")
    private void checked(boolean b) {
        App.getApp().getComponent().userApi()
                .sendOptions(new Options(b))
                .subscribeOn(App.getApp().getNetworkScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(voidResponse -> {
                     if(voidResponse.isSuccessful())
                         ViewUtils.showToast(OptionsActivity.this, "Was changed");
                     else
                         ViewUtils.showToast(OptionsActivity.this, "Was noy changed");
                },
                        this::error);
    }

    private void refreshFcm(){
        SignViewModel.refreshFCM();
    }


}
