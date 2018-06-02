package com.coma.go.Dagger.Misc;

import android.content.Context;

import com.coma.go.Misc.PreferencesHandler;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Koma on 23.03.2018.
 */
@Module(includes = {ContextModule.class})
public class SessionModule {
    private String session = "";

    public SessionModule(){
    }
    public SessionModule(String session){
        this.session = session;
    }

    @Provides
    public String provideSession(Context context){
        return new PreferencesHandler(context).getValueSession();
    }
}
