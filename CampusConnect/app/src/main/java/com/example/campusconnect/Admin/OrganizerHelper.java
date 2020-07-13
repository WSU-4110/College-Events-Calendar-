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
		final Boolean[] organizerFound = new Boolean[1];
		
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
							System.out.println("\n\n\nOrganizer found\n\n\n");
							organizerFound[0] = true;
						}
						else {
							organizerFound[0] = false;
						}
					}
				});
		
		return organizerFound[0];
		
	}// [ updateUserType ]
}
