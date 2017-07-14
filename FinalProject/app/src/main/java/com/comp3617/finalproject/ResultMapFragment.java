package com.comp3617.finalproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;

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
	Polyline myPath;
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

		mapView = (MapView) view.findViewById(R.id.map_result);
		if (mapView != null) {
			mapView.onCreate(null);
			mapView.onResume();
			mapView.getMapAsync(this);
		}
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
		//addPath();
		//moveInBounds();
	}

	void addPath(){
		ResultActivity activity = (ResultActivity)getActivity();
		int COLOR_BLACK_ARGB = 0xff0000ff;
		myPath = map.addPolyline(activity.getPath());
		myPath.setColor(COLOR_BLACK_ARGB);
		myPath.setWidth(20);
	}

	void moveInBounds(){
		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		for(int i = 0; i < myPath.getPoints().size();i++){
			builder.include(myPath.getPoints().get(i));
		}


		LatLngBounds bounds = builder.build();
		int padding = 50; // offset from edges of the map in pixels

		CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
		map.animateCamera(cu);
	}

}
