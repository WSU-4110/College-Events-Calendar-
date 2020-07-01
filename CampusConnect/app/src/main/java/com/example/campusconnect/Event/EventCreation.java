package com.example.campusconnect.Event;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.campusconnect.MainActivity;
import com.example.campusconnect.R;
import com.example.campusconnect.UI.Authentication.SignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;


public class EventCreation extends AppCompatActivity {
	
	String EventName;
	String location;
	String startTime;
	String date;
	String desc;
	String org;
	
	private static final String TAG = "Event";
	
	private TextView mDisplayDate;
	private TextView mDisplayTime;
	private DatePickerDialog.OnDateSetListener mDateSetListener;
	private TimePickerDialog.OnTimeSetListener mTimeSetListener;
	
	EditText EventNameInput;
	EditText locationInput;
	EditText startTimeInput;
	EditText dateInput;
	EditText descInput;
	EditText orgInput;
	
	Button submitButton;
	private static boolean organizer = false;
	private static boolean databasePreviouslyChecked = false;                    // TODO: Reset upon login/logout
	
	public static boolean isOrganizer() {
		FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
		
		if (user == null) {
			// Not logged in
			return false;
		}
		else if (databasePreviouslyChecked) {
			// Database query was already done previously
			return organizer;
		}
		else {
			// Query the database
			updateUserType();
			return organizer;
		}
		
	}// [ isOrganizer ]
	
	
	private static void updateUserType() {
		FirebaseFirestore db = FirebaseFirestore.getInstance();
		FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
		
		if (user == null) {
			organizer = false;
			return;
		}
		
		db.collection("Users")
				.document("Organizers")
				.collection("FirebaseID")
				.whereEqualTo("id", user.getUid())
				.get()
				.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
					@Override
					public void onComplete(@NonNull Task<QuerySnapshot> task) {
						// TODO: Handle isEmpty() poss. NPE
						if (task.isSuccessful()) {
							organizer = true;
							System.out.println("\n\n\nOrganizer found\n\n\n");
						}
						else {
							organizer = false;
						}
						
						databasePreviouslyChecked = true;
					}
				});
		
	}// [ updateUserType ]
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_creation);
		
		mDisplayDate = findViewById(R.id.EventDate);
		
		mDisplayDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
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
		});
		
		mDateSetListener = new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
				//Log.d(TAG, "onDateSet: date: " + month + "/" + dayOfMonth + "/" + year);
				month = month + 1;
				String date = month + "/" + dayOfMonth + "/" + year;
				mDisplayDate.setText(date);
			}
		};
		
		Spinner staticSpinner = findViewById(R.id.tags);
		
		// Create an ArrayAdapter using the string array and a default spinner
		ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
				.createFromResource(this, R.array.tags_array,
						android.R.layout.simple_spinner_item);
		
		// Specify the layout to use when the list of choices appears
		staticAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		// Apply the adapter to the spinner
		staticSpinner.setAdapter(staticAdapter);
		
		submitButton = (Button) findViewById(R.id.event_submit_button);
		submitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FirebaseFirestore db = FirebaseFirestore.getInstance();
				
				EventNameInput = (EditText) findViewById(R.id.EventName);
				locationInput = (EditText) findViewById(R.id.Location);
				startTimeInput = (EditText) findViewById(R.id.Time);
				dateInput = (EditText) findViewById(R.id.EventDate);
				descInput = (EditText) findViewById(R.id.Description);
				orgInput = (EditText) findViewById(R.id.Organization);
				Spinner spinner = (Spinner) findViewById(R.id.tags);
				
				
				EventName = EventNameInput.getText().toString();
				location = locationInput.getText().toString();
				startTime = startTimeInput.getText().toString();
				date = dateInput.getText().toString();
				desc = descInput.getText().toString();
				org = orgInput.getText().toString();
				String tag = spinner.getSelectedItem().toString();
                /*ArrayList<String> tagArray = new ArrayList<String>();
                tagArray.add(tag);
                String[] tagsArr  = new String[tagArray.size()];
                tagsArr = tagArray.toArray(tagsArr);*/
				
				FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
				Event event = new Event(null, EventName, location, startTime,
						date, org, desc, user.getUid(), tag);
				
				// Manual override for organizer account check
				// J uGYNHPfyvwX9ksH2IytA
				// D RhsB4qyI0pE3jOYOZhJv
				//Event event = new Event(null, EventName, location, startTime,
				//        date, org, desc, "RhsB4qyI0pE3jOYOZhJv",tag);
				//                                                              -Jay
				
				db.collection("Events")
						.add(event);
				
				Toast.makeText(EventCreation.this, "Event Added", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(EventCreation.this, MainActivity.class);
				startActivity(intent);
			}
		});
		
		/*
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("Event Creation");*/
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
//      if(item.getItemId() == R.id.newEvent){}
		
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
	
}// end [ CLASS: EventCreation ]






