package com.coma.go.Misc;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.coma.go.Utils.Logger;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/**
 * Created by Koma on 24.03.2018.
 */


public class AppExecutors {

    private final Executor mDiskIO;

    private final Executor mNetworkIO;

    private final Executor mMainThread;

    int threadCount = 5;

    private AppExecutors(Executor diskIO, Executor networkIO, Executor mainThread) {
        this.mDiskIO = diskIO;
        this.mNetworkIO = networkIO;
        this.mMainThread = mainThread;
    }

    public AppExecutors() {
        this.mDiskIO  = Executors.newSingleThreadExecutor();

        try{
            threadCount = Runtime.getRuntime().availableProcessors() + 1;
            Logger.d("AppExecutors", "Threads from device:" + (threadCount - 1));
        }catch (Exception e){
            Logger.wtf("AppExecutors", e.getMessage(), e);
        }

        this.mNetworkIO = Executors.newFixedThreadPool(threadCount);
        this.mMainThread = new MainThreadExecutor();
    }

    public Executor diskIO() {
        return mDiskIO;
    }

    public Executor networkIO() {
        return mNetworkIO;
    }

    public Executor mainThread() {
        return mMainThread;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}