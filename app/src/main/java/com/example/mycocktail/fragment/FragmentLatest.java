package com.example.mycocktail.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mycocktail.R;

public class FragmentLatest extends Fragment {

    private View view;

    public static FragmentLatest newInstance(){
        FragmentLatest fragmentLatest = new FragmentLatest();
        return fragmentLatest;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_latest, container, false);
        return view;
    }
}
