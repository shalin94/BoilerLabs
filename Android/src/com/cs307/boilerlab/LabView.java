package com.cs307.boilerlab;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Document;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.SystemClock;
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
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;


public class LabView extends Activity implements LocationListener{
	LocationManager lm;
	LatLng finalLocation;
	double[] g;
	GoogleMap finalMap;
	Polyline rectLine;
	int currentPt=0;
	private List<Marker> markers = new ArrayList<Marker>();
	ArrayList<LatLng> targetLatLng;
	ArrayList<LatLng> allTargets;
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
			progressDialog = ProgressDialog.show(((Activity)c),"Wait","Downloading Data");
		}
		@Override
		protected Void doInBackground(String... params) {
			String [] apt = params[0].split(" ");
			/*final StringBuilder status = new StringBuilder();
			final TextView oStatus = (TextView) findViewById(R.id.oStatus);*/
			//type = new StringBuilder();
			//noComp = new StringBuilder();
			//noIU = new StringBuilder();
			//printer = new StringBuilder();
			//scanner = new StringBuilder();
			/*TextView oType = (TextView) findViewById(R.id.oType);
			TextView oComp = (TextView) findViewById(R.id.oComp);
			TextView oIU = (TextView) findViewById(R.id.oIU);
			TextView oPrinter = (TextView) findViewById(R.id.oPrinter);
			TextView oScanner = (TextView) findViewById(R.id.oScanner);
			final TextView text=(TextView) findViewById(R.id.oStatus);*/
			/*Thread th = new Thread(){
				public void run(){
			parseInfo pi = new parseInfo();
			pi.start(apt[0],apt[1]);
			if(pi.isOpen){
				text.setTextColor((Color.parseColor("#4f8329")));
				status.append("Open");
			}
			else {
				
				text.setTextColor((Color.parseColor("#eb3d00")));
				status.append("Closed");
			}
			//progressDialog.setProgress(15);
			if(pi.hasPCs && pi.hasMacs) {
				type.append("PC/Mac");
			}
			else if(pi.hasPCs) {
				type.append("PC");
			}
			else if(pi.hasMacs) {
				type.append("Mac");
			}
			//progressDialog.setProgress(30);
			noComp.append("There are ");
			noComp.append(pi.numComputers);
			noComp.append(" Computer(s)");
			//progressDialog.setProgress(45);
			noIU.append(pi.numComputersInUse);
			noIU.append(" Computer(s) are in use");
			//progressDialog.setProgress(60);
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
			//progressDialog.setProgress(75);
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
			}catch(Exception e) {
				
			}*/
			//progressDialog.setProgress(100);
			
					parseInfo pi = new parseInfo();
					pi.start(apt[0],apt[1]);
					if(pi.isOpen){
						//text.setTextColor((Color.parseColor("#4f8329")));
						status= "Open";
					}
					else {
						
						//text.setTextColor((Color.parseColor("#eb3d00")));
						status = "Closed" ;
					}
					if(pi.hasPCs && pi.hasMacs) {
						type= "PC/Mac";
					}
					else if(pi.hasPCs) {
						type= "PC" ;
					}
					else if(pi.hasMacs) {
						type= "Mac";
					}
					noComp = "There are "+(pi.numComputers+" Computer(s)");
					noIU = pi.numComputersInUse+" Computer(s) are in use" ;
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
			//status.append("1");
			//type.append("2");
			//noComp.append("3");
			//noIU.append("4");
			//printer.append("5");
			//scanner.append("6");
				Log.d("EHH","Apt: "+apt[0]+"Status: "+pi.isOpen+"Type: "+type+" oComp"+noComp);
			return null;
		}
		
		protected void onPostExecute(Void result){
			progressDialog.dismiss();
			Activity act = (Activity) c;
			runOnUiThread(new Runnable(){
				public void run(){
					TextView oType = (TextView) findViewById(R.id.oType);
					TextView oComp = (TextView) findViewById(R.id.oComp);
					TextView oIU = (TextView) findViewById(R.id.oIU);
					TextView oPrinter = (TextView) findViewById(R.id.oPrinter);
					TextView oScanner = (TextView) findViewById(R.id.oScanner);
					TextView oStatus = (TextView) findViewById(R.id.oStatus);
					if(status.equals("Open")){
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
			/*TextView oType = (TextView) LabView.this.findViewById(R.id.oType);
			TextView oComp = (TextView) LabView.this.findViewById(R.id.oComp);
			TextView oIU = (TextView) LabView.this.findViewById(R.id.oIU);
			TextView oPrinter = (TextView) LabView.this.findViewById(R.id.oPrinter);
			TextView oScanner = (TextView) LabView.this.findViewById(R.id.oScanner);
			TextView oStatus = (TextView) LabView.this.findViewById(R.id.oStatus);
			if(status.equals("Open")){
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
			oScanner.setText(scanner);*/
			
		}
	}
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
	    	
	    	//g=new double[2];
	    	//g[0]=MainActivity.locationGPS[0];
	    	//g[1]=MainActivity.locationGPS[1];
	    	
	    	
	    	
	    }
	    //while(g==null);
	    //double[] gps = new double[2];
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
		//double[] g=getGPS();
		//g[0]=(double)Math.round(g[0] * 1000000) / 1000000;
		//g[1]=(double)Math.round(g[1] * 1000000) / 1000000;
		//LatLng sydney = new LatLng(g[0], g[1]);
		
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
				//Log.d("Sydney directions","1: "+sydney.latitude+" 2: "+sydney.longitude);
				finloc=new LatLng(Double.parseDouble(locs[0]), Double.parseDouble(locs[1]));
				map.addMarker(new MarkerOptions()
		        .position(finloc)
		        .title(name));
				
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(finloc, 16));
				/*MapDirection md = new MapDirection();

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
		        map.addPolyline(rectLine);*/
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
			txtName.setText(name);
			/*final String [] apt = name.split(" ");
			final StringBuilder status = new StringBuilder();
			final TextView oStatus = (TextView) findViewById(R.id.oStatus);
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
						status.append("Open");
					}
					else {
						
						text.setTextColor((Color.parseColor("#eb3d00")));
						status.append("Closed");
					}
					if(pi.hasPCs && pi.hasMacs) {
						type.append("PC/Mac");
					}
					else if(pi.hasPCs) {
						type.append("PC");
					}
					else if(pi.hasMacs) {
						type.append("Mac");
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
			oScanner.setText(scanner);*/
			LoadData ld = (LoadData) new LoadData(this);
			ld.execute(name);
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
			finalMap=map;
			DatabaseHelper myDbHelper = null;
			double[] g=getGPS();
			/*
			Location l=map.getMyLocation();
			g[0]=l.getLatitude();
			g[1]=l.getLongitude();
			*/
			//g[0]=(double)Math.round(g[0] * 1000000) / 1000000;
			//g[1]=(double)Math.round(g[1] * 1000000) / 1000000;
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
					finalLocation=finloc;
					
					Marker m=map.addMarker(new MarkerOptions()
			        .position(finloc)
			        .title(name));
					markers.clear();
					markers.add(m);
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
			        //map.addPolyline(rectLine);
					break;
				}
			}catch(Exception e){
				Log.e(this.getClass().getName(), "Failed to run query", e);
			} finally {
				myDbHelper.close();
			}
	        /*
			int k=0;
			while(!MainActivity.buildname.get(k).equals(name3))
			{
				k++;
			}*/
			//String loc = temp.getBuildingLoc();
			//String [] locs = loc.split(",");
			//Log.d("directions","1: "+locs[0]+" 2: "+locs[1]);
			//Log.d("Sydney directions","1: "+sydney.latitude+" 2: "+sydney.longitude);
			//finloc=new LatLng(MainActivity.buildLat.get(k), MainActivity.buildLong.get(k));
			//finalLocation=finloc;
			/*
			Marker m=map.addMarker(new MarkerOptions()
	        .position(finloc)
	        .title(name));
			*/
			
		}
 
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lab_view, menu);
		return true;
	}
	CancelableCallback simpleAnimationCancelableCallback;
	Location ll;
	long start;
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		//super.onLocationChanged(location);
		ll=location;
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
			Log.d("Prefs: ","Walk");
			doc= md.getDocument(sydney, finalLocation, MapDirection.MODE_WALKING);
		}
		else
		{	
			Log.d("Prefs: ","Drive");
			doc= md.getDocument(sydney, finalLocation, MapDirection.MODE_DRIVING);
		}
        
        ArrayList<LatLng> directionPoint = md.getDirection(doc);//Color.argb(255, 51, 181, 229)
        
        
        
        PolylineOptions rectopt = new PolylineOptions().width(6).geodesic(true);
        
        rectLine=finalMap.addPolyline(rectopt);
        start = SystemClock.uptimeMillis();
        //long end=start;
        allTargets=new ArrayList<LatLng>();
        for(int i1 = 0 ; i1 < directionPoint.size() ; i1++) {          
        	//rectLine.add(directionPoint.get(i1));
        	//while(SystemClock.uptimeMillis()-start<=20);
        	//updatePolyLine(directionPoint.get(i1));
        	allTargets.add(directionPoint.get(i1));
        	Log.d("THIS"," "+directionPoint.get(i1));
        }
        //allTargets.add(directionPoint.get(0));
        allTargets.add(finalLocation);
        targetLatLng=directionPoint;
        CameraPosition cameraPosition =
        		new CameraPosition.Builder()
        				.target(targetLatLng.get(0))
        				.bearing(45)
        				.tilt(90)
        				.zoom(finalMap.getCameraPosition().zoom)
        				.build();
       
        /*simpleAnimationCancelableCallback =
        		new CancelableCallback(){

        			@Override
        			public void onCancel() {
        			}

        			@Override
        			public void onFinish() {
        				
        				if(++currentPt < 3000)){
        					
        					//Double heading = SphericalUtil.computeHeading(beginLatLng, endLatLng);
        					long elapsed = SystemClock.uptimeMillis() - start;
        		        	LinearInterpolator interpolator=new LinearInterpolator();
        		        	double t = interpolator.getInterpolation((float)elapsed/3000);
        		        	double lat = t * targetLatLng.get(currentPt).latitude + (1-t) * targetLatLng.get(currentPt-1).latitude;
        		        	double lng = t * targetLatLng.get(currentPt).longitude + (1-t) * targetLatLng.get(currentPt-1).longitude;
        		        				
        		        	LatLng intermediatePosition = new LatLng(lat, lng);
        					float heading=ll.getBearing();
        					CameraPosition cameraPosition =
        							new CameraPosition.Builder()
        									.target(intermediatePosition)
        									.tilt(currentPt<targetLatLng.size()-1 ? 90 : 0)
        				                    .bearing((float)heading)
        				                    .zoom(finalMap.getCameraPosition().zoom)
        				                    .build();

        					finalMap.animateCamera(
        							CameraUpdateFactory.newCameraPosition(cameraPosition), 
        							300,
        							simpleAnimationCancelableCallback);
        					
        					//rectLine.add(targetLatLng.get(currentPt));
        					updatePolyLine(intermediatePosition);
        					//highLightMarker(currentPt);

        				}
        			}
        	};
        	finalMap.animateCamera(
        			CameraUpdateFactory.newCameraPosition(cameraPosition), 
        			500,
        			simpleAnimationCancelableCallback
        	);
        */
        
       // while(end)
        /*
        finalMap.animateCamera(
        		CameraUpdateFactory.zoomTo(finalMap.getCameraPosition().zoom + 0.5f), 
        		1500,
        		FinalCancelableCallback);						

        		currentPt = 0-1;
       */
        ArrayList<LatLng> lp=new ArrayList<LatLng>();
        rectLine.setPoints(lp);
        float bearingL = bearingBetweenLatLngs(allTargets.get(0), finalLocation);

    	CameraPosition camPos =
				new CameraPosition.Builder()
						.target(allTargets.get(0)) // changed this...
	                    .bearing(bearingL )
	                    .tilt(45)
	                    .zoom(finalMap.getCameraPosition().zoom)
	                    .build();

		
		finalMap.animateCamera(
				CameraUpdateFactory.newCameraPosition(camPos), 
				1500,
				FinalCancelableCallback
		);
	    

        //Double heading = SphericalUtil.computeHeading(beginLatLng, endLatLng);
		Log.d("BOOM","HErE");
	}
	
	private void updatePolyLine(LatLng latLng) {
		List<LatLng> points = rectLine.getPoints();
		points.add(latLng);
		rectLine.setPoints(points);
		//finalMap.
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
	CancelableCallback MyCancelableCallback = new CancelableCallback(){

		@Override
		public void onCancel() {
			Log.d("THIS","********** oncancel");
		}
		LatLng targetLatLng;
		@Override
		public void onFinish() {
			
			if(++currentPt < markers.size()){
				float targetBearing = bearingBetweenLatLngs( finalMap.getCameraPosition().target, markers.get(currentPt).getPosition());
				targetLatLng = markers.get(currentPt).getPosition();
				
					System.out.println(" ------- " + currentPt + " - " + markers.size() + " - " + targetBearing + " - " + targetLatLng);
				
				CameraPosition cameraPosition =
						new CameraPosition.Builder()
								.target(targetLatLng)
								.tilt(currentPt<markers.size()-1 ? 90 : 0)
			                    .bearing(targetBearing)
			                    .zoom(finalMap.getCameraPosition().zoom)
			                    .build();

				finalMap.animateCamera(
						CameraUpdateFactory.newCameraPosition(cameraPosition), 
						3000,
						currentPt==markers.size()-1 ? FinalCancelableCallback : MyCancelableCallback);
				Log.d("THIS","Size: "+allTargets.size());
//				googleMap.moveCamera(
//						CameraUpdateFactory.newCameraPosition(cameraPosition)); 
				
				markers.get(currentPt).showInfoWindow();
				
				
			}
			
		}

};

CancelableCallback FinalCancelableCallback = new CancelableCallback() {

@Override
public void onFinish() {
	//GoogleMapUtis.fixZoomForMarkers(finalMap,markers);
	final Handler handler = new Handler();
	final LinearInterpolator interpolator = new LinearInterpolator();
	//final 
	 handler.post(new Runnable() {
		 long start = SystemClock.uptimeMillis();
		 int count=1;
            @Override
            public void run() {
            	
            	if(count>=allTargets.size())
            	{
            		Log.d("THIS","ENDING with count: "+count);
            		
            		return;
            	}
    			
            	
            	long elapsed = SystemClock.uptimeMillis() - start;
    			double t = interpolator.getInterpolation((float)elapsed/(1500));
            	double lat = t * allTargets.get(count).latitude + (1-t) * allTargets.get(count-1).latitude;
    			double lng = t * allTargets.get(count).longitude + (1-t) * allTargets.get(count-1).longitude;
    			LatLng newPosition = new LatLng(lat, lng);
    			Log.d("THIS","HERE with t: "+t+" and count: "+count);
    			Log.d("THIS","HERE with Lat: "+lat+" and long: "+lng);
    			
    			/*
    			float bearingL = bearingBetweenLatLngs(allTargets.get(count-1), newPosition);

            	CameraPosition cameraPosition =
						new CameraPosition.Builder()
								.target(allTargets.get(count-1)) // changed this...
			                    .bearing(bearingL  + 20)
			                    .tilt(0)
			                    .zoom(finalMap.getCameraPosition().zoom)
			                    .build();

				
				finalMap.animateCamera(
						CameraUpdateFactory.newCameraPosition(cameraPosition), 
						1500,
						null
				);*/
				
				updatePolyLine(newPosition);
				//start = SystemClock.uptimeMillis();
				//handler.postDelayed(this, 16);
				//start = SystemClock.uptimeMillis();
                if (t < 1.0) {
                	Log.d("THIS","HERE too");
                    // Post again 16ms later.
                	//start = SystemClock.uptimeMillis();
                    handler.postDelayed(this, 16);
                    count++;
                }
                /*
                else
                {
                	start = SystemClock.uptimeMillis();
                	count++;
                	handler.postDelayed(this, 16);
                }
                */
            }
        });
}

@Override
public void onCancel() {
	
}
};

private Location convertLatLngToLocation(LatLng latLng) {
Location loc = new Location("someLoc");
loc.setLatitude(latLng.latitude);
loc.setLongitude(latLng.longitude);
return loc;
}

private float bearingBetweenLatLngs(LatLng begin,LatLng end) {
Location beginL= convertLatLngToLocation(begin);
Location endL= convertLatLngToLocation(end);
return beginL.bearingTo(endL);
}	


}

//SELECT * from Details where lab_id=(select _id from Labs where lab_name='GRIS 121')