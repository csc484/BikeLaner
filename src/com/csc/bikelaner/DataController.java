package com.csc.bikelaner;

import java.util.Observable;
import java.util.Observer;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import android.content.Context;
import android.util.Log;

import com.csc.bikelaner.db.LocalDataStore;
import com.csc.bikelaner.db.data.DataPoint;
import com.google.android.gms.maps.model.LatLng;

public class DataController implements Observer {
   private DescriptiveStatistics accelStats;
   private LocalDataStore store;
   private static final int POPULATION_SIZE = 300;
   private int counter = 0;
   private GPSHandler gps;
   public DataController(Context context, AccelerometerListener accelListener, GPSHandler gps) {
      super();
      this.gps = gps;
      this.accelStats = new DescriptiveStatistics(POPULATION_SIZE);
      this.store = new LocalDataStore(context);
      accelListener.addObserver(this);
      
   }

   @Override
   public void update(Observable observable, Object data) {
      AccelData sensorData = (AccelData) data;

      double x2 = Math.pow(sensorData.getX(), 2);
      double y2 = Math.pow(sensorData.getY(), 2);
      double z2 = Math.pow(sensorData.getZ(), 2);

      double r = Math.sqrt(x2 + y2 + z2);

      accelStats.addValue(r);
      counter++;
      if (counter >= POPULATION_SIZE) {
         LatLng loc = gps.getLocation();
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

}
