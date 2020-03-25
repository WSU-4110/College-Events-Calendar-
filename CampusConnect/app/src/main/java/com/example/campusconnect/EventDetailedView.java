// Documentation on ListView and Custom Adapters:
// https://bit.ly/38ah7WV
// Windows: [Ctrl + Click]
// Mac:     [⌘ + Click]

package com.example.campusconnect;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;
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

        whatsappImg.setOnClickListener(new View.OnClickListener() {
             @Override
               public void onClick(View view) {
                    shareOnWhatsapp();
               }
            });


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String displayName = user.getUid();
        final Event eventSaved = new Event(displayName, name,location,startTime, date, desc, org);
        //Toast.makeText(EventDetailedView.this, "Logged in: " + displayName, Toast.LENGTH_SHORT).show();

        floating_toSavedEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (eventSaved.getUid() != null){

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
            }


    // Back Button
    private void shareOnWhatsapp() {
        System.out.println(" Start sharing in WhatsApp");

        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Test Event Text from Campus Connect");
        try {
            startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            //ToastHelper.MakeShortText("Whatsapp have not been installed.");
        }
    }


    // Back Button
    private void backTo_main() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
