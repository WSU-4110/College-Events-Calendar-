package com.example.campusconnect.admin;

import android.os.AsyncTask;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.example.campusconnect.Event.Event;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;


// com.example.campusconnect.Event.Event
public class DBEventLoader extends AsyncTask<Void, Void, ArrayList<Event>> {
	
	//	public AsyncResponse delegate = null;
	private ArrayList<Event> eventsList = new ArrayList<>();
	private boolean eventsListBuilt = false;
	
	@Override
	protected ArrayList<Event> doInBackground(@NonNull Void... voids) {
		FirebaseFirestore db = FirebaseFirestore.getInstance();
		
		Handler simulatePause = new Handler();
		simulatePause.postDelayed(new Runnable() {
			@Override
			public void run() {
			
			}
		}, 2000);
		
		try {
			Thread.sleep(2000);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		db.collection("Events")
				.get()
				.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
					@Override
					public void onComplete(@NonNull Task<QuerySnapshot> task) {
						if (task.isSuccessful()) {
							
							for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
								Event event = (Event) document.toObject(Event.class);
								
								eventsList.add(event);
							}
						}
					}
				});
		
		return eventsList;
		
	}// [ doInBackground ]
	
	@Override
	protected void onPostExecute(@NonNull ArrayList<Event> indicators) {
		super.onPostExecute(indicators);
//
//		if (isFinishing()) {
//			return;
//		}
		
		eventsListBuilt = true;
	}
	
	public boolean eventsLoaded() {
		return eventsListBuilt;
	}
	
	public ArrayList<Event> indicatorsList() {
		return eventsList;
	}
	
}// [ DBEventLoader ]

