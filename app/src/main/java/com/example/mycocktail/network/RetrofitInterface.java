package com.example.mycocktail.network;

import com.example.mycocktail.network.datamodel.DrinksResult;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitInterface  {

    @GET("api/json/v2/9973533/randomselection.php")
    Call<DrinksResult> getTodaysDrinks ();

    @GET("api/json/v2/9973533/popular.php")
    Call<DrinksResult> getPopularDrinks();

    @GET("api/json/v2/9973533/search.php?s=margarita")
    Call<DrinksResult> getLatestDrinks ();

}
