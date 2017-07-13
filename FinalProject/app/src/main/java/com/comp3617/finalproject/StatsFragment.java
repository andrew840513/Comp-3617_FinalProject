package com.comp3617.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.comp3617.finalproject.com.comp3617.finalproject.gpx.WPT;
import com.roughike.bottombar.BottomBar;

import java.util.Observable;
import java.util.Observer;

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
		mapFragment = (MapFragment) getParentFragment().getChildFragmentManager().findFragmentById(R.id.main_map_fragment);

		locationServices = new LocationServices(mapFragment, getContext(), this);
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
		startBtn.setText(R.string.stats_stopworkout);
		BottomBar bottomBar = (BottomBar) getActivity().findViewById(R.id.bottomBar);
		bottomBar.setVisibility(View.GONE);
		locationServices.start();
	}

	@Override
	public void onStopWorkout() {
		locationServices.stop();
		mapListener.onStopWorkout();
		for (WPT wpt:mapFragment.getGpx().getWpt()
				) {
			try{
				Log.d("Andrew_gpx","lat:"+wpt.getLatitude()+"lon:"+wpt.getLongitude()+"ele:"+wpt.getElevation());
			}catch (Exception e){
				Log.e("Andrew_gpx_err",e.getMessage());
			}

		}
		Intent intent = new Intent(getContext(), ResultActivity.class);
		long elapsedSec = (SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000;
		intent.putExtra("seconds", elapsedSec);
		intent.putExtra("distance", distance.getText());
		intent.putExtra("path", mapFragment.getPath());
		startActivity(intent);

		chronometer.stop();
		startBtn.setText(R.string.stats_startworkout);
		chronometer.setText(R.string.stats_durationText);
		distance.setText(R.string.stats_distanceText);
		BottomBar bottomBar = (BottomBar) getActivity().findViewById(R.id.bottomBar);
		bottomBar.setVisibility(View.VISIBLE);
	}

	@Override
	public void startWorkout() {}

	@Override
	public void update(Observable observable, Object o) {
		distance.setText(locationServices.getDistance());
	}
}
