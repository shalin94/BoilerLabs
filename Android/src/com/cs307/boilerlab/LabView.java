package com.cs307.boilerlab;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Document;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class LabView extends Activity implements LocationListener  {
	LocationManager lm;
	double[] g;
	private double[] getGPS() {
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);  
	    List<String> providers = lm.getProviders(true);
	    
	    /* Loop over the array backwards, and if you get an accurate location, then break out the loop*/
	    Location l = null;
	    /*
	    for (int i=providers.size()-1; i>=0; i--) {
	            //l = lm.getLastKnownLocation(providers.get(i));
	    	l=mLocationClient.getLastLocation();
	            if (l != null) break;
	    }
	    */
	    //l=mLocationClient.getLastLocation();
	    if(g==null)
	    {
	    	g=new double[2];
	    	for (int i=providers.size()-1; i>=0; i--) {
	            l = lm.getLastKnownLocation(providers.get(i));
	    	//l=mLocationClient.getLastLocation();
	            if (l != null) break;
	    	}
	    	if (l != null) {
	            g[0] = l.getLatitude();
	            g[1] = l.getLongitude();
	    	}
	    }
	    //double[] gps = new double[2];
	    
	    return g;
}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.slidein, R.anim.slideout);
		//mLocationClient = new LocationClient(this, this, this);
	    //mLocationClient.connect();
		
		LocationManager locationManager = (LocationManager)
				getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(
				LocationManager.GPS_PROVIDER, 5000, 10, this);
				
		if(MainActivity.online == false) {
		setContentView(R.layout.activity_lab_view);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
		
		Intent i = getIntent();
		final String name = i.getStringExtra("name");
		TextView txtName = (TextView) findViewById(R.id.Name);
		txtName.setText(name);
		DatabaseHelper myDbHelper = null;
		TextView txtType = (TextView) findViewById(R.id.Type);
		TextView txtNo = (TextView) findViewById(R.id.NoOfComputers);
		TextView txtTime = (TextView) findViewById(R.id.MonToFiTime);
		TextView txtSat = (TextView) findViewById(R.id.SatTime);
		TextView txtSun = (TextView) findViewById(R.id.SunTime);
		
		final Button fav = (Button) findViewById (R.id.addtofav);
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		final Editor editor = prefs.edit();
		
		//final TextView favorit=(TextView) findViewById(R.id.addtofav);
		
		if(prefs.contains(name))
		{
			//favorit.setTextColor((Color.parseColor("#dbac00")));

			fav.setText("Favorite -");
		}
		fav.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(prefs.contains(name))
				{
					Log.d("Removed: ",name);
					prefs.edit().remove(name).commit();
					
					//ntent refresh = new Intent(LabView.this, LabView.class);
					//finish();
					//startActivity(refresh);					
					//favorit.setTextColor((Color.parseColor("#000000")));
					MainActivity.ref=true;
					fav.setText("Favorite +");
				}
				else
				{
					Log.d("Added: ",name);
					editor.putBoolean(name, true);
					editor.commit();
					fav.setText("Favorite -");
					
				}
				// TODO Auto-generated method stub
			}
		});
		
		try{
			myDbHelper = new DatabaseHelper(LabView.this);
			Cursor cursor = myDbHelper.rawQuery("SELECT * from Details where lab_id=(select _id from Labs where lab_name='"+name+"')",null);
			if(cursor.moveToFirst()) {
				while (cursor.isAfterLast() == false){
					String closesun = cursor.getString(0);
					String opensun = cursor.getString(1);
					String closesat = cursor.getString(2);
					String opensat = cursor.getString(3);
					String type = cursor.getString(7);
					String number = cursor.getString(8);
					String open = cursor.getString(9);
					String close = cursor.getString(10);
					txtType.setText(type);
					txtNo.setText(number + " Computers Available");
					txtTime.setText(open + " - " + close);
					txtSat.setText(opensat + " - " + closesat);
					txtSun.setText(opensun + " - " + closesun);
	                cursor.moveToNext();
				}
			}
		}catch(Exception e){
			Log.e(this.getClass().getName(), "Failed to run query", e);
		} finally {
			myDbHelper.close();
		}
		
		//The Mini Map Stuff
		GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map2)).getMap();
		map.setMyLocationEnabled(true);
		map.setPadding(0, 0, 0, 0);
		
		
		
		
		double[] g=getGPS();
		g[0]=(double)Math.round(g[0] * 1000000) / 1000000;
		g[1]=(double)Math.round(g[1] * 1000000) / 1000000;
		LatLng sydney = new LatLng(g[0], g[1]);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));
		String [] names = name.split(" ");
		String name3=names[0];
		//name3=name3.trim();
		LatLng finloc=null;
        try{
			myDbHelper = new DatabaseHelper(LabView.this);
			List<NBuildings> nbldg = myDbHelper.getBuilding();
			Iterator<NBuildings> it = nbldg.iterator();
			while(it.hasNext()){
				NBuildings temp = it.next();
				String name2 = temp.getName();
				name2=name2.trim();
				Log.d("TESTTT","I am here with !"+name2+"!");
				Log.d("TESTTT","---and with !"+name3+"!");
				if(!name2.equals(name3))
				{
					continue;
				}
				//Log.d("...","I am here Too");
				String loc = temp.getBuildingLoc();
				String [] locs = loc.split(",");
				Log.d("directions","1: "+locs[0]+" 2: "+locs[1]);
				Log.d("Sydney directions","1: "+sydney.latitude+" 2: "+sydney.longitude);
				finloc=new LatLng(Double.parseDouble(locs[0]), Double.parseDouble(locs[1]));
				map.addMarker(new MarkerOptions()
		        .position(finloc)
		        .title(name));
				
				MapDirection md = new MapDirection();

		        Document doc;
	
				if(prefs.contains("Walk"))
				{
					Log.d("Prefs: ","Walk");
					doc= md.getDocument(sydney, finloc, MapDirection.MODE_WALKING);
				}
				else
				{	
					Log.d("Prefs: ","Drive");
					doc= md.getDocument(sydney, finloc, MapDirection.MODE_DRIVING);
				}
		        
		        ArrayList<LatLng> directionPoint = md.getDirection(doc);

		        PolylineOptions rectLine = new PolylineOptions().width(5).color(Color.argb(255, 51, 181, 229)).geodesic(true);
		        

		        for(int i1 = 0 ; i1 < directionPoint.size() ; i1++) {          
		        rectLine.add(directionPoint.get(i1));
		        }
		        map.addPolyline(rectLine);
				break;
			}
		}catch(Exception e){
			Log.e(this.getClass().getName(), "Failed to run query", e);
		} finally {
			myDbHelper.close();
		}
		}
		else {
			setContentView(R.layout.activity_ll_online);
			if (android.os.Build.VERSION.SDK_INT > 9) {
	            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	            StrictMode.setThreadPolicy(policy);
	        }
			Intent i = getIntent();
			final String name = i.getStringExtra("name");
			final TextView txtName = (TextView) findViewById(R.id.oName);
			final String [] apt = name.split(" ");
			final StringBuilder t = new StringBuilder();
			final StringBuilder status = new StringBuilder();
			final TextView oStatus = (TextView) findViewById(R.id.oStatus);
			txtName.setText(name);
			final StringBuilder type = new StringBuilder();
			final StringBuilder noComp = new StringBuilder();
			final StringBuilder noIU = new StringBuilder();
			final StringBuilder printer = new StringBuilder();
			final StringBuilder scanner = new StringBuilder();
			TextView oType = (TextView) findViewById(R.id.oType);
			TextView oComp = (TextView) findViewById(R.id.oComp);
			TextView oIU = (TextView) findViewById(R.id.oIU);
			TextView oPrinter = (TextView) findViewById(R.id.oPrinter);
			TextView oScanner = (TextView) findViewById(R.id.oScanner);
			final TextView text=(TextView) findViewById(R.id.oStatus);
			Thread th = new Thread(){
				public void run(){
					parseInfo pi = new parseInfo();
					pi.start(apt[0],apt[1]);
					if(pi.isOpen){
						text.setTextColor((Color.parseColor("#4f8329")));
						status.append(" Open");
					}
					else {
						
						text.setTextColor((Color.parseColor("#eb3d00")));
						if(pi.numComputersInUse < 1) {
							status.append(" Closed");
						}
						else {
							status.append(" Closed (Lab Occupied)");
						}
					}
					if(pi.hasPCs && pi.hasMacs) {
						type.append(" PC/Mac");
					}
					else if(pi.hasPCs) {
						type.append(" PC");
					}
					else if(pi.hasMacs) {
						type.append(" Mac");
					}
					noComp.append("There are ");
					noComp.append(pi.numComputers);
					noComp.append(" Computer(s)");
					noIU.append(pi.numComputersInUse);
					noIU.append(" Computer(s) are in use");
					if(pi.hasBlackAndWhitePrinters && pi.hasColorPrinters) {
						if((pi.numBlackAndWhitePrinters + pi.numColorPrinters) >1 ) {
						printer.append("There are ");
						printer.append(pi.numBlackAndWhitePrinters + pi.numColorPrinters);
						printer.append(" Color and Black/White printers");
						}
						else {
							printer.append("There is ");
							printer.append(pi.numBlackAndWhitePrinters + pi.numColorPrinters);
							printer.append(" Color and Black/White printer");
						}
					}
					else if (pi.hasBlackAndWhitePrinters) {
						if(pi.numBlackAndWhitePrinters > 1) {
						printer.append("There are ");
						printer.append(pi.numBlackAndWhitePrinters);
						printer.append(" Black/White printers");
						}
						else{
							printer.append("There is ");
							printer.append(pi.numBlackAndWhitePrinters);
							printer.append(" Black/White printer");
						}
					}
					else if (pi.hasColorPrinters){
						if(pi.numColorPrinters > 1) {
							printer.append("There are ");
							printer.append(pi.numColorPrinters);
							printer.append(" Color printers");
						}
						else {
							printer.append("There is ");
							printer.append(pi.numColorPrinters);
							printer.append(" Color printer");
						}
					}
					else{
						printer.append("No printers!");
					}
					if(pi.hasScanners) {
						if(pi.numScanners > 1) {
							scanner.append("There are ");
							scanner.append(pi.numScanners);
							scanner.append(" scanners");
						}
						else {
							scanner.append("There is ");
							scanner.append(pi.numScanners);
							scanner.append(" scanner");
						}
					}
					else {
						scanner.append("No scanners!");
					}
				}
			};
			th.start();
			try{
			th.join();
			} catch (Exception e) {
				
			}
			oStatus.setText(status);
			oType.setText(type);
			oComp.setText(noComp);
			oIU.setText(noIU);
			oPrinter.setText(printer);
			oScanner.setText(scanner);
			final Button fav = (Button) findViewById (R.id.addtofav2);
			final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			final Editor editor = prefs.edit();
			
			//final TextView favorit2=(TextView) findViewById(R.id.addtofav2);
			
			if(prefs.contains(name))
			{
				//favorit2.setTextColor((Color.parseColor("#dbac00")));
				fav.setText("Favorite -");
			}
			fav.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) 
				{
					if(prefs.contains(name))
					{
						Log.d("Removed: ",name);
						prefs.edit().remove(name).commit();
						
						//Intent refresh = new Intent(LabView.this, SharedPreferences.class);
						//finish();
						//startActivity(refresh);
						//finish();
						MainActivity.ref=true;
						//favorit2.setTextColor((Color.parseColor("#000000")));
						fav.setText("Favorite +");
					}
					
					else{
					Log.d("Removed: ",name);
					editor.putBoolean(name, true);
					editor.commit();
					fav.setText("Favorite -");
					// TODO Auto-generated method stub
					}
				}
			});
			GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map3)).getMap();
			map.setMyLocationEnabled(true);
			map.setPadding(0, 0, 0, 0);
			DatabaseHelper myDbHelper = null;
			double[] g=getGPS();
			g[0]=(double)Math.round(g[0] * 1000000) / 1000000;
			g[1]=(double)Math.round(g[1] * 1000000) / 1000000;
			LatLng sydney = new LatLng(g[0], g[1]);
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));
			String [] names = name.split(" ");
			String name3=names[0];
			//name3=name3.trim();
			LatLng finloc=null;
	        try{
				myDbHelper = new DatabaseHelper(LabView.this);
				List<NBuildings> nbldg = myDbHelper.getBuilding();
				Iterator<NBuildings> it = nbldg.iterator();
				while(it.hasNext()){
					NBuildings temp = it.next();
					String name2 = temp.getName();
					name2=name2.trim();
					Log.d("TESTTT","I am here with !"+name2+"!");
					Log.d("TESTTT","---and with !"+name3+"!");
					if(!name2.equals(name3))
					{
						continue;
					}
					//Log.d("...","I am here Too");
					String loc = temp.getBuildingLoc();
					String [] locs = loc.split(",");
					Log.d("directions","1: "+locs[0]+" 2: "+locs[1]);
					Log.d("Sydney directions","1: "+sydney.latitude+" 2: "+sydney.longitude);
					finloc=new LatLng(Double.parseDouble(locs[0]), Double.parseDouble(locs[1]));
					map.addMarker(new MarkerOptions()
			        .position(finloc)
			        .title(name));
					
					MapDirection md = new MapDirection();

					Document doc;
					
					if(prefs.contains("Walk"))
					{
						Log.d("Prefs: ","Walk");
						doc= md.getDocument(sydney, finloc, MapDirection.MODE_WALKING);
					}
					else
					{	
						Log.d("Prefs: ","Drive");
						doc= md.getDocument(sydney, finloc, MapDirection.MODE_DRIVING);
					}
			        
			        ArrayList<LatLng> directionPoint = md.getDirection(doc);

			        PolylineOptions rectLine = new PolylineOptions().width(5).color(Color.argb(255, 51, 181, 229)).geodesic(true);
			        

			        for(int i1 = 0 ; i1 < directionPoint.size() ; i1++) {          
			        rectLine.add(directionPoint.get(i1));
			        }
			        map.addPolyline(rectLine);
					break;
				}
			}catch(Exception e){
				Log.e(this.getClass().getName(), "Failed to run query", e);
			} finally {
				myDbHelper.close();
			}
		}
 
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lab_view, menu);
		return true;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		g=new double[2];
		if (location != null) {
            g[0] = location.getLatitude();
            g[1] = location.getLongitude();
    }
		
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
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	

//SELECT * from Details where lab_id=(select _id from Labs where lab_name='GRIS 121')
/*
@Override
public void onConnected(Bundle arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void onDisconnected() {
	// TODO Auto-generated method stub
	
}

@Override
protected void onStart() {
    super.onStart();
    // Connect the client.
    mLocationClient = new LocationClient(this, this, this);
    mLocationClient.connect();
}
// ...
/*
 * Called when the Activity is no longer visible.
 */
/*
@Override
protected void onStop() {
    // Disconnecting the client invalidates it.
    mLocationClient.disconnect();
    super.onStop();
}

@Override
public void onConnectionFailed(ConnectionResult result) {
	// TODO Auto-generated method stub
}
	*/
}