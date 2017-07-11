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

import com.roughike.bottombar.BottomBar;

import java.util.Observable;
import java.util.Observer;

public class StatsFragment extends Fragment implements Observer {
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		final View fragmentView = inflater.inflate(R.layout.fragment_stats, container, false);
		startBtn = (Button) fragmentView.findViewById(R.id.startBtn);
		chronometer = (Chronometer) fragmentView.findViewById(R.id.duration);
		distance = (TextView) fragmentView.findViewById(R.id.distance);
		fragment = (MapFragment) getParentFragment().getChildFragmentManager().findFragmentById(R.id.main_map_fragment);
		locationServices = new LocationServices(fragment, getContext(), this);

		fragment.setLocationServices(locationServices);

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
					chronometer.setBase(SystemClock.elapsedRealtime());
					chronometer.start();
					fragment.resetPath();
					startBtn.setText(R.string.stats_stopworkout);
					BottomBar bottomBar = (BottomBar) getActivity().findViewById(R.id.bottomBar);
					bottomBar.setVisibility(View.GONE);
					hasStart = true;
					locationServices.start();
				} else {
					Intent intent = new Intent(getContext(), ResultActivity.class);
					long elapsedSec = (SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000;
					Log.d("Andrew", Long.toString(elapsedSec));
					intent.putExtra("seconds", elapsedSec);
					startActivity(intent);
					chronometer.stop();
					startBtn.setText(R.string.stats_startworkout);
					hasStart = false;
					chronometer.setText(R.string.stats_durationText);
					distance.setText(R.string.stats_distanceText);
					locationServices.stop();
					fragment.map.clear();
					BottomBar bottomBar = (BottomBar) getActivity().findViewById(R.id.bottomBar);
					bottomBar.setVisibility(View.VISIBLE);
				}
			}
		};
	}

	@Override
	public void update(Observable observable, Object o) {
		distance.setText(locationServices.getDistance());
	}
}
