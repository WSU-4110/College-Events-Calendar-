package com.example.campusconnect.Event;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campusconnect.MainActivity;
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
		Button searchEventName = findViewById(R.id.searchEventName);
		Button searchTag = findViewById(R.id.searchTag);
		
		final String searchTerm = getIntent().getStringExtra("result");
		
		if (searchTerm == null) {
			Toast.makeText(Search.this, "No Search Term Entered", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(Search.this, MainActivity.class);
			startActivity(intent);
		}
		else {
			// TODO: Handle null searchTerm better
			final String searchTermUpper = searchTerm.toUpperCase();
			final String whatToSearch = getIntent().getStringExtra("whatToSearch");
			
			router(whatToSearch, searchTerm);
			
			searchEventName.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String newSearchBy = "EventName";
					Intent intent = new Intent(getApplicationContext(), Search.class);
					
					intent.putExtra("result", searchTermUpper);
					intent.putExtra("searchBy", newSearchBy);
					startActivity(intent);
				}
			});
			
			searchTag.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String newSearchBy = "Tag";
					Intent intent = new Intent(getApplicationContext(), Search.class);
					
					intent.putExtra("result", searchTermUpper);
					intent.putExtra("searchBy", newSearchBy);
					startActivity(intent);
				}
			});
		}
	}
	
	
	private void setTitle(String title){
		textView = findViewById(R.id.event_list_dynamic_header);
		
		textView.setText(title);
	}
	
	
	private void router(String whatToSearch, String search) {
		if (whatToSearch.equals("EventName")) {
			String title = "Event Names Matching: " + whatToSearch;
			setTitle(title);
			searchResultName();
		}
		else if (whatToSearch.equals("Tag")) {
			String title = "Event Tags Matching: " + whatToSearch;
			setTitle(title);
			searchResultTag();
		}
		else {
			// This block should not be reached (as of 17:25 Jul 7 2020)
			String title = "No Matches for: " + whatToSearch;
			setTitle(title);
		}
		
	}// [ router ]
	
	
	public static String returnExtraString(String search) {
		search = search + " ";
		return search;
	}
	
	
	// TODO(Refactoring/wip): One method for searching. Parameterized which Event attribute to search.
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
						}
					}
				});
		
	}// [ searchResultName ]
	
	
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




