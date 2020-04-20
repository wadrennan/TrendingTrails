package com.example.trendingtrails.Trails;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class TrailsTabsViewPagerAdapter extends FragmentPagerAdapter {
    static final String TAG = TrailsTabsViewPagerAdapter.class.getSimpleName();
    ArrayList<Fragment> fragments;

    public static final int NEAR_ME = 1;
    public static final int POPULAR = 0;
    public static final String UI_TAB_NEAR_ME = "All Trails";
    public static final String UI_TAB_POPULAR = "Trending Trails";

    public TrailsTabsViewPagerAdapter(@NonNull FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public CharSequence getPageTitle(int position) {
        switch (position) {
            case NEAR_ME:
                return UI_TAB_NEAR_ME;
            case POPULAR:
                return UI_TAB_POPULAR;
            default:
                break;
        }
        return null;
    }
}
