package com.example.remindme;

import android.content.Context;
import android.content.SharedPreferences;

public class sharedPreference {

    public static final String SHARED_PREF = "sharedPref";
    public static final String SWITCH1 = "switch1";
    public static Context context;

    public sharedPreference(Context context) {
        sharedPreference.context = context;
    }

    public static void saveCheck(Boolean b) {
        SharedPreferences sp = sharedPreference.context.getSharedPreferences(SHARED_PREF, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(SWITCH1, b);
        editor.apply();
    }

    public static Boolean loadCheck() {
        SharedPreferences pref = sharedPreference.context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        Boolean ret = pref.getBoolean(SWITCH1, false);
        return ret;
    }

}
