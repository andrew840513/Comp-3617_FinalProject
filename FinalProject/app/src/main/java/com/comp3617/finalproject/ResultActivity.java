package com.comp3617.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.comp3617.finalproject.com.comp3617.finalproject.gpx.GPX;

import java.util.Locale;

public class ResultActivity extends AppCompatActivity {
	TextView discardBtn;
	TextView duration;
	TextView distance;
	TextView averageSpeed;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		Intent intent = getIntent();

		duration = (TextView) findViewById(R.id.result_duration);
		distance = (TextView) findViewById(R.id.result_distance);
		discardBtn = (TextView) findViewById(R.id.result_discard);
		averageSpeed = (TextView) findViewById(R.id.result_averageSpeed);
		discardBtn.setOnClickListener(onDiscardClick());

		//get Current Gpx that load
		GPX gpx = GPX.getGpx();

		duration.setText(String.format(Locale.getDefault(),"%d",intent.getLongExtra("seconds",0)));
		distance.setText(String.format("%skm",intent.getStringExtra("distance")));
		double averageSpeedc = Double.parseDouble(intent.getStringExtra("distance")) / (intent.getLongExtra("seconds",0)/3600.0);
		Log.d("Andrew_parse",Double.parseDouble(intent.getStringExtra("distance"))+"");
		Log.d("Andrew_parse",(intent.getLongExtra("seconds",0)/3600.0)+"");
		averageSpeed.setText(String.format(Locale.getDefault(),"%.2fkm/h",averageSpeedc));
	}

	public View.OnClickListener onDiscardClick() {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		};
	}
}
