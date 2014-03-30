package com.cs307.boilerlab;

import java.util.Hashtable;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Bitmap;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class CustomPopUp implements InfoWindowAdapter {

	LayoutInflater inflater = null;
	View view;
	private Marker marker;
    //private Hashtable<String, String> markers;
    //private ImageLoader imageLoader;
    //private DisplayImageOptions options;
    Map map = new Map();
    
	CustomPopUp(LayoutInflater inflater) {
		this.inflater = inflater;
	}
	

	@Override
	public View getInfoWindow(Marker marker) {
		/*view = inflater.inflate(R.layout.infowindow, null);
		final String title = marker.getTitle();
        final TextView titleUi = ((TextView) view.findViewById(R.id.title));
        if (title != null) {
            titleUi.setText(title);
        } else {
            titleUi.setText("");
        }

        final String snippet = marker.getSnippet();
        final TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
        if (snippet != null) {
            snippetUi.setText(snippet);
        } else {
            snippetUi.setText("");
        }
        
        //ImageView ivIcon = ((ImageView)view.findViewById(R.id.icon));
        //ivIcon.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_gallery));

        return view;*/
		return null;
	}

	@Override
	public View getInfoContents(final Marker marker) {

		view = inflater.inflate(R.layout.infowindow, null);
		//markers = new Hashtable<String, String>();
        //imageLoader = ImageLoader.getInstance();
        
        String url = null;

        if (marker.getId() != null && map.markers != null && map.markers.size() > 0) {
            if ( map.markers.get(marker.getId()) != null &&
                    map.markers.get(marker.getId()) != null) {
                url = map.markers.get(marker.getId());
            }
        }
        final ImageView image = ((ImageView) view.findViewById(R.id.icon));

        if (url != null && !url.equalsIgnoreCase("null")
                && !url.equalsIgnoreCase("")) {
            map.imageLoader.displayImage(url, image, map.options,
                    new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri,
                                View view, Bitmap loadedImage) {
                            super.onLoadingComplete(imageUri, view,
                                    loadedImage);
                            getInfoContents(marker);
                        }
                    });
        } else {
            image.setImageResource(R.drawable.ic_launcher);
        }
		
		final String title = marker.getTitle();
        final TextView titleUi = ((TextView) view.findViewById(R.id.title));
        if (title != null) {
            titleUi.setText(title);
        } else {
            titleUi.setText("");
        }

        final String snippet = marker.getSnippet();
        final TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
        if (snippet != null) {
            snippetUi.setText(snippet);
        } else {
            snippetUi.setText("");
        }
        
        //ImageView ivIcon = ((ImageView)view.findViewById(R.id.icon));
        //ivIcon.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_gallery));

        return view;
		//return null;
	}

}
