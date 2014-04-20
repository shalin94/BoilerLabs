package com.cs307.boilerlab;

public class LabClosest {
	
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

}


