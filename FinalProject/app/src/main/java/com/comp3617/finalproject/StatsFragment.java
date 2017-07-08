package com.comp3617.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;

public class StatsFragment extends Fragment {
    private Chronometer chronometer;
    private Boolean hasStart = false;
    private Button startBtn;

    public StatsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_stats, container, false);
        startBtn = (Button) fragmentView.findViewById(R.id.startBtn);

        chronometer = (Chronometer) fragmentView.findViewById(R.id.duration);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hasStart){
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
                    startBtn.setText("STOP WORKOUT");
                    hasStart = true;
                    test();
                }else{
                    chronometer.stop();
                    test1();
                    Intent intent = new Intent(getContext(),ResultActivity.class);
                    startActivity(intent);
                }
            }
        });

        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        startBtn.setText("START WORKOUT");
        hasStart = false;
        chronometer.setText("00:00");
    }

    public void test(){
        MapFragment fragment = (MapFragment) getParentFragment().getChildFragmentManager().findFragmentById(R.id.fragment2);
        fragment.startDrawing();
    }

    public void test1(){
        MapFragment fragment = (MapFragment) getParentFragment().getChildFragmentManager().findFragmentById(R.id.fragment2);
        fragment.stopDrawing();
    }
}
