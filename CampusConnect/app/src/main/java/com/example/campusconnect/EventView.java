package com.example.campusconnect;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;


public class EventView extends AppCompatActivity {
    
    String EventName, location, startTime, date;

    TextView EventNameInput;
    TextView OrgInput;
    TextView DescInput;
    TextView locationInput;
    TextView startTimeInput;
    TextView dateInput;
    Button backto_main;
    
    // TODO: Constructor for EventView(selectedDateFromCalendar)

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    
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

        Event event = new Event("Freshman Orientation", " Wayne Campus", "11.30",
                "02/20/2020", "Wayne State University","New student welcome ceremony and tour" );


        EventNameInput = findViewById(R.id.EventName);
        OrgInput = findViewById(R.id.Org);
        DescInput = findViewById(R.id.Desc);
        locationInput = findViewById(R.id.Location);
        startTimeInput = findViewById(R.id.Time);
        dateInput = findViewById(R.id.EventDate);

        EventNameInput.setText(event.getName());
        locationInput.setText(event.getLocation());
        startTimeInput.setText(event.getStartTime());
        dateInput.setText(event.getDate());
        OrgInput.setText(event.getOrg());
        DescInput.setText(event.getDesc());
    }

    public void backTo_main(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
        
    private void displayEventsForSelectedDay(String dateSelected) {
        
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        
        // Collection: JayTesting
        // Parameter "date" within .whereEqualTo is the "date" field WITHIN a document (not the document name)
        db.collection("JayTesting")
                .whereEqualTo("date", dateSelected)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                /*Event event = document.toObject(Event.class);
                                //event = document.toObject(Event.class);
                                
                                eventDate.setText(event.getMonth_Name());
                                eventDate.append(" ");
                                eventDate.append(event.getDay());
                                
                                eventOrganization.setText("Organization: ");
                                eventOrganization.append(event.getOrg());
    
                                eventDescription.setText("Event Description: ");
                                eventDescription.append(event.getDesc());*/
								
                                Event event = document.toObject(Event.class);
                                //eventList = new EventListFragment();
								
								//eventList.displayEvents(event);
                            }
                        }
                    }
                });
    
        //clearText();
        
    }// end [ METHOD: displayEvents ]*/

}// end [ CLASS: EventView ]





