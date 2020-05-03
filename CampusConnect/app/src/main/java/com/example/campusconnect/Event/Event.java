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
	
//	// Added for adding indicators to calendar -Jay
//	public int getMonth(){
//		StringBuilder month;
//		String monthInteger;
//
//		month.append(date.charAt())
//
//		return month;
//	}
	
}// end [ CLASS: Event ]
