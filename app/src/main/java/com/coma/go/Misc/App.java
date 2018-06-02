package com.coma.go.Misc;

import android.app.Application;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatDelegate;
import android.util.Base64;
import android.util.Log;

import com.coma.go.BuildConfig;
import com.coma.go.Dagger.Misc.ContextModule;
import com.coma.go.Dagger.Misc.DaggerWebComponent;
import com.coma.go.Dagger.Misc.WebComponent;
import com.coma.go.Utils.Logger;
import com.crashlytics.android.Crashlytics;

import com.google.firebase.FirebaseApp;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.fabric.sdk.android.Fabric;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Koma on 22.03.2018.
 */

public class App extends Application {
    final private String TAG = "APP_TAG";

    private static App app;
    private WebComponent appComponent;

    private final AppExecutors appExecutors = new AppExecutors();

    public static App getApp(){
        return app;
    }


    public void setAppComponent(WebComponent appComponent){
        this.appComponent = appComponent;
    }
    public WebComponent getComponent(){
        return appComponent;
    }


    @Override
    public void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        try {
            MultiDex.install(this);
        } catch (RuntimeException multiDexException) {
            multiDexException.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        initFabric();



        initDrawableSupport();

        FirebaseApp.initializeApp(this);


        appComponent = rebuildComponent().build();


    }


    private boolean checkSignature(){
        try {
            Signature[] signatures = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES).signatures;
            for (Signature signature : signatures) {
                // checkig
                //signature.
            }
        }catch (PackageManager.NameNotFoundException nfe){
            Log.e("packageManager", nfe.toString());

        }
        return true;
    }

    public DaggerWebComponent.Builder rebuildComponent(){
        return DaggerWebComponent.builder()
                .contextModule(new ContextModule(this));
    }


    private void initDrawableSupport(){
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
            Log.d("App", "appCompatEnabled");
        }
    }

    private void initFabric(){
        try {
            Fabric.with(this, new Crashlytics());
        }catch (Exception e){// lame
            Logger.e("App", e.toString());
        }
    }

    public AppExecutors getAppExecutors() {
        return appExecutors;
    }

    public Scheduler getNetworkScheduler(){
        Scheduler scheduler;// = Schedulers.from(getAppExecutors().networkIO());
        return Schedulers.io();
    }


    public void printHashKey(Context pContext) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Logger.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Logger.e(TAG, "printHashKey()", e);
        }
    }

}

