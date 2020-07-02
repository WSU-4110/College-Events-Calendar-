package com.example.campusconnect;

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
import com.example.campusconnect.Event.SavedEventView;
import com.example.campusconnect.Event.Search;
import com.example.campusconnect.UI.Authentication.SignIn;
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
	
	private MenuItem signinout;
	private Button buttonSavedEvents;
	private Button buttonToday;
	
	CompactCalendarView calendar;
	Toolbar toolbar;
	TextView monthTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		toolbar = findViewById(R.id.toolbar);
		calendar = findViewById(R.id.calendar);
		monthTitle = findViewById(R.id.month_name);
		buttonSavedEvents = findViewById(R.id.gotoSavedEvents);
		buttonToday = findViewById(R.id.goToToday);
		
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("Home");
		
		calendar.setFirstDayOfWeek(1);                                          // Set Saturday as first day of week
		monthTitle.setText(dateTitleHelper());
		
		populateEventIndicators();
		
		calendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
			@Override
			public void onDayClick(Date dateClicked) {
				openEventView(dateClicked);
			}
			
			@Override
			public void onMonthScroll(Date firstDayOfNewMonth) {
				// Update month name when user scrolls
				monthTitle.setText(dateTitleHelper());
			}
		});

		buttonSavedEvents.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openSavedEvents();
			}
		});
		
		buttonToday.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				calendar.showCalendar();                                        // Go to "today" on calendar
			}
		});
		
	}
	
	
	// TODO: Resolve Locale issue
	private String dateTitleHelper() {
		Date currentDate;
		Calendar cal;
		String monthName;
		int monthInteger;
		int year;
		
		if (calendar == null) {
			return " ";
		}
		
		currentDate = calendar.getFirstDayOfCurrentMonth();
		cal = Calendar.getInstance();
		cal.setTime(currentDate);
		
		year = cal.get(Calendar.YEAR);
		monthInteger = cal.get(Calendar.MONTH);
		monthName = monthName(monthInteger);
		
		// Only print year if not 2020
		if (year != 2020)
			return String.format("%s, %d", monthName, year);
		else
			return String.format("%s", monthName);
		
	}// [ dateTitleHelper ]
	
	
	private String monthName(int monthNumber){
		String[] monthName = {
				"January", "February", "March",
				"April", "May", "June",
				"July", "August", "September",
				"October", "November", "December"};
		
		return monthName[monthNumber];
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
		
		intent.putExtra("day", stringDay);
		intent.putExtra("month", stringMonth);
		intent.putExtra("year", stringYear);
		
		startActivity(intent);
	}
	
	
	public void openSavedEvents() {
		FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
		
		if (user != null) {
			Intent intent = new Intent(this, SavedEventView.class);
			startActivity(intent);
		}
		else {
			Toast.makeText(MainActivity.this, "Not Logged-in", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		int itemID = item.getItemId();
		
		switch (itemID) {
			default:
				return false;
			
			case R.id.newEvent:
				if (EventCreation.isOrganizer()) {
					intent = new Intent(this, EventCreation.class);
					startActivity(intent);
				}
				else {
					Toast.makeText(MainActivity.this, "Only Organizers Can Add Events", Toast.LENGTH_SHORT).show();
				}
				break;
			
			case R.id.login:
				intent = new Intent(this, SignIn.class);
				startActivity(intent);
				break;
			
			case R.id.logout:
				final FirebaseAuth mAuth = FirebaseAuth.getInstance();
				startActivity(new Intent(MainActivity.this, SignIn.class));
				mAuth.signOut();
				break;
		}// switch
		
		return true;
		
	}// [ onOptionsItemSelected ]
	
	
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
	
	
	private void populateEventIndicators() {
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
	}// [ populateEventIndicators ]
	
	
}// class [ MainActivity ]