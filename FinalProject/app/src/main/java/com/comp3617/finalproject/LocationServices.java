package com.comp3617.finalproject;

import static android.content.Context.LOCATION_SERVICE;

import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

/**
 * Created by Andrew on 2017-07-07
 */
class LocationServices extends Observable implements LocationListener {

	private StartWorkoutListener listener;
	private LocationManager locationManager;
	private Context ctx;
	private Observer observer;
	private double distance = 0.0;
	private double latitude;
	private double longitude;
	private Location startLocation;
	private double elevation;

	LocationServices(StartWorkoutListener listener, Context context, Observer o) {
		this.listener = listener;
		this.ctx = context;
		this.observer = o;
	}

	void start() {
		if (ActivityCompat.checkSelfPermission(ctx,
				android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
				&& ActivityCompat.checkSelfPermission(ctx,
						android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			return;
		}
		locationManager = (LocationManager) ctx.getSystemService(LOCATION_SERVICE);
		Criteria mCriteria = new Criteria();
		String bestProvider = String.valueOf(locationManager.getBestProvider(mCriteria, true));
		this.addObserver(observer);
		locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);

	}

	void stop() {
		locationManager.removeUpdates(this);
	}

	String getDistance() {
		return String.format(Locale.getDefault(), "%.2f", distance / 1000);
	}

	double getLatitude() {
		return latitude;
	}

	double getLongitude() {
		return longitude;
	}

	double getElevation() {
		return elevation;
	}

	@Override
	public void onLocationChanged(Location location) {
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		elevation = location.getAltitude();
		if (startLocation == null) {
			startLocation = location;
		}
		float distanceBetween = location.distanceTo(startLocation);
		distance += distanceBetween;
		startLocation = location;
		listener.startWorkout();
		setChanged();
		notifyObservers();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}
}