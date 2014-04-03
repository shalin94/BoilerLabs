package com.cs307.boilerlab;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.w3c.dom.Document;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.location.Location;
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
import android.widget.EditText;
import android.widget.TextView;


public class LabView extends Activity {
	
	private double[] getGPS() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);  
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
			fav.setText("Added To Favorites!");
		}
		fav.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d("Added: ",name);
				editor.putBoolean(name, true);
				editor.commit();
				fav.setText("Added To Favorites!");
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
			List<Buildings> bldg = myDbHelper.getBuilding();
			Iterator<Buildings> it = bldg.iterator();
			while(it.hasNext()){
				Buildings temp = it.next();
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

		        Document doc = md.getDocument(sydney, finloc, MapDirection.MODE_DRIVING);
		        
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lab_view, menu);
		return true;
	}

}
//SELECT * from Details where lab_id=(select _id from Labs where lab_name='GRIS 121')