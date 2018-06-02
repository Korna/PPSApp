package com.coma.go.Dagger.Misc;

import android.content.Context;

import com.coma.go.BuildConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Koma on 19.12.2017.
 */

@Module(includes= {InterceptorModule.class, //AuthenticatorModule.class,
        ContextModule.class
})
public class OkHttpModule {

    private int TIMEOUT = 3;

    private final int cacheSize = 1024 * 1024 * 5;// 5 mb

    public OkHttpModule(){
        TIMEOUT = BuildConfig.timeout;
        // Log.e("url is", URL + " ");
    }


    @Provides
    public OkHttpClient injectOkHttpKabluchkof(Interceptor interceptorK, TokenAuthenticator tokenAuthenticator,
                                                   Context context) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(interceptorK);
        httpClient.authenticator(tokenAuthenticator);

        return getClient(httpClient, context);
    }

    @Provides
    @Named("AUTH")
    public OkHttpClient injectOkHttpKabluchkofAuth(@Named("AUTH") Interceptor interceptor,
                                                   Context context) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(interceptor);
        return getClient(httpClient, context);
    }

    /*
    @Provides
    @Named(SERVER)
    public OkHttpClient injectOkHttpKabluchkof(@Named(SERVER) Interceptor interceptorK, TokenAuthenticator tokenAuthenticator, Context context) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(interceptorK);
        httpClient.authenticator(tokenAuthenticator);



        return getClient(httpClient, context);
    }*/



    private OkHttpClient getClient(OkHttpClient.Builder httpClient, Context context){
        /*
        try {
            Cache cache = getCache(context);
            if(cache != null)
                httpClient.cache(cache);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        HttpLoggingInterceptor interceptorLog = new HttpLoggingInterceptor();
        interceptorLog.setLevel(HttpLoggingInterceptor.Level.BODY);

        return httpClient
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(interceptorLog)
                .build();
    }

    private Cache getCache(Context context) throws IOException {
        Cache cache= null;
        cache = new Cache(context.getCacheDir(), cacheSize);
        return cache;
    }


}