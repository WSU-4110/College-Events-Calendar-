package com.example.campusconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import com.example.campusconnect.Event.EventCreation;
import com.example.campusconnect.Event.EventView;
import com.example.campusconnect.Event.SavedEvent;
import com.example.campusconnect.Event.Search;
import com.example.campusconnect.UI.Authentication.signIn;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
	
	// TODO: Examine view inflater in CompactCalendar sample program
	
	private MenuItem signinout;
	private Button goto_SavedEvents;
	
	CompactCalendarView compactCalendar;
	Toolbar toolbar;
	TextView calendarTitle;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		toolbar = findViewById(R.id.toolbar_main);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("Home");
		
		calendarTitle = findViewById(R.id.month_name);
		
		compactCalendar = findViewById(R.id.calendar);
		compactCalendar.setFirstDayOfWeek(1);
		calendarTitle.setText(dateTitleHelper());								// Set title AFTER calendar fully initialized
		
		goto_SavedEvents = findViewById(R.id.gotoSavedEvents);
		
		compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
			@Override
			public void onDayClick(Date dateClicked) {
				openEventView(dateClicked);
			}
			
			@Override
			public void onMonthScroll(Date firstDayOfNewMonth) {
				calendarTitle.setText(dateTitleHelper());						// Update to match new month
			}
		});
		
		goto_SavedEvents.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
				
				// TODO: Dedicated EventView for organizers (poss. w/ extra features, e.g. editDate())
				// e.g. if (user != null && user == organizer)
				
				if (user != null) {
					openSavedEvents();
				}
				else {
					Toast.makeText(MainActivity.this, "Not Logged-in", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
	}// [ onCreate ]

//	private void checkAllViewsNotNull(){
//
//	}
	
	// If Time: Return better string for title
	private String dateTitleHelper(){
		SimpleDateFormat dateFormatter = new SimpleDateFormat("L, YYYY", Locale.US);
		
		if(compactCalendar != null)
			return dateFormatter.format(compactCalendar.getFirstDayOfCurrentMonth());
		else
			return "2020";
	}
	
	public void openEventCreator() {
		Intent intent = new Intent(this, EventCreation.class);
		startActivity(intent);
	}
	
	public void openEventView(Date dateClicked) {
		Intent intent;
		Calendar calClicked;
		
		intent = new Intent(this, EventView.class);
		calClicked = Calendar.getInstance();
		calClicked.setTime(dateClicked);
		
		String stringDay = 		String.valueOf(calClicked.get(Calendar.DAY_OF_MONTH));
		String stringMonth = 	String.valueOf(calClicked.get(Calendar.MONTH));
		String stringYear = 	String.valueOf(calClicked.get(Calendar.YEAR));
		
		intent.putExtra("EXTRA_DaySelected", stringDay);							// Attach date info we will need in EventView
		intent.putExtra("EXTRA_MonthSelected", stringMonth);
		intent.putExtra("EXTRA_YearSelected", stringYear);
		
		startActivity(intent);
	}
	
	
	public void openSavedEvents() {
		Intent intent = new Intent(this, SavedEvent.class);
		startActivity(intent);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.newEvent) {
			if (EventCreation.isOrganizer()) {
				Intent intent = new Intent(this, EventCreation.class);
				startActivity(intent);
			}
			else {
				Toast.makeText(MainActivity.this, "Only Organizers Can Add Events", Toast.LENGTH_SHORT).show();
			}
		}
		
		if (item.getItemId() == R.id.login) {
			Intent intent = new Intent(this, signIn.class);
			startActivity(intent);
			
		}
		else if (item.getItemId() == R.id.logout) {
			final FirebaseAuth mAuth = FirebaseAuth.getInstance();
			startActivity(new Intent(MainActivity.this, signIn.class));
			//FirebaseAuth.getInstance().signOut();
			mAuth.signOut();
		}
		else {
			return false;
		}
		return true;
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		final MenuItem searchItem = menu.findItem(R.id.search);
		SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
		
		searchView.setOnQueryTextListener(
				new SearchView.OnQueryTextListener() {
					@Override
					public boolean onQueryTextSubmit(String query) {
						Intent intent = new Intent(getApplicationContext(), Search.class);
						intent.putExtra("result", query);
						intent.putExtra("searchBy", "tag");
						startActivity(intent);
						return false;
					}
					
					@Override
					public boolean onQueryTextChange(String newText) {
						
						return false;
					}
				}
		
		);
		
		return true;
	}
	
	
}// [ MainActivity ]

