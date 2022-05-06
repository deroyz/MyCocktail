package com.example.mycocktail;


import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.mycocktail.network.RetrofitClient;
import com.example.mycocktail.network.RetrofitInterface;
import com.example.mycocktail.network.drinkmodel.Drink;
import com.example.mycocktail.network.drinkmodel.DrinksResult;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private FragmentPagerAdapter fragmentPagerAdapter;

    private RetrofitClient mRetrofitClient;
    private RetrofitInterface mRetrofitInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpViewPager();


        mRetrofitClient = RetrofitClient.getRetrofitClient();
        mRetrofitInterface = RetrofitClient.getRetrofitInterface();

        Log.e(LOG_TAG, "DisplayName: ");

        mRetrofitInterface.getLatestDrinks().enqueue(new Callback<DrinksResult>() {

            @Override
            public void onResponse(Call<DrinksResult> call, Response<DrinksResult> response) {

                DrinksResult drinksResult = response.body();

                List<Drink> drinks = drinksResult.getDrinks();

                String printMessage = "HI";

                Log.e(LOG_TAG, "DisplayName: " + printMessage);

                for (int i = 0; i < drinks.size(); i++) {

                    printMessage = drinks.get(i).getStrDrink();

                    Log.e(LOG_TAG, "DisplayName: " + printMessage);

                }

            }

            @Override
            public void onFailure(Call<DrinksResult> call, Throwable t) {
                Log.e(LOG_TAG, "Connection Fail" );

            }


        });




    }

    private void setUpViewPager () {
        ViewPager viewPager = findViewById(R.id.vp_main);
        TabLayout tabLayout = findViewById(R.id.tab_main);
        fragmentPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}