package com.comp3617.finalproject;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends Activity {
    private MainFragment mapFragment= new MainFragment();
    private RouteFragment routeFragment = new RouteFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.placeholder, mapFragment);
        transaction.commit();

        final FrameLayout map =(FrameLayout) findViewById(R.id.placeholder);
        final FrameLayout route = (FrameLayout) findViewById(R.id.placeholder1);
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setTabTitleTextAppearance(R.style.bottomBarTextView);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(int tabId) {
                if (tabId == R.id.tab_track) {
                    map.bringToFront();
                } else if (tabId == R.id.tab_route) {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.placeholder1, routeFragment);
                    transaction.commit();
                    route.bringToFront();
                }
            }
        });
    }
}