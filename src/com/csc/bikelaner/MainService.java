package com.csc.bikelaner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.csc.bikelaner.db.LocalDataStore;
import com.csc.bikelaner.db.data.DataPoint;
import com.csc.bikelaner.db.data.Defaults;
import com.google.android.gms.maps.model.LatLng;

public class MainService extends Service implements LocationListener, Observer {
	private static final int POPULATION_SIZE = 300;

	private SensorManager sensorManager;
	Button btnStart, btnStop, btnDisplayR, btnDisplayMean, btnDisplayStdDev,
			btnDispMap;
	private boolean started = false;
	private AccelerometerListener accelData;
	private DescriptiveStatistics accelStats;
	private LocalDataStore store;

	private boolean isRunning;

	private int m_onGoingNotificationId = 0x42;

	private Object m_lastLocation;

	private LocationManager m_locManager;

	private ArrayList<LatLng> m_arrPathPoints;

	private int counter;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("onStartCommand", "HERE!!!");
		Toast.makeText(getApplicationContext(), "onStartCommand", Toast.LENGTH_SHORT).show();
		isRunning = true;
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		accelStats = new DescriptiveStatistics(POPULATION_SIZE);
		m_arrPathPoints = new ArrayList<LatLng>();
		Defaults.init(getApplicationContext());
		store = new LocalDataStore(getApplicationContext());

		NotificationCompat.Builder notBuilder = new NotificationCompat.Builder(
				this)
				.setOngoing(true)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(
						getResources().getString(
								R.string.onGoingNotificationTitle))
				.setDefaults(Notification.DEFAULT_ALL);

		startForeground(m_onGoingNotificationId, notBuilder.build());

		initLocationData();
		initAccelerometerListener();
		return startId;
	}

	private void initAccelerometerListener() {
		accelData = new AccelerometerListener(sensorManager, this);
	}

	private void initLocationData() {
		m_lastLocation = null;
		m_locManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		onLocationChanged(m_locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
		m_locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
	}

	@Override
	public void onLocationChanged(Location location) {
		if (location != null) {
			LatLng loc = new LatLng(location.getLatitude(),
					location.getLongitude());
			m_arrPathPoints.add(loc);
		}
	}
	
	@Override
	public void onDestroy() {
		m_locManager.removeUpdates(this);
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
		counter++;
		if (counter >= POPULATION_SIZE) {
			LatLng loc = getLocation();
			if (loc != null) {
				DataPoint point = new DataPoint(loc.latitude, loc.longitude,
						accelStats.getMean());
				counter = 0;
				Log.i("[Controller]", String.format(
						"Storing [lat=%f, lng=%f, rating=%f]", loc.latitude,
						loc.longitude, accelStats.getMean()));
				store.save(point);
				accelStats.clear();
			}
		}
	}

	public LatLng getLocation() {
		// Create a location manager to get a service location provider
		// to find my location.
		String provider = m_locManager.getBestProvider(new Criteria(), true);
		Location location = m_locManager.getLastKnownLocation(provider);

		// Set the camera to my location
		if (location != null) {
			return new LatLng(location.getLatitude(), location.getLongitude());
		}
		return null;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}
}
