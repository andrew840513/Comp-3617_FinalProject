package com.comp3617.finalproject;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.comp3617.finalproject.com.comp3617.finalproject.gpx.GPX;
import com.comp3617.finalproject.com.comp3617.finalproject.gpx.WPT;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        LocationRecord l = new LocationRecord(getApplicationContext());
        l.getListFiles();
        GPX gpx = l.readFile("1499876295507Test.gpx");
        for (WPT wpt : gpx.getWpt()) {
            try {
                Log.d("WPT", wpt.getTime().toString());
            } catch (Exception e) {
                Log.e("WTP_err", e.getMessage());
            }
        }
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setTabTitleTextAppearance(R.style.bottomBarTextView);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_track) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.placeholder, new MainFragment());

                    transaction.commit();
                } else if (tabId == R.id.tab_route) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.placeholder, new RouteFragment());

                    transaction.commit();
                }
            }
        });
    }

}
