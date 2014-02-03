package com.csc.bikelaner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LocalDB extends SQLiteOpenHelper {

   public static final String TABLE_DATA = "data";
   public static final String COLUMN_ID = "_id";

   private static final String DATABASE_NAME = "data.db";
   private static final int DATABASE_VERSION = 1;
   private static final String DATABASE_CREATE;
   private static final String DATABASE_BASE_INSERT;
   static {
      StringBuilder create = new StringBuilder("create table " + TABLE_DATA
            + "(");
      DBColumn[] cols = DBColumn.values();
      for (int i = 0; i < cols.length; i++) {
         String sep = getSeperator(i, cols.length);
         create.append(cols[i] + " " + cols[i].getDataType()).append(sep);
      }
      DATABASE_CREATE = create.toString();
      
      
      StringBuilder insert = new StringBuilder("insert into " + TABLE_DATA
            + "(");
      for (int i = 0; i < cols.length; i++) {
         String sep = getSeperator(i, cols.length);
         insert.append(cols[i]).append(sep);
      }
      DATABASE_BASE_INSERT = insert.toString();

   }

   public static String getSeperator(int current, int max) {
      return current == max - 1 ? "," : ");";
   }
   public LocalDB(Context context) {
     super(context, DATABASE_NAME, null, DATABASE_VERSION);
   }

   @Override
   public void onCreate(SQLiteDatabase database) {
     database.execSQL(DATABASE_CREATE);
     Log.w("D", "created db");
   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
     Log.w(LocalDB.class.getName(),
         "Upgrading database from version " + oldVersion + " to "
             + newVersion + ", which will destroy all old data");
     db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA);
     onCreate(db);
   }
   
   public enum DBColumn {
      LONGITUDE("Decimal(10,2)"),LATITUDE("Decimal(10,2)");
      private String type;
      
      private DBColumn(String type) {
         this.type =type;
      }
      
      public String getDataType() {
         return type;
      }
   }

 } 