package com.csc.bikelaner.db.data;

import android.content.Context;
import android.provider.Settings.Secure;

public class Defaults {
   private static Defaults defaults;
   private Long sessionId;
   private String deviceId;
   private Defaults(Context context) {
      deviceId = Secure.getString(context.getContentResolver(),
            Secure.ANDROID_ID); 
      sessionId = System.currentTimeMillis();
   }
   
   public static Defaults get() {
      if(defaults == null) {
         throw new RuntimeException("Defaults not created");
      }
      return defaults;
   }
   public static Defaults init(Context context) {
      return defaults != null ? defaults : (defaults = new Defaults(context));
   }
   public Long getSessionId() {
      return sessionId;
   }
   public String getDeviceId() {
      return deviceId;
   }
}
