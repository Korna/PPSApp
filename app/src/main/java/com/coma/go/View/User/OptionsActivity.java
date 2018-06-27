package com.coma.go.View.User;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.coma.go.Entity.Options;
import com.coma.go.Misc.App;
import com.coma.go.Misc.SignViewModel;
import com.coma.go.Model.CreatedTokenKey;
import com.coma.go.Model.DataResponse;
import com.coma.go.R;
import com.coma.go.Utils.ViewUtils;
import com.coma.go.VM.misc.OptionsViewModel;
import com.coma.go.VM.misc.TwoFAActivateViewModel;
import com.coma.go.VM.misc.TwoFACreateViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import retrofit2.Response;

public class OptionsActivity extends AppCompatActivity {

    @BindView(R.id.switch_receiveFcm) Switch switch_fcm;

    @BindView(R.id.button_refresh) Button button_refresh;

    @BindView(R.id.editText_token) EditText editText_token;
    @BindView(R.id.editText_code) EditText editText_code;

    @BindView(R.id.button_activate) Button button_activate;

    TwoFACreateViewModel twoFACreateViewModel;
    Context context;
    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        ButterKnife.bind(this);
        context = this;

        twoFACreateViewModel = ViewModelProviders.of(this).get(TwoFACreateViewModel.class);
        twoFACreateViewModel.create();
        twoFACreateViewModel.getValueLiveData().observe(this, createdTokenKeyDataResponse -> {
            if(createdTokenKeyDataResponse.isSuccessful())
                editText_token.setText(createdTokenKeyDataResponse.getData().getKey());
            else
                ViewUtils.showToast(context, createdTokenKeyDataResponse.getThrowableMessage());
        });



        OptionsViewModel optionsViewModel = ViewModelProviders.of(this).get(OptionsViewModel.class);
        optionsViewModel.getOptions();
        optionsViewModel.getValueLiveData().observe(this, optionsDataResponse -> {
            if(optionsDataResponse.isSuccessful())
                loadProfileToUi(optionsDataResponse.getData());
            else
                ViewUtils.showToast(context, "Cant load options");
        });

        switch_fcm.setOnCheckedChangeListener((compoundButton, b) -> checked(b));

        button_refresh.setOnClickListener(view -> {
            refreshFcm();
        });


        TwoFAActivateViewModel twoFAActivateViewModel = ViewModelProviders.of(this).get(TwoFAActivateViewModel.class);
        twoFAActivateViewModel.getValueLiveData().observe(this, voidDataResponse -> {
            if(voidDataResponse.isSuccessful())
                ViewUtils.showToast(context, "Success");
            else
                ViewUtils.showToast(context, voidDataResponse.getThrowableMessage());
        });
        button_activate.setOnClickListener(view -> {
            String code = editText_code.getText().toString();
            twoFAActivateViewModel.activate(code);
        });
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
        App.getApp().getComponent().webApi()
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
