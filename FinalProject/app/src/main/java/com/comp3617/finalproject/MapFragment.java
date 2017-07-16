package com.comp3617.finalproject;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.comp3617.finalproject.gpx.GPX;
import com.comp3617.finalproject.gpx.WPT;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback, StartWorkoutListener,
		GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnMyLocationButtonClickListener {
	GoogleMap map;
	MapView mapView;
	View view;
	PolylineOptions path = new PolylineOptions();
	Polyline myPath;
	private double lastLatitude = 0;
    private double lastLongitude = 0;
    private double totalDistant = 0;
    private boolean dragging = false;
    private boolean didShowMyLocation;
	private LocationServices locationServices;
	private List<WPT> wptList;
	private Context ctx;
	private Activity activity;
	public MapFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		view = inflater.inflate(R.layout.fragment_map, container, false);
		ctx = getActivity().getApplicationContext();
		activity = getActivity();
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mapView = (MapView) view.findViewById(R.id.map);
		if (mapView != null) {
			mapView.onCreate(null);
			mapView.onResume();
			mapView.getMapAsync(this);
		}
		try {
			View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent())
					.findViewById(Integer.parseInt("2"));
			RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
			// position on right bottom
			rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
			rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
			rlp.setMargins(0, 180, 180, 0);
		} catch (Exception e) {
			Log.d("Andrew", "Not able to find the map view");
		}

	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		MapsInitializer.initialize(ctx);
		map = googleMap;
		map.setOnCameraMoveStartedListener(this);
		map.setOnMyLocationButtonClickListener(this);
		String bestProvider = "";
		googleMap.setMyLocationEnabled(true);

		LocationManager locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);

		if (isLocationEnabled()) {
			locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
			Criteria mCriteria = new Criteria();
			bestProvider = String.valueOf(locationManager.getBestProvider(mCriteria, true));

			Location mLocation = locationManager.getLastKnownLocation(bestProvider);
			if (mLocation != null) {
				final double currentLatitude = mLocation.getLatitude();
				final double currentLongitude = mLocation.getLongitude();
				LatLng loc1 = new LatLng(currentLatitude, currentLongitude);
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc1, 15));
			}
		} else {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				if (activity.checkSelfPermission(
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && activity.checkSelfPermission(
                                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
			}
			locationManager.requestLocationUpdates(bestProvider, 1000, 0, (LocationListener) this);
		}
	}

	@Override
	public void onCameraMoveStarted(int reason) {
		if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
			Log.d("Andrew", "I moved camera");
			dragging = true;
		}
	}

	@Override
	public boolean onMyLocationButtonClick() {
		dragging = false;
		return false;
	}

	public boolean isLocationEnabled() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (activity.checkSelfPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && activity.checkSelfPermission(
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
		}
		return true;
	}

	public void moveToCurrentLocation(double currentLatitude, double currentLongitude) {
		LatLng loc1 = new LatLng(currentLatitude, currentLongitude);
		map.animateCamera(CameraUpdateFactory.newLatLngZoom(loc1, 19));
	}

	public void drawLine(double currentLatitude, double currentLongitude) {
		int COLOR_BLACK_ARGB = 0xffff0000;
		path.add(new LatLng(currentLatitude, currentLongitude));
		myPath = map.addPolyline(path);
		myPath.setColor(COLOR_BLACK_ARGB);
		myPath.setWidth(20);
	}

	public PolylineOptions getPath() {
		return path;
	}

    @Override
	public void onStartWorkout() {
        path = new PolylineOptions();
        wptList = new ArrayList<>();
	}

	@Override
	public void startWorkout() {
		double currentLatitude = locationServices.getLatitude();
		double currentLongitude = locationServices.getLongitude();
        double elevation = locationServices.getElevation();
        WPT wpt = new WPT(currentLatitude,currentLongitude,elevation);
        wptList.add(wpt);
		if (!didShowMyLocation) {
			moveToCurrentLocation(currentLatitude, currentLongitude);
			didShowMyLocation = true;
		}

		if (lastLatitude != 0 && lastLongitude != 0) {
			final double latitude = Math.abs(lastLatitude - currentLatitude);
			final double longitude = Math.abs(lastLongitude - currentLongitude);
			totalDistant += latitude + longitude;
			if (totalDistant >= 0.00005) {
				drawLine(currentLatitude, currentLongitude);
			}
		} else {
			lastLatitude = currentLatitude;
			lastLongitude = currentLongitude;
			drawLine(currentLatitude, currentLongitude);
		}
		if (!dragging) {
			moveToCurrentLocation(currentLatitude, currentLongitude);
		}
	}

    @Override
    public void onStopWorkout() {
		GPX gpx = new GPX(wptList);
        GPX.setGpx(gpx);
        map.clear();
    }

    public void setLocationServices(LocationServices locationServices) {
		this.locationServices = locationServices;
	}
}