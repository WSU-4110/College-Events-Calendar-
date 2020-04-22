package com.example.campusconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.campusconnect.Event.EventCreation;
import com.example.campusconnect.Event.EventView;
import com.example.campusconnect.Event.SavedEvent;
import com.example.campusconnect.Event.Search;
import com.example.campusconnect.UI.Authentication.signIn;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity {
	private MenuItem signinout;
	private Button goto_SavedEvents;
	ListView listView;
	Calendar calendar;
	
	// Imported dependency contains its own "CalendarView" class. It is NOT the same as "android.widget.CalendarView"
	com.applandeo.materialcalendarview.CalendarView calendarView;
	
	private List<EventDay> eventList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = findViewById(R.id.toolbar_main);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("Home");
		EventCreation.isOrganizer();
		
		populateEventIndicators();
		
		goto_SavedEvents = findViewById(R.id.gotoSavedEvents);
		goto_SavedEvents.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
				
				if (user != null) {
					openSavedEvents();
				}
				else {
					Toast.makeText(MainActivity.this, "Not Logged-in", Toast.LENGTH_SHORT).show();
				}
				
			}
		});

		calendarView = findViewById(R.id.calendarView);
		calendarView.setOnDayClickListener(new OnDayClickListener() {
			@Override
			public void onDayClick(EventDay eventDay) {
				Calendar selected = eventDay.getCalendar();						// eventDay returns std java Calendar object
				int daySelected = selected.get(Calendar.DATE);					// Extract info w/ std Calendar methodology
				int monthSelected = selected.get(Calendar.MONTH);
				int yearSelected = selected.get(Calendar.YEAR);
				
				openEventView(daySelected, monthSelected, yearSelected);
			}
		});
		
	}
	
	public void openEventCreator() {
		Intent intent = new Intent(this, EventCreation.class);
		startActivity(intent);
	}
	
	public void openEventView(int day, int month, int year) {
		
		Intent intent = new Intent(this, EventView.class);
		
		String str_Day = String.valueOf(day);
		String str_Month = String.valueOf(month);
		String str_Year = String.valueOf(year);
		
		intent.putExtra("EXTRA_DaySelected", str_Day);                    // Pass info to EventView.class
		intent.putExtra("EXTRA_MonthSelected", str_Month);
		intent.putExtra("EXTRA_YearSelected", str_Year);
		
		startActivity(intent);
	}
	
	private void populateEventIndicators(){
		eventList = new ArrayList<>();
		eventList.add(new EventDay(calendar, R.drawable.event_indicator_dot));
		
		//calendarView.setEvents([ARRAY])
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
	
	
}//end [ CLASS: MainActivity ]

