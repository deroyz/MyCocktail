package com.example.mycocktail.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.mycocktail.R;
import com.example.mycocktail.adapter.ViewPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private FragmentPagerAdapter fragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e(LOG_TAG, "onCreate");

        setContentView(R.layout.activity_main);

        getSupportActionBar().setElevation(0f);

        setupViewPager();
        setupUi();

    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setQueryHint("Search by Name and Ingredient");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("searchQuery", query);
                Log.e(LOG_TAG, query);
                startActivity(intent);
                Log.e(LOG_TAG, query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        return true;
    }


    private void setupUi() {

        FloatingActionButton fabAddLogs = findViewById(R.id.fab_add_logs);

        fabAddLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addLogsIntent = new Intent(MainActivity.this, AddLogActivity.class);
                startActivity(addLogsIntent);
            }
        });
    }

    private void setupViewPager() {

        ViewPager viewPager = findViewById(R.id.vp_main);
        TabLayout tabLayout = findViewById(R.id.tab_main);
        fragmentPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

}