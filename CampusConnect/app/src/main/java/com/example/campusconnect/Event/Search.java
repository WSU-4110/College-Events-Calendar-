package com.example.campusconnect.Event;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
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
	public static TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_view);
		listView = (ListView) findViewById(R.id.events_listView);

		Intent intent = getIntent();
		String search= intent.getStringExtra("result");

		textView= findViewById(R.id.EventList_HeaderDynamic);
		textView.setText(search);

		search = returnProperString(search);
		String search2 = returnExtraString(search);

		searchResultName(search);
		searchResultTag(search);


	}

	public static String returnExtraString(String search) {
		search = search + " ";
		return search;
	}


	public void searchResultName(String s){
		FirebaseFirestore db = FirebaseFirestore.getInstance();
		final ArrayList<Event> arrayOfEvents;
		final EventListAdapter adapter;
		arrayOfEvents = new ArrayList<>();                                      // [1]
		adapter = new EventListAdapter(this, arrayOfEvents);			// [2]
		listView = (ListView) findViewById(R.id.events_listView);               // [3]
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> arg0,View arg1, int position, long arg3)
			{
				Event event = (Event)listView.getAdapter().getItem(position);
				Intent intent = new Intent(getApplicationContext(), EventDetailedView.class);
				intent.putExtra("Event", event.toString());
				startActivity(intent);
			}
		});

		db.collection("Events")
				.document("Events")
				.collection("Event_SubCollectionTesting")
				.whereEqualTo("name", s)
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
	}

	public void searchResultTag(String s){
		FirebaseFirestore db = FirebaseFirestore.getInstance();
		final ArrayList<Event> arrayOfEvents;
		final EventListAdapter adapter;
		arrayOfEvents = new ArrayList<>();                                      // [1]
		adapter = new EventListAdapter(this, arrayOfEvents);			// [2]
		listView = (ListView) findViewById(R.id.events_listView);               // [3]
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> arg0,View arg1, int position, long arg3)
			{
				Event event = (Event)listView.getAdapter().getItem(position);
				Intent intent = new Intent(getApplicationContext(), EventDetailedView.class);
				intent.putExtra("Event", event.toString());
				startActivity(intent);
			}
		});

		db.collection("Events")
				.document("Events")
				.collection("Event_SubCollectionTesting")
				.whereEqualTo("tags", s)
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
	}


	// to match tags
	public static String returnProperString(String s){

		if(s == null || s.isEmpty()) {
			return s;
		}
		s.toLowerCase();
		s= s.substring(0, 1).toUpperCase() + s.substring(1);
		return s;
	}


}



