package com.coma.go.Dagger.Misc;

import android.content.Context;

import com.coma.go.Web.IUserApi;
import com.google.gson.Gson;

import dagger.Component;


/**
 * Created by Koma on 19.12.2017.
 */

@Component(modules={
        GsonModule.class, OkHttpModule.class, RetrofitModule.class, InterceptorModule.class, //TokenAuthenticator.class,
        AuthenticatorModule.class,
        ApiModule.class,
        ContextModule.class, SessionModule.class
})
public interface WebComponent {

    public static final String SERVER = "server";
    public static final String SERVER_AUTH = "server_auth";


    Gson gson();
    IUserApi webApi();
    Context context();





    void inject(RetrofitModule retrofitModule);
    void inject(OkHttpModule okHttpModule);
    void inject(ApiModule apiModule);
}
