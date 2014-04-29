/*
 * Class representation of NBuildings table in the offline database
 * Has the helper functions
 * -@SRS
 */
package com.cs307.boilerlab;

public class NBuildings {

	private int id;
	private String name;
	private String building_loc;
	private String fullname;
	private String building_address;
	
	public NBuildings(int id,String name,String fullname,String building_loc,String building_address){
		super();
		this.id = id;
		this.building_loc = building_loc;
		this.name= name;
		this.fullname = fullname;
		this.building_address = building_address;
	}
	public int getId(){
		return id;
	}
	public String getName(){
		return name;
	}
	public String getFullName(){
		return fullname;
	}
	
	public String getBuildingLoc(){
		return building_loc;
	}
	public String getBuildingAddress(){
		return building_address;
	}
	
	public void setId(int id)
	{
		this.id=id;
	}
	
	public void setName(String name)
	{
		this.name=name;
	}
	
	public void setFullName(String fullname)
	{
		this.fullname=fullname;
	}
	
	public void setBuildingLoc(String building_loc)
	{
		this.building_loc=building_loc;
	}
	public void setBuildingAddress(String building_address)
	{
		this.building_address=building_address;
	}

}
