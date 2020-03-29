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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.campusconnect.MainActivity;
import com.example.campusconnect.R;
import com.example.campusconnect.UI.Authentication.signIn;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.StringTokenizer;

public class EventDetailedView extends AppCompatActivity {
    String EventName, location, startTime, date;

    TextView EventNameInput;
    TextView locationInput;
    TextView startTimeInput;
    TextView dateInput;
    TextView descInput;
    TextView orgInput;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FloatingActionButton floating_toSavedEvents;
    ImageView whatsappImg;
    ImageView twitterImg;
    ImageView facebookImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detailed_view);

        String eventStr = getIntent().getStringExtra("Event");

        StringTokenizer st2 = new StringTokenizer(eventStr, "|");
        String name = (String)st2.nextElement();
        String location = (String)st2.nextElement();
        String startTime = (String)st2.nextElement();
        String date = (String)st2.nextElement();
        String desc = (String)st2.nextElement();
        String org = (String)st2.nextElement();



       Event event = new Event(name,location,startTime, date, desc, org);

        EventNameInput = findViewById(R.id.EventName);
        locationInput =  findViewById(R.id.Location);
        startTimeInput = findViewById(R.id.Time);
        dateInput =  findViewById(R.id.EventDate);
        descInput = findViewById(R.id.Description);
        orgInput = findViewById(R.id.Organization);

        EventNameInput.setText(event.getName());
        locationInput.setText(event.getLocation());
        startTimeInput.setText(event.getStartTime());
        dateInput.setText(event.getDate());
        descInput.setText(event.getDesc());
        orgInput.setText(event.getOrg());

        floating_toSavedEvents = findViewById(R.id.floating_back_button);
        whatsappImg = findViewById(R.id.whatsapplogo);
        twitterImg = findViewById(R.id.twitterlogo);
        facebookImg = findViewById(R.id.fblogo);

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
                            descInput.getText().toString(), orgInput.getText().toString());
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


    // Share on WhatsApp
    private void shareOnWhatsapp(String eventDetail) {
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
    private void shareOnTwitter(String eventDetail) {
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
    private void shareOnFacebook(String eventDetail) {
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

        return true;
    }

}