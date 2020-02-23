package com.arie.onlineloan;

import android.content.Context;
import android.content.SharedPreferences;

import com.arie.onlineloan.models.User;

public class UserPreference {

    private static final String PREFS_NAME = "user_pref";
    private static final String USER_ID = "userId";
    private static final String USER_FULLNAME = "userName";
    private static final String USER_EMAIL = "userEmail";
    private static final String USER_NIK = "useNik";
    private static final String USER_ADDRESS = "userAddress";
    private static final String USER_PHONE = "userPhone";
    private static final String USER_ROLE = "userRole";
    private static final String USER_PASSWORD = "userPassword";


    private final SharedPreferences preferences;

    public UserPreference(Context context) {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void setUser(User value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_ID, value.getUserId());
        editor.putString(USER_PASSWORD, value.getPassword());
        editor.putString(USER_FULLNAME, value.getFullname());
        editor.putString(USER_NIK, value.getNik());
        editor.putString(USER_EMAIL, value.getEmail());
        editor.putString(USER_ADDRESS, value.getAddress());
        editor.putString(USER_PHONE, value.getPhone());
       /* editor.putString(USER_ROLE, value.getUserRole());
        */

        editor.apply();
    }

    public User getUser() {
        User model = new User();
        model.setUserId(preferences.getString(USER_ID, ""));
        model.setPassword(preferences.getString(USER_PASSWORD, ""));
        model.setFullname(preferences.getString(USER_FULLNAME, ""));
        model.setNik(preferences.getString(USER_NIK, ""));
        model.setEmail(preferences.getString(USER_EMAIL, ""));
        model.setAddress(preferences.getString(USER_ADDRESS, ""));
        model.setPhone(preferences.getString(USER_PHONE, ""));
      /*  model.setUserRole(preferences.getString(USER_ROLE, ""));
       */

        return model;
    }

    public void logoutUser() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}
