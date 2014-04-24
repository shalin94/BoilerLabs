package com.cs307.boilerlab;

import java.util.List;
import java.util.Iterator;

import com.google.android.gms.maps.model.LatLng;

import android.location.Address;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class ClosestLab extends FragmentActivity{

	LatLng needed_location = null;
	//double distance;
	
	public double computeClosestDistance(double userLat, double userLng, double labLat, double labLng) 
	{
		double earthRadius = 3959.0d; // radius of the earth in miles

		double dLat = toRadian(labLat - userLat);
		double dLng = toRadian(labLng - userLng); 

		double a = Math.pow(Math.sin(dLat/2), 2)  +  Math.cos(toRadian(userLat)) * Math.cos(toRadian(labLat)) * Math.pow(Math.sin(dLng/2), 2); 

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	 
		double distance =  earthRadius * c;

		return distance;// returns result miles
	}

	public static double toRadian(double degrees) 
	{
		return (degrees * Math.PI) / 180.0d;
	}
	
	//@Override
	//protected void onCreate(Bundle savedInstanceState)
	//{
	//	super.onCreate(savedInstanceState);
	//	setContentView(R.layout.activity_closestlab);
		 DatabaseHelper myDbHelper = null;
		 List<Address> addresses;
		 Map m = new Map();
		 //computeClosestDistance();
		 //toRadian();
		 
	     double minDistance  = 0.0d; 
	     double bldlat = 0, bldlong = 0;
	     double[] startLocation = new double[2];
	     double[] endLocation = new double[2];
	     {
     
	     try
	     {
	    	 myDbHelper = new DatabaseHelper(ClosestLab.this);
	    	 List<NBuildings> nbldg = myDbHelper.getBuilding();
	    	 Iterator<NBuildings> it = nbldg.iterator();
	    	 double[] gps = new double[2];
	    	 gps = m.getGPS();
	     
	    	 while(it.hasNext())
	    	 {
	    		 NBuildings temp = it.next();
	    		 String name = temp.getName();
	    		 String loc = temp.getBuildingLoc();
	    		 String [] locs = loc.split(",");
	    		 bldlat = Double.parseDouble(locs[0]);
	    		 bldlong = Double.parseDouble(locs[1]);
	       
	    		 if(minDistance > computeClosestDistance(gps[0],gps[1],bldlat,bldlong))
	    		 {
	    			 minDistance = computeClosestDistance(gps[0],gps[1],bldlat,bldlong);
	    			 startLocation[0] = gps[0];
	    			 startLocation[1] = gps[0];
	    			 endLocation[0]  = bldlat;
	    			 endLocation[1]  = bldlong;
	    			 Log.d("errorAB", "endLocation[0]: " + endLocation[0] + "endLocation[1]: " + endLocation[1]);
	    		 }
	    	 }
	    	 
	    	 needed_location = new LatLng(endLocation[0],endLocation[1]);
	     }
	     catch(Exception e)
	     {
				Log.e(this.getClass().getName(), "Failed to run query", e);
	     } 
	     finally 
	     {
				myDbHelper.close();
	     }
	     
		 } 
	//}
	
	public LatLng getLocation()
	{
		return needed_location;
	}
	
	//@Override
	//public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	//	getMenuInflater().inflate(R.menu.closestlab, menu);
	//	return true;
	//}
}

