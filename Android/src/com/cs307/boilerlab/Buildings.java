package com.cs307.boilerlab;

public class Buildings 
{
	private int id;
	private String name;
	private String building_loc;
	public Buildings(int id,String name,String building_loc){
		super();
		this.id = id;
		this.building_loc = building_loc;
		this.name= name;
	}
	public int getId(){
		return id;
	}
	public String getName(){
		return name;
	}
	public String getBuildingLoc(){
		return building_loc;
	}
	
	public void setId(int id)
	{
		this.id=id;
	}
	
	public void setName(String name)
	{
		this.name=name;
	}
	
	public void setBuildingLoc(String building_loc)
	{
		this.building_loc=building_loc;
	}	
	
}