package com.bulog.equote.utils;

import android.content.Context;
import android.content.SharedPreferences;
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

    //TODO: Bikin pojo isi user detail, ganti String disini ke JSON
    public String getTokenFromSp(){
        return user.getString(USER_STRING, null);
    }

    //TODO: Set JSON to SP
    public void setUserToSp(String obj){
        user.edit().putString(USER_STRING, obj).apply();
    }
}
