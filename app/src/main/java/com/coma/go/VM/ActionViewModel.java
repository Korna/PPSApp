package com.coma.go.VM;

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;

import com.coma.go.Misc.App;
import com.coma.go.Model.DataResponse;
import com.coma.go.Utils.Logger;

import io.reactivex.Observable;
import retrofit2.Response;


/**
 * Created by Koma on 24.03.2018.
 */

public class ActionViewModel<O> extends MyViewModel<DataResponse<O>> {


    public ActionViewModel(@NonNull Application application) {
        super(application);
    }

    public void action(Observable<Response<O>> observable){
        getValueLiveData().postValue(new DataResponse<>(DataResponse.Status.LOADING, null));
        disposables.add(
                observable.subscribeOn(App.getApp().getNetworkScheduler())
                .observeOn(App.getApp().getNetworkScheduler())
                .subscribe(this::subscribe,
                        this::error)
        );
    }

    protected void subscribe(Response<O> response) {
        if(response.isSuccessful()){
            getValueLiveData().postValue(new DataResponse<>(DataResponse.Status.SUCCESS, response.body()));
        }else{
            Logger.e("responseUnsuccess", response.message());
            sendErrorToView(new Exception(response.message()));
        }
    }

    private void error(Throwable throwable) {
        sendErrorToView(new Exception(throwable.getMessage()));
        Log.d("action", throwable.getMessage());

    }



    public void sendErrorToView(Exception e) {
        getValueLiveData().postValue(new DataResponse<>(e));
    }




}
