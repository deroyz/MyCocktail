package com.example.mycocktail.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mycocktail.data.FavoriteDatabase;
import com.example.mycocktail.data.FavoriteEntry;
import com.example.mycocktail.data.LogEntry;

import java.util.List;

public class FavoriteViewModel extends ViewModel {


    private static final String LOG_TAG = FavoriteViewModel.class.getSimpleName();


    public LiveData<List<FavoriteEntry>> favoriteList = new MutableLiveData<List<FavoriteEntry>>();


    public void onCreate(FavoriteDatabase mFavoriteDatbase) {

        favoriteList = mFavoriteDatbase.favoriteDao().loadALLFavorites();

    }



    public LiveData<List<FavoriteEntry>> getFavoriteList() {

        return favoriteList;

    }

    public void setFavoriteList(LiveData<List<FavoriteEntry>> favoriteList) {
        this.favoriteList = favoriteList;
    }

}
