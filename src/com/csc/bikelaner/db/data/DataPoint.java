package com.csc.bikelaner.db.data;

import static com.csc.bikelaner.db.LocalDataStore.DBColumn.DEVICEID;
import static com.csc.bikelaner.db.LocalDataStore.DBColumn.LATITUDE;
import static com.csc.bikelaner.db.LocalDataStore.DBColumn.LONGITUDE;
import static com.csc.bikelaner.db.LocalDataStore.DBColumn.SESSIONID;
import static com.csc.bikelaner.db.LocalDataStore.DBColumn.SPEED;

import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;

import android.content.ContentValues;

import com.csc.bikelaner.db.LocalDataStore;
import com.csc.bikelaner.db.LocalDataStore.DBColumn;

public class DataPoint {
   private Double latitiude;
   private Double longitude;
   private Double speed;
   private String deviceId;
   private Long sessionId;
   
   private DataPoint() {
      
   }
   
   public DataPoint(Double latitiude, Double longitude, Double speed) {
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
   
   public static DataPoint from(Iterable<Entry<String, Object>> results) {
      Map<DBColumn, Object> data = new EnumMap<DBColumn, Object>(DBColumn.class);
      for (Entry<String, Object> entry : results) {
         data.put(DBColumn.from(entry.getKey()), entry.getValue());
      }
      DataPoint point = new DataPoint();
      point.latitiude = Double.parseDouble(data.get(LATITUDE).toString());
      point.longitude = Double.parseDouble(data.get(LONGITUDE).toString());
      point.speed = Double.parseDouble(data.get(SPEED).toString());
      point.sessionId = Long.parseLong(data.get(SESSIONID).toString());
      point.deviceId = data.get(DEVICEID).toString();
      return point;
   }
}
