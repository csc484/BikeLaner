package com.csc.bikelaner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import android.content.Context;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.csc.bikelaner.db.LocalDataStore;
import com.csc.bikelaner.db.data.DataPoint;
import com.csc.bikelaner.db.data.Defaults;

public class MainActivity extends FragmentActivity implements OnClickListener,
		Observer {
   private static final int POPULATION_SIZE = 300;
   
	private SensorManager sensorManager;
	Button btnStart, btnStop, btnDisplayR, btnDisplayMean, btnDisplayStdDev, btnDispMap;
	private boolean started = false;
	private AccelerometerListener accelData;
   private DescriptiveStatistics accelStats;
	private GPSHandler gpsHandler;

	public void init_Buttons() {
		// Nichols stuff
	   btnStart = (Button) findViewById(R.id.btnStart);
      btnStop = (Button) findViewById(R.id.btnStop);
      btnDisplayR = (Button) findViewById(R.id.btnDisplayR);
      btnDisplayMean = (Button) findViewById(R.id.btnDisplayMean);
      btnDisplayStdDev = (Button) findViewById(R.id.btnDisplayStdDev);
      btnStart.setOnClickListener(this);
      btnStop.setOnClickListener(this);
      btnDisplayR.setOnClickListener(this);
      btnDisplayMean.setOnClickListener(this);
      btnDisplayStdDev.setOnClickListener(this);
      btnStart.setEnabled(true);
      btnStop.setEnabled(false);
      btnDisplayR.setEnabled(false);
      btnDisplayMean.setEnabled(false);
      btnDisplayStdDev.setEnabled(false);
      btnStart.setText("Start");
      btnStop.setText("Stop");
      btnDisplayR.setText("<R>");
      btnDisplayMean.setText("<Average>");
      btnDisplayStdDev.setText("<Standard Deviation>");
      
      accelStats = new DescriptiveStatistics(POPULATION_SIZE);

      LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);               
      this.gpsHandler = new GPSHandler(manager, getSupportFragmentManager());

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
		Defaults.init(getApplicationContext());
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
			new DataController(getApplicationContext(), accelData, gpsHandler);
			break;
		case R.id.btnStop:
			btnStart.setEnabled(true);
			btnStop.setEnabled(false);
			started = false;
			accelData.stopListening();

			break;
		case R.id.btnDisplayMap:
			setContentView(R.layout.map_layout);
			
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
      
      double x2 = Math.pow(sensorData.getX(), 2);
      double y2 = Math.pow(sensorData.getY(), 2);
      double z2 = Math.pow(sensorData.getZ(), 2);

      double r = Math.sqrt(x2 + y2 + z2);
      
      accelStats.addValue(r);
      
      btnDisplayR.setText(BigDecimal.valueOf(r).setScale(1, RoundingMode.FLOOR).toString());
      btnDisplayMean.setText(BigDecimal.valueOf(accelStats.getMean()).setScale(1, RoundingMode.FLOOR).toString());
      btnDisplayStdDev.setText(BigDecimal.valueOf(accelStats.getStandardDeviation()).setScale(1, RoundingMode.FLOOR).toString());
   }
}
