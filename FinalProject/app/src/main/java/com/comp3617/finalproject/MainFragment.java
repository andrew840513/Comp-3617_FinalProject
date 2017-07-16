package com.comp3617.finalproject;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainFragment extends Fragment {
	public MainFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (checkPermission() && isLocationServiceEnabled()) {
			return inflater.inflate(R.layout.fragment_main, container, false);
		} else {
			Intent intent = new Intent(getActivity(), CheckGPSActivity.class);
			startActivity(intent);
			getActivity().finish();
		}
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!checkPermission() || !isLocationServiceEnabled()) {
			Intent intent = new Intent(getActivity(), CheckGPSActivity.class);
			startActivity(intent);
			getActivity().finish();
		}
	}

	@TargetApi(Build.VERSION_CODES.M)
	private boolean checkPermission() {
		int MyVersion = Build.VERSION.SDK_INT;
		if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
			int fineLocation = getContext().checkSelfPermission(
					Manifest.permission.ACCESS_FINE_LOCATION);
			int coarseLocation = getContext().checkSelfPermission(
					Manifest.permission.ACCESS_COARSE_LOCATION);
			return (fineLocation == 0 && coarseLocation == 0);
		}
		return true;
	}

	private boolean isLocationServiceEnabled() {
		LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		String provider = lm.getBestProvider(new Criteria(), true);
		return (!provider.trim().isEmpty() && !LocationManager.PASSIVE_PROVIDER.equals(provider));
	}
}