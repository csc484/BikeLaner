package com.csc.bikelaner;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.csc.bikelaner.db.LocalDataStore;
import com.google.android.gms.maps.SupportMapFragment;

public class MainActivity extends FragmentActivity implements OnClickListener,
		Observer {
	private SensorManager sensorManager;
	private Button btnStart, btnStop, btnDisplayX, btnDisplayY, btnDisplayZ, btnDispMap;
	private boolean started = false;
	private AccelerometerListener accelData;
	private GPSHandler gpsHandler;

	public void init_Buttons() {
		// Nichols stuff
		btnStart = (Button) findViewById(R.id.btnStart);
		btnStop = (Button) findViewById(R.id.btnStop);
		btnDisplayX = (Button) findViewById(R.id.btnDisplayX);
		btnDisplayY = (Button) findViewById(R.id.btnDisplayY);
		btnDisplayZ = (Button) findViewById(R.id.btnDisplayZ);
		btnStart.setOnClickListener(this);
		btnStop.setOnClickListener(this);
		btnDisplayX.setOnClickListener(this);
		btnDisplayY.setOnClickListener(this);
		btnDisplayZ.setOnClickListener(this);
		btnStart.setEnabled(true);
		btnStop.setEnabled(false);
		btnDisplayX.setEnabled(false);
		btnDisplayY.setEnabled(false);
		btnDisplayZ.setEnabled(false);
		btnStart.setText("Start");
		btnStop.setText("Stop");
		btnDisplayX.setText("<X>");
		btnDisplayY.setText("<Y>");
		btnDisplayZ.setText("<Z>");

		//Just to go to Mike's section
		btnDispMap = (Button) findViewById(R.id.btnDisplayMap);
		btnDispMap.setText("DisplayMap");
		btnDispMap.setEnabled(true);		
		btnDispMap.setOnClickListener(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set map layout as the main view
		// setContentView(R.layout.map_layout);

		setContentView(R.layout.activity_main);
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

		init_Buttons();
		
		new LocalDataStore(getApplicationContext());
		
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (started == true) {
			accelData.stopListening();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnStart:
			btnStart.setEnabled(false);
			btnStop.setEnabled(true);
			started = true;
			accelData = new AccelerometerListener(sensorManager, this);

			break;
		case R.id.btnStop:
			btnStart.setEnabled(true);
			btnStop.setEnabled(false);
			started = false;
			accelData.stopListening();

			break;
		case R.id.btnDisplayMap:
			setContentView(R.layout.map_layout);
			LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);					
			gpsHandler = new GPSHandler(manager, getSupportFragmentManager());
			gpsHandler.initiate();		
			break;
		default:
			break;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		AccelData sensorData = (AccelData) data;

		btnDisplayX.setText(String.valueOf(sensorData.getX()));
		btnDisplayY.setText(String.valueOf(sensorData.getY()));
		btnDisplayZ.setText(String.valueOf(sensorData.getZ()));
	}
}
