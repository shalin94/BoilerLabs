package com.cs307.boilerlab;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

public class CustomPopUp implements InfoWindowAdapter {

	LayoutInflater inflater = null;
	View view;
	
	CustomPopUp(LayoutInflater inflater) {
		this.inflater = inflater;
	}
	

	@Override
	public View getInfoContents(Marker marker) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getInfoWindow(Marker marker) {

		view = inflater.inflate(R.layout.infowindow, null);
		final String title = marker.getTitle();
        final TextView titleUi = ((TextView) view.findViewById(R.id.title));
        if (title != null) {
            titleUi.setText(title);
        } else {
            titleUi.setText("");
        }

        final String snippet = marker.getSnippet();
        final TextView snippetUi = ((TextView) view
                .findViewById(R.id.snippet));
        if (snippet != null) {
            snippetUi.setText(snippet);
        } else {
            snippetUi.setText("");
        }

        return view;
	}

}
