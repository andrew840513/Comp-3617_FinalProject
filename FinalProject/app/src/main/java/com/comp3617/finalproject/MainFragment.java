package com.comp3617.finalproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {
    public MainFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       if(checkPermission() && isLocationServiceEnabled()){
           View fragmentView = inflater.inflate(R.layout.fragment_main,container,false);
           return fragmentView;
       }else{
           Intent intent = new Intent(getActivity(),CheckGPSActivity.class);
           startActivity(intent);
       }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!checkPermission() || !isLocationServiceEnabled()){
            Intent intent = new Intent(getActivity(),CheckGPSActivity.class);
            startActivity(intent);
        }
    }

    private boolean checkPermission(){
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            int fineLocation = ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION);
            int coarseLocation = ContextCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_COARSE_LOCATION);
            return (fineLocation == 0 && coarseLocation == 0);
        }
        return true;
    }

    private boolean isLocationServiceEnabled() {
        LocationManager lm = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);
        String provider = lm.getBestProvider(new Criteria(), true);
         return (!provider.trim().isEmpty() && !LocationManager.PASSIVE_PROVIDER.equals(provider));
    }
}
