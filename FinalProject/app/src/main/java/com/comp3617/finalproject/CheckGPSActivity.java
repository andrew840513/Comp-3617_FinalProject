package com.comp3617.finalproject;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class CheckGPSActivity extends Activity {
	private Button enableBtn;
	private Activity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_gps);
		enableBtn = (Button) findViewById(R.id.checkGPS_Btn);
		activity = this;
		if (!checkPermission()) {
			enableBtn.setOnClickListener(onClickEnable_permission());
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				this.requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION,
						Manifest.permission.ACCESS_COARSE_LOCATION }, 101);
			}
		} else if (!isLocationServiceEnabled()) {
			enableBtn.setOnClickListener(onClickEnable_setting());
			Log.d("Andrew", "GPS is not turn on");
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!checkPermission()) {
			enableBtn.setOnClickListener(onClickEnable_permission());
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				this.requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION }, 101);
			}
		} else if (!isLocationServiceEnabled()) {
			enableBtn.setOnClickListener(onClickEnable_setting());
			Log.d("Andrew", "GPS is not turn on");
		}

	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case 101: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					Log.d("Andrew", "Success");
					Intent intent = new Intent(this, MainActivity.class);
					startActivity(intent);
					finish();
				}
			}
		}
	}

	private boolean checkPermission() {
		int fineLocation = 0;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
			fineLocation = this.checkSelfPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION);
		}
		int coarseLocation = 0;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
			coarseLocation = this.checkSelfPermission(
                    Manifest.permission.ACCESS_COARSE_LOCATION);
		}
		return (fineLocation == 0 && coarseLocation == 0);
	}

	private boolean isLocationServiceEnabled() {
		LocationManager lm = (LocationManager) getApplication().getSystemService(Context.LOCATION_SERVICE);
		String provider = lm.getBestProvider(new Criteria(), true);
		return (!provider.trim().isEmpty() && !LocationManager.PASSIVE_PROVIDER.equals(provider));
	}

	public View.OnClickListener onClickEnable_permission() {
		return new View.OnClickListener() {
			@TargetApi(Build.VERSION_CODES.M)
			@Override
			public void onClick(View view) {
				activity.requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION,
						Manifest.permission.ACCESS_COARSE_LOCATION }, 101);
			}
		};
	}

	public View.OnClickListener onClickEnable_setting() {
		return new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivityForResult(intent, 0);
			}
		};
	}
}