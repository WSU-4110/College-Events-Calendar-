// Documentation on ListView and Custom Adapters:
// https://bit.ly/38ah7WV
// Windows: [Ctrl + Click]
// Mac:     [⌘ + Click]

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

import com.example.campusconnect.MainActivity;
import com.example.campusconnect.R;
import com.example.campusconnect.UI.Authentication.signIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.CollectionReference;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;
import java.util.StringTokenizer;

public class EventDetailedView extends AppCompatActivity {
    String EventName, location, startTime, date;

    TextView EventNameInput;
    TextView locationInput;
    TextView startTimeInput;
    TextView dateInput;
    TextView descInput;
    TextView orgInput;
    TextView OrgUidInput;
    TextView tagInput;

    FirebaseFirestore db = null;
    FloatingActionButton floating_toSavedEvents;
    ImageView whatsappImg;
    ImageView twitterImg;
    ImageView facebookImg;

    private Button delete;
    private Button unfollow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detailed_view);

        db = FirebaseFirestore.getInstance();
        delete = (Button) findViewById(R.id.delete);
        unfollow = (Button) findViewById(R.id.unfollow);


        String eventStr = getIntent().getStringExtra("Event");

        StringTokenizer st2 = new StringTokenizer(eventStr, "|");
        final String name = (String)st2.nextElement();
        final String location = (String)st2.nextElement();
        final String startTime = (String)st2.nextElement();
        final String date = (String)st2.nextElement();
        final String desc = (String)st2.nextElement();
        final String org = (String)st2.nextElement();
        final String OrgUid = (String)st2.nextElement();
        final String tag = (String)st2.nextElement();

        validateDate(date);
        validateStartTime(startTime);
        checkNameEmpty(name);
        checkLocationValid(location);

        //Toast.makeText(EventDetailedView.this, "Ouid"+OrgUid, Toast.LENGTH_SHORT).show();

        Event event = new Event(null, name,location,startTime, date, desc, org, OrgUid, tag);

        EventNameInput = findViewById(R.id.EventName);
        locationInput =  findViewById(R.id.Location);
        startTimeInput = findViewById(R.id.Time);
        dateInput =  findViewById(R.id.EventDate);
        descInput = findViewById(R.id.Description);
        orgInput = findViewById(R.id.Organization);
        OrgUidInput = findViewById(R.id.OrgUid);
        tagInput = findViewById(R.id.Tags); //tbd


        EventNameInput.setText(event.getName());
        locationInput.setText(event.getLocation());
        startTimeInput.setText(event.getStartTime());
        dateInput.setText(event.getDate());
        descInput.setText(event.getDesc());
        orgInput.setText(event.getOrg());
        OrgUidInput.setText(event.getOrgUid());
        tagInput.setText(event.getTags());

        //Toast.makeText(EventDetailedView.this, "Ouid"+OrgUidInput.getText().toString(), Toast.LENGTH_SHORT).show();

        floating_toSavedEvents = findViewById(R.id.floating_back_button);
        whatsappImg = findViewById(R.id.whatsapplogo);
        twitterImg = findViewById(R.id.twitterlogo);
        facebookImg = findViewById(R.id.fblogo);

        unfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (true) {

                    db.collection("SavedEvent")
                            .document("SavedEvent")
                            .collection("Event_SubCollectionTesting")
                            .whereEqualTo("uid", user.getUid())
                            .whereEqualTo("name", EventNameInput.getText().toString())
                            .whereEqualTo("location",locationInput.getText().toString())
                            .whereEqualTo("startTime", startTimeInput.getText().toString())
                            .whereEqualTo("date", dateInput.getText().toString())
                            .whereEqualTo("desc", descInput.getText().toString())
                            .whereEqualTo("org",orgInput.getText().toString())
                            .whereEqualTo("tags",tagInput.getText().toString())
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
            }});

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (true) {
                    db.collection("Events")
                            .document("Events")
                            .collection("Event_SubCollectionTesting")
                            .whereEqualTo("orgUid", user.getUid())
                            .whereEqualTo("name", EventNameInput.getText().toString())
                            .whereEqualTo("location",locationInput.getText().toString())
                            .whereEqualTo("startTime", startTimeInput.getText().toString())
                            .whereEqualTo("date", dateInput.getText().toString())
                            .whereEqualTo("desc", descInput.getText().toString())
                            .whereEqualTo("org",orgInput.getText().toString())
                            .whereEqualTo("tags",tagInput.getText().toString())
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
                            .whereEqualTo("location",locationInput.getText().toString())
                            .whereEqualTo("startTime", startTimeInput.getText().toString())
                            .whereEqualTo("date", dateInput.getText().toString())
                            .whereEqualTo("desc", descInput.getText().toString())
                            .whereEqualTo("org",orgInput.getText().toString())
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
                } else
                    Toast.makeText(EventDetailedView.this, "Only Creator can delete", Toast.LENGTH_SHORT).show();
            }});


        whatsappImg.setOnClickListener(new View.OnClickListener() {
             @Override
               public void onClick(View view) {
                 String eventDetail =  "Campus Connect - Wayne State" + "\n" +
                         "Event Name : " + EventNameInput.getText().toString() + "\n" +
                         "Location : " + locationInput.getText().toString()+ "\n" +
                         "Start Time : " + startTimeInput.getText().toString() + "\n" +
                         "End Time :" + dateInput.getText().toString();
                    shareOnWhatsapp( eventDetail );
               }
            });


        twitterImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eventDetail =  "Campus Connect - Wayne State" + "\n" +
                        "Event Name : " + EventNameInput.getText().toString() + "\n" +
                        "Location : " + locationInput.getText().toString()+ "\n" +
                        "Start Time : " + startTimeInput.getText().toString() + "\n" +
                        "End Time :" + dateInput.getText().toString();
                shareOnTwitter( eventDetail );
            }
        });

        facebookImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eventDetail =  "Campus Connect - Wayne State" + "\n" +
                        "Event Name : " + EventNameInput.getText().toString() + "\n" +
                        "Location : " + locationInput.getText().toString()+ "\n" +
                        "Start Time : " + startTimeInput.getText().toString() + "\n" +
                        "End Time :" + dateInput.getText().toString();
                shareOnFacebook( eventDetail );
            }
        });




        floating_toSavedEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

               // Toast.makeText(EventDetailedView.this, "Logged in: " + user, Toast.LENGTH_SHORT).show();
               //Event eventSaved = new Event(displayName, name,location,startTime, date, desc, org);

                if (user != null){
                    String displayName = user.getUid();
                    Event eventSaved = new Event(displayName, EventNameInput.getText().toString(),locationInput.getText().toString(),
                            startTimeInput.getText().toString(), dateInput.getText().toString(),
                            descInput.getText().toString(), orgInput.getText().toString(), OrgUidInput.getText().toString(), tagInput.getText().toString());
                    Toast.makeText(EventDetailedView.this, "Adding to Saved Events", Toast.LENGTH_SHORT).show();

//                SavedEvent sEvent = new SavedEvent(displayName, EventNameInput.getText().toString(),locationInput.getText().toString(),
//                        startTimeInput.getText().toString(), dateInput.getText().toString(),
//                        descInput.getText().toString(), orgInput.getText().toString());

                    db.collection("SavedEvent")
                            .document("SavedEvent")
                            .collection("Event_SubCollectionTesting")
                            .add(eventSaved);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                //intent.putExtra("SavedEvent", sEvent.toString());
                startActivity(intent);





            }else {Toast.makeText(EventDetailedView.this, "Not Logged-in", Toast.LENGTH_SHORT).show();}

            }
            });

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Event Details");
            }

    public boolean checkLocationValid(String location) {
        if(location == null || location.isEmpty()  )
        {
            System.out.println("Name  is empty.");
            return false;
        }
        return true;


    }

    public boolean checkNameEmpty(String name) {

        if(name == null || name.isEmpty())
        {
            System.out.println("Name  is empty.");
            return false;
        }
        return true;
    }

    public boolean validateStartTime(String startTime) {
        boolean flag = false;

        try {
            int startT = Integer.parseInt(startTime);
            System.out.println(startT);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return(true);
    }

    public boolean validateDate(String date) {
        DateFormat sdf = new SimpleDateFormat(date);
        sdf.setLenient(false);
        try {
            sdf.parse(date);
            System.out.println( " ~~~~~~~~ "+ sdf.parse(date));
        } catch (ParseException e) {
            return false;
        }
        return true;
    }


    // Share on WhatsApp
    public void shareOnWhatsapp(String eventDetail) {
        System.out.println(" Start sharing in WhatsApp");

        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, eventDetail);
        try {
            startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            //ToastHelper.MakeShortText("Whatsapp has not been installed.");
        }
    }


    // Share on Twitter
    public void shareOnTwitter(String eventDetail) {
        System.out.println(" Start sharing in WhatsApp");

        Intent twitterIntent = new Intent(Intent.ACTION_SEND);
        twitterIntent.setType("text/plain");
        twitterIntent.setPackage("com.twitter.android");
        twitterIntent.putExtra(Intent.EXTRA_TEXT, eventDetail);
        try {
            startActivity(twitterIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            //ToastHelper.MakeShortText("Whatsapp has not been installed.");
        }
    }

    // Share on Facebook
    public void shareOnFacebook(String eventDetail) {
        System.out.println(" Start sharing in WhatsApp");

        Intent facebookIntent = new Intent(Intent.ACTION_SEND);
        facebookIntent.setType("text/plain");
        facebookIntent.setPackage("com.facebook.katana");
        facebookIntent.putExtra(Intent.EXTRA_TEXT, eventDetail);
        try {
            startActivity(facebookIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            //ToastHelper.MakeShortText("Facebook has not been installed.");
        }
    }


    // Back Button
    private void backTo_main() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.newEvent){
            if (EventCreation.isOrganizer()){
                Intent intent = new Intent(this, EventCreation.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(EventDetailedView.this, "Only Organizers Can Add Events", Toast.LENGTH_SHORT).show();
            }
        }

        if(item.getItemId()==R.id.login)
        {

            Intent intent = new Intent(this, signIn.class);
            startActivity(intent);

        }
        else if(item.getItemId()==R.id.logout){
            final FirebaseAuth mAuth=FirebaseAuth.getInstance();
            startActivity(new Intent(EventDetailedView.this, signIn.class));
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

}
