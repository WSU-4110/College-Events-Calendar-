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
    private String org;
    private String desc;

//    public Event(){
//        this.name = "";
//        this.location = "";
//        this.startTime = "";
//        this.date = "";
//    }

    public Event(String name, String location, String startTime, String date, String org, String desc) {
        this.name = name;
        this.location = location;
        this.startTime = startTime;
        this.date = date;
        this.org = org;
        this.desc = desc;
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

    public String getOrg() {
        return org;
    }

    public String getDesc() {
        return desc;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

                //to do: fix event date so realistic event dates are put in.
                // month -> day -> event name -> set(event)
                // fix XML display so a calendar pops up when they need to input an event.

                Event event = new Event(EventName, location, startTime,
                        date, "Org", "Desc");
                db.collection("Events").document("Events").collection(date).document(EventName).set(event);
            }
        });
    }
}












