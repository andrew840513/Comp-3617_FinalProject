package com.comp3617.finalproject;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ResultMapFragment extends Fragment implements OnMapReadyCallback {
	Activity activity;
	private GoogleMap map;
	private Polyline myPath;
	private PolylineOptions path;
	private Context ctx;

	public ResultMapFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ctx = getActivity().getApplicationContext();
		activity = getActivity();
		return inflater.inflate(R.layout.fragment_result_map, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		MapView mapView = (MapView) view.findViewById(R.id.map_result);
		if (mapView != null) {
			mapView.onCreate(null);
			mapView.onResume();
			mapView.getMapAsync(this);
		}
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		MapsInitializer.initialize(ctx);
		map = googleMap;
		addPath();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (activity
					.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
					&& activity.checkSelfPermission(
							Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

				return;
			}
		}
		map.setMyLocationEnabled(false);
		moveInBounds();
	}

	void addPath() {
		int COLOR_BLACK_ARGB = 0xff0000ff;
		myPath = map.addPolyline(path);
		myPath.setColor(COLOR_BLACK_ARGB);
		myPath.setWidth(20);
	}

	public void setPath(PolylineOptions path) {
		this.path = path;
	}

	void moveInBounds() {
		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		for (int i = 0; i < myPath.getPoints().size(); i++) {
			builder.include(myPath.getPoints().get(i));
		}

		try {
			LatLngBounds bounds = builder.build();
			int width = getResources().getDisplayMetrics().widthPixels;
			int height = getResources().getDisplayMetrics().heightPixels;
			int padding = (int) (width * 0.3);
			CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
			map.moveCamera(cu);
		} catch (Exception e) {
			Log.e(getString(R.string.ResultMapFragment_err_tag), e.getMessage());
		}
	}
}