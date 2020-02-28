package com.example.campusconnect;


import android.content.Intent;
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
    Button backto_main;

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

}// end [ CLASS: EventView ]





