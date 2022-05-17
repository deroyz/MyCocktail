package com.example.mycocktail.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycocktail.R;
import com.example.mycocktail.adapter.DrinkAdapter;
import com.example.mycocktail.network.RetrofitClient;
import com.example.mycocktail.network.RetrofitInterface;
import com.example.mycocktail.network.drinkmodel.Drink;
import com.example.mycocktail.network.drinkmodel.DrinksResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopularFragment extends Fragment implements DrinkAdapter.DrinkItemClickListener {

    private static final String LOG_TAG = PopularFragment.class.getSimpleName();

    private View mView;

    private RetrofitClient mRetrofitClient;
    private RetrofitInterface mRetrofitInterface;

    private RecyclerView mRecyclerView;
    private DrinkAdapter mDrinkAdapter;

    private List<Drink> mDrinks;
    private Context mContext;

    public static PopularFragment newInstance() {

        PopularFragment popularFragment = new PopularFragment();

        return popularFragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getPopularCocktails();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_popular, container, false);

        setupUi();

        return mView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        super.onViewCreated(view, savedInstanceState);

    }


    private void setupUi() {

        Log.e(LOG_TAG, "Recyclerview init in popular fragment");

        mRecyclerView = mView.findViewById(R.id.rv_drinks);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);

        mDrinkAdapter = new DrinkAdapter (mDrinks, this, mContext, getActivity());
        mRecyclerView.setAdapter(mDrinkAdapter);

    }

    private void getPopularCocktails() {
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
    }

    @Override
    public void onItemClickListener(int itemId) {

    }
}
