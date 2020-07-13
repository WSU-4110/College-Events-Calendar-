package com.example.campusconnect.Event;

// Documentation on ListView and Custom Adapters:
// https://bit.ly/38ah7WV

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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


public class EventView extends AppCompatActivity {

	ListView listView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_list);
		listView = (ListView) findViewById(R.id.events_listView);
	
		String str_Day = getIntent().getStringExtra("day");
		String str_Month = getIntent().getStringExtra("month");
		String str_Year = getIntent().getStringExtra("year");
	
        displayEventsForSelectedDay(str_Day, str_Month, str_Year);
    }
    
    
    private void displayEventsForSelectedDay(String day, String month, String year) {
    
        TextView title = findViewById(R.id.event_list_dynamic_header);
        title.setText(titleCreator(day, month, year));
        
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ArrayList<Event> arrayOfEvents;
		final EventListAdapter adapter;
			
        																		// [A]
        arrayOfEvents = new ArrayList<>();										// [1]
        adapter = new EventListAdapter(this, arrayOfEvents);					// [2]
        listView = findViewById(R.id.events_listView);							// [3]
        listView.setAdapter(adapter);											// [4]
	
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Intent intent = new Intent(getApplicationContext(), EventDetailedView.class);
				Event clickedEvent = (Event) listView.getAdapter().getItem(position);
				
				intent.putExtra("Event Parcel", clickedEvent);
				startActivity(intent);
			}
		});
	
		String formattedDate = formattedDateBuilder(day, month, year);
		db.collection("Events")
                .whereEqualTo("date", formattedDate)
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

    }// [ displayEventsForSelectedDay ]
    
	
    String titleCreator(String day, String month, String year){
		// !! NOTE: Jan == 0, Dec == 11
		
		int monthInteger = Integer.parseInt(month);

		String[] monthName = {	"January", 	"February", "March",
								"April", 	"May", 		"June",
								"July", 	"August", 	"September",
								"October", 	"November", "December" };
		
		return String.format("%s %s, %s", monthName[monthInteger], day, year);
	}
	
	
	String formattedDateBuilder(String day, String month, String year){
		// !! NOTE: Jan == 0, Dec == 11 (before adding 1)
		
		int monthNumber = Integer.parseInt(month) + 1;
		
		return String.format("%s/%s/%s", monthNumber, day, year);
	}
	
}// class [ EventView ]