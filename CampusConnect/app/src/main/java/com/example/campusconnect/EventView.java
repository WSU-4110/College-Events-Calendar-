package com.example.campusconnect;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;


public class EventView extends AppCompatActivity {

    String EventName, location, startTime, date;

    TextView EventNameInput;
    TextView OrgInput;
    TextView DescInput;
    TextView locationInput;
    TextView startTimeInput;
    TextView dateInput;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //DocumentReference serializedDay = db.;                                    // ??CHECK: Can we implement a serial variable to make DB searches easy?


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_view);


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
}











