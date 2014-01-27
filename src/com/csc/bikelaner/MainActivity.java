package com.csc.bikelaner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

public class MainActivity extends FragmentActivity {

	public static final int DEFAULT_ZOOM_LEVEL = 17;

	/** The interactive Google Map fragment. */
	private GoogleMap m_vwMap;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set map layout as the main view
		setContentView(R.layout.map_layout);
		initMapLayout();
		initMapSettings();
		setCameraToMyLocation();
	}

	public void initMapLayout() {
		// Obtain the support map fragment specified in the XML to get the map
		// object
	   
		SupportMapFragment map = (SupportMapFragment)getSupportFragmentManager()
				.findFragmentById(R.id.map);
		m_vwMap = map.getMap();
	}

	public void initMapSettings() {
		// Enables in the view the ability to track your location.
		// Rest is self explanatory.
		m_vwMap.setMyLocationEnabled(true);		
		m_vwMap.getUiSettings().setCompassEnabled(true);
		m_vwMap.getUiSettings().setZoomControlsEnabled(true);

	}

	public void setCameraToMyLocation() {
		//Create a location manager to get a service location provider
		//to find my location.
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		String provider = locationManager.getBestProvider(new Criteria(), true);
		Location location = locationManager.getLastKnownLocation(provider);

		//Set the camera to my location
		if (location != null) {
			LatLng latLng = new LatLng(location.getLatitude(),
					location.getLongitude());

			m_vwMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,
					DEFAULT_ZOOM_LEVEL));
		}
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
