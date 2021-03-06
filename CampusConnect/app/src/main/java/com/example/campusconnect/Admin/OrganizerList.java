package com.example.campusconnect.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campusconnect.Admin.Authentication.SignIn;
import com.example.campusconnect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrganizerList extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_org_list);
		
		FirebaseFirestore.getInstance().collection("Users")
				.document("Organizers")
				.collection("FirebaseID")
				.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
			@Override
			public void onComplete(@NonNull Task<QuerySnapshot> task) {
				if (task.isSuccessful()) {
					List list = new ArrayList<>();
					for (final QueryDocumentSnapshot document : task.getResult()) {
						Map<String, Object> data = document.getData();
						data.put("id", document.getId());
						list.add(data);
					}
					OrgListAdapter adapter = new OrgListAdapter(OrganizerList.this, list);
					ListView listView = (ListView) findViewById(R.id.orgList);
					listView.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				}
				else {
					Toast.makeText(OrganizerList.this, "Error getting documents: ", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		
	}
	
	public void logOut(View view) {
		FirebaseAuth.getInstance().signOut();
		startActivity(new Intent(OrganizerList.this, SignIn.class));
	}
}
