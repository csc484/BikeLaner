package com.csc.bikelaner.db.data;

public class Defaults {
   private static Defaults defaults;
   private Defaults() {
      
   }
   
   public static Defaults get() {
      return defaults != null ? defaults : (defaults = new Defaults());
   }
   public String getSessionId() {
      return "";
   }
   public String getDeviceId() {
      return "";
   }
}
