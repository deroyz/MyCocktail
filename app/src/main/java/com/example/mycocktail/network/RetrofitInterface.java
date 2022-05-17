package com.example.mycocktail.network;

import com.example.mycocktail.network.drinkmodel.DrinksResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitInterface  {

    @GET("api/json/v2/9973533/randomselection.php")
    Call<DrinksResult> getRandomSelectionDrinks ();

    @GET("api/json/v2/9973533/popular.php")
    Call<DrinksResult> getPopularDrinks();

    @GET("api/json/v2/9973533/search.php?s=margarita")
    Call<DrinksResult> getLatestDrinks ();

}
