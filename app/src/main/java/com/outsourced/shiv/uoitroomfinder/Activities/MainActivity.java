package com.outsourced.shiv.uoitroomfinder.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.outsourced.shiv.uoitroomfinder.Fragments.HelpFragment;
import com.outsourced.shiv.uoitroomfinder.Fragments.HomeFragment;
import com.outsourced.shiv.uoitroomfinder.Fragments.SearchFragment;
import com.outsourced.shiv.uoitroomfinder.R;

public class MainActivity extends AppCompatActivity {

  private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
    = new BottomNavigationView.OnNavigationItemSelectedListener() {

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
      Fragment selectedFragment = null;
      switch (item.getItemId()) {
        case R.id.navigation_home:
          selectedFragment = HomeFragment.newInstance();
          break;
        case R.id.navigation_search:
          selectedFragment = SearchFragment.newInstance();
          break;
        case R.id.navigation_help:
          selectedFragment = HelpFragment.newInstance();
          break;
      }
      FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
      transaction.replace(R.id.fragment_container, selectedFragment);
      transaction.commit();
      return true;
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.fragment_container, HomeFragment.newInstance());
    transaction.commit();

  }

}
