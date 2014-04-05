package com.cs307.boilerlab;

import java.util.Iterator;
import java.util.List;

import android.location.Address;
import android.util.Log;

public class LabClosest {
	double[] endLocation = new double[2];

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

	public double toRadian(double degrees) 
	{
		return (degrees * Math.PI) / 180.0d;
	}
	
	public double[] getEndLocation()
	{
		compute();
	  return endLocation;
	}
	
	public void compute() {
		DatabaseHelper myDbHelper = null;
		 List<Address> addresses;
		 Map m = new Map();
		 
	     double minDistance  = 0.0d; 
	     double bldlat = 0, bldlong = 0;
	     double[] startLocation = new double[2];
	     {
    
	     try
	     {
	    	 myDbHelper = new DatabaseHelper(MainActivity.getContext());
	    	 List<Buildings> bldg = myDbHelper.getBuilding();
	    	 Iterator<Buildings> it = bldg.iterator();
	    	 double[] gps = new double[2];
	    	 gps = m.getGPS();
	     
	    	 while(it.hasNext())
	    	 {
	    		 Buildings temp = it.next();
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
	    		 }
	    	 }
	    	 
	    	 
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
	}
}
