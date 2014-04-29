/*
 * This class is used to get the Data from the SQLite Database
 * It populates the relevant data classes and has a get function for these objects
 * -@SRS
 */
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
	private static SQLiteDatabase myDatabase;
	private static List<NBuildings> nbldg = null;
	private static List<Labs> labs = null;
	private static List<Details> dets = null;
	private final Context myContext;
	protected DatabaseHelper(Context context) throws IOException {
		super(context,ORIG_DB_NAME, null, 1);
		this.myContext = context;
		//Database Path -@SRS
		DB_PATH = "/data/data/com.cs307.boilerlab/databases/";
		initializeDatabase();
	}
	private void initializeDatabase(){
		try{
			copyDataBase();
		}catch (Exception e){
			Log.e("message: ",e.getMessage());
		}
		myDatabase = SQLiteDatabase.openDatabase(DB_PATH+ORIG_DB_NAME, null, SQLiteDatabase.OPEN_READONLY);
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
//Returns a List of NBuildings objects which contains Building Name, Full Name, Location coordinates and address -@SRS
	public List<NBuildings> getBuilding(){
		nbldg = new ArrayList<NBuildings>();
		Cursor c = rawQuery("select * from NBuildings", null);
		while(c.moveToNext()){
			Integer id = c.getInt(0);
			String name = c.getString(1);
			String fullname = c.getString(2);
			String location = c.getString(3);
			String address = c.getString(4);
			NBuildings bld = new NBuildings(id,name,fullname,location,address);
			nbldg.add(bld);
		}
		return nbldg;
	}
	//Returns a list of Labs objects which contains the lab name and which building it is located in -@SRS 
	public List<Labs> getLab(){
		labs = new ArrayList<Labs>();
		Cursor c2 = rawQuery("select * from Labs", null);
		while(c2.moveToNext()){
			Integer id = c2.getInt(0);
			String lab_name = c2.getString(1);
			Integer building_id = c2.getInt(2);
			Labs lab = new Labs(id,lab_name,building_id);
			labs.add(lab);
		}
		return labs;
	}
	//Returns a list of Details objects which is used in offline mode
	//It contains Lab timings and details like how many computers it has -@SRS
	public List<Details> getDetail(){
		dets = new ArrayList<Details>();
		Cursor c3 = rawQuery("select * from Details", null);
		while(c3.moveToNext()){
			String closesun = c3.getString(0);
			String opensun = c3.getString(1);
			String closesat = c3.getString(2);
			String opensat = c3.getString(3);
			Integer id = c3.getInt(4);
			Integer lab_id= c3.getInt(5);
			Integer building_id = c3.getInt(6);
			String lab_type = c3.getString(7);
			String lab_comp = c3.getString(8);
			String lab_open = c3.getString(9);
			String lab_close = c3.getString(10);
			
			Details det = new Details(id,lab_id,building_id, lab_type, lab_comp, lab_open, lab_close, opensat, closesat, opensun, closesun);
			dets.add(det);
		}
		return dets;
	}
	//Copies Database to phone memory -@SRS
	private void copyDataBase() throws IOException{
		InputStream myInput = myContext.getAssets().open(ORIG_DB_NAME);
		String outFileName = DB_PATH;
		File f = new File(DB_PATH);
		f.mkdirs();
		f.createNewFile();
		outFileName=DB_PATH+ORIG_DB_NAME;
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
