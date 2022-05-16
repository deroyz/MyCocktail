package com.example.mycocktail.adapter;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.mycocktail.fragment.MyLogFragment;
import com.example.mycocktail.fragment.FavoriteFragment;
import com.example.mycocktail.fragment.LatestFragment;
import com.example.mycocktail.fragment.PopularFragment;
import com.example.mycocktail.fragment.TodaysFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {


    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MyLogFragment.newInstance();
            case 1:
                return TodaysFragment.newInstance();
            case 2:
                return PopularFragment.newInstance();
            case 3:
                return LatestFragment.newInstance();
            case 4:
                return FavoriteFragment.newInstance();
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
                return "MY LOGS";
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
