package com.example.mycocktail.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mycocktail.R;

public class FragmentTodays extends Fragment {

    private View view;

    public static FragmentTodays newInstance(){
        FragmentTodays fragmentTodays = new FragmentTodays();
        return fragmentTodays;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_todays, container, false);
        return view;
    }
}
