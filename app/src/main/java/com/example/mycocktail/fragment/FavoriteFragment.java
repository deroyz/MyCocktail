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

import com.example.mycocktail.R;
import com.example.mycocktail.adapter.DrinkAdapter;
import com.example.mycocktail.data.FavoriteDatabase;
import com.example.mycocktail.data.FavoriteEntry;
import com.example.mycocktail.data.LogEntry;
import com.example.mycocktail.network.RetrofitClient;
import com.example.mycocktail.network.RetrofitInterface;
import com.example.mycocktail.network.datamodel.Drink;
import com.example.mycocktail.viewmodel.FavoriteViewModel;
import com.example.mycocktail.viewmodel.MyLogViewModel;

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

        if (favoriteDatabase.favoriteDao().loadALLFavorites().getValue() != null) {

            FavoriteConverter();

            mDrinkAdapter.setDrinks(mDrinks);

        } else {
            return;
        }
    }

    private void FavoriteConverter() {

        int favoriteSize = favoriteDatabase.favoriteDao().loadALLFavorites().getValue().size();

        Log.e(LOG_TAG, "Favorite data size: " + favoriteSize);

        for (int i = 0; i < favoriteSize; i++) {

            FavoriteEntry favorite = favoriteDatabase.favoriteDao().loadALLFavorites().getValue().get(i);
            Drink mDrink = null;
            mDrink.setStrDrink(favorite.getStrDrink());
            mDrink.setStrDrinkThumb(favorite.getStrDrinkThumb());
            mDrink.setIdDrink(favorite.getIdDrink());

            mDrinks.add(mDrink);

        }
    }

    private void setupViewModel() {

        FavoriteViewModel favoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);

        Log.e(LOG_TAG, "Creating instance of myLog viewModel");

        favoriteViewModel.onCreate(favoriteDatabase);

        favoriteViewModel.getFavoriteList().observe(getViewLifecycleOwner(), new Observer<List<FavoriteEntry>>() {

            @Override
            public void onChanged(@Nullable List<FavoriteEntry> favoriteEntries) {

                Log.e(LOG_TAG, "Updating list of favorite drinks from LiveData in ViewModel");

                mDrinkAdapter.setDrinks(favoriteEntries);

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

    }

    @Override
    public void favoriteOnClick(View v, int position) {

    }
}
