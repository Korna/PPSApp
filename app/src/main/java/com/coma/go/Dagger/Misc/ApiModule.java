package com.coma.go.Dagger.Misc;

import com.coma.go.Web.IUserApi;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by Koma on 26.12.2017.
 */
@Module(includes= {RetrofitModule.class})
public class ApiModule {


    public ApiModule(){}


    @Provides
    public IUserApi userApi(Retrofit retrofit){
        return retrofit.create(IUserApi.class);
    }


    @Provides
    @Named("AUTH")
    public IUserApi userApiAuth(@Named("AUTH") Retrofit retrofit){
        return retrofit.create(IUserApi.class);
    }

}
