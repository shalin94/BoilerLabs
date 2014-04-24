package com.cs307.boilerlab;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import android.app.Activity;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

public class CustomPopUp extends Activity implements InfoWindowAdapter  {

	LayoutInflater inflater = null;
	View view;
    Map map = new Map();
    DatabaseHelper myDbHelper = null;
    String fullname;
    
	CustomPopUp(LayoutInflater inflater) {
		this.inflater = inflater;
	}
	

	@Override
	public View getInfoWindow(Marker marker) {
		return null;
	}

	@Override
	public View getInfoContents(final Marker marker) {

		view = inflater.inflate(R.layout.infowindow, null);

        final ImageView image = ((ImageView) view.findViewById(R.id.icon));
        final TextView titleUi = ((TextView) view.findViewById(R.id.title));
        
        	String name = marker.getTitle();
        	name = name.trim();
        	if(name.equals("GRIS")) {
        		image.setImageResource(R.drawable.gris);
        		titleUi.setText("Grissom Hall (GRIS)");
        	}
        	else if(name.equals("BCC")) {
            	image.setImageResource(R.drawable.bcc);
            	titleUi.setText("Black Cultural Center (BCC)");
        	}
        	else if(name.equals("BRNG")) {
            	image.setImageResource(R.drawable.brng);
            	titleUi.setText("Beering Hall (BRNG)");
        	}
        	else if(name.equals("HEAV")) {
            	image.setImageResource(R.drawable.heav);
            	titleUi.setText("Heavilon Hall (HEAV)");
        	}
        	else if(name.equals("LCC")) {
            	image.setImageResource(R.drawable.lcc);
            	titleUi.setText("Latino Cultural Center (LCC)");
        	}
        	else if(name.equals("LILY")) {
            	image.setImageResource(R.drawable.lily);
            	titleUi.setText("Lilly Hall of Life Sciences (LILY)");
        	}
        	else if(name.equals("LWSN")) {
            	image.setImageResource(R.drawable.lwsn);
            	titleUi.setText("Lawson Computer Science Building (LWSN)");
        	}
        	else if(name.equals("LYNN")) {
            	image.setImageResource(R.drawable.lynn);
            	titleUi.setText("Lynn Hall of Veterinary Medicine (LYNN)");
        	}
        	else if(name.equals("MATH")) {
            	image.setImageResource(R.drawable.math);
            	titleUi.setText("Maths Building (MATH)");
        	}
        	else if(name.equals("MCUT")) {
            	image.setImageResource(R.drawable.mcut);
            	titleUi.setText("McCutcheon Residence Hall (MCUT)");
        	}
        	else if(name.equals("MRDH")) {
            	image.setImageResource(R.drawable.mrdh);
            	titleUi.setText("Meredith Residence Hall (MRDH)");
        	}
        	else if(name.equals("MTHW")) {
            	image.setImageResource(R.drawable.mthw);
            	titleUi.setText("Matthews Hall (MTHW)");
        	}
        	else if(name.equals("NLSN")) {
            	image.setImageResource(R.drawable.nlsn);
            	titleUi.setText("Nelson Hall of Food Science (NLSN)");
        	}
        	else if(name.equals("PHYS")) {
            	image.setImageResource(R.drawable.phys);
            	titleUi.setText("Physics Building (PHYS)");
        	}
        	else if(name.equals("POTR")) {
            	image.setImageResource(R.drawable.potr);
            	titleUi.setText("Potter Engineering Center (POTR)");
        	}
        	else if(name.equals("RHPH")) {
            	image.setImageResource(R.drawable.rhph);
            	titleUi.setText("Heine Pharmacy Building (RHPH)");
        	}
        	else if(name.equals("SC")) {
            	image.setImageResource(R.drawable.sc);
            	titleUi.setText("Stanley Coulter Hall (SC)");
        	}
        	else if(name.equals("SCCB")) {
            	image.setImageResource(R.drawable.sccb);
            	titleUi.setText("South Campus Courts (SCCB)");
        	}
        	else if(name.equals("STEW")) {
            	image.setImageResource(R.drawable.stew);
            	titleUi.setText("Stewart Center (STEW)");
        	}
        	else if(name.equals("WTHR")) {
            	image.setImageResource(R.drawable.wthr);
            	titleUi.setText("Wetherill Laboratory (WTHR)");
        	}
        	else if(name.equals("HAMP")) {
            	image.setImageResource(R.drawable.hamp);
            	titleUi.setText("Hampton Hall of Civil Engineering (HAMP)");
        	}
        	else if(name.equals("HIKS")) {
            	image.setImageResource(R.drawable.hicks);
            	titleUi.setText("Hicks Undergraduate Library (HIKS)");
        	}
        	else if(name.equals("BRES")) {
            	image.setImageResource(R.drawable.bres);
            	titleUi.setText("Brees Academic Center (BRES)");
        	}
        	else
        		titleUi.setText("");
		//final String title = marker.getTitle();
		
		//String fullname = map.getFullName();
        /*(final TextView titleUi = ((TextView) view.findViewById(R.id.title));
        if (fullname != null) {
        	titleUi.setText(fullname);
        } else {
            titleUi.setText("h");
        }*/

        final String snippet = marker.getSnippet();
        final TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
        if (snippet != null) {
            snippetUi.setText(snippet);
        } else {
            snippetUi.setText("");
        }
        

        return view;
	}

}
