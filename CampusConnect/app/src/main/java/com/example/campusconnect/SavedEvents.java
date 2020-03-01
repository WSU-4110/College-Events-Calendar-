package com.example.campusconnect;


import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

// TODO: Update. HTML.fromHtml is deprecated
@SuppressWarnings("deprecation")
public class SavedEvents extends AppCompatActivity {

    TextView event_link1;
    String linkText1 ;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_events);

        event_link1 = findViewById(R.id.event_link1);

        linkText1= "Event1";
        event_link1 = findViewById(R.id.event_link1);
        event_link1.setText(Html.fromHtml(linkText1));
        event_link1.setMovementMethod(LinkMovementMethod.getInstance());

        event_link1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                openEventView("1");
            }
        });
    }

    public void openEventView(String eventId) {
        Intent intent = new Intent(this, EventView.class);
        intent.putExtra("EVENT_ID", eventId);
        startActivity(intent);
    }


    }

// end [ CLASS: SavedEvents ]





