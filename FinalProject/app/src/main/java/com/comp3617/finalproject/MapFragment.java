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


public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener,Onmap {
    GoogleMap map;
    MapView mapView;
    View view;
    PolylineOptions path = new PolylineOptions();
    Polyline myPath;
    double lastLatitude = 0;
    double lastLongitude = 0;
    double totalDistent = 0;
    private LocationManager locationManager;

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
        View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
// position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        rlp.setMargins(0, 180, 180, 0);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        map = googleMap;
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
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc1, 19));
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
        //startDrawing();
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
    public void test(){
        Log.d("Andrew_Map",map.toString());
    }

    public void startDrawing(){
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
        Criteria mCriteria = new Criteria();
        String bestProvider = String.valueOf(locationManager.getBestProvider(mCriteria, true));
        locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);
    }

    public void stopDrawing(){
        locationManager = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeUpdates(this);
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

    @Override
    public void onLocationChanged(Location location) {
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        moveToCurrentLocation(currentLatitude,currentLongitude);
        if(lastLatitude !=0 && lastLongitude!=0){
            final double latitude = Math.abs(lastLatitude - currentLatitude);
            final double longitude = Math.abs(lastLongitude - currentLongitude);
            totalDistent+= latitude+longitude;
            if(totalDistent >= 0.00005){
                drawLine(currentLatitude,currentLongitude);
            }
        }else{
            lastLatitude = currentLatitude;
            lastLatitude = currentLatitude;
            drawLine(currentLatitude,currentLongitude);
        }
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
