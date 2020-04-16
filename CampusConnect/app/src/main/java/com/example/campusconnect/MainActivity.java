package com.example.campusconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.SearchManager;
import android.widget.ListView;
import android.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.view.MenuItemCompat;
import com.example.campusconnect.Event.*;
import com.example.campusconnect.Event.EventCreation;
import com.example.campusconnect.UI.Authentication.signIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

import static java.lang.Integer.valueOf;

public class MainActivity extends AppCompatActivity {
	private MenuItem signinout;
	private Button goto_SavedEvents;
	CalendarView calendar;
	ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = findViewById(R.id.toolbar_main);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("Home");




		goto_SavedEvents = findViewById(R.id.gotoSavedEvents);
		goto_SavedEvents.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

				if (user != null) {

					openSavedEvents();
				}
				else {
					Toast.makeText(MainActivity.this, "Not Logged-in", Toast.LENGTH_SHORT).show();}

			}
		});

		calendar = findViewById(R.id.calendarView);
		calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
			@Override
			public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
				openEventView(day, month, year);
			}
		});
	}


	// *NOTE: putExtra() in its current usage require Strings. Will change to int in the future [-Jay]
	public void openEventView(int day, int month, int year) {

		Intent intent = new Intent(this, EventView.class);

		String str_Day 		= String.valueOf(day);
		String str_Month 	= String.valueOf(month);
		String str_Year 	= String.valueOf(year);

		intent.putExtra("EXTRA_DaySelected", str_Day);					// Attach date info we will need in EventView
		intent.putExtra("EXTRA_MonthSelected", str_Month);
		intent.putExtra("EXTRA_YearSelected", str_Year);

		startActivity(intent);
	}


	public void openSavedEvents() {
			Intent intent = new Intent(this, SavedEvent.class);
			startActivity(intent);
	}


	@Override public boolean onOptionsItemSelected(MenuItem item){
		if(item.getItemId() == R.id.newEvent){
			Intent intent = new Intent(this, EventCreation.class);
			startActivity(intent);
		}

		if(item.getItemId()==R.id.login)
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
		final MenuItem searchItem = menu.findItem(R.id.search);
		SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

		searchView.setOnQueryTextListener(
				new SearchView.OnQueryTextListener() {
					@Override
					public boolean onQueryTextSubmit(String query) {
						Intent intent= new Intent(getApplicationContext(), Search.class);
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

