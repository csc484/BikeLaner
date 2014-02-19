package com.csc.bikelaner;

import java.util.ArrayList;

import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


public class GPSHandler implements android.location.LocationListener {
	/** The interactive Google Map fragment. */
	private GoogleMap m_vwMap;
	private final LocationManager locationManager;
	private final FragmentManager fragmentManager;

	/** The list of locations, each having a latitude and longitude. */
	private ArrayList<LatLng> m_arrPathPoints;

	/** The continuous set of lines drawn between points on the map. */
	private Polyline m_pathLine;

	public static final int DEFAULT_ZOOM_LEVEL = 13;
	private static final int CIRCLE_RADIUS = 1;

	public GPSHandler(LocationManager locationManager,
			FragmentManager fragmentManager) {
		this.locationManager = locationManager;
		this.fragmentManager = fragmentManager;
		m_arrPathPoints = new ArrayList<LatLng>();
		
	}

	public void createPointsOnMap() {
		m_pathLine = m_vwMap.addPolyline(new PolylineOptions());
		m_pathLine.setColor(Color.GREEN);
		//TIMER: start the timer to call onlocation call.
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
	}
	
	public void initiate() {
		initMapLayout();
		initMapSettings();
		setCameraToMyLocation();
		createPointsOnMap();
	}
	
	
	public void initMapLayout() {
		// Obtain the support map fragment specified in the XML to get the map
		// object
		SupportMapFragment map = (SupportMapFragment) fragmentManager
				.findFragmentById(R.id.map);
		m_vwMap = map.getMap();
		assert (m_vwMap != null);
	}

	public void initMapSettings() {
		// Enables in the view the ability to track your location.
		// Rest is self explanatory.
		m_vwMap.setMyLocationEnabled(true);
		m_vwMap.getUiSettings().setCompassEnabled(true);
		m_vwMap.getUiSettings().setZoomControlsEnabled(true);

	}

	public void setCameraToMyLocation() {
		// Create a location manager to get a service location provider
		// to find my location.
		String provider = locationManager.getBestProvider(new Criteria(), true);
		Location location = locationManager.getLastKnownLocation(provider);

		// Set the camera to my location
		if (location != null) {
			LatLng latLng = new LatLng(location.getLatitude(),
					location.getLongitude());

			m_vwMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,
					DEFAULT_ZOOM_LEVEL));
		}
	}
	
	public LatLng getLocation() {
	// Create a location manager to get a service location provider
      // to find my location.
      String provider = locationManager.getBestProvider(new Criteria(), true);
      Location location = locationManager.getLastKnownLocation(provider);

      // Set the camera to my location
      if (location != null) {
         return new LatLng(location.getLatitude(),
               location.getLongitude());
      }
      return null;
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		Location location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		onLocationChanged(location);
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(Location location) {
		if (location != null) {
			LatLng loc = new LatLng(location.getLatitude(),
					location.getLongitude());
			m_arrPathPoints.add(loc);

			m_pathLine.setPoints(m_arrPathPoints);
			m_vwMap.addCircle(new CircleOptions().center(loc)
					.radius(CIRCLE_RADIUS).fillColor(Color.CYAN)
					.strokeColor(Color.BLUE));
			m_vwMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc,
					DEFAULT_ZOOM_LEVEL));
		}
	}
}
