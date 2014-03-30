package com.cs307.boilerlab;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.w3c.dom.Document;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.content.Context;
import android.graphics.Color;
//import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;

public class Map extends FragmentActivity {

	LocationManager lm;
	Geocoder geocoder;
	List<Address> addresses;
	Marker marker;
	
	private double[] getGPS() {
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
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
		
		double[] g=getGPS();
		
		// Get a handle to the Map Fragment
        GoogleMap map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
      
        CustomPopUp custompopup = new CustomPopUp(getLayoutInflater());
        map.setInfoWindowAdapter(custompopup);
       // map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        /*map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        map.setMapType(GoogleMap.MAP_TYPE_NONE);*/
        
        LatLng location = new LatLng(g[0], g[1]);
        LatLng finLocation = null;
        String[] address = new String[5];
        
        map.setMyLocationEnabled(true);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        DatabaseHelper myDbHelper = null;
        double templat = 0,templong = 0;
        try{
			myDbHelper = new DatabaseHelper(Map.this);
			List<Buildings> bldg = myDbHelper.getBuilding();
			Iterator<Buildings> it = bldg.iterator();
			while(it.hasNext()){
				Buildings temp = it.next();
				String name = temp.getName();
				String loc = temp.getBuildingLoc();
				String [] locs = loc.split(",");
				templat = Double.parseDouble(locs[0]);
				templong = Double.parseDouble(locs[1]);
				finLocation = new LatLng(templat,templong);
				geocoder = new Geocoder(this, Locale.getDefault());
				addresses = geocoder.getFromLocation(templat, templong, 1);
				address[0] = addresses.get(0).getAddressLine(0);
				address[1] = addresses.get(0).getAddressLine(1);
				address[2] = addresses.get(0).getAddressLine(2);
				address[3] = address[0] +" "+ address[1]+" " +" "+ address[3];
				map.addMarker(new MarkerOptions()
		        .position(finLocation)
		        .title(name)
				//.snippet(address[0])
				//.snippet(address[1])
				.snippet(address[3]));
			}
		}catch(Exception e){
			Log.e(this.getClass().getName(), "Failed to run query", e);
		} finally {
			myDbHelper.close();
		}
        
        /*MapView myMap = (MapView)findViewById(R.id.map);
        final MapController controller = myMap.getController();
        LocationListener listener = new LocationListener() {

			@Override
			public void onLocationChanged(Location location) {
				controller.setCenter(new GeoPoint ((int)location.getLatitude(),(int)location.getLongitude()));
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
				
			}
        };
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,listener);*/
        
        /*final Intent intent = new Intent(Intent.ACTION_VIEW,
        	    Uri.parse("+http://maps.google.com/maps?" + "saddr="+ g[0] + "," + g[1] + "&daddr="+ templat + "," + templong));
        	    intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
        	    startActivity(intent);*/
       
        MapDirection md = new MapDirection();

        Document doc = md.getDocument(location, finLocation, MapDirection.MODE_DRIVING);
		
        ArrayList<LatLng> directionPoint = md.getDirection(doc);
        PolylineOptions rectLine = new PolylineOptions().width(8).color(Color.BLUE).geodesic(true);

        for(int i = 0 ; i < directionPoint.size() ; i++) {          
        rectLine.add(directionPoint.get(i));
        }
        map.addPolyline(rectLine);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}
	

}
