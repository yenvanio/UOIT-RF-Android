package com.outsourced.shiv.uoitroomfinder.Fragments;

import android.animation.Animator;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Movie;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.outsourced.shiv.uoitroomfinder.Adapters.HelpAdapter;
import com.outsourced.shiv.uoitroomfinder.Models.Help;
import com.outsourced.shiv.uoitroomfinder.R;

import java.util.ArrayList;
import java.util.List;

public class HelpFragment extends Fragment {

    boolean isFabOpen = false;
    Toolbar toolbar;
    com.github.clans.fab.FloatingActionButton mail, devpage, website, star;
    FloatingActionMenu fam;

    private List<Help> helpList = new ArrayList<>();
    private RecyclerView recyclerView;
    private HelpAdapter mAdapter;

    private AdView mAdView;

    public static HelpFragment newInstance() {
        HelpFragment fragment = new HelpFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);

        setUpAds(view);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.faq_title);

        fam = (FloatingActionMenu) view.findViewById(R.id.fab_menu);

        mail = view.findViewById(R.id.fab1);
        devpage = view.findViewById(R.id.fab2);
        website = view.findViewById(R.id.fab3);
        star = view.findViewById(R.id.fab4);

        fam.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened) {
                    mail.setOnClickListener(onButtonClick());
                    devpage.setOnClickListener(onButtonClick());
                    website.setOnClickListener(onButtonClick());
                    star.setOnClickListener(onButtonClick());
                } else {
                    mail.setOnClickListener(null);
                    devpage.setOnClickListener(null);
                    website.setOnClickListener(null);
                    star.setOnClickListener(null);
                }
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.rv);

        mAdapter = new HelpAdapter(helpList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        prepareHelpData();
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    private void prepareHelpData() {
        Help faq1 = new Help(getString(R.string.does_do_title), getString(R.string.does_do));
        helpList.add(faq1);
        Help faq2 = new Help(getString(R.string.does_not_do_title), getString(R.string.does_not_do));
        helpList.add(faq2);
        Help faq3 = new Help(getString(R.string.use_title), getString(R.string.use));
        helpList.add(faq3);
    }

    private View.OnClickListener onButtonClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == mail) {
                    final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

                    emailIntent.setType("plain/text");
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"yoganathan.shiv@gmail.com"});
                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "UOIT Room Finder Feedback");
                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hey, I had some feedback regarding your Room Finder!");

                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                } else if (view == devpage) {
                    Uri uri = Uri.parse("https://play.google.com/store/apps/dev?id=5450137585172063918");
                    Intent goToDevPage = new Intent(Intent.ACTION_VIEW, uri);
                    // To count with Play market backstack, After pressing back button,
                    // to taken back to our application, we need to add following flags to intent.
                    goToDevPage.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    try {
                        startActivity(goToDevPage);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/dev?id=5450137585172063918")));
                    }
                } else if (view == website) {
                    Uri uri = Uri.parse("http://yshiv.com");
                    Intent goToDevPage = new Intent(Intent.ACTION_VIEW, uri);
                    // To count with Play market backstack, After pressing back button,
                    // to taken back to our application, we need to add following flags to intent.
                    goToDevPage.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    try {
                        startActivity(goToDevPage);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http:yshiv.com")));
                    }
                } else if (view == star) {

                    Uri uri = Uri.parse("market://details?id=" + getActivity().getApplicationContext().getPackageName());
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    // To count with Play market backstack, After pressing back button,
                    // to taken back to our application, we need to add following flags to intent.
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    try {
                        startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getApplicationContext().getPackageName())));
                    }
                }
                fam.close(true);
            }
        };
    }

    public void setUpAds(View view) {

        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(getActivity(), "ca-app-pub-2173238213882820~7350740510");

        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}