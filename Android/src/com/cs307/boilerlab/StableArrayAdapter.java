package com.cs307.boilerlab;

import java.util.HashMap;
import java.util.List;

import com.hb.views.PinnedSectionListView.PinnedSectionListAdapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class StableArrayAdapter extends ArrayAdapter<String> implements PinnedSectionListAdapter{
	HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();
	List<String> stuff;
	
	public String getFullNameFrom(String n)
	{
		//String n=name;
		if(n.equals("LWSN")){
			n = "Lawson Computer Science Building";
		}
		else if(n.equals("GRIS"))
		{
			n = "Grissom Hall";
		}
		else if(n.equals("BCC"))
		{
			n = "Black Cultural Center";
		}
		else if(n.equals("BRES"))
		{
			n = "Brees Academic Center";
		}
		else if(n.equals("BRNG"))
		{
			n = "Beering Hall";
		}
		else if(n.equals("HAMP"))
		{
			n = "Hampton Hall of Civil Engineering";
		}
		else if(n.equals("HEAV"))
		{
			n = "Heavilon Hall";
		}
		else if(n.equals("HIKS"))
		{
			n = "Hicks Undergraduate Library";
		}
		else if(n.equals("LCC"))
		{
			n = "Latino Cultural Center";
		}
		else if(n.equals("LILY"))
		{
			n = "Lilly Hall of Life Sciences";
		}
		else if(n.equals("LYNN"))
		{
			n = "Lynn Hall of Veterinary Medicine";
		}
		else if(n.equals("MATH"))
		{
			n = "Maths Building";
		}
		else if(n.equals("MCUT"))
		{
			n = "McCutcheon Residence Hall";
		}
		else if(n.equals("MRDH"))
		{
			n = "Meredith Residence Hall";
		}
		else if(n.equals("MTHW"))
		{
			n = "Matthews Hall";
		}
		else if(n.equals("NLSN"))
		{
			n = "Nelson Hall of Food Science";
		}
		else if(n.equals("PHYS"))
		{
			n = "Physics Building";
		}
		else if(n.equals("POTR"))
		{
			n = "Potter Engineering Center";
		}
		else if(n.equals("RHPH"))
		{
			n = "Heine Pharmacy Building";
		}
		else if(n.equals("SC"))
		{
			n = "Stanley Coulter Hall";
		}
		else if(n.equals("SCCB"))
		{
			n = "South Campus Courts";
		}
		else if(n.equals("STEW"))
		{
			n = "Stewart Center";
		}
		else if(n.equals("WTHR"))
		{
			n = "Wetherill Laboratory";
		}
		return n;
	}
	/*
	public String getFullNameFrom(String n)
	{
		//String n=name;
		if(n.equals("LWSN")){
			n = "Lawson";
		}
		else if(n.equals("GRIS"))
		{
			n = "Grissom";
		}
		else if(n.equals("BCC"))
		{
			n = "Black";
		}
		else if(n.equals("BRES"))
		{
			n = "Brees";
		}
		else if(n.equals("BRNG"))
		{
			n = "Beering";
		}
		else if(n.equals("HAMP"))
		{
			n = "Hampton";
		}
		else if(n.equals("HEAV"))
		{
			n = "Heavilon";
		}
		else if(n.equals("HIKS"))
		{
			n = "Hicks";
		}
		else if(n.equals("LCC"))
		{
			n = "Latino";
		}
		else if(n.equals("LILY"))
		{
			n = "Lilly";
		}
		else if(n.equals("LYNN"))
		{
			n = "Lynn";
		}
		else if(n.equals("MATH"))
		{
			n = "Maths";
		}
		else if(n.equals("MCUT"))
		{
			n = "McCutcheon";
		}
		else if(n.equals("MRDH"))
		{
			n = "Meredith";
		}
		else if(n.equals("MTHW"))
		{
			n = "Matthews";
		}
		else if(n.equals("NLSN"))
		{
			n = "Nelson";
		}
		else if(n.equals("PHYS"))
		{
			n = "Physics";
		}
		else if(n.equals("POTR"))
		{
			n = "Potter";
		}
		else if(n.equals("RHPH"))
		{
			n = "Heine";
		}
		else if(n.equals("SC"))
		{
			n = "Stanley";
		}
		else if(n.equals("SCCB"))
		{
			n = "South";
		}
		else if(n.equals("STEW"))
		{
			n = "Stewart";
		}
		else if(n.equals("WTHR"))
		{
			n = "Wetherill";
		}
		return n;
	}*/
	public StableArrayAdapter(Context context, int textViewResourceId, List<String> objects) {
		super(context, textViewResourceId, objects);
		stuff=objects;
		for (int i = 0; i< objects.size(); ++i) {
			String n=stuff.get(i);
			
			String n2;
			if(i==0)
			{
				n2="..!";
			}
			else
			{
				n2=stuff.get(i-1);
			}
			/*
			String ns[]=n.split(" ");
			n=ns[0];
			
			ns=n2.split(" ");
			n2=ns[0];
			n=n.trim();
			
			n2=n2.trim();
			*/
			String n3=n;
			char g=n.charAt(n.length()-2);
			char k=n.charAt(n.length()-1);
			if((g>='0'&&g<='9')||(k>='0'&&k<='9'))
			{
				String ns[]=n.split(" ");
				n=ns[0];
				n3=getFullNameFrom(n);
			}
			
			//String n3=getFullNameFrom(n);
			String n4=n2;
			g=n2.charAt(n2.length()-2);
			k=n2.charAt(n2.length()-1);
			if((g>='0'&&g<='9')||(k>='0'&&k<='9'))
			{
				String ns[]=n2.split(" ");
				n2=ns[0];
				n4=getFullNameFrom(n2);
			}
			
			Log.d("ERROR","1: "+n+" to "+n3);
			Log.d("ERROR","2: "+n2+" to "+n4);
			
			if((!n4.equals(n3)))
			{
				//mIdMap.put(objects.get(i), i);
				String n100=getFullNameFrom(n);
				Log.d("ERROR","3: "+n100);
				objects.add(i, n100);
			}
			
			mIdMap.put(objects.get(i), i);
		}
	}
	
	@Override
	public long getItemId(int position) {
		String item = getItem(position);
		return mIdMap.get(item);
	}
	@Override
	public boolean areAllItemsEnabled ()
	{
		return false;
	}
	@Override public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setTag("" + position);

        if (!isEnabled(position)) {
            view.setBackgroundColor(Color.argb(200, 154, 132, 73));
            view.setTextColor(Color.BLACK);
        }
        return view;
    }
	@Override
	public boolean isEnabled (int position)
	{
		if(position==0)
			return false;
		String n=stuff.get(position);
		/*
		String n2=stuff.get(position-1);
		String ns[]=n.split(" ");
		n=ns[0];
		ns=n2.split(" ");
		n2=ns[0];
		n=n.trim();
		n2=n2.trim();
		if(n2.equals(n))
		{
			return true;
		}
		*/
		char g=n.charAt(n.length()-2);
		char k=n.charAt(n.length()-1);
		if((g>='0'&&g<='9')||(k>='0'&&k<='9'))
			return true;
		return false;
	}
	
	public boolean hasStableIds() {
		return true;
	}
	@Override
	public int getViewTypeCount()
	{
		return 2;	
	}
	@Override
	public int getItemViewType (int position)
	{
		if(position==0)
			return 1;
		String n=stuff.get(position);
		/*
		String n2=stuff.get(position-1);
		String ns[]=n.split(" ");
		n=ns[0];
		ns=n2.split(" ");
		n2=ns[0];
		n=n.trim();
		n2=n2.trim();
		if(n2.equals(n))
		{
			return 0;
		}
		*/
		char g=n.charAt(n.length()-2);
		char k=n.charAt(n.length()-1);
		if((g>='0'&&g<='9')||(k>='0'&&k<='9'))
			return 0;
		return 1;
	}
	@Override
	public boolean isItemViewTypePinned(int viewType) {
		// TODO Auto-generated method stub
		if(viewType==0)
			return false;
		else
			return true;
	}
}