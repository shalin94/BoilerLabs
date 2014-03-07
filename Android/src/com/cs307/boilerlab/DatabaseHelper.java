package com.cs307.boilerlab;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.util.*;
public class DatabaseHelper extends SQLiteOpenHelper{
	private static String DB_PATH = null;
	private static String DB_NAME = "offline.db";
	private SQLiteDatabase myDatabase;
	private final Context myContext;
	public DatabaseHelper(Context context) throws IOException {
		super(context,DB_NAME, null, 1);
		this.myContext = context;
		DB_PATH = "/data/data/" + context.getPackageName()+"/databases/" + DB_NAME;
		
	}
	private void initializeDatabase(){
		myDatabase = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READONLY);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// Not relevant
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Not relevant
		
	}
	public Cursor rawQuery(String sql, String[] selectionArgs){
		return myDatabase.rawQuery(sql, selectionArgs);
	}
	public List<Buildings> getBuilding(){
		List<Buildings> temp = new ArrayList<Buildings>();
		Cursor c = rawQuery("select * from Buildings", null);
		while(c.moveToNext()){
			Integer id = c.getInt(0);
			String name = c.getString(1);
			String location = c.getString(2);
			Buildings bld = new Buildings(id,name,location);
			temp.add(bld);
		}
		return temp;
	}
	
	public List<Labs> getLab(){
		List<Labs> temp = new ArrayList<Labs>();
		Cursor c = rawQuery("select * from Labs", null);
		while(c.moveToNext()){
			Integer id = c.getInt(0);
			String lab_name = c.getString(1);
			String building_id = c.getString(2);
			Labs lab = new Labs(id,lab_name,building_id);
			temp.add(lab);
		}
		return temp;
	}
	
	public List<Details> getDetail(){
		List<Details> temp = new ArrayList<Details>();
		Cursor c = rawQuery("select * from Details", null);
		while(c.moveToNext()){
			Integer id = c.getInt(0);
			String lab_name = c.getString(1);
			String building_id = c.getString(2);
			Labs lab = new Labs(id,lab_name,building_id);
			//temp.add(lab);
		}
		return temp;
	}
	
}
