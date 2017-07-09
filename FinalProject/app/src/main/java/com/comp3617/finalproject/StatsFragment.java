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
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

public class StatsFragment extends Fragment implements Observer{
    private Chronometer chronometer;
    private Boolean hasStart = false;
    private Button startBtn;
    private TextView distance;
    private MapFragment fragment;
    private LocationServices locationServices;
    public StatsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View fragmentView = inflater.inflate(R.layout.fragment_stats, container, false);
        startBtn = (Button) fragmentView.findViewById(R.id.startBtn);
        chronometer = (Chronometer) fragmentView.findViewById(R.id.duration);
        distance = (TextView) fragmentView.findViewById(R.id.distance);
        fragment = (MapFragment) getParentFragment().getChildFragmentManager().findFragmentById(R.id.main_map_fragment);
        locationServices = new LocationServices(fragment,getContext(),this);

        fragment.setLocationServices(locationServices);

        startBtn.setOnClickListener(onstartBtnClick());

        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        startBtn.setText(R.string.stats_startworkout);
        hasStart = false;
        chronometer.setText(R.string.stats_durationText);
        distance.setText(R.string.stats_distanceText);
    }

    public View.OnClickListener onstartBtnClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!hasStart){
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
                    startBtn.setText(R.string.stats_stopworkout);
                    hasStart = true;
                    locationServices.start();
                }else{
                    chronometer.stop();
                    locationServices.stop();
                    fragment.map.clear();
                    Intent intent = new Intent(getContext(),ResultActivity.class);
                    startActivity(intent);
                }
            }
        };
    }

    @Override
    public void update(Observable observable, Object o) {
        distance.setText(locationServices.getDistance());
    }
}
