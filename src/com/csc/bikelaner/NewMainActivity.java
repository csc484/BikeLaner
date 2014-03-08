package com.csc.bikelaner;

import com.csc.bikelaner.MainService.LocalBinder;
import com.csc.bikelaner.db.LocalDataStore;

import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class NewMainActivity extends FragmentActivity {

	/** UI elements **/
	ImageView m_vwImage;
	Button m_vwStartButton;
	Button m_vwDebugButton;
	Button m_vwMapButton;
	TextView m_vwAppNameText;
	
	/** Running state **/
	boolean m_running;

	/** Location foreground service **/
	protected Intent m_dataServiceIntent;
	private MainService m_dataService;
	boolean mBound = false;
	
	/** GPS Handler **/
	private GPSHandler gpsHandler;
	private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalBinder binder = (LocalBinder) service;
            m_dataService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_main);
		initLayout();
		initListeners();
		
	}
			
	public void initLayout() {
		this.m_vwImage = (ImageView) findViewById(R.id.main_activity_image);
		this.m_vwStartButton = (Button) findViewById(R.id.start_button);
		this.m_vwDebugButton = (Button) findViewById(R.id.debug_button);
		this.m_vwAppNameText = (TextView) findViewById(R.id.app_title);		
		this.m_vwMapButton = (Button) findViewById(R.id.map_button);
		this.m_vwMapButton.setEnabled(false);
	}
	
	public void initListeners() {
		if (this.m_vwStartButton != null)
			this.m_vwStartButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Resources r = getResources();
					if (!m_running) {
						initDataService();
						m_vwStartButton.setText(r.getString(R.string.stop_button));
						m_vwMapButton.setEnabled(true);
					}
					else {
						m_running = false;
						stopDataService();
						m_vwStartButton.setText(r.getString(R.string.start_button));
						m_vwMapButton.setEnabled(false);
					}
				}
			});
		
		if (this.m_vwDebugButton != null)
			this.m_vwDebugButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
				    Intent i = new Intent(NewMainActivity.this, MainActivity.class);
					startActivity(i);		
				} 
			});
		
		//init map
		if (this.m_vwMapButton != null)
			this.m_vwMapButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (m_dataService != null) {
						setContentView(R.layout.map_layout);
						gpsHandler = new GPSHandler(m_dataService.getLocationManager(),
							getSupportFragmentManager(),
							m_dataService.getStore());
						gpsHandler.initiate();						
					}
				}
			});
	}
	
	private void initDataService() {
		m_running = true;
		m_dataServiceIntent = new Intent(this, MainService.class);
		Log.d("initDataService", "starting dataService");
		startService(m_dataServiceIntent);
		bindService(m_dataServiceIntent, mConnection, Context.BIND_AUTO_CREATE);
	}
	
	private void stopDataService() {
		stopService(m_dataServiceIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_main, menu);
		return true;
	}

}
