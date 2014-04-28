package com.cs307.boilerlab;

import java.util.ArrayList;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ListFragment extends Fragment {
	EditText search; 
	StableArrayAdapter adapter;
	ArrayList<String> list2;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    View v = inflater.inflate(R.layout.listview, container, false);
	    final ListView listview = (ListView) v.findViewById(R.id.listview);
	    listview.setBackgroundColor(Color.LTGRAY);
	    
	    TextView baseView=(TextView) v.findViewById(R.id.something);
		baseView.setBackgroundColor(Color.argb(255, 26, 26, 38));
		baseView.setText("List Of Labs");
		baseView.setTextSize(27);
		baseView.setTextColor(Color.WHITE);
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "BebasNeue.otf");
		baseView.setTypeface(font);
		View lv=v.findViewById(R.id.llayout);
		//lv.setBackgroundColor(Color.argb(255, 26, 26, 38));
		//lv=v.findViewById(R.id.inputSearch);
		//lv.setBackgroundColor(Color.argb(255, 26, 26, 38));
		//((EditText) lv).setTextColor(Color.WHITE);
		//((EditText) lv).setHintTextColor(Color.WHITE);
		
	    //final ListView listview=(ListView) inflater.inflate(R.id.listview,container,false);
	    final ArrayList<String> list = new ArrayList<String>();
		DatabaseHelper myDbHelper = null;
		
		//Intent in = getActivity().getIntent();
		//String onlyClosest = in.getStringExtra("closest");
		//String closestName = in.getStringExtra("closestLab");
		
		search = (EditText) getActivity().findViewById(R.id.inputSearch);
		try{
			myDbHelper = new DatabaseHelper(getActivity());
			Cursor cursor = myDbHelper.rawQuery("select * from Labs",null);
			if(cursor.moveToFirst()) {
				while (cursor.isAfterLast() == false){
					String name = cursor.getString(1);

					/*
					if(onlyClosest.equals("true"))
					{
						String ns[]=closestName.split(" ");
						String test=ns[0].trim();
						String lab[]=name.split(" ");
						if(test.equals(lab[0]))
						{
							list.add(name);
						}
					}
					else*/
						list.add(name);
						
	                cursor.moveToNext();
				}
			}
		}catch(Exception e){
			Log.e(this.getClass().getName(), "Failed to run query", e);
		} finally {
			myDbHelper.close();
		}
		if(/*!onlyClosest.equals("true") &&*/ MainActivity.online == true)
		{
			list.add("HAAS G40");
			list.add("HAAS G56");
			list.add("HAAS 257");
			list.add("LWSN B131");
			list.add("LWSN B146");
			list.add("LWSN B148");
			list.add("LWSN B158");
			list.add("LWSN B160");
		}
		
		list2=list;
		adapter = new StableArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, list);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		      @Override
		      public void onItemClick(AdapterView<?> parent, final View view,
		          int position, long id) {
		    	  String name = ((TextView) view).getText().toString();
		    	  if(!(name.contains("HAAS") || name.contains("LWSN"))){
		    	  Intent labView = new Intent(getActivity(),LabView.class);
		    	  labView.putExtra("name",name);
		    	  Log.d("NAME","NAME: "+name);
					getActivity().startActivity(labView);
					getActivity().overridePendingTransition(R.anim.slidein, R.anim.slideout);
		    	  }
		    	  else {
		    		  Intent CSlab = new Intent(getActivity(),CSlab.class);
		    		  CSlab.putExtra("name",name);
		    		  Log.d("NAME","NAME: "+name);
		    		  getActivity().startActivity(CSlab);
		    		  getActivity().overridePendingTransition(R.anim.slidein, R.anim.slideout);
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
                    adapter = new StableArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, filteredTitles);
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
                    adapter = new StableArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, ft);
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
		listview.requestFocus();
		return v;
	}
	

}
