package com.example.edward.orthography;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by root on 9/10/16.
 */

public class sessionManager {

    //method to save status
    public void setPreferences(Context context, String key, String value) {

        SharedPreferences.Editor editor = context.getSharedPreferences("ortografia", Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }


    public String getPreferences(Context context, String key) {

        SharedPreferences prefs = context.getSharedPreferences("ortografia",     Context.MODE_PRIVATE);
        String position = prefs.getString(key, "");
        return position;
    }
}
