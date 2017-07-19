package com.comp3617.finalproject;

import java.util.Locale;

import com.comp3617.finalproject.database.Database;
import com.comp3617.finalproject.model.Workout;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.realm.Realm;

public class RecordActivity extends Activity {
	private Realm realm = Realm.getDefaultInstance();
	private Database database = new Database(realm);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		Intent intent = getIntent();
		Workout workout = database.getWorkout(intent.getStringExtra("fileName"));

		PolylineOptions path = intent.getExtras().getParcelable("path");
		ResultMapFragment fragment = (ResultMapFragment) getFragmentManager().findFragmentById(R.id.result_map);
		fragment.setPath(path);

		LinearLayout nameLayout = (LinearLayout) findViewById(R.id.reslut_nameLayout);
		LinearLayout saveBtnLayout = (LinearLayout) findViewById(R.id.result_saveBtnLayout);

		nameLayout.setVisibility(View.GONE);
		saveBtnLayout.setVisibility(View.GONE);

		TextView durationText = (TextView) findViewById(R.id.result_duration);
		TextView distanceText = (TextView) findViewById(R.id.result_distance);
		TextView discardBtn = (TextView) findViewById(R.id.result_discard);
		TextView averageSpeedText = (TextView) findViewById(R.id.result_averageSpeed);

		long seconds = workout.getDuration();
		double kms = workout.getDistance();
		double averageSpeed = kms / (seconds / 3600.0);

		durationText.setText(convertTime(seconds));
		distanceText.setText(String.format("%skm", kms));
		discardBtn.setText(R.string.record_backBtn);
		averageSpeedText.setText(String.format(Locale.getDefault(), "%.2fkm/h", averageSpeed));

		discardBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
	}

	private String convertTime(Long time) {
		int hours = time.intValue() / 3600;
		int minutes = time.intValue() / 60 % 60;
		int seconds = time.intValue() % 60;
		return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
	}

}