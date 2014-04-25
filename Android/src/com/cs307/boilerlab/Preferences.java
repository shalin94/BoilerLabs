package com.cs307.boilerlab;

import java.util.ArrayList;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class Preferences extends Activity {

	EditText search; 
	StableArrayAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.slidein, R.anim.slideout);
		setContentView(R.layout.activity_preferences);
		final ListView listview = (ListView) findViewById(R.id.listviewFav);
		
		if(MainActivity.ref==true)
			{
			Intent refresh=new Intent(Preferences.this, Preferences.class);
			startActivity(refresh);
			this.finish();
			MainActivity.ref=false;
			}
		
		
		
		final ArrayList<String> list = new ArrayList<String>();
		DatabaseHelper myDbHelper = null;
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		search = (EditText) findViewById(R.id.inputSearch);
		try{
			myDbHelper = new DatabaseHelper(Preferences.this);
			Cursor cursor = myDbHelper.rawQuery("select * from Labs",null);
			if(cursor.moveToFirst()) {
				while (cursor.isAfterLast() == false){
					String name = cursor.getString(1);
					boolean a=prefs.contains(name);
					Log.d("Got This: ",name+", "+a);
					if(prefs.contains(name))
					{
						Log.d("IN IF","!");
						list.add(name);
					}
					Log.d("Name", name);
	                cursor.moveToNext();
				}
			}
		}catch(Exception e){
			Log.e(this.getClass().getName(), "Failed to run query", e);
		} finally {
			myDbHelper.close();
		}
		adapter = new StableArrayAdapter(this,android.R.layout.simple_list_item_1, list);
		listview.setAdapter(adapter);
		MainActivity.ref=false;
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		      @Override
		      public void onItemClick(AdapterView<?> parent, final View view,
		          int position, long id) {
		    	  String name = ((TextView) view).getText().toString();
		    	  Intent labView = new Intent(Preferences.this,LabView.class);
		    	  labView.putExtra("name",name);
		    	  Log.d("NAME","NAME: "+name);
					Preferences.this.startActivity(labView);
		      }
		      

		    });
		search.addTextChangedListener(new TextWatcher() {
            
            @Override
            
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                Preferences.this.adapter.getFilter().filter(cs);  
            }
             
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                    int arg3) {
                // TODO Auto-generated method stub
                 
            }
             
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub                         
            }
        });
		listview.requestFocus();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lab_list, menu);
		return true;
	}
	public void onResume()
	{  // After a pause OR at startup
	    super.onResume();
	    //Refresh your stuff here
	    //Preferences.this.adapter info = new GroupDb(this);
	    //info.open();
	    //mcba.updateResults(info.getView());
	    //info.close();
	    //this.adapter.notifyDataSetChanged();
	    final ListView listview = (ListView) findViewById(R.id.listviewFav);
	    final ArrayList<String> list = new ArrayList<String>();
		DatabaseHelper myDbHelper = null;
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		search = (EditText) findViewById(R.id.inputSearch);
		try{
			myDbHelper = new DatabaseHelper(Preferences.this);
			Cursor cursor = myDbHelper.rawQuery("select * from Labs",null);
			if(cursor.moveToFirst()) {
				while (cursor.isAfterLast() == false){
					String name = cursor.getString(1);
					boolean a=prefs.contains(name);
					Log.d("Got This: ",name+", "+a);
					if(prefs.contains(name))
					{
						Log.d("IN IF","!");
						list.add(name);
					}
					Log.d("Name", name);
	                cursor.moveToNext();
				}
			}
		}catch(Exception e){
			Log.e(this.getClass().getName(), "Failed to run query", e);
		} finally {
			myDbHelper.close();
		}
		adapter = new StableArrayAdapter(this,android.R.layout.simple_list_item_1, list);
		listview.setAdapter(adapter);
	}
	

}

