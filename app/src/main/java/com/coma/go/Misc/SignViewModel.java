package com.coma.go.Misc;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.coma.go.Dagger.Misc.SessionModule;
import com.coma.go.Utils.Logger;
import com.coma.go.Utils.ParseResponse;
import com.coma.go.Utils.ViewUtils;
import com.coma.go.View.User.OptionsActivity;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.iid.FirebaseInstanceId;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import retrofit2.Response;


/**
 * Created by Koma on 24.03.2018.
 */

public class SignViewModel  {
    protected CompositeDisposable disposables = new CompositeDisposable();



    public static String getFCMToken(){
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("FCM", "Token is:" + token);
        return token;
    }


    @SuppressLint("CheckResult")
    public static void refreshFCM(){
        App.getApp().getComponent().userApi()
                .refreshFcmToken(getFCMToken())
                .subscribeOn(App.getApp().getNetworkScheduler())
                .observeOn(App.getApp().getNetworkScheduler())
                .subscribe(voidResponse -> {

                        },
                        throwable -> Logger.e("refreshFCMSignView", throwable.getMessage()));
    }

    public void sign(Observable<Response<Void>> observable, Context context){
        disposables.add(
                observable.subscribeOn(App.getApp().getNetworkScheduler())
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
                .subscribe(s -> subscribe(s, context),
                        this::error)
        );
    }

    public static boolean isLoggedIn(Context context) {
        PreferencesHandler preferencesHandler = new PreferencesHandler(context);
        if(preferencesHandler.getValueSession().equals(""))
            return false;
        else
            return true;
    }



    private void subscribe(String session, Context context) {
        if(session.equals("")){
        //    sendErrorToView(new Exception("Cant get session from server"));
            Logger.e("errorAccepted", " :" + session);
        }else{
            saveSession(session, context);

      //      getValueLiveData().postValue(new DataResponse<>(Status.SUCCESS, null));

            Log.d("accepted", " :" + session);
        }
    }

    public static void saveSession(String session, Context context){
        PreferencesHandler preferencesHandler = new PreferencesHandler(context);
        preferencesHandler.putValueSession(session);

        App.getApp().setAppComponent(App.getApp()
                        .rebuildComponent()
                        .sessionModule(new SessionModule(session))
                        .build());
    }

    private String mapError(Throwable throwable) {
      //  sendErrorToView(new Exception(throwable.getMessage()));
        Log.d("sign", throwable.getMessage());
        return "";
    }


    private void error(Throwable throwable) {
     //   sendErrorToView(new Exception(throwable.getMessage()));
        Log.d("sign", throwable.getMessage());

    }

  /*  public static Observable<Response<Void>> sendNewFcmServerToken(String token){
        return App.getApp().getComponent().userApi()
                .updateFCMToken(token)
                .observeOn(App.getApp().getNetworkScheduler())
                .observeOn(App.getApp().getNetworkScheduler());
    }
*/





}
