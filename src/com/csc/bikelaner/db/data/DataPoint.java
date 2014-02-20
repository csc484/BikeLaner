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
import com.google.android.gms.maps.model.LatLng;

public class DataPoint {
	private Double latitude;
	private Double longitude;
	private Double speed;
	private String deviceId;
	private Long sessionId;

	private DataPoint() {

	}

	public DataPoint(Double latitude, Double longitude, Double speed) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.speed = speed;
		this.sessionId = Defaults.get().getSessionId();
		this.deviceId = Defaults.get().getDeviceId();
	}

	public ContentValues to() {
		ContentValues to = new ContentValues();
		to.put(LATITUDE.toString(), latitude);
		to.put(LONGITUDE.toString(), longitude);
		to.put(SPEED.toString(), speed);
		to.put(DEVICEID.toString(), deviceId);
		to.put(SESSIONID.toString(), sessionId);
		return to;
	}

	public static DataPoint from(Iterable<Entry<String, Object>> results) {
		Map<DBColumn, Object> data = new EnumMap<DBColumn, Object>(
				DBColumn.class);
		for (Entry<String, Object> entry : results) {
			data.put(DBColumn.from(entry.getKey()), entry.getValue());
		}
		DataPoint point = new DataPoint();
		point.latitude = Double.parseDouble(data.get(LATITUDE).toString());
		point.longitude = Double.parseDouble(data.get(LONGITUDE).toString());
		point.speed = Double.parseDouble(data.get(SPEED).toString());
		point.sessionId = Long.parseLong(data.get(SESSIONID).toString());
		point.deviceId = data.get(DEVICEID).toString();
		return point;
	}

	public Double getlatitude() {
		return latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public Double getSpeed() {
		return speed;
	}

	public String toString() {
		return latitude + " " + longitude;
	}
	
	public LatLng getLatLng() {
		return new LatLng(latitude, longitude);
	}
}
