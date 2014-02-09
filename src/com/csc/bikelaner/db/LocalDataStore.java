package com.csc.bikelaner.db;

import java.util.Arrays;
import java.util.Collection;

import com.csc.bikelaner.db.data.DataPoint;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.provider.Settings.Secure;
import android.util.Log;

@SuppressLint("DefaultLocale")
public class LocalDataStore extends SQLiteOpenHelper implements DataStore{

   public static final String TABLE_NAME = "data";
   public static final String COLUMN_ID = "_id";

   private static final String DATABASE_NAME = "data.db";
   private static final int DATABASE_VERSION = 1;
   private static final String DATABASE_CREATE;
   
   public final String deviceId;
   static {
      StringBuilder create = new StringBuilder("create table " + TABLE_NAME
            + "(");
      SQLiteQueryBuilder query = new SQLiteQueryBuilder();

      DBColumn[] cols = DBColumn.values();
      for (int i = 0; i < cols.length; i++) {
         String sep = getSeperator(i, cols.length);
         create.append(cols[i] + " " + cols[i].getDataType()).append(sep);
      }
      DATABASE_CREATE = create.toString();

   }

   public static String getSeperator(int current, int max) {

      return current < max -1 ? "," : ");";
   }
   public LocalDataStore(Context context) {
     super(context, DATABASE_NAME, null, DATABASE_VERSION);
     deviceId = Secure.getString(context.getContentResolver(),
           Secure.ANDROID_ID);
   }

   @Override
   public void onCreate(SQLiteDatabase database) {
     
     
     database.execSQL(DATABASE_CREATE);
     Log.w("D", "created db");
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
      DEVICEID("VARCHAR(100)"), LONGITUDE("INTEGER"),LATITUDE("INTEGER"), SPEED("REAL"), SESSIONID("INTEGER");
      private String type;
      
      private DBColumn(String type) {
         this.type =type;
      }
      
      public String getDataType() {
         return type;
      }
      

      @Override
      public String toString() {
         return name().toLowerCase();
      }
   }

   @Override
   public void save(DataPoint datapoint) {
     save(Arrays.asList(datapoint));
   }
   @Override
   public void save(Collection<DataPoint> datapoints) {
      ContentValues values = new ContentValues();
      SQLiteDatabase database = getWritableDatabase();
      
      database.beginTransaction();
      for(DataPoint point : datapoints) {
         database.insert(TABLE_NAME, null, point.to());
      }
      database.endTransaction();
      
   }
   @Override
   public Collection<DataPoint> getData(String where) {
     
      return null;
   }

 } 