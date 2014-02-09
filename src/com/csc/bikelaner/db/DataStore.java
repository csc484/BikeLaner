package com.csc.bikelaner.db;

import java.util.Collection;

import com.csc.bikelaner.db.data.DataPoint;

public interface DataStore {
   void save(DataPoint datapoint);
   void save(Collection<DataPoint> datapoint);
   Collection<DataPoint> getData(String where);
}
