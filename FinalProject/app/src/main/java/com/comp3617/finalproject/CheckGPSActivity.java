package com.comp3617.finalproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class CheckGPSActivity extends AppCompatActivity {
    private Button enableBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_gps);
        enableBtn = (Button) findViewById(R.id.checkGPS_Btn);
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if(!checkPermission()){
                enableBtn.setOnClickListener(onClickEnable_permission());
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            }else if(!isLocationServiceEnabled()){
                enableBtn.setOnClickListener(onClickEnable_setting());
                Log.d("Andrew","GPS is not turn on");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if(!checkPermission()){
                enableBtn.setOnClickListener(onClickEnable_permission());
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            }else if(!isLocationServiceEnabled()){
                enableBtn.setOnClickListener(onClickEnable_setting());
                Log.d("Andrew","GPS is not turn on");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 101: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Andrew","Success");
                    Intent intent = new Intent(this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }

    private boolean checkPermission(){
        int fineLocation = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLocation = ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION);
        return (fineLocation == 0 && coarseLocation == 0);
    }

    private boolean isLocationServiceEnabled() {
        LocationManager lm = (LocationManager)
                getApplication().getSystemService(Context.LOCATION_SERVICE);
        String provider = lm.getBestProvider(new Criteria(), true);
        return (!provider.trim().isEmpty() && !LocationManager.PASSIVE_PROVIDER.equals(provider));
    }

    public View.OnClickListener onClickEnable_permission(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(getParent(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            }
        };
    }

    public View.OnClickListener onClickEnable_setting(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent,0);
            }
        };
    }
}
