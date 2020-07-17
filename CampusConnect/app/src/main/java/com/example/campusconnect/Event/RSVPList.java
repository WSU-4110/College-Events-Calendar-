package com.example.campusconnect.Event;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campusconnect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class RSVPList extends AppCompatActivity {
	
	ArrayAdapter adapter;
	ListView listView;
	ArrayList<String> listOfNames;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO: Error handling
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rsvp_list);
		listView = (ListView) findViewById(R.id.rsvp_people);
		
		String eventID = getIntent().getStringExtra("eventID");
		
		findRSVPEntryDB(eventID);
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listOfNames);
	}
	
	private void findRSVPEntryDB(String eventID) {
		FirebaseFirestore db = FirebaseFirestore.getInstance();
		
		db.collection("RSVP")
				.document(eventID)
				.get()
				.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
					@Override
					public void onComplete(@NonNull Task<DocumentSnapshot> task) {
						if (task.isSuccessful()) {
							DocumentSnapshot document = task.getResult();
							RSVP foundRSVP = (RSVP) document.toObject(RSVP.class);
							listOfNames = foundRSVP.getUsersWhoAreRSVP();
						}
					}
				});
	}
	
}// class RSVPList
