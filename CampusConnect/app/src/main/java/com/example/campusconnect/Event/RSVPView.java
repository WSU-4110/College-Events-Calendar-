package com.example.campusconnect.Event;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campusconnect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class RSVPView extends AppCompatActivity {
	
	ArrayAdapter<String> adapter;
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
		
		adapter = new ArrayAdapter<String>(this, R.layout.rsvp_list, listOfNames);
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
	
}// class RSVPView
