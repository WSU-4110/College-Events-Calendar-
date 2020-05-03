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

import com.example.campusconnect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import com.example.campusconnect.Event.Event;

import java.util.ArrayList;
import java.util.Objects;


public class EventView extends AppCompatActivity {

	ListView listView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_view);
		listView = (ListView) findViewById(R.id.events_listView);
	
		String str_Day = getIntent().getStringExtra("EXTRA_DaySelected");				// Extract date info from intent
		String str_Month = getIntent().getStringExtra("EXTRA_MonthSelected");
		String str_Year = getIntent().getStringExtra("EXTRA_YearSelected");
        System.out.printf("DAY %s, MONTH %s, YEAR %s", str_Day, str_Month, str_Year);
        displayEventsForSelectedDay(str_Day, str_Month, str_Year);
    }
    

    private void displayEventsForSelectedDay(String day, String month, String year) {
    
        TextView title = findViewById(R.id.EventList_HeaderDynamic);
        title.setText(titleCreator(day, month, year));
        
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ArrayList<Event> arrayOfEvents;
		final EventListAdapter adapter;
	
		String wholeDate = wholeDateBuilder(day, month, year);
        																		// [A]
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
                .whereEqualTo("date", wholeDate)
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

    }// end method [ displayEventsForSelectedDay ]
    
	
    public String titleCreator(String day, String month, String year){
		// !! NOTE: Jan == 0, Dec == 11
		
		int monthInteger = Integer.parseInt(month);

		String[] monthName = {	"January", 	"February", "March",
								"April", 	"May", 		"June",
								"July", 	"August", 	"September",
								"October", 	"November", "December" };
		
		return String.format("%s %s, %s", monthName[monthInteger], day, year);
	}
	
	
	public String wholeDateBuilder(String day, String month, String year){
		// !! NOTE: Jan == 0, Dec == 11 (before adding 1)

		StringBuilder date = new StringBuilder();

		int monthNumber = Integer.parseInt(month) + 1;
		
		if(monthNumber > 0 && monthNumber <= 12)
			date.append(monthNumber);
		else
			date.append(" ");
		
        date.append("/");

		date.append(day);
        date.append("/");
		date.append(year);

		return date.toString();			// StringBuilder --> String
	}
	
}// END CLASS [ EventView ]


class EventListAdapter extends ArrayAdapter<Event>  {
    
    public EventListAdapter(Context context, ArrayList<Event> events){
        super(context, 0, events);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
    																								// [B]
        if (convertView == null) {																	// [1]
            convertView = LayoutInflater
                    .from(getContext())
                    .inflate(R.layout.list_events, parent, false);
        }
        
        Event event = getItem(position);															// [2]

        TextView eventName =		(TextView) convertView.findViewById(R.id.list_EventName);		// [3a]
        TextView eventDate =		(TextView) convertView.findViewById(R.id.list_EventDate);		// [3b]
        TextView eventLocation =	(TextView) convertView.findViewById(R.id.list_EventLocation);	// [3c]
        TextView eventTag =			(TextView) convertView.findViewById(R.id.list_EventTag);		// [3d]
																									// [4]
		eventName.setText(event.getName());

        eventDate.setText("Date:    ");
        eventDate.append(event.getDate());

        eventLocation.setText("Location:    ");
        eventLocation.append(event.getLocation());
	
		eventTag.setText("Tag:    ");
		eventTag.append(event.getTag());
        
        return convertView;																			// [5]
    }

}// END CLASS [ EventListAdapter ]




//--------------------------------------------------- NOTES ---------------------------------------------------//

//				[A]
// --| ATTACHING THE ADAPTER TO A LIST VIEW |--
// 1. Construct the data source
// 2. Create the Adapter to convert the array to views
// 3. Declare the ListView object
// 4. Attach the adapter to a ListView (XML)

// 				[B]
// --| CUSTOM ADAPTER CLASS [B] |--
// 1. Check if an existing view is being reused, otherwise inflate the view
// 2. Get the data item for this position
// 3. Lookup view for data population
// 4. Populate template view using the temp Event object
// 5. Return the completed view to render on screen