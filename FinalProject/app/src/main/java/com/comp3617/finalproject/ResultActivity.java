package com.comp3617.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.comp3617.finalproject.com.comp3617.finalproject.gpx.GPX;

public class ResultActivity extends AppCompatActivity {
	TextView discardBtn;
	TextView duration;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		Intent intent = getIntent();

		duration = (TextView) findViewById(R.id.result_duration);
		discardBtn = (TextView) findViewById(R.id.result_discard);
		discardBtn.setOnClickListener(onDiscardClick());

		//get Current Gpx that load
		GPX gpx = GPX.getGpx();

		duration.setText(Long.toString(intent.getLongExtra("seconds",0)));
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
