package com.coma.go.View.User;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.coma.go.Dagger.Misc.SessionModule;
import com.coma.go.Misc.App;
import com.coma.go.Misc.SignViewModel;
import com.coma.go.R;
import com.coma.go.Utils.Logger;
import com.coma.go.Utils.ParseResponse;
import com.coma.go.View.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewLoginActivity extends AppCompatActivity {


    @BindView(R.id.button_signin)
    Button buttonSignIn;
    @BindView(R.id.button_signup)
    Button buttonSignUp;
    @BindView(R.id.editText_email)
    EditText textEmail;
    @BindView(R.id.editText_pass)
    EditText textPass;

    @BindView(R.id.layout_signup)
    RelativeLayout layout_signup;
    @BindView(R.id.layout_loading)
    RelativeLayout layout_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(SignViewModel.isLoggedIn(this)){
            startActivity(new Intent(this, MainActivity.class));
            return;
        }
        else
            setContentView(R.layout.activity_new_login);

        ButterKnife.bind(this);
        layout_signup.setVisibility(View.VISIBLE);
        layout_loading.setVisibility(View.INVISIBLE);

        buttonSignIn.setOnClickListener(view -> clickSignIn());
        buttonSignUp.setOnClickListener(view -> clickSignUp());
    }

    private void clickSignUp(){
        //показываем слой экрана с уведомлением о загрузке
       // layout_signup.setVisibility(View.INVISIBLE);
       // layout_loading.setVisibility(View.VISIBLE);
        //получаем почту и пароль
        String email = textEmail.getText().toString();
        String pass = textPass.getText().toString();
        //вызываем функцию отправки запроса о верности данных
        signUp(email, pass);
        //очищаем поля

    }

    private void clickSignIn(){
        String email = textEmail.getText().toString();
        String pass = textPass.getText().toString();
        signIn(email, pass);
        //очищаем поля
       // textEmail.setText("");
       // textPass.setText("");


    }
    @SuppressLint("CheckResult")
    private void signIn(String email, String password){
        String fcmToken = SignViewModel.getFCMToken();

        App.getApp().getComponent().webApi()
                .login(email, password, fcmToken)
                .subscribeOn(App.getApp().getNetworkScheduler())
                .observeOn(App.getApp().getNetworkScheduler())
                .map(response ->  {
                    String responseString = response.headers().get("set-cookie");
                    String id = "";
                    Log.d("accept", " :" + responseString);
                    if(response.isSuccessful()){
                        id = new ParseResponse().getSessionId(responseString);
                    }else{
                        // sendErrorToView(new Exception(response.message()));
                    }
                    return id;
                })
                .onErrorReturn(this::mapError)
                .subscribe(this::subscribe,
                        this::error);


    }


    @SuppressLint("CheckResult")
    private void signUp(String email, String password){
        App.getApp().getComponent().webApi().signup(email, password)
                .subscribeOn(App.getApp().getNetworkScheduler())
                .observeOn(App.getApp().getNetworkScheduler())
                .map(response ->  {
                    String responseString = response.headers().get("set-cookie");
                    String id = "";
                    Log.d("accept", " :" + responseString);
                    if(response.isSuccessful()){
                        id = new ParseResponse().getSessionId(responseString);
                    }else{
                       // sendErrorToView(new Exception(response.message()));
                    }
                    return id;
                })
                .onErrorReturn(this::mapError)
                .subscribe(this::subscribe,
                        this::error);


    }

    private void subscribe(String session) {
        if(session.equals("")){
           // sendErrorToView(new Exception("Cant get session from server"));
            Logger.e("errorAccepted", " :" + session);
            textEmail.setText("");
            textPass.setText("");
        }else{
            saveSession(session, getApplication());
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
          //  getValueLiveData().postValue(new DataResponse<>(Status.SUCCESS, null));

            Log.d("accepted", " :" + session);
        }
    }

    public static void saveSession(String session, Context context){
        SignViewModel.saveSession(session, context);
        App.getApp().setAppComponent(App.getApp()
                .rebuildComponent()
                .sessionModule(new SessionModule(session))
                .build());
    }

    private String mapError(Throwable throwable) {
       // sendErrorToView(new Exception(throwable.getMessage()));
        Log.d("sign", throwable.getMessage());
        return "";
    }

    private void error(Throwable throwable) {
       // sendErrorToView(new Exception(throwable.getMessage()));
        Log.d("sign", throwable.getMessage());

    }





}