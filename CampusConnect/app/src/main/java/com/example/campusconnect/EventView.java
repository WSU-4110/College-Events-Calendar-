// Documentation on ListView and Custom Adapters:
// https://bit.ly/38ah7WV
// Windows: [Ctrl + Click]
// Mac:     [⌘ + Click]

package com.example.campusconnect;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

// TODO: Discuss class placements (e.g. moving the custom ArrayAdapter() to Event class)
// CHECK: Classes without access specifiers [Seems to make items Package-Private]
public class EventView extends AppCompatActivity {
	
	String day_selected;        // Intermediate variable for ease of reading
	Button backto_main;
	
	TextView eventName;
	TextView eventOrganization;
	TextView eventDescription;
	TextView eventLocation;
	TextView eventStartTime;
	TextView eventDate;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_view);
		
		backto_main = findViewById(R.id.back_button);
		backto_main.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				backTo_main();
			}
		});
        /*
        eventName = findViewById(R.id.EventName);
        eventOrganization = findViewById(R.id.Org);
        eventDescription = findViewById(R.id.Desc);
        eventLocation = findViewById(R.id.Location);
        eventStartTime = findViewById(R.id.StartTime);
        eventDate = findViewById(R.id.EventDate);*/
		
		day_selected = getIntent().getStringExtra("EXTRA_DAY_SELECTED");                // Info passed in from MainActivity
		displayEventsForSelectedDay(day_selected);
	}
	
	// Back Button
	public void backTo_main() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
	
	// CHECK: Look into view recycling: https://bit.ly/2I6nPCV
	// CHECK: Remove "event" from TextView variable names?
	private void displayEventsForSelectedDay(String dateSelected) {
		
		FirebaseFirestore db = FirebaseFirestore.getInstance();
		final ArrayList<Event> arrayOfEvents;
		final EventListAdapter adapter;
		ListView listView;
		
		// --| ATTACHING THE ADAPTER TO A LIST VIEW |--
		// 1. Construct the data source
		// 2. Create the Adapter to convert the array to views
		// 3. Declare the ListView object
		// 4. Attach the adapter to a ListView (XML)
		
		arrayOfEvents = new ArrayList<>();                                           // [1]
		adapter = new EventListAdapter(this, arrayOfEvents);                 // [2]
		listView = (ListView) findViewById(R.id.events_listView);                    // [3]
		listView.setAdapter(adapter);                                                // [4]
		
		// Current Collection: JayTesting (return to "Events" once DB structuring finalized)
		db.collection("JayTesting")
				.whereEqualTo("date", dateSelected)
				.get()
				.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
					@Override
					public void onComplete(@NonNull Task<QuerySnapshot> task) {
						if (task.isSuccessful()) {
							for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
								
								arrayOfEvents.add((Event) document.toObject(Event.class));
								
								// BELOW: OLD OUTPUT METHOD
								//Event event = document.toObject(Event.class);
                                /*
                                eventName           .setText(event.getName());
                                eventOrganization   .setText(event.getOrg());
                                eventDescription    .setText(event.getDesc());
                                eventLocation       .setText(event.getLocation());
                                eventStartTime      .setText(event.getStartTime_Formatted());   // HH:MM
                                eventDate           .setText(event.getDate_Formatted());        // MM/DD/YYYY
                                */
								
								
							}// end Query for-loop
							
							adapter.addAll(arrayOfEvents);
						}
					}
				});
		
	}// end [ METHOD: displayEventsForSelectedDay() ]
	
}// end [ CLASS: EventView() ]


// Custom class for displaying Events in a list using Adapters
// 1. Check if an existing view is being reused, otherwise inflate the view
// 2. Get the data item for this position
// 3. Lookup view for data population
// 4. Populate the data into the template view using the data object
// 5. Return the completed view to render on screen
class EventListAdapter extends ArrayAdapter<Event> {
	
	public EventListAdapter(Context context, ArrayList<Event> events) {
		super(context, 0, events);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Event event;
		
		if (convertView == null) {                                                                      // [1]
			convertView = LayoutInflater
					.from(getContext())
					.inflate(R.layout.list_events, parent, false);
		}
		
		event = getItem(position);                                                                      // [2]
		
		TextView eventName = (TextView) convertView.findViewById(R.id.list_EventName);           		// [3a]
		TextView eventDate = (TextView) convertView.findViewById(R.id.list_EventDate);            		// [3b]
		TextView eventLocation = (TextView) convertView.findViewById(R.id.list_EventLocation);        	// [3c]
		
		// TODO: Once complete, switch to getDate_formatted()
		eventName.setText(event.getName());                                                             // [4]
		eventDate.setText(event.getDate());
		eventLocation.setText(event.getLocation());
		
		return convertView;                                                                             // [5]
		
	}// end [ METHOD: getView() ]
	
}// end [ CLASS: EventListAdapter() ]
