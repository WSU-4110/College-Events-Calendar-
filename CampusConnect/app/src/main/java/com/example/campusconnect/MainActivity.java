package com.example.campusconnect;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.google.firebase.auth.*;




import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campusconnect.UI.Authentication.signIn;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
	//private Button button;
	private MenuItem signinout;
	private Button goto_calendar;
	private Button goto_signin;
	//private Button goto_eventView;
	private Button goto_SavedEvents;
	CalendarView calendar;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = findViewById(R.id.toolbar_main);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("Home");
		//toolbar.setTitleTextAppearance();
		/* This code displays our icon in the toolbar, but i'm not a huge fan of the design, check it out
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setLogo(R.mipmap.ic_launcher);
		getSupportActionBar().setDisplayUseLogoEnabled(true);
		*/

/*
		//logout button
		final FirebaseAuth mAuth=FirebaseAuth.getInstance();
		Button btn_logout=findViewById(R.id.logout_button);
		btn_logout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				startActivity(new Intent(MainActivity.this, signIn.class));
				mAuth.signOut();
			}
		});
*/

		/* after adding the toolbar, we dont need this
		button = findViewById(R.id.gotoEventCreation);								// [BUTTON ACTION]: Event Creation Page
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openEventCreator();
			}
		});
*/
		/*goto_eventView = findViewById(R.id.gotoEventView);						// [BUTTON ACTION]: Event View Page
		goto_eventView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openEventView();
			}
		});*/

		goto_SavedEvents = findViewById(R.id.gotoSavedEvents);						// [BUTTON ACTION]: Saved Events Page
		goto_SavedEvents.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openSavedEvents();
			}
		});
		
		calendar = findViewById(R.id.calendarView);
		calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
			@Override
			public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
				
				EventView eventView = new EventView();
				StringBuilder dayMonthYear = new StringBuilder();
				
				if ((month + 1) < 10) {
					dayMonthYear.append('0');
					dayMonthYear.append(month + 1);
				} else
					dayMonthYear.append(month + 1);
				
				if (day < 10) {
					dayMonthYear.append('0');
					dayMonthYear.append(day);
				} else
					dayMonthYear.append(day);
				
				dayMonthYear.append(year);
				
				openEventView(dayMonthYear.toString());
			}
		});

	}
	
	public void openEventCreator() {
		Intent intent = new Intent(this, EventCreation.class);
		startActivity(intent);
	}

	// CHECK: Determine whether name change from EventView to DayView is good idea
	public void openEventView(String dayMonthYear) {
		// .putExtra() allows data transfer between Activities
		// e.g. we can send the String dayMonthYear to EventView
		
		Intent intent = new Intent(this, EventView.class);
		intent.putExtra("EXTRA_DAY_SELECTED", dayMonthYear);
		startActivity(intent);
	}

	public void openSavedEvents() {
		Intent intent = new Intent(this, SavedEvents.class);
		startActivity(intent);
	}


	@Override public boolean onOptionsItemSelected(MenuItem item){
		if(item.getItemId() == R.id.newEvent){
			Intent intent = new Intent(this, EventCreation.class);
			startActivity(intent);
		}
		else if(item.getItemId() == R.id.Option1)
		{
			// need to add what it will do when selected -T
		}
		else if(item.getItemId()==R.id.login)
		{

				Intent intent = new Intent(this, signIn.class);
				startActivity(intent);

		}
		else if(item.getItemId()==R.id.logout){
			final FirebaseAuth mAuth=FirebaseAuth.getInstance();
			startActivity(new Intent(MainActivity.this, signIn.class));
			//FirebaseAuth.getInstance().signOut();
			mAuth.signOut();
		}
		else
		{
			return false;
		}
		return true;
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);



		return true;
	}

}//end [ CLASS: MainActivity ]

