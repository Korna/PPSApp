package com.coma.go.Web;

import android.util.Log;

import com.coma.go.Misc.SignViewModel;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


/**
 * Created by Koma on 06.04.2018.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {


    private static final String TAG = MyFirebaseInstanceIdService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        String refreshedToken = getFCMToken();

        if (refreshedToken!=null) {
           // SettingPreferences.setStringValueInPref(this, SettingPreferences.REGid, refreshedToken);
            sendRegistrationToServer(refreshedToken);
        }


    }

    private void sendRegistrationToServer(String token) {
/*
        SignViewModel.sendNewFcmServerToken(token)
                .subscribe(response -> {
                    Log.d(TAG, "response:" + response.message());
                    //if(response.isSuccessful())

                }, null);//TODO make scheduler for later api call*/

    }

    public static String getFCMToken(){
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("FCM", "Token is:" + token);
        return token;
    }
}