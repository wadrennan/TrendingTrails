package com.example.trendingtrails.Profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.trendingtrails.BaseActivity;
import com.example.trendingtrails.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class ProfileActivity extends BaseActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments;
    private ProfileTabsViewPagerAdapter myViewPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        tabLayout = findViewById(R.id.profiletab);
        viewPager = findViewById(R.id.profileviewpager);
        fragments = new ArrayList<Fragment>();
        fragments.add(new UserInfoFragment());
        fragments.add(new CompletedTrailsFragment());
        fragments.add(new AddedTrailsFragment());
        myViewPageAdapter = new ProfileTabsViewPagerAdapter(this.getSupportFragmentManager(),
                fragments);
        viewPager.setAdapter(myViewPageAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }
}
