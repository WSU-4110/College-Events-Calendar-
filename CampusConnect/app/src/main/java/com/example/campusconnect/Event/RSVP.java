package com.example.campusconnect.Event;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

class RSVP {
	static FirebaseFirestore db;
	private ArrayList<String> usersWhoAreRSVP;
	
	RSVP(){
		usersWhoAreRSVP = new ArrayList<>();
		db = FirebaseFirestore.getInstance();
	}
	
	public void addUserToRSVP(String eventID){
		return;
	}
	
}// class [ RSVP ]
