package com.arie.onlineloan;

import android.content.Context;
import android.content.SharedPreferences;

import com.arie.onlineloan.models.User;

public class UserPreference {

    private static final String PREFS_NAME = "user_pref";
    private static final String USER_ID = "userId";
    private static final String USER_FULLNAME = "userName";
    private static final String USER_EMAIL = "userEmail";
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
        editor.putString(USER_PASSWORD, value.getUserPassword());
        editor.putString(USER_FULLNAME, value.getUserFullName());
        editor.putString(USER_EMAIL, value.getUserEmail());
        editor.putString(USER_ADDRESS, value.getUserAddress());
        editor.putString(USER_PHONE, value.getUserPhone());
       /* editor.putString(USER_ROLE, value.getUserRole());
        */

        editor.apply();
    }

    public User getUser() {
        User model = new User();
        model.setUserId(preferences.getString(USER_ID, ""));
        model.setUserPassword(preferences.getString(USER_PASSWORD, ""));
        model.setUserFullName(preferences.getString(USER_FULLNAME, ""));
        model.setUserEmail(preferences.getString(USER_EMAIL, ""));
        model.setUserAddress(preferences.getString(USER_ADDRESS, ""));
        model.setUserPhone(preferences.getString(USER_PHONE, ""));
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
