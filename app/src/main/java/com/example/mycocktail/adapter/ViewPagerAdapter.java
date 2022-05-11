package com.example.mycocktail.adapter;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.mycocktail.fragment.FragmentMyLog;
import com.example.mycocktail.fragment.FragmentFavorite;
import com.example.mycocktail.fragment.FragmentLatest;
import com.example.mycocktail.fragment.FragmentPopular;
import com.example.mycocktail.fragment.FragmentTodays;

public class ViewPagerAdapter extends FragmentPagerAdapter {


    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return FragmentMyLog.newInstance();
            case 1:
                return FragmentTodays.newInstance();
            case 2:
                return FragmentPopular.newInstance();
            case 3:
                return FragmentLatest.newInstance();
            case 4:
                return FragmentFavorite.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 5;
    }

    //
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "ALL";
            case 1:
                return "TODAYS";
            case 2:
                return "POPULAR";
            case 3:
                return "LATEST";
            case 4:
                return "FAVORITE";
            default:
                return null;
        }
    }

}
