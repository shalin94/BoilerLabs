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
			String ns[]=n.split(" ");
			n=ns[0];
			ns=n2.split(" ");
			n2=ns[0];
			n=n.trim();
			n2=n2.trim();
			if(!n2.equals(n))
			{
				//mIdMap.put(objects.get(i), i);
				objects.add(i, n);
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
            view.setBackgroundColor(Color.argb(200, 51, 181, 229));
            view.setTextColor(Color.WHITE);
        }
        return view;
    }
	@Override
	public boolean isEnabled (int position)
	{
		if(position==0)
			return false;
		String n=stuff.get(position);
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