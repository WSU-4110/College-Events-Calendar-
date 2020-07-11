package com.example.campusconnect.UI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campusconnect.Admin.Authentication.SignIn;
import com.example.campusconnect.Admin.OrganizerList;
import com.example.campusconnect.MainActivity;
import com.example.campusconnect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.Map;
import java.util.Objects;


public class SplashScreen extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		int SPLASH_TIME_OUT = 4000;
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				final KProgressHUD progressDialog = KProgressHUD.create(SplashScreen.this)
						.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
						.setLabel("Please wait")
						.setCancellable(false);
				progressDialog.show();
				FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
				if (user == null) {
					progressDialog.dismiss();
					startActivity(new Intent(SplashScreen.this, SignIn.class));
				}
				else if (Objects.equals(user.getEmail(), "admin@campus.connect")) {
					progressDialog.dismiss();
					startActivity(new Intent(SplashScreen.this, OrganizerList.class));
				}
				else {
					FirebaseFirestore.getInstance()
							.collection("Users")
							.document("Organizers")
							.collection("FirebaseID")
							.document(Objects.requireNonNull(user.getUid()))
							.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
						@Override
						public void onComplete(@NonNull Task<DocumentSnapshot> task) {
							if (task.isSuccessful()) {
								DocumentSnapshot result = task.getResult();
								Map<String, Object> data = result.getData();
								if (data == null) {
									// for users
									startActivity(new Intent(SplashScreen.this, MainActivity.class));
									finish();
								}
								else {
									boolean orgcheck = result.getBoolean("Status");
									progressDialog.dismiss();
									if (orgcheck) {
										// for admin
										startActivity(new Intent(SplashScreen.this, MainActivity.class));
										finish();
									}
									else {
										Toast.makeText(SplashScreen.this, "This Account is Banned", Toast.LENGTH_SHORT).show();
										FirebaseAuth.getInstance().signOut();
										moveTaskToBack(true);
										android.os.Process.killProcess(android.os.Process.myPid());
										System.exit(1);
									}
								}
							}
							else {
								progressDialog.dismiss();
								Toast.makeText(SplashScreen.this, " Internet Error ", Toast.LENGTH_SHORT).show();
							}
						}
					});
				}
			}
		}, SPLASH_TIME_OUT);
	}
}


