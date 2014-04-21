package com.cs307.boilerlab;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.widget.VideoView;


public class CSlab extends Activity {

	private VideoView v;
	 
    
	 
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	overridePendingTransition(R.anim.slidein, R.anim.slideout);
    	setContentView(R.layout.activity_cslab);
    	/*v = new VideoView(this);*/
    	String videourl = "rtsp://lwsnb158-cam.cs.purdue.edu/mpeg4/media.amp";
    	Intent i = getIntent();
		final String name = i.getStringExtra("name");
		if(name.equals("HAAS G40"))
			videourl = "rtsp://haasg040-cam.cs.purdue.edu/mpeg4/media.amp";
		else if(name.equals("HAAS G56"))
			videourl = "rtsp://haasg056-cam.cs.purdue.edu/mpeg4/media.amp";
		else if(name.equals("HAAS 257"))
			videourl = "rtsp://haas257-cam.cs.purdue.edu/mpeg4/media.amp";
		else if(name.equals("LWSN B131"))
			videourl = "rtsp://lwsnb131-cam.cs.purdue.edu/mpeg4/media.amp";
		else if(name.equals("LWSN B146"))
			videourl = "rtsp://lwsnb146-cam.cs.purdue.edu/mpeg4/media.amp";
		else if(name.equals("LWSN B148"))
			videourl = "rtsp://lwsnb148-cam.cs.purdue.edu/mpeg4/media.amp";
		else if(name.equals("LWSN B158"))
			videourl = "rtsp://lwsnb158-cam.cs.purdue.edu/mpeg4/media.amp";
		else if(name.equals("LWSN B160"))
			videourl = "rtsp://lwsnb160-cam.cs.purdue.edu/mpeg4/media.amp";
    	Uri uri = Uri.parse(videourl);
    	/*v.setVideoURI(uri);
    	v.start();*/
    	Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

}
