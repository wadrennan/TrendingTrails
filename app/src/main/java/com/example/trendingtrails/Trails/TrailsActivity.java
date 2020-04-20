package com.example.trendingtrails.Trails;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.trendingtrails.BaseActivity;
import com.example.trendingtrails.Location.LocationTrack;
import com.example.trendingtrails.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class TrailsActivity extends BaseActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    ArrayList<Fragment> fragments;
    TrailsTabsViewPagerAdapter myViewPageAdapter;
    LocationTrack lt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trails);
        tabLayout = findViewById(R.id.trailstab);
        viewPager = findViewById(R.id.trailsviewpager);
        fragments = new ArrayList<>();
        fragments.add(new PopularTrailsFragment());
        fragments.add(new TrailsNearMeFragment());
        myViewPageAdapter = new TrailsTabsViewPagerAdapter(this.getSupportFragmentManager(), fragments);
        viewPager.setAdapter(myViewPageAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }
}
