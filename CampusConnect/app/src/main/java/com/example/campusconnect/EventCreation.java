package com.example.campusconnect;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class EventCreation extends AppCompatActivity{


    TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_creation);

        test= (TextView) findViewById(R.id.testText);
        test.setText("testing.");
        // testing commit and push
    }
}
