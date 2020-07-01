package com.example.campusconnect.Event;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campusconnect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class Search extends AppCompatActivity {
	
	ListView listView;
	private TextView textView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_view);
		
		// TODO: Add minimum letters for search
		listView = (ListView) findViewById(R.id.events_listView);
		textView = findViewById(R.id.EventList_HeaderDynamic);
		Button toggleSearchBy = findViewById(R.id.toggleSearchBy);
		
		final String searchTerm = getIntent().getStringExtra("result");			// Get exactly what the user typed in
		final String searchTermUpper = searchTerm.toUpperCase();				// For case-sensitive methods
		final String searchBy = getIntent().getStringExtra("searchBy");			// What attribute of Event to search
		
		if (searchBy == null) {
			System.err.println("No search string received");
		}
		else if (searchBy.equals("EventName")) {
			String title = "Event Names Matching: " + searchTerm;
			textView.setText(title);
			searchResultName();
		}
		else if (searchBy.equals("Tag")) {
			String title = "Event Tags Matching: " + searchTerm;
			textView.setText(title);
			searchResultTag();
		}
		else {
			System.err.println("Invalid and/or error with \"searchBy\" value.");
			System.out.println("Defaulting: Search by tag");
			
			String title = "No Matches for: " + searchTerm;
			textView.setText(title);
		}
		
		toggleSearchBy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String newSearchBy = searchBy.equals("Tag") ? "EventName" : "Tag";
				Intent intent = new Intent(getApplicationContext(), Search.class);
				
				intent.putExtra("result", searchTermUpper);
				intent.putExtra("searchBy", newSearchBy);
				startActivity(intent);
			}
		});
	}
	
	public static String returnExtraString(String search) {
		search = search + " ";
		return search;
	}
	
	private void searchResultName() {
		FirebaseFirestore db = FirebaseFirestore.getInstance();
		final ArrayList<Event> arrayOfEvents;
		final EventListAdapter adapter;
		final String searchTerm = getIntent().getStringExtra("result");
		
		arrayOfEvents = new ArrayList<>();										// [1]
		adapter = new EventListAdapter(this, arrayOfEvents);					// [2]
		listView = (ListView) findViewById(R.id.events_listView);				// [3]
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Event event = (Event) listView.getAdapter().getItem(position);
				Intent intent = new Intent(getApplicationContext(), EventDetailedView.class);
				intent.putExtra("Event", event.toString());
				startActivity(intent);
			}
		});
		
		db.collection("Events")
				.get()
				.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
					@Override
					public void onComplete(@NonNull Task<QuerySnapshot> task) {
						if (task.isSuccessful()) {
							for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
								Event event = (Event) document.toObject(Event.class);
								String eventName = event.getName();
								
								if (checkForMatch(eventName, searchTerm)) {
									adapter.add(event);
								}
							}
						}// if(task)
					}
				});
	}
	
	private void searchResultTag() {
		FirebaseFirestore db = FirebaseFirestore.getInstance();
		final ArrayList<Event> arrayOfEvents;
		final EventListAdapter adapter;
		final String searchTerm = getIntent().getStringExtra("result");
		
		arrayOfEvents = new ArrayList<>();										// [1]
		adapter = new EventListAdapter(this, arrayOfEvents);					// [2]
		listView = (ListView) findViewById(R.id.events_listView);				// [3]
		listView.setAdapter(adapter);											// [4]
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Event event = (Event) listView.getAdapter().getItem(position);
				Intent intent = new Intent(getApplicationContext(), EventDetailedView.class);
				intent.putExtra("Event", event.toString());
				startActivity(intent);
			}
		});
		
		db.collection("Events")
				.get()
				.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
					@Override
					public void onComplete(@NonNull Task<QuerySnapshot> task) {
						if (task.isSuccessful()) {
							for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
								Event event = (Event) document.toObject(Event.class);
								String eventTag = event.tag();
								
								// Event was not given a tag
								if (eventTag == null)
									continue;
								
								if (checkForMatch(eventTag, searchTerm)) {
									adapter.add(event);
								}
							}
						}
					}
				});
	}
	
	private boolean checkForMatch(String eventAttribute, String searchTerm) {
		String attribute = eventAttribute.toUpperCase();
		String searchTermUpper = searchTerm.toUpperCase();
		
		return attribute.contains(searchTermUpper);
	}
	
	
	// to match tags
	public static String returnProperString(String s) {
		
		if (s == null || s.isEmpty()) {
			return s;
		}
		s.toLowerCase();
		s = s.substring(0, 1).toUpperCase() + s.substring(1);
		return s;
	}
	
	
}// class [ Search ]




