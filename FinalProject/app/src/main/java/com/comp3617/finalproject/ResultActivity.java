package com.comp3617.finalproject;

import java.util.Date;
import java.util.Locale;

import com.comp3617.finalproject.database.Database;
import com.comp3617.finalproject.gpx.GPX;
import com.comp3617.finalproject.model.Workout;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import io.realm.Realm;

public class ResultActivity extends Activity {
	private long seconds;
	private double kms;
	private EditText workoutName;
	private Realm realm = Realm.getDefaultInstance();
	private Database database = new Database(realm);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		Intent intent = getIntent();
		PolylineOptions path = intent.getExtras().getParcelable("path");
		ResultMapFragment fragment = (ResultMapFragment) getFragmentManager().findFragmentById(R.id.result_map);
		fragment.setPath(path);
		workoutName = (EditText) findViewById(R.id.result_workoutName);
		TextView durationText = (TextView) findViewById(R.id.result_duration);
		TextView distanceText = (TextView) findViewById(R.id.result_distance);
		TextView discardBtn = (TextView) findViewById(R.id.result_discard);
		TextView averageSpeedText = (TextView) findViewById(R.id.result_averageSpeed);
		Button saveBtn = (Button) findViewById(R.id.result_saveBtn);

		seconds = intent.getLongExtra("seconds", 0);
		kms = Double.parseDouble(intent.getStringExtra("distanceText"));
		discardBtn.setOnClickListener(onDiscardClick());
		saveBtn.setOnClickListener(onSaveClick());

		durationText.setText(convertTime(seconds));
		distanceText.setText(String.format("%skm", kms));
		double averageSpeed = kms / (seconds / 3600.0);
		averageSpeedText.setText(String.format(Locale.getDefault(), "%.2fkm/h", averageSpeed));
	}

	public String convertTime(Long time) {
		int hours = time.intValue() / 3600;
		int minutes = time.intValue() / 60 % 60;
		int seconds = time.intValue() % 60;
		return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
	}

	public View.OnClickListener onDiscardClick() {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		};
	}

	public View.OnClickListener onSaveClick() {
		return new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				LocationRecord locationRecord = new LocationRecord(getApplicationContext());
				Workout workout = new Workout();
				String name = workoutName.getText().toString();
				// locationRecord.deleteAllFiles();
				String fileName = locationRecord.saveFile(name, GPX.getGpx());

				workout.setWorkout(fileName, name, new Date(), kms, seconds);
				database.addNewWorkout(workout);
				locationRecord.getListFiles();
				finish();
			}
		};
	}

}