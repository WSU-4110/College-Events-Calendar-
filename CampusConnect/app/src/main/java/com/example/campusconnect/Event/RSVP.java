package com.example.campusconnect.Event;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

class RSVP {
	private String eventID;
	private ArrayList<String> usersWhoAreRSVP;
	FirebaseFirestore db;
	
	RSVP(String ID){
		this.eventID = ID;
		usersWhoAreRSVP = new ArrayList<>();
		db = FirebaseFirestore.getInstance();
	}
	
	public String getEventID() { return this.eventID; }
	public ArrayList<String> getUsersWhoAreRSVP(){ return usersWhoAreRSVP;	}
	
	public void addUserToRSVP(String eventID){
		return;
	}
	
}// class [ RSVP ]
