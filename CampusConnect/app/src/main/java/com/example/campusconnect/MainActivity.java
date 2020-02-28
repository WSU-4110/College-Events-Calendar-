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
	private Button goto_eventView;
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		button = findViewById(R.id.gotoEventCreation);						// [BUTTON ACTION]: Event Creation Page
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openEventCreator();
			}
		});

		goto_eventView = findViewById(R.id.gotoEventView);						// [BUTTON ACTION]: Event View Page
		goto_eventView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openEventView();
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

	public void openEventView() {
		Intent intent = new Intent(this, EventView.class);
		startActivity(intent);
	}

	public void open_View_Calendar() {
		Intent intent = new Intent(this, View_Calendar.class);
		startActivity(intent);
	}


	
}//end [ CLASS: MainActivity ]

