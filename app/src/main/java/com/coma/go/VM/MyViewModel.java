package com.coma.go.VM;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;

import android.support.annotation.NonNull;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Koma on 22.03.2018.
 */

public class MyViewModel<T> extends AndroidViewModel  {
    protected CompositeDisposable disposables = new CompositeDisposable();
    protected MediatorLiveData<T> valueLiveData;

    public MyViewModel(@NonNull Application application) {
        super(application);
        valueLiveData = new MediatorLiveData<>();
    }

    public MutableLiveData<T> getValueLiveData() {
        return valueLiveData;
    }


    protected void sendErrorToView(T t) {
        getValueLiveData().postValue(t);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.dispose();
    }

/*
    //observable code
    private PropertyChangeRegistry callbacks = new PropertyChangeRegistry();
    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        callbacks.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        callbacks.remove(callback);
    }

    void notifyChange() {
        callbacks.notifyCallbacks(this, 0, null);
    }

    void notifyPropertyChanged(int fieldId) {
        callbacks.notifyCallbacks(this, fieldId, null);
    }
*/
}