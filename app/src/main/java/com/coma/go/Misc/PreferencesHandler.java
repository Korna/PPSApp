package com.coma.go.Misc;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Koma on 18.12.2017.
 */

public class PreferencesHandler {
    public static final String APP_PREFERENCES = "System";

    public static final String APP_PREFERENCES_SESSION = "Session";//хранит ID сессии
    public static final String DEFAULT_STRING = "";

    public static final String APP_PREFERENCES_FIRSTSTART = "FirstStart";//первый ли старт
    public static final boolean DEFAULT_FIRSTSTART = true;

    public static final String APP_PREFERENCES_USERNAME = "Username";
    public static final String APP_PREFERENCES_PASSWORD = "Userpassword";



    private SharedPreferences mSettings;
    private SharedPreferences.Editor editor;

    public PreferencesHandler(Context context){
        mSettings = context.getSharedPreferences(
                APP_PREFERENCES + context.getPackageName(),//сложил тк были коллизии с другой программой и сессии друг друга сменяли
                Context.MODE_PRIVATE);
        Log.e("getContext", context.getPackageName());
        editor = mSettings.edit();
    }

    public void putValueSession(String session){
        putValue(APP_PREFERENCES_SESSION, session);
    }

    public String getValueSession(){
        String value = getValue(APP_PREFERENCES_SESSION);
        return value;
    }

    public String getUsername(){
        return getValue(APP_PREFERENCES_USERNAME);
    }

    public String getPassword(){
        return getValue(APP_PREFERENCES_PASSWORD);
    }

    public void putUsername(String username){
        putValue(APP_PREFERENCES_USERNAME, username);
    }

    public void putPassword(String password){
        putValue(APP_PREFERENCES_PASSWORD, password);
    }



    public void removeValueSession(){
        removeValue(APP_PREFERENCES_SESSION);
    }




    public void putValueStart(boolean start){
        editor.putBoolean(APP_PREFERENCES_FIRSTSTART, start);
        editor.apply();
    }

    public boolean getValueStart(){
        boolean value;
        if(mSettings.contains(APP_PREFERENCES_FIRSTSTART)) {
            value = mSettings.getBoolean(APP_PREFERENCES_FIRSTSTART, DEFAULT_FIRSTSTART);
        }else
            value = true;
        return value;
    }

    public void removeValueStart(){
        removeValue(APP_PREFERENCES_FIRSTSTART);
    }


    public void clear(){
        editor.clear();
        editor.apply();
    }


    private void putValue(String key, String value){
        editor.putString(key, value);
        editor.apply();
    }
    private String getValue(String key){
        String value = "";
        if(mSettings.contains(key)) {
            value = mSettings.getString(key, DEFAULT_STRING);
        }
        return value;
    }
    private void removeValue(String key){
        editor.remove(key);
        editor.apply();
    }
}
