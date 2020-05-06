package com.example.campusconnect;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import com.example.campusconnect.Event.Event;
import com.example.campusconnect.Event.EventCreation;
import com.example.campusconnect.Event.EventIndicator;
import com.example.campusconnect.Event.EventView;
import com.example.campusconnect.Event.SavedEvent;
import com.example.campusconnect.Event.Search;
import com.example.campusconnect.UI.Authentication.signIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;

public class MainActivity extends AppCompatActivity {
	
	// TODO: Examine view inflater in CompactCalendar sample program
	
	private MenuItem signinout;
	private Button goto_SavedEvents;
	
	CompactCalendarView calendar;
	Toolbar toolbar;
	TextView calendarTitle;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		toolbar = findViewById(R.id.toolbar_main);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("Home");
		
		calendar = findViewById(R.id.calendar);
		calendar.setFirstDayOfWeek(1);
		
		calendarTitle = findViewById(R.id.month_name);
		calendarTitle.setText(dateTitleHelper());                               // Set title AFTER calendar fully initialized
		
		goto_SavedEvents = findViewById(R.id.gotoSavedEvents);
		
		loadEvents();
		
		calendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
			@Override
			public void onDayClick(Date dateClicked) {
				openEventView(dateClicked);
			}
			
			@Override
			public void onMonthScroll(Date firstDayOfNewMonth) {
				calendarTitle.setText(dateTitleHelper());                        // Update title to match new month
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
	
	
	// TODO: Resolve "Implicitly using the default locale string format..."
	@SuppressLint("DefaultLocale")
	private String dateTitleHelper() {
		// getFirstDay...() seems like the only way to get a Date object w/ CompactCalendarView
		Date currentDate;
		Calendar cal;
		int year;
		int monthInteger;
		String[] monthName;
		
		if (calendar == null) {
			return " ";
		}
		
		monthName = new String[]{
				"January", "February", "March",
				"April", "May", "June",
				"July", "August", "September",
				"October", "November", "December"};
		
		currentDate = calendar.getFirstDayOfCurrentMonth();
		cal = Calendar.getInstance();
		cal.setTime(currentDate);
		
		year = cal.get(Calendar.YEAR);
		monthInteger = cal.get(Calendar.MONTH);
		
		// Looks cleaner without year (but print year if NOT current year)
		if(year != 2020)
			return String.format("%s, %d", monthName[monthInteger], year);
		else
			return String.format("%s", monthName[monthInteger]);
	}
	
	public void openEventView(Date dateClicked) {
		Intent intent;
		Calendar calClicked;
		
		intent = new Intent(this, EventView.class);
		calClicked = Calendar.getInstance();
		calClicked.setTime(dateClicked);
		
		String stringDay = String.valueOf(calClicked.get(Calendar.DAY_OF_MONTH));
		String stringMonth = String.valueOf(calClicked.get(Calendar.MONTH));
		String stringYear = String.valueOf(calClicked.get(Calendar.YEAR));
		
		// TODO: Look into switching to a Date object parameter vs individual Strings
		intent.putExtra("EXTRA_DaySelected", stringDay);
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
	}// [ onCreateOptionsMenu ]
	
	private void loadEvents() {
		addEventIndicators();
		
		// TODO: Look into moving the actual loading of events to an Async task
	}
	
	private void addEventIndicators() {
		FirebaseFirestore db = FirebaseFirestore.getInstance();
		
		db.collection("Events")
				.get()
				.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
					@Override
					public void onComplete(@NonNull Task<QuerySnapshot> task) {
						if (task.isSuccessful()) {
							Event event;
							long dateInMilliseconds;
							final String gold_dark = "#FFFFAD33";
							
							for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
								event = (Event) document.toObject(Event.class);
								dateInMilliseconds = event.getMillisecondsForEvent();
								
								if (event.eventPassed())
									calendar.addEvent(new EventIndicator(Color.GRAY, dateInMilliseconds));
								else if (event.eventIsToday())
									calendar.addEvent(new EventIndicator(Color.RED, dateInMilliseconds));
								else
									calendar.addEvent(new EventIndicator(gold_dark, dateInMilliseconds));
							}
						}
					}
				});
		
	}// [ addEventIndicators ]
	
}// [ MainActivity ]