package com.example.campusconnect.Event;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import com.example.campusconnect.Admin.Authentication.SignIn;
import com.example.campusconnect.MainActivity;
import com.example.campusconnect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class EventDetailedView extends AppCompatActivity {
	TextView EventNameInput;
	TextView locationInput;
	TextView startTimeInput;
	TextView dateInput;
	TextView descInput;
	TextView orgInput;
	TextView OrgUidInput;
	TextView tagInput;
	
	Toolbar toolbar;
	FirebaseFirestore db = null;
	ImageView whatsappImg;
	ImageView twitterImg;
	ImageView facebookImg;
	
	Button floating_toSavedEvents;
	Button delete;
	Button unfollow;
	Event event;
	
	// TODO: Error handling for loading Event from DB
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_detailed_view);
		db = FirebaseFirestore.getInstance();
		
		delete = (Button) findViewById(R.id.delete);
		unfollow = (Button) findViewById(R.id.unfollow);
		
		EventNameInput = findViewById(R.id.EventNameField);
		locationInput = findViewById(R.id.LocationField);
		startTimeInput = findViewById(R.id.StartTimeField);
		dateInput = findViewById(R.id.EventDate);
		descInput = findViewById(R.id.DescriptionField);
		orgInput = findViewById(R.id.Organization);
		OrgUidInput = findViewById(R.id.OrgUid);
		tagInput = findViewById(R.id.TagsField); //tbd
		
		Intent intent = getIntent();
		event = intent.getParcelableExtra("Event Parcel");
		
		try {
			// TODO: Reevaluate handling of poss. null pointer for name
			EventNameInput.setText(event.getName());
		}
		catch (NullPointerException noName) {
			String emptyName = "EventNameNotProvided";
			EventNameInput.setText(emptyName);
		}
		
		locationInput.setText(event.getLocation());
		startTimeInput.setText(event.getStartTime());
		dateInput.setText(event.getDate());
		descInput.setText(event.getDesc());
		orgInput.setText(event.getOrg());
		OrgUidInput.setText(event.getOrgUid());
		tagInput.setText(event.tag());
		
		floating_toSavedEvents = findViewById(R.id.save_event);
		whatsappImg = findViewById(R.id.whatsapp_logo);
		twitterImg = findViewById(R.id.twitter_logo);
		facebookImg = findViewById(R.id.facebook_logo);
		
		unfollow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
				
				if (true) {
					// CHECK: Feasible to use a SavedEvent derived class to link user to saved event? (1 of 2)
					db.collection("SavedEvent")
							.document("SavedEvent")
							.collection("Event_SubCollectionTesting")
							.whereEqualTo("uid", user.getUid())
							.whereEqualTo("name", EventNameInput.getText().toString())
							.whereEqualTo("location", locationInput.getText().toString())
							.whereEqualTo("startTime", startTimeInput.getText().toString())
							.whereEqualTo("date", dateInput.getText().toString())
							.whereEqualTo("desc", descInput.getText().toString())
							.whereEqualTo("org", orgInput.getText().toString())
							.whereEqualTo("tags", tagInput.getText().toString())
							.get()
							.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
								@Override
								public void onComplete(@NonNull Task<QuerySnapshot> task) {
									if (task.isSuccessful()) {
										for (QueryDocumentSnapshot document : task.getResult()) {
											String DocId = document.getId();
											db.collection("SavedEvent")
													.document("SavedEvent")
													.collection("Event_SubCollectionTesting")
													.document(DocId).delete();
										}
									}
								}
							});
					Toast.makeText(EventDetailedView.this, "Un-followed Event", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(getApplicationContext(), MainActivity.class);
					startActivity(intent);
				}
			}
		});
		
		delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				deleteEvent();
			}
		});
		
		floating_toSavedEvents.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
				
				if (user == null) {
					Toast.makeText(EventDetailedView.this, "Not Logged In", Toast.LENGTH_SHORT).show();
				}
				
				// CHECK: Feasible to extract a SavedEvent derived class to link user to saved event? (2 of 2)
				db.collection("SavedEvent")
						.document("SavedEvent")
						.collection("Event_SubCollectionTesting")
						.add(event);
				
				Toast.makeText(EventDetailedView.this, "Added to Saved Events", Toast.LENGTH_SHORT).show();
				
			}// [ onClick ]
		});
		
		// [CURRENT]: Extract eventDetail string
		whatsappImg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String eventDetail = generateDetailsString();
				shareOnWhatsapp(eventDetail);
			}
		});
		
		twitterImg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String eventDetail = generateDetailsString();
				shareOnTwitter(eventDetail);
			}
		});
		
		facebookImg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String eventDetail = generateDetailsString();
				shareOnFacebook(eventDetail);
			}
		});
		
	}// method [ onCreate: EventDetailedView ]
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.newEvent) {
			// !! CRITICAL: Enable Organizer check after testing!
			//if (EventCreation.isOrganizer()){
			if (true) {
				Intent intent = new Intent(this, EventCreation.class);
				startActivity(intent);
			}
			else {
				Toast.makeText(EventDetailedView.this, "Only Organizers Can Add Events", Toast.LENGTH_SHORT).show();
			}
		}
		
		if (item.getItemId() == R.id.login) {
			
			Intent intent = new Intent(this, SignIn.class);
			startActivity(intent);
			
		}
		else if (item.getItemId() == R.id.logout) {
			final FirebaseAuth mAuth = FirebaseAuth.getInstance();
			startActivity(new Intent(EventDetailedView.this, SignIn.class));
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
	
	
	private void deleteEvent() {
		FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
		
		// !! TODO: This needs to check if user is an organizer
		if (true) {
			db.collection("Events")
					.document("Events")
					.collection("Event_SubCollectionTesting")
					.whereEqualTo("orgUid", user.getUid())
					.whereEqualTo("name", EventNameInput.getText().toString())
					.whereEqualTo("location", locationInput.getText().toString())
					.whereEqualTo("startTime", startTimeInput.getText().toString())
					.whereEqualTo("date", dateInput.getText().toString())
					.whereEqualTo("desc", descInput.getText().toString())
					.whereEqualTo("org", orgInput.getText().toString())
					.whereEqualTo("tags", tagInput.getText().toString())
					.get()
					.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
						@Override
						public void onComplete(@NonNull Task<QuerySnapshot> task) {
							if (task.isSuccessful()) {
								for (QueryDocumentSnapshot document : task.getResult()) {
									String DocId = document.getId();
									db.collection("Events")
											.document("Events")
											.collection("Event_SubCollectionTesting")
											.document(DocId).delete();
								}
							}
						}
					});
			
			db.collection("SavedEvent")
					.document("SavedEvent")
					.collection("Event_SubCollectionTesting")
					.whereEqualTo("orgUid", user.getUid())
					.whereEqualTo("name", EventNameInput.getText().toString())
					.whereEqualTo("location", locationInput.getText().toString())
					.whereEqualTo("startTime", startTimeInput.getText().toString())
					.whereEqualTo("date", dateInput.getText().toString())
					.whereEqualTo("desc", descInput.getText().toString())
					.whereEqualTo("org", orgInput.getText().toString())
					.get()
					.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
						@Override
						public void onComplete(@NonNull Task<QuerySnapshot> task) {
							if (task.isSuccessful()) {
								for (QueryDocumentSnapshot document : task.getResult()) {
									String DocId = document.getId();
									db.collection("SavedEvent")
											.document("SavedEvent")
											.collection("Event_SubCollectionTesting")
											.document(DocId).delete();
								}
							}
						}
					});
			Intent intent = new Intent(getApplicationContext(), MainActivity.class);
			startActivity(intent);
		}
		else
			Toast.makeText(EventDetailedView.this, "Only Creator can delete", Toast.LENGTH_SHORT).show();
	
	}// [ deleteEvent ]
	
	private String generateDetailsString() {
		return "Campus Connect - Wayne State" + "\n" +
				"Event Name : " + EventNameInput.getText().toString() + "\n" +
				"Location : " + locationInput.getText().toString() + "\n" +
				"Start Time : " + startTimeInput.getText().toString() + "\n" +
				"End Time :" + dateInput.getText().toString();
	}
	
	private void shareOnWhatsapp(String eventDetail) {
		System.out.println(" Start sharing in WhatsApp");
		
		Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
		whatsappIntent.setType("text/plain");
		whatsappIntent.setPackage("com.whatsapp");
		whatsappIntent.putExtra(Intent.EXTRA_TEXT, eventDetail);
		try {
			startActivity(whatsappIntent);
		}
		catch (android.content.ActivityNotFoundException ex) {
			//ToastHelper.MakeShortText("Whatsapp has not been installed.");
		}
	}
	
	private void shareOnTwitter(String eventDetail) {
		System.out.println(" Start sharing in WhatsApp");
		
		Intent twitterIntent = new Intent(Intent.ACTION_SEND);
		twitterIntent.setType("text/plain");
		twitterIntent.setPackage("com.twitter.android");
		twitterIntent.putExtra(Intent.EXTRA_TEXT, eventDetail);
		try {
			startActivity(twitterIntent);
		}
		catch (android.content.ActivityNotFoundException ex) {
			//ToastHelper.MakeShortText("Whatsapp has not been installed.");
		}
	}
	
	private void shareOnFacebook(String eventDetail) {
		System.out.println(" Start sharing in WhatsApp");
		
		Intent facebookIntent = new Intent(Intent.ACTION_SEND);
		facebookIntent.setType("text/plain");
		facebookIntent.setPackage("com.facebook.katana");
		facebookIntent.putExtra(Intent.EXTRA_TEXT, eventDetail);
		try {
			startActivity(facebookIntent);
		}
		catch (android.content.ActivityNotFoundException ex) {
			//ToastHelper.MakeShortText("Facebook has not been installed.");
		}
	}
	

	
}// class [ EventDetailedView ]
