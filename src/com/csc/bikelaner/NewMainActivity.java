package com.csc.bikelaner;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class NewMainActivity extends Activity {

	/** UI elements **/
	ImageView m_vwImage;
	Button m_vwStartButton;
	Button m_vwDebugButton;
	TextView m_vwAppNameText;
	
	/** Running state **/
	boolean m_running;
	
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
	}
	
	public void initListeners() {
		if (this.m_vwStartButton != null)
			this.m_vwStartButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Resources r = getResources();
					if (!m_running) {
						m_running = true;
						m_vwStartButton.setText(r.getString(R.string.stop_button));	
					}
					else {
						m_running = false;
						m_vwStartButton.setText(r.getString(R.string.start_button));
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_main, menu);
		return true;
	}

}
