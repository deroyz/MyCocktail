package com.example.mycocktail.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mycocktail.R;

public class SearchFragment extends Fragment {

    private static final String LOG_TAG = SearchFragment.class.getSimpleName();

    private View view;

    public static SearchFragment newInstance(){

        SearchFragment searchFragment = new SearchFragment();

        return searchFragment;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.e(LOG_TAG, "onCreateView");

        view = inflater.inflate(R.layout.fragment_all, container, false);

        return view;

    }

}
