package com.bulog.equote.utils;

import com.bulog.equote.model.RPKMap;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface ApiService {
    @FormUrlEncoded
    @POST("login")
    Call<JsonObject> login(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("register")
    Call<JsonObject> register(@Field("fullname") String fullname, @Field("email") String email, @Field("password") String password,
                            @Field("c_password") String cPassword, @Field("address") String address, @Field("phone") String phone, @Field("id_role") int role);


    @GET("rpk")
    Call<List<RPKMap>> searchNearestRPK(@Query("lat") double latitude, @Query("lng") double longitude);
}
