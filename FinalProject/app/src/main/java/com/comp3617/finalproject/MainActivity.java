package com.comp3617.finalproject;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
//        LocationRecord l = new LocationRecord();
//        l.addWpt(49.2838482,-123.114134,1.0);
//        l.endDocument();
//        l.saveFile(getApplicationContext());
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setTabTitleTextAppearance(R.style.bottomBarTextView);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_track) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.placeholder, new MainFragment());

                    transaction.commit();
                }else if(tabId == R.id.tab_route) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.placeholder, new RouteFragment());

                    transaction.commit();
                }
            }
        });
    }

}
