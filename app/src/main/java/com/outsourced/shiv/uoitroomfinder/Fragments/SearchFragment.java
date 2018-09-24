package com.outsourced.shiv.uoitroomfinder.Fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.outsourced.shiv.uoitroomfinder.Adapters.SearchPagerAdapter;
import com.outsourced.shiv.uoitroomfinder.R;

public class SearchFragment extends Fragment {

    Toolbar toolbar;

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_search);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        SearchPagerAdapter myPagerAdapter = new SearchPagerAdapter(getActivity().getSupportFragmentManager());

        viewPager.setAdapter(myPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}