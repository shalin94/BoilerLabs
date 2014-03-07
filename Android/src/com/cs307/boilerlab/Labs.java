package com.cs307.boilerlab;

public class Labs 
{
	private int id;
	private String name;
	private String building_id;
	
	public Labs(int id, String name, String building_id){
		super();
		this.id = id;
		this.name = name;
		this.building_id= building_id;
	}
	
	public int getId(){
		return id;
	}
	public String getName(){
		return name;
	}
	public String getBuildingID(){
		return building_id;
	}
	
	public void setId(int id)
	{
		this.id=id;
	}
	
	public void setName(String name)
	{
		this.name=name;
	}
	
	public void setBuildingID(String building_id)
	{
		this.building_id=building_id;
	}
	
	
}
