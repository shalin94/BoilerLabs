package com.cs307.boilerlab;

import java.util.ArrayList;
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
	
	
	
	public void compute() 
	{
	
      Map m = new Map();
      ArrayList<Double> buidDistance = new ArrayList<Double>();
      
   	  double[] gps = new double[2];
   	  gps = m.getGPS();
   	  int i=0;
   	  int closest = 0;
   	  for(i=0; i < m.buildLat.size(); i++)
   	  {
   		  
   		m.buildLat.get(i);
   		buidDistance.add(computeClosestDistance(gps[0], gps[1], m.buildLat.get(i), m.buildLong.get(i)));
   		
   		if(buidDistance.get(closest) > buidDistance.get(i))
   		{
   			closest = i;
   		}
   	  }
   
   	  endLocation[0] =  m.buildLat.get(closest);
   	  endLocation[1]=   m.buildLong.get(closest);
   	   
		
	 } 

}


