package com.coma.go.Dagger.Misc;

import android.content.Context;
import android.support.annotation.Nullable;

import com.coma.go.Misc.PreferencesHandler;
import com.coma.go.Misc.SignViewModel;
import com.coma.go.Utils.Logger;
import com.coma.go.Utils.ParseResponse;
import com.coma.go.Web.Interface.IUserApi;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;


/**
 * Created by Koma on 22.02.2018.
 */


public class TokenAuthenticator implements Authenticator {
    IUserApi iAccountApi;
    Context context;

    private int authTryCount = 0;
    private int maxAuthTryCount = 3;

    public TokenAuthenticator(IUserApi accountApi, Context context) {
        this.iAccountApi = accountApi;
        this.context = context;
    }



    @Override
    public Request authenticate(Route route, Response response) {
        Logger.d("Authenticating for:", response.message());
        System.out.println("Challenges: " + response.challenges());

        ++authTryCount;

        if(authTryCount > maxAuthTryCount){
            Logger.d("cant auth", "max auth retry count reached");
            return null;
        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null)
            return null;


        Task<GetTokenResult> task = user.getIdToken(true);
        String idToken = null;
        try {
            idToken = Tasks.await(task).getToken();
        } catch (ExecutionException e) {
            Logger.w("taskAwaitToken", e.getMessage());
        } catch (InterruptedException e) {
            Logger.w("taskAwaitToken", e.getMessage());
        }
        if(idToken == null)
            return null;

        PreferencesHandler preferencesHandler = new PreferencesHandler(context);
        String email = preferencesHandler.getUsername();
        String password = preferencesHandler.getPassword();

        String session = getSession(email, password);
        if(session == null)
            return null;

        SignViewModel.saveSession(session, context);

       return response.request()
                .newBuilder()
                .header("Cookie", session)
                .build();
    }

    private @Nullable String getSession(String email, String password){
        String session = null;
        String fcmToken = SignViewModel.getFCMToken();
        retrofit2.Response<Void> responseFromLogin = iAccountApi
                .login(email, password)
                .blockingFirst();

        try {
            session = sessionMapper(responseFromLogin);
            authTryCount = 0;
        }catch (IOException io){
            Logger.d("cant connect", io.getMessage(), io);
        }

        return session;
    }


    public static @Nullable String sessionMapper(retrofit2.Response<Void> response) throws IOException{
        String responseString = response.headers().get("set-cookie");
        String id = null;

        Logger.d("loginPresenter", "string:" + responseString);
        if(response.isSuccessful()){
            ParseResponse parseResponse = new ParseResponse();
            id = parseResponse.getSessionId(responseString);
        }else{
            //ERROR
            id = null;
        }
        return id;
    }

}