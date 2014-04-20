package com.cs307.boilerlab;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
public static Boolean online = true;
private static Context context;
public static Boolean ref = false;
LocationManager lm;
Geocoder geocoder;
List<Address> addresses;
ArrayList<Double> buildLat = new ArrayList<Double>();
ArrayList<Double> buildLong = new ArrayList<Double>();
ArrayList<Double> buidDistance = new ArrayList<Double>();
ArrayList<String> buildname=new ArrayList<String>();
int close=0;

double[] getGPS() {
    lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);  
    List<String> providers = lm.getProviders(true);

    /* Loop over the array backwards, and if you get an accurate location, then break out the loop*/
    Location l = null;
    
    for (int i=providers.size()-1; i>=0; i--) {
            l = lm.getLastKnownLocation(providers.get(i));
            if (l != null) break;
    }
    
    double[] gps = new double[2];
    if (l != null) {
            gps[0] = l.getLatitude();
            gps[1] = l.getLongitude();
    }
    return gps;
}

public void getClosestInfo()
{
	double[] gps=getGPS();
	DatabaseHelper myDbHelper = null;
	
	double templat = 0,templong = 0;
    try{
		myDbHelper = new DatabaseHelper(MainActivity.this);
		List<Buildings> bldg = myDbHelper.getBuilding();
		Iterator<Buildings> it = bldg.iterator();
		while(it.hasNext()){
			Buildings temp = it.next();
			String name = temp.getName();
			name = name.trim();
			String loc = temp.getBuildingLoc();
			String [] locs = loc.split(",");
			templat = Double.parseDouble(locs[0]);
			templong = Double.parseDouble(locs[1]);
			buildLat.add(templat);
			buildLong.add(templong);
			buildname.add(name);
		}
	}catch(Exception e){
		Log.e(this.getClass().getName(), "Failed to run query", e);
	} finally {
		myDbHelper.close();
	}
    LabClosest lc = new LabClosest();
    int i=0;
 	  //int closest = 0;
 	  for(i=0; i < buildLat.size(); i++)
 	  {
 		  
 		buildLat.get(i);
 		buidDistance.add(lc.computeClosestDistance(gps[0], gps[1], buildLat.get(i), buildLong.get(i)));
 		
 		if(buidDistance.get(close) > buidDistance.get(i))
 		{
 			close = i;
 		}
 	  }
}

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
				//map.putExtra("closest","false");
				MainActivity.this.startActivity(map);
				// TODO Auto-generated method stub
			}
		});
		
		closest.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent clo = new Intent(MainActivity.this,LabList.class);
				getClosestInfo();
				String n=buildname.get(close);
				clo.putExtra("closestLab", n);
				clo.putExtra("closest", "true");
				MainActivity.this.startActivity(clo);
				// TODO Auto-generated method stub
			}
		});
		
		listlab.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent listlab = new Intent(MainActivity.this,LabList.class);
				listlab.putExtra("closest", "false");
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
