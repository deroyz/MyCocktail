package com.example.mycocktail.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mycocktail.R;

public class FragmentPopular extends Fragment {

    private View view;

    public static FragmentPopular newInstance(){
        FragmentPopular fragmentPopular = new FragmentPopular();
        return fragmentPopular;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_popular, container, false);
        return view;
    }
}
