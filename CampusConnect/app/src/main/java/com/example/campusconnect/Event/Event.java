package com.example.campusconnect.Event;

import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

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
	
	//    public Event(String name, String location, String startTime, String date, String org, String desc) {
//        this.name = name;
//        this.location = location;
//        this.startTime = startTime;
//        this.date = date;
//        this.org = org;
//        this.desc = desc;
//    }
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
	public String getLocation() { return location; }
	public String getStartTime() { return startTime; }
	public String getDate() { return date; }
	public String getOrg() { return org; }
	public String getDesc() { return desc; }
	public String getUid() { return uid; }
	public String getOrgUid() { return OrgUid; }
	public void setName(String name) { this.name = name; }
	public void setLocation(String location) { this.location = location; }
	public void setStartTime(String startTime) { this.startTime = startTime; }
	public void setDate(String date) { this.date = date; }
	public void setOrg(String org) { this.org = org; }
	public void setDesc(String desc) { this.desc = desc; }
	public void setUid(String uid) { this.uid = uid; }
	public void setOrgUid(String OrgUid) { this.OrgUid = OrgUid; }
	public String getTag() { return tag;  }
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public String toString()
	{
		return name + "|" + location + "|" + startTime + "|" + date + "|" + org + "|" + desc + "|" + OrgUid + "|" + tag;
	}
	
	// Added for adding event indicators to calendar -Jay
	public Long getMillisecondsForEvent(){
		// MM/DD/YYYY
		String stringDay;
		String stringMonth;
		String stringYear;
		String[] dateFragments;
		Calendar cal;
		int day;
		int month;
		int year;
		
		dateFragments = this.date.split("/");
		System.out.println("DATE: " + this.date);
		
		stringMonth = dateFragments[0];
		month = Integer.parseInt(stringMonth);
		
		stringDay = dateFragments[0];
		day = Integer.parseInt(stringDay);
		
		stringYear = dateFragments[0];
		year = Integer.parseInt(stringYear);
		
		System.out.println("\nDATE FRAGMENTS \n");
		System.out.printf("\nint: %d %d %d \n", month, day, year);
		System.out.printf("\nString: %s %s, %s \n", stringMonth, stringDay, stringYear);
		
		cal = Calendar.getInstance();
		// year, month, day
		cal.set(year, month, day);
		
		return cal.getTimeInMillis();
	}
	
}// end [ CLASS: Event ]
