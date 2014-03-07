package com.cs307.boilerlab;

public class Labs 
{
	private int id;
	private String name;
	private int building_id;
	
	public Labs(int id, String name, int building_id){
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
	public int getBuildingID(){
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
	
	public void setBuildingID(int building_id)
	{
		this.building_id=building_id;
	}
	
	
}
