package com.cs307.boilerlab;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;

public class LabView extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lab_view);
		Intent i = getIntent();
		String name = i.getStringExtra("name");
		TextView txtName = (TextView) findViewById(R.id.Name);
		txtName.setText(name);
		DatabaseHelper myDbHelper = null;
		TextView txtType = (TextView) findViewById(R.id.Type);
		TextView txtNo = (TextView) findViewById(R.id.NoOfComputers);
		TextView txtTime = (TextView) findViewById(R.id.MonToFiTime);
		TextView txtSat = (TextView) findViewById(R.id.SatTime);
		TextView txtSun = (TextView) findViewById(R.id.SunTime);
		
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
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lab_view, menu);
		return true;
	}

}
//SELECT * from Details where lab_id=(select _id from Labs where lab_name='GRIS 121')