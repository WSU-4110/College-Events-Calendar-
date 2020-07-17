package com.example.campusconnect.Event;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import com.example.campusconnect.Admin.Authentication.SignIn;
import com.example.campusconnect.Admin.OrganizerHelper;
import com.example.campusconnect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
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
	ImageView whatsapp_button;
	ImageView twitter_button;
	ImageView facebook_button;
	
	Button saveEvent_button;
	Button deleteEvent_button;
	Button RSVP_button;
	Button unfollow_button;
	static Event event;
	
	FirebaseFirestore db = FirebaseFirestore.getInstance();
	
	// TODO: Error handling for loading Event from DB
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_detailed_view);
		
		Intent intent = getIntent();
		event = intent.getParcelableExtra("Event Parcel");
		
		setupTextFields();
		setupButtons();
		
		unfollow_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				unfollowEvent();
			}
		});
		
		deleteEvent_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				deleteEvent();
			}
		});
		
		RSVP_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent;
				checkRSVPEligible();
				
				intent = new Intent(getApplicationContext(), RSVPView.class);
				intent.putExtra("eventID", event.getID());
				startActivity(intent);
			}
		});
		
		saveEvent_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				saveEvent();
			}
		});
		
		whatsapp_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String eventDetail = generateDetailsString();
				shareOnWhatsapp(eventDetail);
			}
		});
		
		twitter_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String eventDetail = generateDetailsString();
				shareOnTwitter(eventDetail);
			}
		});
		
		facebook_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String eventDetail = generateDetailsString();
				shareOnFacebook(eventDetail);
			}
		});
		
	}// [ onCreate ]
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemID = item.getItemId();
		
		switch (itemID) {
			case (R.id.newEvent):
				if (OrganizerHelper.isOrganizer()) {
					Intent intent = new Intent(this, EventCreation.class);
					startActivity(intent);
				}
				else {
					Toast.makeText(EventDetailedView.this, "Only Organizers Can Add Events", Toast.LENGTH_SHORT).show();
				}
				break;
			
			case (R.id.login):
				Intent intent = new Intent(this, SignIn.class);
				startActivity(intent);
				break;
			
			case (R.id.logout):
				final FirebaseAuth mAuth = FirebaseAuth.getInstance();
				startActivity(new Intent(EventDetailedView.this, SignIn.class));
				//FirebaseAuth.getInstance().signOut();
				mAuth.signOut();
				break;
			
			default:
				return false;
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
	
	private void setupTextFields() {
		EventNameInput = findViewById(R.id.EventNameField);
		locationInput = findViewById(R.id.LocationField);
		startTimeInput = findViewById(R.id.StartTimeField);
		dateInput = findViewById(R.id.EventDate);
		descInput = findViewById(R.id.DescriptionField);
		orgInput = findViewById(R.id.Organization);
		OrgUidInput = findViewById(R.id.OrgUid);
		tagInput = findViewById(R.id.TagsField);
		
		EventNameInput.setText(event.getName());
		locationInput.setText(event.getLocation());
		startTimeInput.setText(event.getStartTime());
		dateInput.setText(event.getDate());
		descInput.setText(event.getDesc());
		orgInput.setText(event.getOrg());
		OrgUidInput.setText(event.getOrgUid());
		tagInput.setText(event.tag());
	}// [ setupTextFields ]
	
	private void setupButtons() {
		deleteEvent_button = findViewById(R.id.delete);
		unfollow_button = findViewById(R.id.unfollow);
		saveEvent_button = findViewById(R.id.save_event);
		RSVP_button = findViewById(R.id.RSVP);
		whatsapp_button = findViewById(R.id.whatsapp_logo);
		twitter_button = findViewById(R.id.twitter_logo);
		facebook_button = findViewById(R.id.facebook_logo);
	}// [ setupButtons ]
	
	private void unfollowEvent() {
		FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
		
		if (user == null) {
			Toast.makeText(EventDetailedView.this, "Not Logged In", Toast.LENGTH_SHORT).show();
			return;
		}
		
		// CHECK: Feasible to use a SavedEvent derived class to link user to saved event? (1 of 2)
		db.collection("SavedEvent")
				.document("SavedEvent")
				.collection("Event_SubCollectionTesting")
				.whereEqualTo("uid", user.getUid())
				.whereEqualTo("name", event.getName())
				.whereEqualTo("location", event.getLocation())
				.whereEqualTo("startTime", event.getStartTime())
				.whereEqualTo("date", event.getDate())
				.whereEqualTo("desc", event.getDesc())
				.whereEqualTo("org", event.getOrg())
				.whereEqualTo("tags", event.tag())
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
		
		Toast.makeText(EventDetailedView.this, "Unfollowed Event", Toast.LENGTH_SHORT).show();
		
	}// [ unfollowEvent ]
	
	private void deleteEvent() {
		FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
		
		if (!OrganizerHelper.isOrganizer()) {
			// !! TODO: Is this THE organizer that created the event? (make new method in OrganizerHelper)
			Toast.makeText(EventDetailedView.this, "Only Creator can delete", Toast.LENGTH_SHORT).show();
			return;
		}
		else if (user == null) {
			Toast.makeText(EventDetailedView.this, "Not Logged In", Toast.LENGTH_SHORT).show();
			return;
		}
		
		db.collection("Events")
				.document("Events")
				.collection("Event_SubCollectionTesting")
				.whereEqualTo("orgUid", user.getUid())
				.whereEqualTo("name", event.getName())
				.whereEqualTo("location", event.getLocation())
				.whereEqualTo("startTime", event.getStartTime())
				.whereEqualTo("date", event.getDate())
				.whereEqualTo("desc", event.getDesc())
				.whereEqualTo("org", event.getOrg())
				.whereEqualTo("tags", event.tag())
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
				.whereEqualTo("name", event.getName())
				.whereEqualTo("location", event.getLocation())
				.whereEqualTo("startTime", event.getStartTime())
				.whereEqualTo("date", event.getDate())
				.whereEqualTo("desc", event.getDesc())
				.whereEqualTo("org", event.getOrg())
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
		
	}// [ deleteEvent ]
	
	private void checkRSVPEligible() {
		FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
		
		if (user == null) {
			Toast.makeText(EventDetailedView.this, "Login to RSVP", Toast.LENGTH_SHORT).show();
		}
		else if (OrganizerHelper.isOrganizer()) {
			Toast.makeText(EventDetailedView.this, "Logged-in as Organizer", Toast.LENGTH_SHORT).show();
		}
		else {
			RSVPToEvent();
		}
		
	}// [ checkRSVPEligible ]
	
	private void RSVPToEvent(){
		FirebaseFirestore db = FirebaseFirestore.getInstance();
		FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
		String eventID = event.getID();
		String userID = user.getUid();
		
		db.collection("RSVP")
				.document(eventID)
				.update("usersWhoAreRSVP", FieldValue.arrayUnion(userID));
		
		Toast.makeText(EventDetailedView.this, "[TESTING] RSVP Clicked", Toast.LENGTH_SHORT).show();
	}// [ RSVPToEvent ]
	
	private void saveEvent() {
		FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
		
		if (user == null) {
			Toast.makeText(EventDetailedView.this, "Not Logged In", Toast.LENGTH_SHORT).show();
			return;
		}
		
		// CHECK: Feasible to extract a SavedEvent derived class to link user to saved event? (2 of 2)
		db.collection("SavedEvent")
				.document("SavedEvent")
				.collection("Event_SubCollectionTesting")
				.add(event);
		
		Toast.makeText(EventDetailedView.this, "Added to Saved Events", Toast.LENGTH_SHORT).show();
	}
	
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
