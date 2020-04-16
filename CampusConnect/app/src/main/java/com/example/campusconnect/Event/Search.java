package com.example.campusconnect.Event;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.SearchManager;
import android.widget.ListView;
import android.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.view.MenuItemCompat;

import com.example.campusconnect.R;
import com.example.campusconnect.UI.Authentication.signIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

import static java.lang.Integer.valueOf;


public class Search extends AppCompatActivity {

    ListView listView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_view);
        listView = (ListView) findViewById(R.id.events_listView);

        Intent intent = getIntent();
        String search= intent.getStringExtra("result");

        textView= findViewById(R.id.EventList_HeaderDynamic);
        textView.setText(search);

		searchResultName(search);
		searchResultTag(search);

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

							//adapter.addAll(arrayOfEvents);
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

							//adapter.addAll(arrayOfEvents);
						}
					}
				});
	}


}



