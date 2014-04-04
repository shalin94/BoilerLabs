package com.cs307.boilerlab;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class parseInfo
{
	ArrayList<String> info;
	String building,room;
	
	public void getInfo()
	{
		info=Scraper.getData(building,room);
	}
	public int whereAmI(String str)
	{
		Pattern pattern=Pattern.compile("Windows");
		Matcher matcher=pattern.matcher(str);
		if(matcher.find())
			return 3;
			
		pattern=pattern.compile("Mac");
		matcher=pattern.matcher(str);
		if(matcher.find())
			return 4;
		
		pattern=pattern.compile("black");
		matcher=pattern.matcher(str);
		if(matcher.find())
			return 5;
			
		pattern=pattern.compile("color");
		matcher=pattern.matcher(str);
		if(matcher.find())
			return 6;
		
		return 7;
	}
	public void search(int i, String str)
	{
		Pattern pattern;
		Matcher matcher;
		String count;
		int n;
		int wai;
		if(i<3)
			wai=i;
		else
			wai=whereAmI(str);
		switch(wai)
		{
			case 0:
				/* Check if the lab is open */
				
				pattern=Pattern.compile("The lab is OPEN.");
				matcher=pattern.matcher(str);
				if(matcher.find())
				{
					//THE LAB IS OPEN
				}
				else
				{
					//THE LAB IS CLOSED
				}
				break;
				
			case 1:
				/* Find the number of computers in use */
				
				pattern=Pattern.compile("[0-9]+");
				matcher=pattern.matcher(str);
				
				if(matcher.find())
				{
					count=matcher.group();
					n=Integer.parseInt(count);
					
					//SAVE NUMBER OF COMPUTERS IN USE
				}
				else
				{
					//Error...
				}
				
				break;
				
			case 2:
			/* Find the number of computers */
				pattern=Pattern.compile("[0-9]+");
				matcher=pattern.matcher(str);
				
				if(matcher.find())
				{
					count=matcher.group();
					n=Integer.parseInt(count);
					
					//SAVE NUMBER OF COMPUTERS
				}
				else
				{
					//Error...
				}
				break;
				
			case 3:
				/* Check for PCs */
				pattern=Pattern.compile("^[0-9]+");
				matcher=pattern.matcher(str);
				if(matcher.find())
				{
					count=matcher.group();
					n=Integer.parseInt(count);
					
					//SAVE NUMBER OF PCS
				}
				else
				{
					//Error...
				}
				break;
				
			case 4:
				/* Check for Macs */
				pattern=Pattern.compile("^[0-9]+");
				matcher=pattern.matcher(str);
				if(matcher.find())
				{
					count=matcher.group();
					n=Integer.parseInt(count);
					
					//SAVE NUMBER OF MACS
				}
				else
				{
					//Error...
				}
				break;
				
			case 5:
				pattern=Pattern.compile("^[0-9]+");
				matcher=pattern.matcher(str);
				if(matcher.find())
				{
					count=matcher.group();
					n=Integer.parseInt(count);
					
					//SAVE NUMBER OF BLACK AND WHITE PRINTERS
				}
				else
				{
					//Error...
				}
				break;
			case 6:
				pattern=Pattern.compile("^[0-9]+");
				matcher=pattern.matcher(str);
				if(matcher.find())
				{
					count=matcher.group();
					n=Integer.parseInt(count);
					
					//SAVE NUMBER OF COLOR PRINTERS
				}
				else
				{
					//Error...
				}
				break;
			default:
				pattern=Pattern.compile("^[0-9]+");
				matcher=pattern.matcher(str);
				if(matcher.find())
				{
					count=matcher.group();
					n=Integer.parseInt(count);
					
					//SAVE NUMBER OF SCANNERS
				}
				else
				{
					//Error...
				}
				break;
		}
	}
	public void parseInfo(ArrayList<String> info)
	{
		if(info.isEmpty())
		{
			//Error...
			System.out.println("Error");
			return;
		}
		int i=0;
		for(String str : info)
		{
			search(i,str);
			i++;
		}
	}
}