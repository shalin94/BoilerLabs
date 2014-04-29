/*
 * The main activity of the Application. Displays the four buttons of the app and starts getting the location of the person
 * -@SRS
 */
package com.cs307.boilerlab;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.android.gms.location.LocationClient;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

@SuppressLint("CommitPrefEdits")
public class MainActivity extends Activity implements LocationListener{
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
boolean isData = false;
double gps[];
boolean waitForIt=true;
LocationClient mLocationClient;
public static double locationGPS[];
double[] getGPS() {
	
    lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);  
    List<String> providers = lm.getProviders(true);
    
    /* Loop over the array backwards, and if you get an accurate location, then break out the loop*/
    Location l = null;
    
    if(gps==null)
    {
    	gps=new double[2];
    	for (int i=providers.size()-1; i>=0; i--) {
            l = lm.getLastKnownLocation(providers.get(i));
            if (l != null) break;
    	}
    	if (l != null) {
            gps[0] = l.getLatitude();
            gps[1] = l.getLongitude();
    	}
    }
    
    return gps;
}

public void getClosestInfo()
{
	double[] g=getGPS();
	DatabaseHelper myDbHelper = null;
	double templat = 0,templong = 0;
    try{
		myDbHelper = new DatabaseHelper(MainActivity.this);
		List<NBuildings> nbldg = myDbHelper.getBuilding();
		Iterator<NBuildings> it = nbldg.iterator();
		while(it.hasNext()){
			NBuildings temp = it.next();
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
//    LabClosest lc = new LabClosest();
    int i=0;
 	  for(i=0; i < buildLat.size(); i++)
 	  {
 		  
 		buildLat.get(i);
 		float results[]=new float[10];
 		Location.distanceBetween(g[0], g[1], buildLat.get(i), buildLong.get(i),results);
 		buidDistance.add((double) results[0]);
 		if(buidDistance.get(close) > buidDistance.get(i))
 		{
 			close = i;
 		}
 	  }
}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.slidein, R.anim.slideout);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
	    getActionBar().hide();
		setContentView(R.layout.main2);
		LocationManager locationManager = (LocationManager)
				getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(
				LocationManager.GPS_PROVIDER, 50, 5, this);
		
		Button map = (Button) findViewById (R.id.buttonmap);
		Button closest = (Button) findViewById (R.id.buttonclosest);
		Button listlab = (Button) findViewById (R.id.buttonlist);
		Button preferences = (Button) findViewById (R.id.buttonpref);	
		context = getApplicationContext();
		map.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(online) {
					isData = checkInternet(context);
					if(isData) {
						Intent map = new Intent(MainActivity.this,Map.class);
						MainActivity.this.startActivity(map);
						overridePendingTransition(R.anim.slidein, R.anim.slideout);
					}
					else {
						Toast.makeText(MainActivity.this,"No Network! Make sure you are connected to the internet and your GPS is turned on!",9000).show();
					}
				}
				else {
					Toast.makeText(MainActivity.this,"Please change mode to online mode!",5000).show();
				}
			}
		});
		
		closest.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(online) {
					lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
					isData = checkInternet(context);
					if ( lm.isProviderEnabled( LocationManager.GPS_PROVIDER ) && (isData == true)) {
						Intent clo = new Intent(MainActivity.this,LabList.class);
						getClosestInfo();
						String n=buildname.get(close);
						clo.putExtra("closestLab", n);
						clo.putExtra("closest", "true");
						MainActivity.this.startActivity(clo);
					}
					else {
						Toast.makeText(MainActivity.this,"No Network! Make sure you are connected to the internet and your GPS is turned on!",9000).show();
					}
				}
				else {
					Toast.makeText(MainActivity.this,"Please change mode to online mode!",5000).show();
				}
			}
		});
		
		listlab.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(online) {
					isData = checkInternet(context);
					if(isData) {
						Intent listlab = new Intent(MainActivity.this,LabList.class);
						listlab.putExtra("closest", "false");
						MainActivity.this.startActivity(listlab);
						overridePendingTransition(R.anim.slidein, R.anim.slideout);
					}
					else {
						Toast.makeText(MainActivity.this,"No Network! Make sure you are connected to the internet and your GPS is turned on! OR Switch to offline mode!",9000).show();
					}
				}
				else {
					Intent listlab = new Intent(MainActivity.this,LabList.class);
					listlab.putExtra("closest", "false");
					MainActivity.this.startActivity(listlab);
					overridePendingTransition(R.anim.slidein, R.anim.slideout);
				}
			}
		});
		
		preferences.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(online) {
					isData = checkInternet(context);
					if(isData) {
						Intent preferences = new Intent(MainActivity.this,Preferences.class);
						MainActivity.this.startActivity(preferences);
						overridePendingTransition(R.anim.slidein, R.anim.slideout);
					}
					else {
						Toast.makeText(MainActivity.this,"No Network! Make sure you are connected to the internet and your GPS is turned on! OR Switch to offline mode!",9000).show();
					}
				}
				else {
					Intent preferences = new Intent(MainActivity.this,Preferences.class);
					MainActivity.this.startActivity(preferences);
					overridePendingTransition(R.anim.slidein, R.anim.slideout);
				}
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
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		final Editor editor = prefs.edit();
		if(item.getItemId() == R.id.walk)
		{
			editor.putBoolean("Walk", true);
		}
		else
		if(item.getItemId() == R.id.drive)
		{
			editor.remove("Walk");
		}
		else
		if(item.getItemId()==R.id.online)
		{
			editor.putBoolean("online",true);
			online=true;
		}
		else
		if(item.getItemId()==R.id.offline)
		{
			editor.remove("online");
			online=false;
		}
		editor.commit();
		return true;
	}
	
	public boolean checkInternet(Context ctx) {
	    ConnectivityManager connec = (ConnectivityManager) ctx
	            .getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo wifi = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	    NetworkInfo mobile = connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	    return wifi.isConnected() || mobile.isConnected();
	}

	@SuppressWarnings("static-access")
	@Override
	public void onLocationChanged(Location location) {
		gps=new double[2];
		if (location != null) {
            gps[0] = location.getLatitude();
            gps[1] = location.getLongitude();
    }
		waitForIt=false;
		this.locationGPS=gps;
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
}