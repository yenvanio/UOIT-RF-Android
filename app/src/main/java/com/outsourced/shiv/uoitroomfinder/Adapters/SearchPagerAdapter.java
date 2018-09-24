package com.outsourced.shiv.uoitroomfinder.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.outsourced.shiv.uoitroomfinder.Fragments.SearchRoomFragment;
import com.outsourced.shiv.uoitroomfinder.Fragments.SearchTimeFragment;

public class SearchPagerAdapter extends FragmentStatePagerAdapter {

    public SearchPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return SearchTimeFragment.newInstance();
            case 1: return SearchRoomFragment.newInstance();
        }
        return null;
    }
    @Override
    public int getCount() {
        return 2;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return "Time";
            case 1: return "Room";
            default: return null;
        }
    }
}
