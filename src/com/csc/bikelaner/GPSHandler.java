package com.csc.bikelaner;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class GPSHandler {
   /** The interactive Google Map fragment. */
   private GoogleMap m_vwMap;
   private final LocationManager locationManager;
   private final FragmentManager fragmentManager;

   public static final int DEFAULT_ZOOM_LEVEL = 17;

   public GPSHandler(LocationManager locationManager, FragmentManager fragmentManager) {
      this.locationManager = locationManager;
      this.fragmentManager = fragmentManager;
      initMapLayout();
      initMapSettings();
      setCameraToMyLocation();
   }

   public void initMapLayout() {
      // Obtain the support map fragment specified in the XML to get the map
      // object
      
      SupportMapFragment map = (SupportMapFragment)fragmentManager.findFragmentById(R.id.map);
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
}
