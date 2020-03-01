package com.example.campusconnect;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    
    // TextView objects follow match the order they are displayed in [ LAYOUT: event_view.xml ]
    TextView eventName;
    TextView eventOrganization;
    TextView eventDescription;
    TextView eventLocation;
    TextView eventStartTime;
    TextView eventDate;
    Button backto_main;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    
    // I need to send Day month year String from MainActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_view);
        
        String day_selected = getIntent().getStringExtra("EXTRA_DAY_SELECTED");
        
        
        backto_main = findViewById(R.id.back_button);
        backto_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backTo_main();
            }
        });

        /*Event event = new Event("Freshman Orientation", " Wayne Campus", "11.30",
                "02/20/2020", "Wayne State University","New student welcome ceremony and tour" );*/
    
    
        eventName = findViewById(R.id.EventName);
        eventOrganization = findViewById(R.id.Org);
        eventDescription = findViewById(R.id.Desc);
        eventLocation = findViewById(R.id.Location);
        eventStartTime = findViewById(R.id.StartTime);
        eventDate = findViewById(R.id.EventDate);
    
        displayEventsForSelectedDay(day_selected);
    }

    public void backTo_main(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    
    // CHECK: Is declaring TextView as final okay?
    // CHECK: Remove "event" from TextView variable names?
    private void displayEventsForSelectedDay(String dateSelected) {
        System.out.println("DISPLAY CALLED");
        System.out.println("DATE PASSED: " + dateSelected);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        
        // Collection: JayTesting
        // Parameter "date" within .whereEqualTo is the "date" field within a document (NOT the document name)
        db.collection("JayTesting")
                .whereEqualTo("date", dateSelected)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                
                                // TODO: Edit getStartTime_Formatted()
                                Event event = document.toObject(Event.class);
                                
                                eventName           .setText(event.getName());
                                eventOrganization   .setText(event.getOrg());
                                eventDescription    .setText(event.getDesc());
                                eventLocation       .setText(event.getLocation());
                                eventStartTime      .setText(event.getStartTime_Formatted());   // HH:MM
                                eventDate           .setText(event.getDate_Formatted());        // MM/DD/YYYY
                                
                            }
                        }
                    }
                });
    
        //clearText();
        
    }// end [ METHOD: displayEvents() ]

}// end [ CLASS: EventView() ]





