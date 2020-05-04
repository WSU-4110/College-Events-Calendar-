package com.example.campusconnect.Event;

public class Event {
	
	// Replace "String date" with "Date date"
	private String name;
	private String location;
	private String startTime;
	private String date;
	private String org;
	private String desc;
	private String uid;
	private String OrgUid;
	private String tag;
	
	
	Event() {
		name = "EventNameNotProvided";
		location = "NoEventLocationProvided";
		startTime = "11:59";
		date = "01012019";
		org = "EventOrgNotEntered";
		desc = "N/A";
	}
	
	Event(String dateSelected){
		this.date = dateSelected;
	}

	public Event(String uid, String name, String location,
				 String startTime, String date, String org, String desc, String OrgUid, String tags) {
		this.uid = uid;
		this.name = name;
		this.location = location;
		this.startTime = startTime;
		this.date = date;
		this.org = org;
		this.desc = desc;
		this.OrgUid = OrgUid;
		this.tag =  tags;
	}
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public void setOrg(String org) { this.org = org; }
	
	// Package-private methods
	String location() 		{ return location; }
	String startTime() 		{ return startTime; }
	String fullDateString() { return date; }
	String org() 			{ return org; }
	String desc() 			{ return desc; }
	String UID() 			{ return uid; }
	String orgUID() 		{ return OrgUid; }
	String tag() 			{ return tag; }
	void setUid(String uid) { this.uid = uid; }
	
}// class [ Event ]
