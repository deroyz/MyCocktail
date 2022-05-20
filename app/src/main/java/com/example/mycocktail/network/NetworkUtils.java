package com.example.mycocktail.network;

import android.util.Log;

import com.example.mycocktail.fragment.MyLogFragment;
import com.example.mycocktail.network.drinkmodel.Drink;
import com.example.mycocktail.network.drinkmodel.DrinksResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();


    private RetrofitClient mRetrofitClient;
    private RetrofitInterface mRetrofitInterface;
    private List<Drink> mDrinks;

    public List<Drink> getPopularDrinks() {

        if(mDrinks !=null){
            mDrinks = null;
        }
        mRetrofitClient = RetrofitClient.getRetrofitClient();
        mRetrofitInterface = RetrofitClient.getRetrofitInterface();

        Log.e(LOG_TAG, "Network Connection Start Using Retrofit ");

        mRetrofitInterface.getLatestDrinks().enqueue(new Callback<DrinksResult>() {

            @Override
            public void onResponse(Call<DrinksResult> call, Response<DrinksResult> response) {

                DrinksResult drinksResult = response.body();

                mDrinks = drinksResult.getDrinks();

                String printMessage = "Connection Success";

                Log.e(LOG_TAG, "DisplayName: " + printMessage);

                for (int i = 0; i < mDrinks.size(); i++) {

                    printMessage = mDrinks.get(i).getStrDrink();

                    Log.e(LOG_TAG, "DisplayName: " + printMessage);

                }

            }

            @Override
            public void onFailure(Call<DrinksResult> call, Throwable t) {
                Log.e(LOG_TAG, "Connection Fail" + t.toString());

            }
        });

        return mDrinks;
    }

}
