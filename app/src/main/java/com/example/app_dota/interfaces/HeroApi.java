package com.example.app_dota.interfaces;

import com.example.app_dota.model.Heroes;
import com.example.app_dota.model.Users;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HeroApi {

    @GET("heroStats")
    Call<List<Heroes>> getHeroes();

}
