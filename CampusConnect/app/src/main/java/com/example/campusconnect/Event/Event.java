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
		
		return eventDate.before(today);					// built-in method of Calendar
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
		
		return todayDay == eventDay;		// TODO: Look in to changing name "todayDay"
	}
	
	public Long getMillisecondsForEvent() {
		// M(M) / D(D) / YYYY
		// Jan == 0, Dec == 11
		
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
		month = Integer.parseInt(stringMonth) - 1;
		
		stringDay = dateFragments[1];
		day = Integer.parseInt(stringDay);
		
		stringYear = dateFragments[2];
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
