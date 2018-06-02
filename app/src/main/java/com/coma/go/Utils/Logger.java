package com.coma.go.Utils;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

/**
 * Created by Koma on 29.03.2018.
 */

public class Logger {
    private static boolean DEBUG =  true;// world.proteus.proteusapp.BuildConfig.DEBUG;

    public static void i(String tag, String msg){
        if(DEBUG)
            Log.i(tag, msg);
        logToCrashlytics(Log.INFO, tag, msg);
    }

    public static void i(String tag, String msg, Throwable throwable){
        if(DEBUG)
            Log.i(tag, "" + msg, throwable);
    }

    public static void v(String tag, String msg){
        if(DEBUG)
            Log.v(tag, "" + msg);
    }



    public static void d(String tag, String msg){
        if(DEBUG)
            Log.d(tag, "" + msg);
    }

    public static void d(String tag, String msg, Throwable throwable){
        if(DEBUG)
            Log.d(tag, "" + msg, throwable);
    }

    public static void w(String tag, String msg){
        if(DEBUG)
            Log.w(tag,"" +  msg);
        logToCrashlytics(Log.WARN, tag, "" + msg);
    }

    public static void e(String tag, String msg){
        if(DEBUG)
            Log.e(tag, msg);
        logToCrashlytics(Log.ERROR, tag, "" + msg);
    }

    public static void e(String tag, String msg, Throwable throwable){
        if(DEBUG)
            Log.e(tag, "" + msg, throwable);
        logToCrashlytics(throwable);
    }
    public static void e(String tag, Throwable throwable){
        if(DEBUG)
            Log.e(tag, throwable.getMessage() + "", throwable);
        logToCrashlytics(throwable);
    }
    public static void wtf(String tag, String msg){
        if(DEBUG)
            Log.wtf(tag, msg);
        logToCrashlytics(Log.ERROR, tag, "" + msg);
    }
    public static void wtf(String tag, Throwable throwable){
        if(DEBUG)
            Log.wtf(tag, throwable.getMessage());
        logToCrashlytics(Log.ERROR, tag, throwable.getMessage());
    }

    public static void wtf(String tag, String msg, Throwable throwable){
        if(DEBUG)
            Log.wtf(tag, "" + msg, throwable);
        logToCrashlytics(throwable);
    }



    private static void logToCrashlytics(Throwable throwable){
        try{
            Crashlytics.logException(throwable);
        }catch (Exception e){
            wtf("logToCrash", e.toString());
        }
    }
    private static void logToCrashlytics(int code, String tag, String msg){
        try{
            Crashlytics.log(code, tag, "" + msg);
        }catch (Exception e){
            wtf("logToCrash", e.toString());
        }
    }

}
