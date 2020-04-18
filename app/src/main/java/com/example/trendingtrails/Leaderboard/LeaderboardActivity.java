package com.example.trendingtrails.Leaderboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.trendingtrails.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class LeaderboardActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    ArrayList<Fragment> fragments;
    LeaderboardTabsViewPagerAdapter myViewPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        tabLayout = findViewById(R.id.leaderboardtab);
        viewPager = findViewById(R.id.leaderboardviewpager);
        fragments = new ArrayList<>();
        fragments.add(new MonthlyLeaderboardFragment());
        fragments.add(new WeeklyLeaderboardFragment());
        fragments.add(new DailyLeaderboardFragment());
        myViewPageAdapter = new LeaderboardTabsViewPagerAdapter(this.getSupportFragmentManager(), fragments);
        viewPager.setAdapter(myViewPageAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }
}
