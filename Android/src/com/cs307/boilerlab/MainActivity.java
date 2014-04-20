package com.cs307.boilerlab;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
public static Boolean online = true;
private static Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button map = (Button) findViewById (R.id.buttonmap);
		Button closest = (Button) findViewById (R.id.buttonclosest);
		Button listlab = (Button) findViewById (R.id.buttonlist);
		Button preferences = (Button) findViewById (R.id.buttonpref);	
		final Button mode = (Button) findViewById (R.id.mode);
		
		context = getApplicationContext();
		map.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent map = new Intent(MainActivity.this,Map.class);
				map.putExtra("closest","false");
				MainActivity.this.startActivity(map);
				// TODO Auto-generated method stub
			}
		});
		
		closest.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent closest = new Intent(MainActivity.this,Map.class);
				closest.putExtra("closest","true");

				MainActivity.this.startActivity(closest);
				// TODO Auto-generated method stub
			}
		});
		
		listlab.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent listlab = new Intent(MainActivity.this,LabList.class);
				MainActivity.this.startActivity(listlab);
				// TODO Auto-generated method stub
			}
		});
		
		preferences.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent preferences = new Intent(MainActivity.this,Preferences.class);
				MainActivity.this.startActivity(preferences);
				// TODO Auto-generated method stub
			}
		});
		
		mode.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(online) {
					mode.setText("Offline");
					online = false;
				}
				else {
					mode.setText("Online");
					online = true;
				}
				// TODO Auto-generated method stub
			}
		});
	}
	public static Context getContext() {
		return context;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
