package com.comp3617.finalproject;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


public class MapFragment extends Fragment implements OnMapReadyCallback, StartWorkoutListener,GoogleMap.OnCameraMoveStartedListener,GoogleMap.OnMyLocationButtonClickListener {
    GoogleMap map;
    MapView mapView;
    View view;
    PolylineOptions path = new PolylineOptions();
    Polyline myPath;
    double lastLatitude = 0;
    double lastLongitude = 0;
    double totalDistant = 0;
    boolean dragging = false;
    boolean didShowMylocation;
    private LocationServices locationServices;
    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_map, container, false);
        Log.d("Andrew_Map",this.toString());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = (MapView) view.findViewById(R.id.map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
        try{
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            // position on right bottom
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            rlp.setMargins(0, 180, 180, 0);
        }catch (Exception e){
            Log.d("Andrew","Not able to find the map view");
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        map = googleMap;
        map.setOnCameraMoveStartedListener(this);
        map.setOnMyLocationButtonClickListener(this);
        String bestProvider = "";
        googleMap.setMyLocationEnabled(true);

        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        if (isLocationEnabled()) {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
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
            if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(bestProvider, 1000, 0, (LocationListener) this);
        }
    }

    @Override
    public void onCameraMoveStarted(int reason) {
        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            Log.d("Andrew","I moved camera");
            dragging = true;
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        dragging = false;
        return false;
    }

    public boolean isLocationEnabled(){
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    public void moveToCurrentLocation(double currentLatitude, double currentLongitude){
        LatLng loc1 = new LatLng(currentLatitude, currentLongitude);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(loc1, 19));
    }

    public void drawLine(double currentLatitude, double currentLongitude){
        int COLOR_BLACK_ARGB = 0xffff0000;
        path.add(new LatLng(currentLatitude,currentLongitude));
        myPath = map.addPolyline(path);
        myPath.setColor(COLOR_BLACK_ARGB);
        myPath.setWidth(20);
    }

    public void resetPath(){
        path = new PolylineOptions();
    }

    @Override
    public void startWorkout() {
        double currentLatitude = locationServices.getLatitude();
        double currentLongitude = locationServices.getLongitude();
        if(!didShowMylocation){
            moveToCurrentLocation(currentLatitude,currentLongitude);
            didShowMylocation = true;
        }

        if(lastLatitude !=0 && lastLongitude!=0){
            final double latitude = Math.abs(lastLatitude - currentLatitude);
            final double longitude = Math.abs(lastLongitude - currentLongitude);
            totalDistant += latitude+longitude;
            if(totalDistant >= 0.00005){
                drawLine(currentLatitude,currentLongitude);
            }
        }else{
            lastLatitude = currentLatitude;
            lastLatitude = currentLatitude;
            drawLine(currentLatitude,currentLongitude);
        }
        if(!dragging){
            moveToCurrentLocation(currentLatitude,currentLongitude);
        }
    }

    public void setLocationServices(LocationServices locationServices) {
        this.locationServices = locationServices;
    }
}
