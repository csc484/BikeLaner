package com.csc.bikelaner;

import java.util.Observable;
import java.util.Observer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class AccelerometerListener extends Observable
   implements SensorEventListener {
   private Sensor accel;
   private SensorManager sensorManager;
   
   public AccelerometerListener(SensorManager service, Observer o) {
      sensorManager = service;
      accel = sensorManager
         .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
      sensorManager.registerListener(this, accel,
         SensorManager.SENSOR_DELAY_FASTEST);
      addObserver(o);
   }

   @Override
   public void onAccuracyChanged(Sensor sensor, int accuracy) {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void onSensorChanged(SensorEvent event) {
      double x = event.values[0];
      double y = event.values[1];
      double z = event.values[2];
      long timestamp = System.currentTimeMillis();
      AccelData data = new AccelData(timestamp, x, y, z);
      this.setChanged();
      this.notifyObservers(data);
   }
   
   public void stopListening() {
      sensorManager.unregisterListener(this);
   }
}
