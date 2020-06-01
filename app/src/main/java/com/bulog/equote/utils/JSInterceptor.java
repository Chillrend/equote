package com.bulog.equote.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;
import com.bulog.equote.DrawerActivity;
import com.bulog.equote.MainActivity;
import com.bulog.equote.model.UserModel;
import com.google.gson.Gson;
import es.dmoral.toasty.Toasty;
import org.json.JSONException;
import org.json.JSONObject;

public class JSInterceptor {

    private Context ctx;
    private Gson gson;
    private SPService sharedPreferenceService;

    public JSInterceptor(Context ctx, SPService sharedPreferenceService){
        this.ctx = ctx;
        this.gson = new Gson();
        this.sharedPreferenceService = sharedPreferenceService;
    }

    @JavascriptInterface
    public void showHTML(String html){
        Log.d("RESPONSE : ", html);
        try{
            JSONObject response = new JSONObject(html);

            UserModel user = gson.fromJson(html, UserModel.class);

            if(user.getEmail() != null){
                sharedPreferenceService.setUserToSp(html);
            }

            Intent i = new Intent(ctx, DrawerActivity.class);
            ctx.startActivity(i);
            ((Activity) ctx).finish();
        }catch (JSONException e){
            //debug info only, remove this and leave the catch blank on implementatiion
            Toasty.error(ctx, "Not a valid json response", Toasty.LENGTH_LONG).show();
        }
    }
}
