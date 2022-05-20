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

public class TodaysFragment extends Fragment {

    private static final String LOG_TAG = TodaysFragment.class.getSimpleName();

    private View view;

    public static TodaysFragment newInstance(){
        TodaysFragment todaysFragment = new TodaysFragment();
        return todaysFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.e(LOG_TAG, "onCreateView");

        view = inflater.inflate(R.layout.fragment_todays, container, false);
        return view;
    }
}
