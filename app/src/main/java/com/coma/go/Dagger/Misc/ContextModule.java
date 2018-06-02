package com.coma.go.Dagger.Misc;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Koma on 15.01.2018.
 */

@Module
public class ContextModule {

    private static Context context;

    public ContextModule(){

    }

    public ContextModule(@NonNull Context context){
        if (!(context instanceof Application))
            throw new IllegalArgumentException("ContextModule requires an App context");
        this.context = context;
    }

    @Provides

    public Context provideContext() {
        return context;
    }

}