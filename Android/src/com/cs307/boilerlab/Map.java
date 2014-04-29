/*
 * Map activity to display campus map and markers for all the labs
 * -@SRS
 */
package com.cs307.boilerlab;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class Map extends FragmentActivity {

	LocationManager lm;
	Geocoder geocoder;
	List<Address> addresses;
	ArrayList<Double> buildLat = new ArrayList<Double>();
	ArrayList<Double> buildLong = new ArrayList<Double>();
	ArrayList<Double> buidDistance = new ArrayList<Double>();
	boolean wantsHybrid = true;
	String closests;
	String fullname = null, name = null;
	GoogleMap map;
	int check = 1;
	
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.slidein, R.anim.slideout);
		setContentView(R.layout.activity_map);	
		
		if(savedInstanceState != null) {
			wantsHybrid = savedInstanceState.getBoolean("wantsHybrid");
		}
		
		Intent in = getIntent();
		closests = in.getStringExtra("closest");
		if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
		
		double[] g=getGPS();
		
		// Get a handle to the Map Fragment
        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        
        final LatLng location = new LatLng(g[0], g[1]);
        LatLng finLocation = null;
        
        map.setMyLocationEnabled(true);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        
        CustomPopUp custompopup = new CustomPopUp(getLayoutInflater());
        map.setInfoWindowAdapter(custompopup);
        
        DatabaseHelper myDbHelper = null;
        double templat = 0,templong = 0;
        try{
			myDbHelper = new DatabaseHelper(Map.this);
			List<NBuildings> nbldg = myDbHelper.getBuilding();
			Iterator<NBuildings> it = nbldg.iterator();
			while(it.hasNext()){
				NBuildings temp = it.next();
				name = temp.getName();
				fullname = temp.getFullName();
				String address = temp.getBuildingAddress();
				name = name.trim();
				String loc = temp.getBuildingLoc();
				String [] locs = loc.split(",");
				templat = Double.parseDouble(locs[0]);
				templong = Double.parseDouble(locs[1]);
				finLocation = new LatLng(templat,templong);
				map.addMarker(new MarkerOptions()
		        .position(finLocation)
		        .title(name)
				.snippet(address));
			}
		}catch(Exception e){
			Log.e(this.getClass().getName(), "Failed to run query", e);
		} finally {
			myDbHelper.close();
		}
	}
        
	public String getFullName()
	{
		return fullname;
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean("wantsHybrid", wantsHybrid);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		menu.getItem(0).setChecked(wantsHybrid);
		menu.getItem(1).setChecked(!wantsHybrid);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if(item.getItemId() == R.id.hybrid)
		{
			map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		}
		else
		if(item.getItemId() == R.id.normal)
		{
			map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		}
		return true;
	}
	

}
