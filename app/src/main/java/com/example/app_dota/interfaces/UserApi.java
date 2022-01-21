package com.example.app_dota.interfaces;

import com.example.app_dota.model.Users;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserApi {

    @GET("users")
    Call<List<Users>> getUser(@Query("email") String email);

    @POST("users")
    Call<Users> createUser(@Body Users users);
}
