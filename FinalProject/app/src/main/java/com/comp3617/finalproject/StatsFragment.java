package com.comp3617.finalproject;

import java.util.Observable;
import java.util.Observer;

import com.roughike.bottombar.BottomBar;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

public class StatsFragment extends Fragment implements Observer, StartWorkoutListener {
	private Chronometer chronometer;
	private Boolean hasStart = false;
	private Button startBtn;
	private TextView distance;
	private MapFragment mapFragment;
	private LocationServices locationServices;
	private StartWorkoutListener mapListener;
	private StartWorkoutListener statsListener;

	public StatsFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this mapFragment
		final View fragmentView = inflater.inflate(R.layout.fragment_stats, container, false);

		startBtn = (Button) fragmentView.findViewById(R.id.startBtn);
		chronometer = (Chronometer) fragmentView.findViewById(R.id.duration);
		distance = (TextView) fragmentView.findViewById(R.id.distance);
		mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.main_map_fragment);

		locationServices = new LocationServices(mapFragment, getActivity().getApplicationContext(), this);
		mapFragment.setLocationServices(locationServices);

		mapListener = mapFragment;
		statsListener = this;
		startBtn.setOnClickListener(onstartBtnClick());
		return fragmentView;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	public View.OnClickListener onstartBtnClick() {
		return new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!hasStart) {
					statsListener.onStartWorkout();
					hasStart = true;
				} else {
					statsListener.onStopWorkout();
					hasStart = false;
				}
			}
		};
	}

	@Override
	public void onStartWorkout() {
		chronometer.setBase(SystemClock.elapsedRealtime());
		chronometer.start();
		mapListener.onStartWorkout();
		startBtn.setText(R.string.stats_stopWorkout);
		BottomBar bottomBar = (BottomBar) getActivity().findViewById(R.id.bottomBar);
		bottomBar.setVisibility(View.GONE);
		locationServices.start();
	}

	@Override
	public void onStopWorkout() {
		locationServices.stop();
		mapListener.onStopWorkout();
		Intent intent = new Intent(getActivity().getApplicationContext(), ResultActivity.class);
		long elapsedSec = (SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000;
		intent.putExtra("seconds", elapsedSec);
		intent.putExtra("distanceText", distance.getText());
		intent.putExtra("path", mapFragment.getPath());
		startActivity(intent);

		chronometer.stop();
		startBtn.setText(R.string.stats_startWorkout);
		chronometer.setText(R.string.stats_durationText);
		distance.setText(R.string.stats_distanceText);
		BottomBar bottomBar = (BottomBar) getActivity().findViewById(R.id.bottomBar);
		bottomBar.setVisibility(View.VISIBLE);
	}

	@Override
	public void startWorkout() {
	}

	@Override
	public void update(Observable observable, Object o) {
		distance.setText(locationServices.getDistance());
	}
}