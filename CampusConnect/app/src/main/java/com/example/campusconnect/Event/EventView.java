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
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusconnect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;


public class EventView extends AppCompatActivity {
	
	private RecyclerView recyclerView;
	private RecyclerView.Adapter adapter;
	private RecyclerView.LayoutManager layoutManager;
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
        
        arrayOfEvents = new ArrayList<>();
        adapter = new EventListAdapter(this, arrayOfEvents);
        
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
	
		// CURRENT: Changing to RecyclerView
		// About 10min in on this video: https://www.youtube.com/watch?v=17NbUcEts9c
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Intent intent = new Intent(getApplicationContext(), EventDetailedView.class);
				
				Event eventClickedOn = (Event) listView.getAdapter().getItem(position);	// Event clicked on from list
			
				intent.putExtra("Event Clicked", eventClickedOn);
				startActivity(intent);
			}
		});

    }// method [ displayEventsForSelectedDay ]
    
	
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
	
}// class [ EventView ]


class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventViewHolder> {
 
	private ArrayList<Event> listOfEvents;
	
	public static class EventViewHolder extends RecyclerView.ViewHolder{
		TextView eventName;
		TextView eventDate;
		TextView eventLocation;
		TextView eventTag;
		
		public EventViewHolder(@NonNull View itemView) {
			super(itemView);
			
			eventName = (TextView) itemView.findViewById(R.id.list_EventName);
			eventDate = (TextView) itemView.findViewById(R.id.list_EventDate);
			eventLocation = (TextView) itemView.findViewById(R.id.list_EventLocation);
			eventTag = (TextView) itemView.findViewById(R.id.list_EventTag);
		}
	}
	
	public EventListAdapter(ArrayList<Event> events) {
		listOfEvents = events;
	}
	
	@NonNull
	@Override
	public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view;
		EventViewHolder viewHolder;
		
		view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_events, parent, false);
		viewHolder = new EventViewHolder(view);
		
		return viewHolder;
	}
	
	@Override
	public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
		Event event = listOfEvents.get(position);
		
		holder.eventName.setText(event.getName());
		holder.eventLocation.setText(String.format	("Location:    %s", event.location()));
		holder.eventDate.setText(String.format		("    Date:    %s", event.getDate()));
		holder.eventTag.setText(String.format		("     Tag:    %s", event.tag()));
	}
	
	@Override
	public int getItemCount() {
		return listOfEvents.size();
	}
	
	/*
	@Override
    public View getView(int position, View convertView, ViewGroup parent){
    				
        if (convertView == null) {
            convertView = LayoutInflater
                    .from(getContext())
                    .inflate(R.layout.list_events, parent, false);
        }
        
        Event event = getItem(position);
	
		// CHECK: These get flagged as potential NPE. Even in a TRY/CATCH and if/else!!
        try {
			eventName.setText(event.getName());
		} catch (NullPointerException np){
        	eventName.setText(" ");
		}
        eventLocation.setText(String.format("Location:    %s", event.location()));
        eventLocation.setText(String.format("    Date:    %s", event.getDate()));
        eventLocation.setText(String.format("     Tag:    %s", event.tag()));
        
        return convertView;
    }*/

}// class [ EventListAdapter ]