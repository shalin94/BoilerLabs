/*
 * Parse infromation from the Scraper class
 * -@SRS
 */
package com.cs307.boilerlab;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class parseInfo
{
	ArrayList<String> info;
	String building,room;
	
	/* PUBLIC VARS */
	boolean isOpen;
	static public boolean error = false;
	int numComputersInUse;
	boolean hasComputers;
	int numComputers;
	boolean hasPCs;
	boolean hasMacs;
	boolean hasBlackAndWhitePrinters;
	int numBlackAndWhitePrinters;
	boolean hasColorPrinters;
	int numColorPrinters;
	boolean hasScanners;
	int numScanners;
	
	/* END PUBLIC VARS*/
	
	
	
	
	
	public void start(String building,String room)
	{
		info=Scraper.getData(building,room);
		parseInfos(info);
	}
	@SuppressWarnings("static-access")
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
					isOpen=true;
				}
				else
				{
					//THE LAB IS CLOSED
					isOpen=false;
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
					numComputersInUse=n;
					error = false;
				}
				else
				{
					//Error...
					error = true;
					numComputersInUse=0;
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
					hasComputers=true;
					numComputers=n;
					error = false;
				}
				else
				{
					//Error...
					error = true;
					hasComputers=false;
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
					hasPCs=true;
				}
				else
				{
					//Error...
					error = true;
					hasPCs=false;
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
					hasMacs=true;
					error = false;
				}
				else
				{
					//Error...
					error = true;
					hasMacs=false;
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
					hasBlackAndWhitePrinters=true;
					numBlackAndWhitePrinters=n;
					error = false;
				}
				else
				{
					//Error...
					error = true;
					hasBlackAndWhitePrinters=false;
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
					hasColorPrinters=true;
					numColorPrinters=n;
				}
				else
				{
					//Error...
					hasColorPrinters=false;
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
					hasScanners=true;
					numScanners=n;
				}
				else
				{
					//Error...
					hasScanners=false;
				}
				break;
		}
	}
	public void parseInfos(ArrayList<String> info)
	{
		if(info.isEmpty())
		{
			//Error...
			error = true;
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