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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycocktail.AppExecutors;
import com.example.mycocktail.R;
import com.example.mycocktail.adapter.DrinkAdapter;
import com.example.mycocktail.data.FavoriteDatabase;
import com.example.mycocktail.data.FavoriteEntry;
import com.example.mycocktail.network.RetrofitClient;
import com.example.mycocktail.network.RetrofitInterface;
import com.example.mycocktail.network.datamodel.Drink;

import java.util.List;

public class FavoriteFragment extends Fragment implements DrinkAdapter.DrinkAdapterListener {

    private static final String LOG_TAG = FavoriteFragment.class.getSimpleName();

    private View mView;

    private RecyclerView mRecyclerView;
    private DrinkAdapter mDrinkAdapter;

    private RetrofitClient mRetrofitClient;
    private RetrofitInterface mRetrofitInterface;


    private List<Drink> mDrinks;
    private Context mContext;

    FavoriteDatabase favoriteDatabase;

    public static FavoriteFragment newInstance() {

        FavoriteFragment favoriteFragment = new FavoriteFragment();

        return favoriteFragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        Log.e(LOG_TAG, "onCreate");

        super.onCreate(savedInstanceState);

        mContext = getActivity().getApplicationContext();
        favoriteDatabase = FavoriteDatabase.getInstance(mContext);

        setupDataModel();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.e(LOG_TAG, "onCreateView");

        mView = inflater.inflate(R.layout.fragment_popular, container, false);

        setupUi();

        return mView;

    }

    private void setupUi() {

        Log.e(LOG_TAG, "setupUi");

        mRecyclerView = mView.findViewById(R.id.rv_popular_drinks);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);

        mDrinkAdapter = new DrinkAdapter(mDrinks, this, mContext, getActivity());
        mRecyclerView.setAdapter(mDrinkAdapter);

    }

    private void setupDataModel() {

        Log.e(LOG_TAG, "setupDataModel");

        if (mDrinks != null) {
            mDrinks = null;
        }

        AppExecutors.getInstance().diskIO().execute(new Runnable() {

            @Override
            public void run() {

                List<FavoriteEntry> favoriteEntries = favoriteDatabase.favoriteDao().loadAllFavorites();

                Log.e(LOG_TAG, "setupDataModel loaded favorites successfully");

                mDrinks = FavoriteConverter(favoriteEntries);


            }
        });

        mDrinkAdapter.setDrinks(mDrinks);

    }

    private List<Drink> FavoriteConverter(List<FavoriteEntry> favoriteEntries) {

        int favoriteSize = favoriteEntries.size();

        List<Drink> drinkEntries = null;

        Log.e(LOG_TAG, "Favorite data size: " + favoriteSize);

        for (int i = 0; i < favoriteSize; i++) {


            FavoriteEntry favoriteEntry = favoriteDatabase.favoriteDao().loadAllFavorites().get(i);
            Drink drinkEntry = null;
            drinkEntry.setStrDrink(favoriteEntry.getStrDrink());
            drinkEntry.setStrDrinkThumb(favoriteEntry.getStrDrinkThumb());
            drinkEntry.setIdDrink(favoriteEntry.getIdDrink());

            Log.e(LOG_TAG, drinkEntry.getStrDrink());
            Log.e(LOG_TAG, drinkEntry.getIdDrink());
            Log.e(LOG_TAG, drinkEntry.getStrDrinkThumb());

            drinkEntries.add(drinkEntry);
        }

        return drinkEntries;
    }

    /*
    private void setupViewModel() {

        FavoriteViewModel favoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);

        Log.e(LOG_TAG, "Creating instance of FavoriteViewModel");

        favoriteViewModel.onCreate(favoriteDatabase);

        favoriteViewModel.getFavoriteList().observe(getViewLifecycleOwner(), new Observer<List<FavoriteEntry>>() {

            @Override
            public void onChanged(@Nullable List<FavoriteEntry> favoriteEntries) {

                Log.e(LOG_TAG, "Updating list of favorite drinks from LiveData in ViewModel");

               List<Drink> drinkEntries = FavoriteConverter(favoriteEntries);

                mDrinkAdapter.setDrinks(drinkEntries);

            }
        });
    }

     */

    @Override
    public void onItemClickListener(int itemId) {

    }

    @Override
    public void recipeOnclick(View v, int position) {

    }

    @Override
    public void addLogOnClick(View v, int position) {

    }

    @Override
    public void favoriteOnClick(View v, int position) {

    }
}
