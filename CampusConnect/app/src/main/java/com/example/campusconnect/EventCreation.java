package com.example.campusconnect;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


class Event {


    private String name;
    private String location;
    private String startTime;
    private String date;

//    public Event(){
//        this.name = "";
//        this.location = "";
//        this.startTime = "";
//        this.date = "";
//    }

    public Event(String name, String location, String startTime, String date) {
        this.name = name;
        this.location = location;
        this.startTime = startTime;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getDate() {
        return date;
    }

}



public class EventCreation extends AppCompatActivity{

    String EventName, location, startTime, date;

    EditText EventNameInput;
    EditText locationInput;
    EditText startTimeInput;
    EditText dateInput;

    Button submitButton;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //DocumentReference serializedDay = db.;                                    // ??CHECK: Can we implement a serial variable to make DB searches easy?





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_creation);


        submitButton = findViewById(R.id.event_submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventNameInput = (EditText) findViewById(R.id.EventName);
                locationInput = (EditText) findViewById(R.id.Location);
                startTimeInput = (EditText) findViewById(R.id.Time);
                dateInput = (EditText) findViewById(R.id.EventDate);

                EventName = EventNameInput.getText().toString();
                location = locationInput.getText().toString();
                startTime = startTimeInput.getText().toString();
                date = dateInput.getText().toString();

                Event event = new Event(EventName, location, startTime,
                        date);
                db.collection("Events").document("Practice3").set(event);
            }
        });
    }


    //------| Jay |-------//

    // FireBase Documentation Resources:
    // 1. [Offline Data Access] https://firebase.google.com/docs/firestore/manage-data/enable-offline
    // 2. [Reading Data] https://firebase.google.com/docs/firestore/query-data/get-data

    /*
    
    // ?? CHECK: Can we serialize the date in some way to make search quick and unambiguous?
    void listTodaysEvents(String targetDay){
        while(db.) {

        }
    }
    
    
    */

}












