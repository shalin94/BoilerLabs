/*
 * This class is used to display all the details about the lab
 * -@SRS
 */
package com.cs307.boilerlab;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Document;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.ProgressDialog;
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


public class LabView extends Activity implements LocationListener{
	LocationManager lm;
	LatLng finalLocation;
	double[] g;
	GoogleMap finalMap;
	
	//Asynctask used to load data in background from the ITaP server
	private class LoadData extends AsyncTask<String,Void,Void> {
		private Context c;
		public LoadData(Context c){
			this.c = c;
		}
		ProgressDialog progressDialog;
		String status;
		String type;
		String noComp;
		String noIU;
		String printer;
		String scanner;
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			progressDialog = ProgressDialog.show(((Activity)c),"Please Wait","Downloading Data");
		}
		@Override
		protected Void doInBackground(String... params) {
			String [] apt = params[0].split(" ");
			
			parseInfo pi = new parseInfo();
			pi.start(apt[0],apt[1]);
			if(pi.isOpen){	
				status= " Open";
			}
			else {
				if(pi.numComputersInUse < 2) {
					status = " Closed" ;
				}
				else {
					status = " Closed (Lab Occupied)";
				}
						
				Calendar c = Calendar.getInstance(); 
				int hour = c.get(Calendar.HOUR_OF_DAY);
				if((hour > 21 || hour < 7)) {
					status = " Closed";
				}
			}
			if(pi.hasPCs && pi.hasMacs) {
				type= " PC/Mac";
			}
			else if(pi.hasPCs) {
				type= " PC" ;
			}
			else if(pi.hasMacs) {
				type= " Mac";
			}
			noComp = "There are "+(pi.numComputers+" Computer(s)");
			if(pi.numComputersInUse > 1) {
				noIU = pi.numComputersInUse+" Computers are in use" ;
			}
			else {
				noIU = pi.numComputersInUse+" Computer is in use" ;
			}
			if(pi.hasBlackAndWhitePrinters && pi.hasColorPrinters) {
				if((pi.numBlackAndWhitePrinters + pi.numColorPrinters) >1 ) {
					printer="There are " +pi.numBlackAndWhitePrinters + pi.numColorPrinters + " Color and Black/White printers";
				}
				else {
					printer= "There is "+ pi.numBlackAndWhitePrinters + pi.numColorPrinters+" Color and Black/White printer";
				}
			}
			else if (pi.hasBlackAndWhitePrinters) {
				if(pi.numBlackAndWhitePrinters > 1) {
					printer = "There are "+ (pi.numBlackAndWhitePrinters + " Black/White printers");
				}
				else{
					printer= "There is "+ pi.numBlackAndWhitePrinters+" Black/White printer";
				}
			}
			else if (pi.hasColorPrinters){
				if(pi.numColorPrinters > 1) {
					printer = "There are "+ pi.numColorPrinters + " Color printers";
				}
				else {
					printer = "There is "+ pi.numColorPrinters + " Color printer";
				}
			}
			else{
					printer= "No printers!" ;
				}
			if(pi.hasScanners) {
				if(pi.numScanners > 1) {
					scanner = "There are " + pi.numScanners + " scanners";
				}
				else {
					scanner = "There is " + pi.numScanners+ " scanner";
				}
			}
			else {
					scanner = "No scanners!";
			}
			return null;
		}
		
		protected void onPostExecute(Void result){
			progressDialog.dismiss();
			runOnUiThread(new Runnable(){
				public void run(){
					TextView oType = (TextView) findViewById(R.id.oType);
					TextView oComp = (TextView) findViewById(R.id.oComp);
					TextView oIU = (TextView) findViewById(R.id.oIU);
					TextView oPrinter = (TextView) findViewById(R.id.oPrinter);
					TextView oScanner = (TextView) findViewById(R.id.oScanner);
					TextView oStatus = (TextView) findViewById(R.id.oStatus);
					if(status.equals(" Open")){
						oStatus.setTextColor((Color.parseColor("#4f8329")));
					}
					else {
						oStatus.setTextColor((Color.parseColor("#eb3d00")));
					}
					oStatus.setText(status);
					oType.setText(type);
					oComp.setText(noComp);
					oIU.setText(noIU);
					oPrinter.setText(printer);
					oScanner.setText(scanner);
				}
			});
		}
	}
	private double[] getGPS() {
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);  
	    List<String> providers = lm.getProviders(true);

	    /* Loop over the array backwards, and if you get an accurate location, then break out the loop*/
	    Location l = null;
	    if(g==null)
	    {
	    	
	    	g=new double[2];
	    	for (int i=providers.size()-1; i>=0; i--) {
	            l = lm.getLastKnownLocation(providers.get(i));
	            if (l != null) break;
	    	}
	    	if (l != null) {
	            g[0] = l.getLatitude();
	            g[1] = l.getLongitude();
	    	}
	    }

	    else
	    {
	    	g=new double[2];
	    	g[0]=MainActivity.locationGPS[0];
    		g[1]=MainActivity.locationGPS[1];
	    }
	    return g;
}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.slidein, R.anim.slideout);
		LocationManager locationManager = (LocationManager)
				getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(
				LocationManager.GPS_PROVIDER, 50, 5, this);
		//If the application is in offline mode then load data from the database
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
		
			if(prefs.contains(name))
			{
				fav.setText("Favorite -");
			}
			fav.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(prefs.contains(name))
				{
					prefs.edit().remove(name).commit();
					MainActivity.ref=true;
					fav.setText("Favorite +");
				}
				else
				{
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
						txtType.setText("Type :"+ type);
						txtNo.setText(number + " Computers Available");
						if(open.equals("closed") || close.equals("closed")) {
							txtTime.setText("Lab Closed");
						}
						else {
							txtTime.setText(open + " - " + close);
						}
						if(opensat.equals("closed") || closesat.equals("closed")) {
							txtSat.setText("Lab Closed");
						}
						else {
							txtSat.setText(opensat + " - " + closesat);
						}
						if(opensun.equals("closed") || closesun.equals("closed")) {
							txtSun.setText("Lab Closed");
						}
						else {
							txtSun.setText(opensun + " - " + closesun);
						}
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
		
			String [] names = name.split(" ");
			String name3=names[0];
			LatLng finloc=null;
			try{
				myDbHelper = new DatabaseHelper(LabView.this);
				List<NBuildings> nbldg = myDbHelper.getBuilding();
				Iterator<NBuildings> it = nbldg.iterator();
				while(it.hasNext()){
					NBuildings temp = it.next();
					String name2 = temp.getName();
					name2=name2.trim();
					if(!name2.equals(name3))
					{
						continue;
					}
					String loc = temp.getBuildingLoc();
					String [] locs = loc.split(",");
					finloc=new LatLng(Double.parseDouble(locs[0]), Double.parseDouble(locs[1]));
					map.addMarker(new MarkerOptions()
					.position(finloc)
					.title(name));
					map.moveCamera(CameraUpdateFactory.newLatLngZoom(finloc, 17));
					break;
				}
			}catch(Exception e){
				Log.e(this.getClass().getName(), "Failed to run query", e);
			} finally {
				myDbHelper.close();
			}
		}
		//If it is online mode -@SRS
		else {
			setContentView(R.layout.activity_ll_online);
			if (android.os.Build.VERSION.SDK_INT > 9) {
	            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	            StrictMode.setThreadPolicy(policy);
	        }
			Intent i = getIntent();
			final String name = i.getStringExtra("name");
			final TextView txtName = (TextView) findViewById(R.id.oName);
			txtName.setText(name);
			LoadData ld = (LoadData) new LoadData(this);
			ld.execute(name);
			final Button fav = (Button) findViewById (R.id.addtofav2);
			final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			final Editor editor = prefs.edit();
			if(prefs.contains(name))
			{
				fav.setText("Favorite -");
			}
			fav.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) 
				{
					if(prefs.contains(name))
					{
						prefs.edit().remove(name).commit();
						MainActivity.ref=true;
						fav.setText("Favorite +");
					}
					
					else{
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
			finalMap=map;
			DatabaseHelper myDbHelper = null;
			double[] g=getGPS();
			LatLng sydney = new LatLng(g[0], g[1]);
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 40));
			String [] names = name.split(" ");
			String name3=names[0];
			LatLng finloc=null;
	        try{
				myDbHelper = new DatabaseHelper(LabView.this);
				List<NBuildings> nbldg = myDbHelper.getBuilding();
				Iterator<NBuildings> it = nbldg.iterator();
				while(it.hasNext()){
					NBuildings temp = it.next();
					String name2 = temp.getName();
					name2=name2.trim();
					if(!name2.equals(name3))
					{
						continue;
					}
					String loc = temp.getBuildingLoc();
					String [] locs = loc.split(",");
					finloc=new LatLng(Double.parseDouble(locs[0]), Double.parseDouble(locs[1]));
					finalLocation=finloc;
					
					map.addMarker(new MarkerOptions()
			        .position(finloc)
			        .title(name));
					
					map.moveCamera(CameraUpdateFactory.newLatLngZoom(finloc, 15));
					
					MapDirection md = new MapDirection();

					Document doc;
					
					if(prefs.contains("Walk"))
					{
						doc= md.getDocument(sydney, finloc, MapDirection.MODE_WALKING);
					}
					else
					{	
						doc= md.getDocument(sydney, finloc, MapDirection.MODE_DRIVING);
					}
			        
			        ArrayList<LatLng> directionPoint = md.getDirection(doc);

			        PolylineOptions rectLine = new PolylineOptions().width(5).color(Color.argb(255, 51, 181, 229)).geodesic(true);
			        

			        for(int i1 = 0 ; i1 < directionPoint.size() ; i1++) {          
			        rectLine.add(directionPoint.get(i1));
			        }
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
		//super.onLocationChanged(location);
		if(MainActivity.online==false)
			return;
		g=new double[2];
		if (location != null) {
            g[0] = location.getLatitude();
            g[1] = location.getLongitude();
    }
		else
		{
			return;
		}
		LatLng sydney = new LatLng(g[0], g[1]);
		MapDirection md = new MapDirection();

		Document doc;
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		if(prefs.contains("Walk"))
		{
			doc= md.getDocument(sydney, finalLocation, MapDirection.MODE_WALKING);
		}
		else
		{	
			doc= md.getDocument(sydney, finalLocation, MapDirection.MODE_DRIVING);
		}
        
        ArrayList<LatLng> directionPoint = md.getDirection(doc);

        PolylineOptions rectLine = new PolylineOptions().width(5).color(Color.argb(255, 51, 181, 229)).geodesic(true);
        

        for(int i1 = 0 ; i1 < directionPoint.size() ; i1++) {          
        rectLine.add(directionPoint.get(i1));
        }
        finalMap.addPolyline(rectLine);
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

}

//SELECT * from Details where lab_id=(select _id from Labs where lab_name='GRIS 121')