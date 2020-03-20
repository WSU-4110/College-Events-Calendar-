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
import java.util.StringTokenizer;

public class EventDetailedView extends AppCompatActivity {
    String EventName, location, startTime, date;

    TextView EventNameInput;
    TextView locationInput;
    TextView startTimeInput;
    TextView dateInput;
    TextView descInput;
    TextView orgInput;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FloatingActionButton floating_toSavedEvents;

//DocumentReference serializedDay = db.;
//
// ??CHECK: Can we implement a serial variable to make DB searches easy?


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detailed_view);

        String eventStr = getIntent().getStringExtra("Event");

        StringTokenizer st2 = new StringTokenizer(eventStr, "|");
        String name = (String)st2.nextElement();
        String location = (String)st2.nextElement();
        String startTime = (String)st2.nextElement();
        String date = (String)st2.nextElement();
        String desc = (String)st2.nextElement();
        String org = (String)st2.nextElement();


        Event event = new Event(name,location,startTime, date, desc, org);

        EventNameInput = findViewById(R.id.EventName);
        locationInput =  findViewById(R.id.Location);
        startTimeInput = findViewById(R.id.Time);
        dateInput =  findViewById(R.id.EventDate);
        descInput = findViewById(R.id.Description);
        orgInput = findViewById(R.id.Organization);

        EventNameInput.setText(event.getName());
        locationInput.setText(event.getLocation());
        startTimeInput.setText(event.getStartTime());
        dateInput.setText(event.getDate());
        descInput.setText(event.getDesc());
        orgInput.setText(event.getOrg());

        floating_toSavedEvents = findViewById(R.id.floating_back_button);

        floating_toSavedEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SavedEvent sEvent = new SavedEvent("Ponnila", EventNameInput.getText().toString(),locationInput.getText().toString(),
                        startTimeInput.getText().toString(), dateInput.getText().toString(),
                        descInput.getText().toString(), orgInput.getText().toString());
                Intent intent = new Intent(getApplicationContext(), SavedEvents.class);
                intent.putExtra("SavedEvent", sEvent.toString());
                startActivity(intent);
                /*
                db.collection("SavedEvent")
                        .document("SavedEvent")
                        .collection("Event_SubCollectionTesting")
                        .add(SavedEvent);

                 */
            }
            });
            }


    // Back Button
    private void backTo_main() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
