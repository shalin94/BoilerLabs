/*
 * Representation of Details table of the offline database
 * Has the helper functions
 * -@SRS
 */
package com.cs307.boilerlab;

public class Details {
	private int id;
	private int lab_id;
	private int building_id;
	private String lab_type;
	private String lab_comp;
	private String lab_open;
	private String lab_close;
	private String opensat;
	private String closesat;
	private String opensun;
	private String closesun;
	
	public Details(int id, int lab_id, int building_id, String lab_type, String lab_comp, String lab_open, String lab_close, String opensat, String closesat, String opensun, String closesun ){
		super();
		this.id = id;
		this.lab_id = lab_id;
		this.building_id= building_id;
		this.lab_type= lab_type;
		this.lab_comp= lab_comp;
		this.lab_open= lab_open;
		this.lab_close= lab_close;
		this.opensat= opensat;
		this.closesat= closesat;
		this.opensun= opensun;
		this.closesun= closesun;
		
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLab_id() {
		return lab_id;
	}
	public void setLab_id(int lab_id) {
		this.lab_id = lab_id;
	}
	public int getBuilding_id() {
		return building_id;
	}
	public void setBuilding_id(int building_id) {
		this.building_id = building_id;
	}
	public String getLab_type() {
		return lab_type;
	}
	public void setLab_type(String lab_type) {
		this.lab_type = lab_type;
	}
	public String getLab_comp() {
		return lab_comp;
	}
	public void setLab_comp(String lab_comp) {
		this.lab_comp = lab_comp;
	}
	public String getLab_open() {
		return lab_open;
	}
	public void setLab_open(String lab_open) {
		this.lab_open = lab_open;
	}
	public String getLab_close() {
		return lab_close;
	}
	public void setLab_close(String lab_close) {
		this.lab_close = lab_close;
	}
	public String getOpensat() {
		return opensat;
	}
	public void setOpensat(String opensat) {
		this.opensat = opensat;
	}
	public String getClosesat() {
		return closesat;
	}
	public void setClosesat(String closesat) {
		this.closesat = closesat;
	}
	public String getOpensun() {
		return opensun;
	}
	public void setOpensun(String opensun) {
		this.opensun = opensun;
	}
	public String getClosesun() {
		return closesun;
	}
	public void setClosesun(String closesun) {
		this.closesun = closesun;
	}
}
