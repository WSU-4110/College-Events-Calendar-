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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class EventView extends AppCompatActivity {
    
    String day_selected;        // Date selected by user and passed in from Intent
    Button backto_main;
    FloatingActionButton floating_backToMain;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_view);
    
        floating_backToMain = findViewById(R.id.floating_back_button);
        floating_backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backTo_main();
            }
        });
    
        day_selected = getIntent().getStringExtra("EXTRA_DAY_SELECTED");                // Extract String from Intent attached from MainActivity
        System.out.println("day: " + day_selected);
        displayEventsForSelectedDay(day_selected);
    }
    
    // Back Button
    private void backTo_main() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    
    // Local helper class for setting header/title for events list
    class DateHeaderHelper extends Event {
        public String dateFullFormat_helper() {
            this.setDate(day_selected);
            return this.getDate_Full();
        }
    }
    
    // SEE BELOW for attaching adapter to a ListView
    private void displayEventsForSelectedDay(String dateSelected) {
    
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        TextView eventListHeader = findViewById(R.id.EventList_HeaderDynamic);
        eventListHeader.setText(new DateHeaderHelper().dateFullFormat_helper());
    
        final ArrayList<Event> arrayOfEvents;
        final EventListAdapter adapter;
        ListView listView;
        
        arrayOfEvents = new ArrayList<>();                                           // [1]
        adapter = new EventListAdapter(this, arrayOfEvents);                 // [2]
        listView = (ListView) findViewById(R.id.events_listView);                    // [3]
        listView.setAdapter(adapter);                                                // [4]
        
        db.collection("Events")
                .document("Events")
                .collection("Event_SubCollectionTesting")
                .whereEqualTo("date", dateSelected)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                arrayOfEvents.add((Event) document.toObject(Event.class));
                            }
    
                            adapter.addAll(arrayOfEvents);
                        }
                    }
                });
        
        
    }
    
}



// SEE BELOW for Adapter custom class procedure
class EventListAdapter extends ArrayAdapter<Event> {
    
    public EventListAdapter(Context context, ArrayList<Event> events){
        super(context, 0, events);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        if (convertView == null) {                                                                      // [1]
            convertView = LayoutInflater
                    .from(getContext())
                    .inflate(R.layout.list_events, parent, false);
        }
        
        Event event = getItem(position);                                                                // [2]

        TextView eventName =        (TextView) convertView.findViewById(R.id.list_EventName);           // [3a]
        TextView eventDate =        (TextView) convertView.findViewById(R.id.list_EventDate);			// [3b]
        TextView eventLocation =    (TextView) convertView.findViewById(R.id.list_EventLocation);		// [3c]

        eventName.setText("Event Name:    ");                                                           // [4]
        eventName.append(event.getName());
    
        eventDate.setText("Date:    ");
        eventDate.append(event.getDate());
    
        eventLocation.setText("Location:    ");
        eventLocation.append(event.getLocation());
        
        return convertView;                                                                             // [5]
        
    }// end [ METHOD: getView() ]
    
}// end [ CLASS: EventListAdapter() ]




//--------------------------------------------------- NOTES ---------------------------------------------------//

// --| ATTACHING THE ADAPTER TO A LIST VIEW |--
// 1. Construct the data source
// 2. Create the Adapter to convert the array to views
// 3. Declare the ListView object
// 4. Attach the adapter to a ListView (XML)

// --| CUSTOM ADAPTER CLASS |--
// 1. Check if an existing view is being reused, otherwise inflate the view
// 2. Get the data item for this position
// 3. Lookup view for data population
// 4. Populate the data into the template view using the data object
// 5. Return the completed view to render on screen



// CHECK: Classes without access specifiers [Seems to make items Package-Private]
// CHECK: Can we apply a method similar to the singleton DB connection example in [Lecture 11]?
// CHECK: Also, better to make use an Event object local to DateHelper [OR] keep extends?
// CHECK: Look into view recycling: https://bit.ly/2I6nPCV