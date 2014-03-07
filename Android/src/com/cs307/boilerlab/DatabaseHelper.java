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
	private static String ORIG_DB_NAME = "offline.db";
	private static String DB_NAME;
	private SQLiteDatabase myDatabase;
	private final Context myContext;
	public DatabaseHelper(Context context) throws IOException {
		super(context,DB_NAME, null, 1);
		this.myContext = context;
		DB_PATH = "/data/data/" + context.getPackageName()+"/databases/" + ORIG_DB_NAME;
		initializeDatabase();
	}
	private void initializeDatabase(){
		try{
			copyDataBase();
		}catch (Exception e){
			Log.e("message: ",e.getMessage());
		}
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
			Integer building_id = c.getInt(2);
			Labs lab = new Labs(id,lab_name,building_id);
			temp.add(lab);
		}
		return temp;
	}
	
	public List<Details> getDetail(){
		List<Details> temp = new ArrayList<Details>();
		Cursor c = rawQuery("select * from Details", null);
		while(c.moveToNext()){
			String closesun = c.getString(0);
			String opensun = c.getString(1);
			String closesat = c.getString(2);
			String opensat = c.getString(3);
			Integer id = c.getInt(4);
			Integer lab_id= c.getInt(5);
			Integer building_id = c.getInt(6);
			String lab_type = c.getString(7);
			String lab_comp = c.getString(8);
			String lab_open = c.getString(9);
			String lab_close = c.getString(10);
			
			Details det = new Details(id,lab_id,building_id, lab_type, lab_comp, lab_open, lab_close, opensat, closesat, opensun, closesun);
			temp.add(det);
		}
		return temp;
	}
	private void copyDataBase() throws IOException{
		InputStream myInput = myContext.getAssets().open(ORIG_DB_NAME);

		String outFileName = DB_PATH;

		OutputStream myOutput = new FileOutputStream(outFileName);

		byte[] buffer = new byte[1024];
		int length;
		while((length = myInput.read(buffer))>0){
			myOutput.write(buffer, 0, length);
		}

		myOutput.flush();
		myOutput.close();
		myInput.close();
	}
	public synchronized void close() {
		if(myDatabase != null)
			myDatabase.close();

		super.close();
	}
}
