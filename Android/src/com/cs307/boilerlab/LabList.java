package com.cs307.boilerlab;

import java.util.ArrayList;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
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

public class LabList extends Activity {
	EditText search; 
	StableArrayAdapter adapter;
	ArrayList<String> list2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);
		final ListView listview = (ListView) findViewById(R.id.listview);
		final ArrayList<String> list = new ArrayList<String>();
		DatabaseHelper myDbHelper = null;
		
		search = (EditText) findViewById(R.id.inputSearch);
		try{
			myDbHelper = new DatabaseHelper(LabList.this);
			Cursor cursor = myDbHelper.rawQuery("select * from Labs",null);
			if(cursor.moveToFirst()) {
				while (cursor.isAfterLast() == false){
					String name = cursor.getString(1);
						list.add(name);
						
	                cursor.moveToNext();
				}
			}
		}catch(Exception e){
			Log.e(this.getClass().getName(), "Failed to run query", e);
		} finally {
			myDbHelper.close();
		}
		list.add("HAAS G40");
		list.add("HAAS G56");
		list.add("HAAS 257");
		list.add("LWSN B131");
		list.add("LWSN B146");
		list.add("LWSN B148");
		list.add("LWSN B158");
		list.add("LWSN B160");
		list2=list;
		adapter = new StableArrayAdapter(this,android.R.layout.simple_list_item_1, list);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		      @Override
		      public void onItemClick(AdapterView<?> parent, final View view,
		          int position, long id) {
		    	  String name = ((TextView) view).getText().toString();
		    	  if(!(name.contains("HAAS") || name.contains("LWSN"))){
		    	  Intent labView = new Intent(LabList.this,LabView.class);
		    	  labView.putExtra("name",name);
		    	  Log.d("NAME","NAME: "+name);
					LabList.this.startActivity(labView);
		    	  }
		    	  else {
		    		  Intent CSlab = new Intent(LabList.this,CSlab.class);
		    		  CSlab.putExtra("name",name);
		    		  Log.d("NAME","NAME: "+name);
		    		  LabList.this.startActivity(CSlab);
		    	  }
		      }

		    });
search.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                //LabList.this.adapter.getFilter().filter(cs);  
            	
            	if (!cs.toString().equals("")) {
                    ArrayList<String> filteredTitles = new ArrayList<String>();
                    for (int i=0; i<list2.size(); i++) {
                    	String n=list2.get(i).toString();
                    	char c=n.charAt(n.length()-2);
                    	if(!(c>='0'&&c<='9'))
                    		continue;
                    	
                         if (list2.get(i).toString().contains(cs.toString().toUpperCase())) {
                             filteredTitles.add(list2.get(i));                   
                         }            
                    }
                    for(int i=0;i<list2.size();i++)
                    {
                    	Log.d("List: ",list2.get(i).toString());
                    }
                    adapter = new StableArrayAdapter(LabList.this, android.R.layout.simple_list_item_1, filteredTitles);
                    listview.setAdapter(adapter);
               }
               else {
            	   ArrayList<String> ft = new ArrayList<String>();
            	   for(int i=0;i<list2.size();i++)
                   {
            		   String n=list2.get(i).toString();
                   		char c=n.charAt(n.length()-2);
            		   if(!(c>='0'&&c<='9'))
                   		continue;
            		   ft.add(list2.get(i));
                   }
                    adapter = new StableArrayAdapter(LabList.this, android.R.layout.simple_list_item_1, ft);
                    listview.setAdapter(adapter);             
               }
            }
             
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                    int arg3) {
                // TODO Auto-generated method stub
                 
            }
             
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub     
            	adapter.notifyDataSetChanged();
            	
            }
        });
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lab_list, menu);
		return true;
	}

}
