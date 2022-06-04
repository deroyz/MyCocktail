package com.example.mycocktail.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycocktail.R;
import com.example.mycocktail.activity.AddLogActivity;
import com.example.mycocktail.adapter.DrinkAdapter;
import com.example.mycocktail.data.FavoriteDatabase;
import com.example.mycocktail.data.FavoriteEntry;
import com.example.mycocktail.network.RetrofitClient;
import com.example.mycocktail.network.RetrofitInterface;
import com.example.mycocktail.network.datamodel.Drink;
import com.example.mycocktail.network.datamodel.DrinksResult;
import com.example.mycocktail.viewmodel.FavoriteViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodaysFragment extends Fragment implements DrinkAdapter.DrinkAdapterListener {

    private static final String LOG_TAG = TodaysFragment.class.getSimpleName();

    private View mView;

    private RecyclerView mRecyclerView;
    private DrinkAdapter mDrinkAdapter;

    private RetrofitClient mRetrofitClient;
    private RetrofitInterface mRetrofitInterface;

    private List<Drink> mDrinks;
    private Context mContext;

    FavoriteDatabase favoriteDatabase;
    FavoriteEntry favoriteEntry;
    private List<FavoriteEntry> mFavoriteEntries;

    public static TodaysFragment newInstance() {

        TodaysFragment todaysFragment = new TodaysFragment();

        return todaysFragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        Log.e(LOG_TAG, "onCreate");

        super.onCreate(savedInstanceState);

        setupNetwork();
        favoriteDatabase = FavoriteDatabase.getInstance(getContext());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.e(LOG_TAG, "onCreateView");

        mView = inflater.inflate(R.layout.fragment_todays, container, false);

        setupUi();

        return mView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Log.e(LOG_TAG, "onViewCreated");

        super.onViewCreated(view, savedInstanceState);

        setupUi();
        setupFavoriteViewModel();

    }


    private void setupUi() {

        Log.e(LOG_TAG, "setupUi");

        mRecyclerView = mView.findViewById(R.id.rv_todays_drinks);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);

        mDrinkAdapter = new DrinkAdapter(mDrinks, mFavoriteEntries,this, mContext, getActivity());
        mRecyclerView.setAdapter(mDrinkAdapter);

    }

    private void setupNetwork() {

        if (mDrinks != null) {
            mDrinks = null;
        }

        mRetrofitClient = RetrofitClient.getRetrofitClient();
        mRetrofitInterface = RetrofitClient.getRetrofitInterface();

        Log.e(LOG_TAG, "Network Connection Attempt");

        mRetrofitInterface.getTodaysDrinks().enqueue(new Callback<DrinksResult>() {

            @Override
            public void onResponse(Call<DrinksResult> call, Response<DrinksResult> response) {

                DrinksResult drinksResult = response.body();
                mDrinks = drinksResult.getDrinks();
                mDrinkAdapter.setDrinks(mDrinks);

                Log.e(LOG_TAG, "Connection Success");

                for (int i = 0; i < mDrinks.size(); i++) {

                    String printMessage = "";
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

    private void setupFavoriteViewModel() {

        FavoriteViewModel favoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);

        Log.e(LOG_TAG, "Creating instance of FavoriteViewModel");

        favoriteViewModel.onCreate(favoriteDatabase);

        favoriteViewModel.getFavoriteList().observe(getViewLifecycleOwner(), new Observer<List<FavoriteEntry>>() {

            @Override
            public void onChanged(@Nullable List<FavoriteEntry> favoriteEntries) {

                Log.e(LOG_TAG, "Updating list of favorite drinks from LiveData in ViewModel");

                mDrinkAdapter.setFavoriteEntries(favoriteEntries);

            }
        });
    }


    @Override
    public void onItemClickListener(int itemId) {

    }

    @Override
    public void recipeOnclick(View v, int position) {

    }

    @Override
    public void addLogOnClick(View v, int position) {

        String name = mDrinks.get(position).getStrDrink();
        String imageUrl = mDrinks.get(position).getStrDrinkThumb();

        Log.e(LOG_TAG, imageUrl);
        Log.e(LOG_TAG, name);

        Intent intent = new Intent(this.getActivity(), AddLogActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("imageUrl", imageUrl);

        startActivity(intent);

    }

    @Override
    public void favoriteOnClick(View v, int position) {

    }
}