package com.example.trendingtrails.Profile;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ProfileTabsViewPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = ProfileTabsViewPagerAdapter.class.getSimpleName();
    private ArrayList<Fragment> fragments;

    public static final int USER_INFO = 0;
    public static final int COMPLETED_TRAILS = 1;
    public static final int ADDED_TRAILS = 2;
    public static final String UI_TAB_USER = "User Info";
    public static final String UI_TAB_COMPLETED = "Completed Trails";
    public static final String UI_TAB_ADDED = "Added Trails";

    public ProfileTabsViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments){
        super(fm);
        this.fragments = fragments;
    }

    // Return the Fragment associated with a specified position.
    public Fragment getItem(int pos){
        Log.d(TAG, "getItem " + "position" + pos);
        return fragments.get(pos);
    }

    // Return the number of views available
    public int getCount(){
        Log.d(TAG, "getCount " + "size " + fragments.size());
        return fragments.size();
    }

    // This method may be called by the ViewPager to obtain a title string
    // to describe the specified page
    public CharSequence getPageTitle(int position) {
        Log.d(TAG, "getPageTitle " + "position " + position);
        switch (position) {
            case USER_INFO:
                return UI_TAB_USER;
            case COMPLETED_TRAILS:
                return UI_TAB_COMPLETED;
            case ADDED_TRAILS:
                return UI_TAB_ADDED;
            default:
                break;
        }
        return null;
    }
}