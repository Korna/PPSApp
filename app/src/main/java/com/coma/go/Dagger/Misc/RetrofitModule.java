package com.coma.go.Dagger.Misc;

import android.arch.lifecycle.LiveData;

import com.coma.go.BuildConfig;
import com.coma.go.Model.ApiResponse;
import com.google.gson.Gson;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Koma on 19.12.2017.
 */
@Module(includes= {GsonModule.class, OkHttpModule.class})
public class RetrofitModule {
    private String URL = null;

    public RetrofitModule(){
       URL = BuildConfig.serverip;
    }

    @Provides
    public Retrofit injectOkHttpClient(OkHttpClient okHttpClient, Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(URL + "/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build();

        return retrofit;
    }

    @Provides
    @Named("AUTH")
    public Retrofit injectOkHttpClientAuth(@Named("AUTH") OkHttpClient okHttpClient, Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(URL + "/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build();

        return retrofit;
    }

    public class LiveDataCallAdapterFactory extends CallAdapter.Factory {

        @Override
        public CallAdapter get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
            if (getRawType(returnType) != LiveData.class) {
                return null;
            }
            Type observableType = getParameterUpperBound(0, (ParameterizedType) returnType);
            Class<?> rawObservableType = getRawType(observableType);
            if (rawObservableType != ApiResponse.class) {
                throw new IllegalArgumentException("type must be a resource");
            }
            if (! (observableType instanceof ParameterizedType)) {
                throw new IllegalArgumentException("resource must be parameterized");
            }
            Type bodyType = getParameterUpperBound(0, (ParameterizedType) observableType);
            return new LiveDataCallAdapter<>(bodyType);
        }
    }

    final class LiveDataCallAdapter<R> implements CallAdapter<R, LiveData<ApiResponse<R>>> {
        private final Type responseType;
        public LiveDataCallAdapter(Type responseType) {
            this.responseType = responseType;
        }

        @Override
        public Type responseType() {
            return responseType;
        }


        public LiveData<ApiResponse<R>> adapt(final Call<R> call) {
            return new LiveData<ApiResponse<R>>() {
                AtomicBoolean started = new AtomicBoolean(false);
                @Override
                protected void onActive() {
                    super.onActive();
                    if (started.compareAndSet(false, true)) {
                        call.enqueue(new Callback<R>() {
                            @Override
                            public void onResponse(Call<R> call, Response<R> response) {
                                postValue(new ApiResponse<>(response));
                            }

                            @Override
                            public void onFailure(Call<R> call, Throwable throwable) {
                                postValue(new ApiResponse<R>(throwable));
                            }
                        });
                    }
                }
            };
        }
    }

}
