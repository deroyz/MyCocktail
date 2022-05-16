package com.example.mycocktail.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mycocktail.R;

public class LatestFragment extends Fragment {

    private View view;

    public static LatestFragment newInstance(){
        LatestFragment latestFragment = new LatestFragment();
        return latestFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_latest, container, false);
        return view;
    }
}
