package com.example.mycocktail.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mycocktail.R;

public class FavoriteFragment extends Fragment {

    private View view;

    public static FavoriteFragment newInstance(){

        FavoriteFragment favoriteFragment = new FavoriteFragment();

        return favoriteFragment;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_favorite, container, false);

        return view;

    }
}