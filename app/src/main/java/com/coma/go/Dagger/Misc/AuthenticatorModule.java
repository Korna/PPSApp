package com.coma.go.Dagger.Misc;

import android.content.Context;

import com.coma.go.Web.Interface.IUserApi;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Koma on 22.02.2018.
 */

@Module
public class AuthenticatorModule {

    @Provides
    public TokenAuthenticator tokenAuthenticator(@Named("AUTH") IUserApi accountApi, Context context) {
        return new TokenAuthenticator(accountApi, context);
    }


}
