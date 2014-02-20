package com.csc.bikelaner.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.csc.bikelaner.GPSHandler;
import com.csc.bikelaner.db.data.DataPoint;
import com.google.android.gms.maps.model.LatLng;

@SuppressLint("DefaultLocale")
public class LocalDataStore extends SQLiteOpenHelper implements DataStore{

   public static final String TABLE_NAME = "data";
   public static final String COLUMN_ID = "_id";

   private static final String DATABASE_NAME = "data.db";
   private static final int DATABASE_VERSION = 1;
   private static final String DATABASE_CREATE;

   public static String getSeperator(int current, int max) {

      return current < max -1 ? "," : ");";
   }
   static {
      StringBuilder create = new StringBuilder("create table " + TABLE_NAME
            + "(");

      DBColumn[] cols = DBColumn.values();
      for (int i = 0; i < cols.length; i++) {
         String sep = getSeperator(i, cols.length);
         create.append(cols[i] + " " + cols[i].getDataType()).append(sep);
      }
      DATABASE_CREATE = create.toString();
   }
   public LocalDataStore(Context context) {
     super(context, DATABASE_NAME, null, DATABASE_VERSION);
    
   }

   @Override
   public void onCreate(SQLiteDatabase database) {    
     database.execSQL(DATABASE_CREATE);
     Log.d("[local db]", "created db");
   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
     Log.w(LocalDataStore.class.getName(),
         "Upgrading database from version " + oldVersion + " to "
             + newVersion + ", which will destroy all old data");
     db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
     onCreate(db);
   }
   
   public enum DBColumn {
      DEVICEID("VARCHAR(100)"), LONGITUDE("REAL"),LATITUDE("REAL"), SPEED("REAL"), SESSIONID("LONG");
      private String type;
      
      private DBColumn(String type) {
         this.type = type;
      }
      
      public String getDataType() {
         return type;
      }
      

      @Override
      public String toString() {
         return name().toLowerCase();
      }

      public static DBColumn from(String key) {
         for (DBColumn col : values()) {
            if(col.toString().equalsIgnoreCase(key)) {
               return col;
            }
         }
         throw new RuntimeException("No column found for " + key);
      }
   }

   @Override
   public void save(DataPoint datapoint) {
     save(Arrays.asList(datapoint));
   }
   @Override
   public void save(Collection<DataPoint> datapoints) {
      SQLiteDatabase database = getWritableDatabase();
      Log.i("[local db]", "Saving " + datapoints);
     // database.beginTransaction();
      for(DataPoint point : datapoints) {
         database.insert(TABLE_NAME, null, point.to());
      }
     // database.endTransaction();
      
   }
   
   @Override
   public Collection<DataPoint> getData(String where) {
      SQLiteDatabase database = getReadableDatabase();
      Cursor query = database.query(TABLE_NAME, null, where, null, null,null,null);
      List<DataPoint> rtn = new ArrayList<DataPoint>();
      while(query.moveToNext()) {
         
         ContentValues rowValues = new ContentValues();
         DatabaseUtils.cursorRowToContentValues(query, rowValues );
         rtn.add(DataPoint.from(rowValues.valueSet()));
      }
      return rtn;
   }

   public void generateDummyData() {
      LatLng[] dummy = GPSHandler.dummy_taxiData;
      List<DataPoint> store = new ArrayList<DataPoint>();
      for (int i = 0; i < dummy.length ; i++) {
         store.add(new DataPoint(dummy[i].latitude, dummy[i].longitude, (1.0 * i)%10));
      }
      save(store);
   }

 } 