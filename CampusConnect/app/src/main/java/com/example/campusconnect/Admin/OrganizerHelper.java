package com.example.campusconnect.Admin;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class OrganizerHelper {
	OrganizerHelper() {}
	
	public static boolean isOrganizer() {
		FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
		
		if (user == null) {
			// Not logged in
			return false;
		}
		else {
			return queryDBForOrganizer();
		}
		
		
	}// [ isOrganizer ]
	
	private static boolean queryDBForOrganizer() {
		// Boolean array to circumvent the req'd declaration of value as final for inner class
		FirebaseFirestore db = FirebaseFirestore.getInstance();
		FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
		final Boolean[] organizerWasFound = new Boolean[1];
		
		if (user == null)
			return false;
		
		organizerWasFound[0] = false;
		
		db.collection("Users")
				.document("Organizers")
				.collection("FirebaseID")
				.whereEqualTo("id", user.getUid())
				.get()
				.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
					@Override
					public void onComplete(@NonNull Task<QuerySnapshot> task) {
						if (task.isSuccessful()) {
							System.out.println("\n\n\nOrganizer found\n\n\n");
							organizerWasFound[0] = true;
						}
					}
				});
		
		return organizerWasFound[0];    // True/false if the current user's ID was found to be an organizer
		
	}// [ updateUserType ]
}
