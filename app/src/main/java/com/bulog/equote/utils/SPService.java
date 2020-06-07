package com.bulog.equote.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.bulog.equote.model.UserModel;
import com.google.gson.Gson;

public class SPService {
    private static final String USER_SP = "USER_SHARED_PREFERENCES";

    private static final String USER_STRING = "user";

    private Context ctx;
    private SharedPreferences user;
    private Gson gson = new Gson();

    public SPService(Context ctx){
        this.ctx = ctx;
        user = ctx.getSharedPreferences(USER_SP, Context.MODE_PRIVATE);
    }

    public Boolean isUserLoggedIn(){
        return user.contains(USER_STRING);
    }

    public UserModel getUserFromSp(){
        return gson.fromJson(user.getString(USER_STRING, null), UserModel.class);
    }

    public void setUserToSp(String obj){
        user.edit().putString(USER_STRING, obj).apply();
    }

    public void doLogout(){
        //For now we do logout from device only by deleting the user cache
        //Doing this in a long run may raise a huge security concern
        //TODO: Make an API Call to revoke the access token from the backend
        user.edit().clear().apply();
    }
}
