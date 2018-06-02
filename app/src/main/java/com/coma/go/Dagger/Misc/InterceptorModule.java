package com.coma.go.Dagger.Misc;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.Request;

/**
 * Created by Koma on 25.12.2017.
 */
@Module(includes = {SessionModule.class})
public class InterceptorModule {


    @Provides
    @Named("AUTH")
    public Interceptor injectAuthInterceptor() {
        Log.d("init", "intercepto w\\o session:");
        return chain ->  {
            Request original = chain.request();
            Request request = getBuilder(original).build();
            return chain.proceed(request);
        };
    }

    @Provides
    public Interceptor injectInterceptor(String session) {
        Log.d("init", "interceptor:" + session );
        Interceptor interceptor = chain ->  {
                Request original = chain.request();


                Log.d("injectHttpClient", "session:" + session );

                Request.Builder requestBuilder;
                if(session != null){
                    requestBuilder = getBuilderSession(original, session);
                }else{
                    requestBuilder = getBuilder(original);
                }

                Request request = requestBuilder.build();
                return chain.proceed(request);
        };

        return interceptor;
    }

    private Request.Builder getBuilderSession(Request original, String id){
        return original.newBuilder()
             //   .header("X-Requested-With", "XMLHttpRequest")
                .header("Cookie", id)
                .removeHeader("Pragma");
    }

    private Request.Builder getBuilder(Request original){
        return original.newBuilder()
              //  .header("X-Requested-With", "XMLHttpRequest")
                .removeHeader("Pragma");
    }

    private boolean isOnline(Context context) throws NullPointerException{
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm != null && cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

}
