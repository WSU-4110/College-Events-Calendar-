package com.example.campusconnect.Event;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campusconnect.Admin.Authentication.SignIn;
import com.example.campusconnect.MainActivity;
import com.example.campusconnect.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;


public class EventCreation extends AppCompatActivity {
	
	String EventName;
	String location;
	String startTime;
	String date;
	String desc;
	String org;
	String tag;
	
	private TextView mDisplayDate;
	private DatePickerDialog.OnDateSetListener mDateSetListener;
	
	EditText EventNameInput;
	EditText locationInput;
	EditText startTimeInput;
	EditText dateInput;
	EditText descInput;
	EditText orgInput;
	Spinner tagSpinner;
	Spinner staticTagSpinner;
	Button submitButton;
	
	// !! TODO: Add an empty fields check
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_creation);
		
		setupFields();
		setupStaticSpinner();
		
		mDisplayDate = findViewById(R.id.EventDate);
		mDisplayDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setupDisplayDate();
			}
		});
		
		mDateSetListener = new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
				month = month + 1;
				String date = month + "/" + dayOfMonth + "/" + year;
				mDisplayDate.setText(date);
			}
		};
		
		submitButton = (Button) findViewById(R.id.event_submit_button);
		submitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				processEventCreation();
				
				Intent intent = new Intent(EventCreation.this, MainActivity.class);
				startActivity(intent);
			}
		});
		
		/*
		// TODO: Toolbar is crashing app
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("Event Creation");*/
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.login) {
			
			Intent intent = new Intent(this, SignIn.class);
			startActivity(intent);
		}
		else if (item.getItemId() == R.id.logout) {
			final FirebaseAuth mAuth = FirebaseAuth.getInstance();
			startActivity(new Intent(EventCreation.this, SignIn.class));
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
		return true;
	}
	
	private void setupDisplayDate() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		
		DatePickerDialog dialog = new DatePickerDialog(
				EventCreation.this,
				android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth,
				mDateSetListener,
				year, month, day);
		dialog.show();
	}
	
	private void setupFields() {
		EventNameInput = (EditText) findViewById(R.id.EventNameField);
		locationInput = (EditText) findViewById(R.id.LocationField);
		startTimeInput = (EditText) findViewById(R.id.StartTimeField);
		dateInput = (EditText) findViewById(R.id.EventDate);
		descInput = (EditText) findViewById(R.id.DescriptionField);
		orgInput = (EditText) findViewById(R.id.Organization);
		tagSpinner = (Spinner) findViewById(R.id.tags);
	}
	
	private void setupStaticSpinner() {
		// TODO: Is this or tagSpinner not needed?
		staticTagSpinner = findViewById(R.id.tags);
		
		// Create an ArrayAdapter using the string array and a default spinner
		ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
				.createFromResource(this, R.array.tags_array,
						android.R.layout.simple_spinner_item);
		
		// Specify the layout to use when the list of choices appears
		staticAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		// Apply the adapter to the spinner
		staticTagSpinner.setAdapter(staticAdapter);
	}
	
	private void processEventCreation() {
		FirebaseFirestore db = FirebaseFirestore.getInstance();
		FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
		// Event event;
		// RSVP rsvp;
		
//		EventName = EventNameInput.getText().toString();
//		location = locationInput.getText().toString();
//		startTime = startTimeInput.getText().toString();
//		date = dateInput.getText().toString();
//		desc = descInput.getText().toString();
//		org = orgInput.getText().toString();
//		tag = tagSpinner.getSelectedItem().toString();
		
		EventName = "TEST";
		location = "TEST";
		startTime = "TEST";
		date = "7/14/2020";
		desc = "TEST";
		org = "TEST";
		String tag = "(None)";
		String userID;
		
		try {
			userID = user.getUid();
		}
		catch (NullPointerException npe) {
			System.err.println("User Empty");
			userID = "NoUserID";
		}
		
		Event event = new Event(EventName, location, startTime,
				date, org, desc, userID, tag);
		final String[] ID = new String[1];
		// TODO: final RSVP rsvp;
		
		db.collection("Events")
				.add(event)
				.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
					@Override
					public void onSuccess(DocumentReference docRef) {
						String id = docRef.getId();                             // Get the auto-generated ID
						docRef.update("ID", id);                                // Go back and add the ID to the Event
						addRSVPIntoDB(id);									// Add an RSVP document to the DB
					}
				});
		
		Toast.makeText(EventCreation.this, "Event Added", Toast.LENGTH_SHORT).show();
		
	}// [ processEventCreation ]
	
	private void addRSVPIntoDB(String ID){
		FirebaseFirestore db = FirebaseFirestore.getInstance();
		RSVP rsvp = new RSVP(ID);
		
		db.collection("RSVP")
				.add(rsvp);
	}
	
}// class [ EventCreation ]