package com.prantology.birthdayhelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DefaultSettings {
    private static SharedPreferences sharedPreferences;

    private static void getSharedPreferencesInstance(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static boolean notificationEnanbled(Context context) {
        getSharedPreferencesInstance(context);
        return sharedPreferences.getBoolean("birth_notification", true);
    }


    public static String getDefaultMessage(Context context){
        getSharedPreferencesInstance(context);
        return sharedPreferences.getString("default_msg", "Happy Birthday");
    }

    public static boolean splashScreenEnabled(Context context){
        getSharedPreferencesInstance(context);
        return sharedPreferences.getBoolean("splash", false);
    }

    public static boolean getSortByName(Context context){
        getSharedPreferencesInstance(context);
        return sharedPreferences.getBoolean("sort", false);
    }

}
