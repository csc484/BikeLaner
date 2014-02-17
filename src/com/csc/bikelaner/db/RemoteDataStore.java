package com.csc.bikelaner.db;

import java.util.Collection;

import org.apache.http.client.ResponseHandler;

import android.app.DownloadManager.Request;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.csc.bikelaner.db.data.DataPoint;

public class RemoteDataStore implements DataStore {

   public RemoteDataStore() {
      
   }
   @Override
   public void save(DataPoint datapoint) {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void save(Collection<DataPoint> datapoint) {
      // TODO Auto-generated method stub
      
   }

   @Override
   public Collection<DataPoint> getData(String where) {
      // TODO Auto-generated method stub
      return null;
   }

   private void setup() {
      //create client
      AWSCredentials credentials = new BasicAWSCredentials("access", "secret");

      AmazonDynamoDBClient ddb = new AmazonDynamoDBClient(credentials);
      
                
   }
}
