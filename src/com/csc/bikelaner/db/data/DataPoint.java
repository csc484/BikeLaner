package com.csc.bikelaner.db.data;

import static com.csc.bikelaner.db.LocalDataStore.DBColumn.DEVICEID;
import static com.csc.bikelaner.db.LocalDataStore.DBColumn.LATITUDE;
import static com.csc.bikelaner.db.LocalDataStore.DBColumn.LONGITUDE;
import static com.csc.bikelaner.db.LocalDataStore.DBColumn.SESSIONID;
import static com.csc.bikelaner.db.LocalDataStore.DBColumn.SPEED;

import java.util.Map;

import android.content.ContentValues;

import com.csc.bikelaner.db.LocalDataStore.DBColumn;

public class DataPoint {
   private Integer latitiude;
   private Integer longitude;
   private Double speed;
   private String deviceId;
   private String sessionId;
   
   private DataPoint() {
      
   }
   
   public DataPoint(Integer latitiude, Integer longitude, Double speed) {
      this.latitiude = latitiude;
      this.longitude = longitude;
      this.speed = speed;
      this.sessionId = Defaults.get().getSessionId();
      this.deviceId = Defaults.get().getDeviceId();
   }


   public ContentValues to() {
      ContentValues to = new ContentValues();
      to.put(LATITUDE.toString(), latitiude);
      to.put(LONGITUDE.toString(), longitude);
      to.put(SPEED.toString(), speed);
      to.put(DEVICEID.toString(), deviceId);
      to.put(SESSIONID.toString(), sessionId);
      return to;
   }
   
   public static DataPoint from(Map<DBColumn, Object> data) {
      DataPoint point = new DataPoint();
      point.latitiude = Integer.parseInt(data.get(LATITUDE).toString());
      point.longitude = Integer.parseInt(data.get(LONGITUDE).toString());
      point.speed = Double.parseDouble(data.get(SPEED).toString());
      point.sessionId = data.get(DEVICEID).toString();
      point.deviceId = data.get(SESSIONID).toString();
      return point;
   }
}