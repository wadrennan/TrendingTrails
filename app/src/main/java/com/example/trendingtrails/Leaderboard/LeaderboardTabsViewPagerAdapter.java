package com.example.trendingtrails.Leaderboard;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class LeaderboardTabsViewPagerAdapter extends FragmentPagerAdapter {
    static final String TAG = LeaderboardTabsViewPagerAdapter.class.getSimpleName();
    ArrayList<Fragment> fragments;

    public static final int DAILY = 2;
    public static final int WEEKLY = 1;
    public static final int MONTHLY = 0;
    public static final String UI_TAB_DAILY = "Daily";
    public static final String UI_TAB_WEEKLY = "Weekly";
    public static final String UI_TAB_MONTHLY = "Monthly";

    public LeaderboardTabsViewPagerAdapter(@NonNull FragmentManager fm, ArrayList<Fragment> fragments) {
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
            case DAILY:
                return UI_TAB_DAILY;
            case WEEKLY:
                return UI_TAB_WEEKLY;
            case MONTHLY:
                return UI_TAB_MONTHLY;
            default:
                break;
        }
        return null;
    }
}
