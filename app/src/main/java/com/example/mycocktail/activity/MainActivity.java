package com.example.mycocktail.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
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