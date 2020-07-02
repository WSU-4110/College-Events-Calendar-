package com.example.campusconnect.Event;

// Documentation on ListView and Custom Adapters:
// https://bit.ly/38ah7WV

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campusconnect.MainActivity;
import com.example.campusconnect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;


public class SavedEventView extends AppCompatActivity {
	
	ListView listView;
	FirebaseUser user;
	FirebaseFirestore db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_view);
		listView = (ListView) findViewById(R.id.events_listView);
		
		user = FirebaseAuth.getInstance().getCurrentUser();
		db = FirebaseFirestore.getInstance();
		
		if (user == null) {
			Intent intent = new Intent(getApplicationContext(), MainActivity.class);
			startActivity(intent);
		}
		
		if (EventCreation.isOrganizer())
			displayEventsForOrganizer();
		else
			displayEventsForUser();
	}
	
	
	private void displayEventsForUser() {
		TextView title = findViewById(R.id.event_list_dynamic_header);
		title.setText("Your Saved Events");
		
		final ArrayList<Event> arrayOfEvents;
		final SavedEventListAdapter adapter;
		
		arrayOfEvents = new ArrayList<>();
		adapter = new SavedEventListAdapter(this, arrayOfEvents);
		listView = (ListView) findViewById(R.id.events_listView);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Intent intent = new Intent(getApplicationContext(), EventDetailedView.class);
				Event clickedEvent = (Event) listView.getAdapter().getItem(position);
				
				intent.putExtra("Event Parcel", clickedEvent);
				startActivity(intent);
			}
		});
		
		db.collection("Events")
				.document("Events")
				.collection("Event_SubCollectionTesting")
				.whereEqualTo("orgUid", user.getUid())
				.get()
				.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
					@Override
					public void onComplete(@NonNull Task<QuerySnapshot> task) {
						if (task.isSuccessful()) {
							for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
								adapter.add((Event) document.toObject(Event.class));
							}
						}
					}
				});
		
	}// [ displayEventsForUser ]
	
	
	private void displayEventsForOrganizer() {
		TextView title = findViewById(R.id.event_list_dynamic_header);
		title.setText("Your Created Events");
		
		final ArrayList<Event> arrayOfEvents;
		final SavedEventListAdapter adapter;
		
		arrayOfEvents = new ArrayList<>();
		adapter = new SavedEventListAdapter(this, arrayOfEvents);
		listView = findViewById(R.id.events_listView);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Intent intent = new Intent(getApplicationContext(), EventDetailedView.class);
				Event clickedEvent = (Event) listView.getAdapter().getItem(position);
				
				intent.putExtra("Event Parcel", clickedEvent);
				startActivity(intent);
			}
		});
		
		db.collection("Events")
				.whereEqualTo("orgUid", user.getUid())
				.get()
				.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
					@Override
					public void onComplete(@NonNull Task<QuerySnapshot> task) {
						if (task.isSuccessful()) {
							for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
								adapter.add((Event) document.toObject(Event.class));
							}
						}
					}
				});
		
	}// [ displayEventsForOrganizer ]
	
}// class [ EventView ]


class SavedEventListAdapter extends ArrayAdapter<Event> {
	
	public SavedEventListAdapter(Context context, ArrayList<Event> events) {
		super(context, 0, events);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater
					.from(getContext())
					.inflate(R.layout.list_events, parent, false);
		}
		
		Event event = getItem(position);
		
		TextView eventName = (TextView) convertView.findViewById(R.id.list_EventName);
		TextView eventDate = (TextView) convertView.findViewById(R.id.list_EventDate);
		TextView eventLocation = (TextView) convertView.findViewById(R.id.list_EventLocation);
		
		eventName.setText("Event Name:    ");
		eventName.append(event.getName());
		
		eventDate.setText("Date:    ");
		eventDate.append(event.getDate());
		
		eventLocation.setText("Location:    ");
		eventLocation.append(event.location());
		
		return convertView;
	}
	
}// class [ EventListAdapter ]
