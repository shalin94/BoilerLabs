package com.cs307.boilerlab;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.w3c.dom.Document;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
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
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Map extends FragmentActivity {

	LocationManager lm;
	Geocoder geocoder;
	List<Address> addresses;
	ArrayList<Double> buildLat = new ArrayList<Double>();
	ArrayList<Double> buildLong = new ArrayList<Double>();
	ArrayList<Double> buidDistance = new ArrayList<Double>();
	boolean wantsToWalk=true;
	String closests;
	GoogleMap map;
	int check = 0;
	
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
		setContentView(R.layout.activity_map);	
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
        String s1,s2,s3,s4 = null,s5;
        
        map.setMyLocationEnabled(true);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        
     // Getting reference to btn_find of the layout activity_main
        Button btn_find = (Button) findViewById(R.id.btn_find);
 
        // Defining button click event listener for the find button
        OnClickListener findClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Getting reference to EditText to get the user input location
                EditText etLocation = (EditText) findViewById(R.id.et_location);
 
                // Getting user input location
                String tmplocation = etLocation.getText().toString();
 
                if(tmplocation!=null && !tmplocation.equals("")){
                	Geocoder geocoder = new Geocoder(getBaseContext());
                    //List<Address> addresses = null;
                	map.clear();
                	check = 1;
                    try {
                        addresses = geocoder.getFromLocationName(tmplocation, 3);
                        
                        Address address = (Address) addresses.get(0);
                        double templat = 0,templong = 0;
                        templat = address.getLatitude();
                        templong = address.getLongitude();
                        LatLng finLoc = new LatLng(templat,templong);
                       
                        /*s1 = addresses.get(0).getAddressLine(0);
        				s2 = addresses.get(0).getAddressLine(1);
        				s3 = addresses.get(0).getAddressLine(2);
        				s4 = addresses.get(0).getAddressLine(3);
        				if(s4 != null)
        					s5 = s1 +"\n"+ s2+"\n"+s3+"\n"+s4;
        				else
        					s5 = s1 +"\n"+ s2+"\n"+s3;*/
                   
        				map.addMarker(new MarkerOptions()
        		        .position(finLoc)
        		        .title(tmplocation));
        				//.snippet(s5));
        				map.animateCamera(CameraUpdateFactory.newLatLng(finLoc));
        				
        				addDirection(templat,templong,location);
        				
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
 
        // Setting button click event listener for the find button
        btn_find.setOnClickListener(findClickListener);
        
        CustomPopUp custompopup = new CustomPopUp(getLayoutInflater());
        map.setInfoWindowAdapter(custompopup);
        
        DatabaseHelper myDbHelper = null;
        double templat = 0,templong = 0;
        try{
			myDbHelper = new DatabaseHelper(Map.this);
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
				finLocation = new LatLng(templat,templong);
				geocoder = new Geocoder(this, Locale.getDefault());
				addresses = geocoder.getFromLocation(templat, templong, 1);
				s1 = addresses.get(0).getAddressLine(0);
				s2 = addresses.get(0).getAddressLine(1);
				s3 = addresses.get(0).getAddressLine(2);
				s4 = addresses.get(0).getAddressLine(3);
				if(s4 != null)
					s5 = s1 +"\n"+ s2+"\n"+s3+"\n"+s4;
				else
					s5 = s1 +"\n"+ s2+"\n"+s3;
				map.addMarker(new MarkerOptions()
		        .position(finLocation)
		        .title(name)
				.snippet(s5));
			}
		}catch(Exception e){
			Log.e(this.getClass().getName(), "Failed to run query", e);
		} finally {
			myDbHelper.close();
		}
        
        
      double[] endLocation = new double[2];  
      LabClosest lc = new LabClosest();          
      double[] gps = new double[2];
   	  gps = getGPS();        
      
   	  int i=0;
   	  int closest = 0;
   	  for(i=0; i < buildLat.size(); i++)
   	  {
   		  
   		buildLat.get(i);
   		buidDistance.add(lc.computeClosestDistance(gps[0], gps[1], buildLat.get(i), buildLong.get(i)));
   		
   		if(buidDistance.get(closest) > buidDistance.get(i))
   		{
   			closest = i;
   		}
   	  }
   
   	  endLocation[0] =  buildLat.get(closest);
   	  endLocation[1]=   buildLong.get(closest);
      
   	  addDirection(endLocation[0],endLocation[1],location);
	}
	
	void addDirection(double lat, double longi, LatLng location)
	{
		MapDirection md = new MapDirection();
        
        LatLng closeLab = new LatLng(lat,longi);
          Document doc;
          if(wantsToWalk)
        	  doc = md.getDocument(location, closeLab, MapDirection.MODE_WALKING);
          else
        	  doc = md.getDocument(location, closeLab, MapDirection.MODE_DRIVING);
          
        ArrayList<LatLng> directionPoint = md.getDirection(doc);
        PolylineOptions rectLine = new PolylineOptions().width(8).color(Color.argb(255, 51, 181, 229)).geodesic(true);

        for(int i1 = 0 ; i1 < directionPoint.size() ; i1++) {          
        rectLine.add(directionPoint.get(i1));
        }
        if(check == 1) {//if(closests.equals("true")) {
        map.addPolyline(rectLine);
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		menu.getItem(1).setChecked(true);
		menu.getItem(2).setChecked(false);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if(item.getItemId()==1)
		{
			wantsToWalk=true;
		}
		else
		if(item.getItemId()==2)
		{
			wantsToWalk=false;
		}
		return true;
	}
	

}
