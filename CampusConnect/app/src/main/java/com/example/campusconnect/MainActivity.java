package com.example.campusconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
	private Button button;
	private Button goto_calendar;
	private Button goto_signin;
	
	TextView test; // delete later


//    testing commit and push
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		test = (TextView) findViewById(R.id.testText);                       // delete later
		test.setText("testing.");                                           // delete later
		
		button = findViewById(R.id.gotoEventCreation);						// [BUTTON ACTION]: Event Creation Page
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openEventCreator();
			}
		});

        goto_signin = findViewById(R.id.gotoSignIn);						// [BUTTON ACTION]: Sign In Page
        goto_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignIn();
            }
        });

		goto_calendar = findViewById(R.id.goto_calendar);					// [BUTTON ACTION]: Calendar View (Month)
		goto_calendar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				open_View_Calendar();
			}
		});
	}
	
	public void openEventCreator() {
		Intent intent = new Intent(this, EventCreation.class);
		startActivity(intent);
	}

	public void open_View_Calendar() {
		Intent intent = new Intent(this, View_Calendar.class);
		startActivity(intent);
	}

    public void openSignIn() {
        Intent intent = new Intent(this, UserSignIn.class);
        startActivity(intent);
    }
	
}//end [ CLASS: MainActivity ]

