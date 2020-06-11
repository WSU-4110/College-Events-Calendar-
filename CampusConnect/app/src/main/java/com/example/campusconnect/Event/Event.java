package com.example.campusconnect.Event;

import java.util.Calendar;

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
	
	Event(String dateSelected) {
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
		this.tag = tags;
	}
	
	//@formatter:OFF
	public String getName() { return name; }
	public String getDate() { return date; }
	public String getOrg() { return org; }
	public void setName(String name) { this.name = name; }
	public void setOrg(String org) { this.org = org; }
	
	// Package-private methods
	String location() { return location; }
	String startTime() { return startTime; }
	String desc() { return desc; }
	String UID() { return uid; }
	String orgUid() { return OrgUid; }
	void setUid(String uid) { this.uid = uid; }
	//@formatter:ON
	
	String tag() {
		if (this.tag == null)
			return "None";
		else
			return tag;
	}
	
	public boolean eventPassed() {
		Calendar today;
		Calendar eventDate;
		long milliseconds;
		
		today = Calendar.getInstance();
		eventDate = Calendar.getInstance();
		milliseconds = this.getMillisecondsForEvent();
		
		eventDate.setTimeInMillis(milliseconds);
		
		return eventDate.before(today);                    // built-in method of Calendar
	}
	
	public boolean eventIsToday() {
		Calendar today;
		Calendar event;
		long milliseconds;
		int todayDay;
		int eventDay;
		
		today = Calendar.getInstance();
		event = Calendar.getInstance();
		milliseconds = this.getMillisecondsForEvent();
		
		event.setTimeInMillis(milliseconds);
		todayDay = today.get(Calendar.DAY_OF_MONTH);
		eventDay = event.get(Calendar.DAY_OF_MONTH);
		
		return todayDay == eventDay;        // TODO: Look in to changing name of "todayDay"
	}
	
	public Long getMillisecondsForEvent() {
		
		// Firebase Date Format: M(M) / D(D) / YYYY
		String stringMonth;
		String stringDay;
		String stringYear;
		String[] dateFragments;
		Calendar cal;
		int day;
		int month;
		int year;
		
		dateFragments = this.date.split("/");
		
		stringMonth = dateFragments[0];
		stringDay = dateFragments[1];
		stringYear = dateFragments[2];
		
		month = Integer.parseInt(stringMonth) - 1;	// (Firebase/Event: Jan == 1)
		day = Integer.parseInt(stringDay);
		year = Integer.parseInt(stringYear);
		
		cal = Calendar.getInstance();
		cal.set(year, month, day);					// (Calendar.java: Jan == 0)
		
		return cal.getTimeInMillis();
	}
	
}// class [ Event ]
