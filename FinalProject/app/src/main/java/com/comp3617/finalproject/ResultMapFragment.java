package com.comp3617.finalproject;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ResultMapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ResultMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultMapFragment extends Fragment implements OnMapReadyCallback {
	View view;
	MapView mapView;
	GoogleMap map;

	public ResultMapFragment() {
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_result_map, container, false);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mapView = (MapView) view.findViewById(R.id.result_map);
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		MapsInitializer.initialize(getContext());
		map = googleMap;
		if (ActivityCompat.checkSelfPermission(getContext(),
				Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
				&& ActivityCompat.checkSelfPermission(getContext(),
						Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

			return;
		}
		map.setMyLocationEnabled(false);
	}
}
