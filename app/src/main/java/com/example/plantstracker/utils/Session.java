package com.example.plantstracker.utils;

import static com.example.plantstracker.utils.Constants.ID;
import static com.example.plantstracker.utils.Constants.IS_LOGGED_IN;
import static com.example.plantstracker.utils.Constants.PREFS_KEY;
import static com.example.plantstracker.utils.Constants.USER_EMAIL;
import static com.example.plantstracker.utils.Constants.USER_NAME;

import android.content.Context;
import android.content.SharedPreferences;
public class Session {
    private SharedPreferences prefs;
    public Session(Context context) {
        if (context !=null)
            prefs = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
    }

    public int getUserId() {
        return prefs.getInt(ID, 0);
    }

    public void setUserId(int userId) {
        prefs.edit().putInt(ID, userId).apply();
    }

    public String getUserName() {
        return prefs.getString(USER_NAME, "");
    }

    public void setUserName(String userName) {
        prefs.edit().putString(USER_NAME, userName).apply();
    }

    public String getUserEmail() {
        return prefs.getString(USER_EMAIL, "");
    }

    public void setUserEmail(String userEmail) {
        prefs.edit().putString(USER_EMAIL, userEmail).apply();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(IS_LOGGED_IN, false);
    }

    public void setLoggedIn(boolean isLoggedIn) {
        prefs.edit().putBoolean(IS_LOGGED_IN, isLoggedIn).apply();
    }

    public void clear() {
        prefs.edit().remove(ID).apply();
        prefs.edit().remove(USER_NAME).apply();
        prefs.edit().remove(USER_EMAIL).apply();
        prefs.edit().remove(IS_LOGGED_IN).apply();
    }
}
