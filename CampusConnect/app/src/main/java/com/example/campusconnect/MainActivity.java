package com.example.campusconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button button;
    TextView test;


//    testing commit and push

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        test = (TextView) findViewById(R.id.testText);
        test.setText("testing.");

        button = findViewById(R.id.gotoEventCreation);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEventCreator();
            }
        });

    }

    public void openEventCreator() {
        Intent intent = new Intent(this, EventCreation.class);
        startActivity(intent);
    }



    }

