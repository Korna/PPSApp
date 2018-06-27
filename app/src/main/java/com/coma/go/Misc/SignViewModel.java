package com.coma.go.Misc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.coma.go.Dagger.Misc.SessionModule;
import com.coma.go.Utils.Logger;
import com.coma.go.Utils.ParseResponse;
import com.google.firebase.iid.FirebaseInstanceId;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
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
        App.getApp().getComponent().webApi()
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
            Logger.e("errorAccepted", " :" + session);
        }else{
            saveSession(session, context);


            Log.d("accepted", " :" + session);
        }
    }

    public static void saveSession(String session, Context context){
        PreferencesHandler preferencesHandler = new PreferencesHandler(context);
        preferencesHandler.putValueSession(session);
        rebuildDagger(session);
    }

    public static void saveCredi(String email, String pass, Context context){
        PreferencesHandler preferencesHandler = new PreferencesHandler(context);
        preferencesHandler.putUsername(email);
        preferencesHandler.putPassword(pass);
    }

    public static void rebuildDagger(String session){
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
        return App.getApp().getComponent().webApi()
                .updateFCMToken(token)
                .observeOn(App.getApp().getNetworkScheduler())
                .observeOn(App.getApp().getNetworkScheduler());
    }
*/





}
