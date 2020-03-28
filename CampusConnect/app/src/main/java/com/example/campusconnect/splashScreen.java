package com.example.campusconnect;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campusconnect.UI.Authentication.signIn;
import com.example.campusconnect.UI.Authentication.signUp;
import com.example.campusconnect.admin.listAdapter;
import com.example.campusconnect.admin.orgList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class splashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        int SPLASH_TIME_OUT = 4000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final KProgressHUD progressDialog = KProgressHUD.create(splashScreen.this)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setLabel("Please wait")
                        .setCancellable(false);
                progressDialog.show();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null) {
                    progressDialog.dismiss();
                    startActivity(new Intent(splashScreen.this, signIn.class));
                }
                else
                if (Objects.equals(user.getEmail(), "admin@campus.connect")) {
                    progressDialog.dismiss();
                    startActivity(new Intent(splashScreen.this, orgList.class));
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
                                if(data == null) {
                                    // for users
                                    startActivity(new Intent(splashScreen.this, MainActivity.class));
                                    finish();
                                }
                                else {
                                    boolean orgcheck = result.getBoolean("Status");
                                    progressDialog.dismiss();
                                    if (orgcheck) {
                                        // for admin
                                        startActivity(new Intent(splashScreen.this, MainActivity.class));
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(splashScreen.this, "This Account is Banned", Toast.LENGTH_SHORT).show();
                                        FirebaseAuth.getInstance().signOut();
                                        moveTaskToBack(true);
                                        android.os.Process.killProcess(android.os.Process.myPid());
                                        System.exit(1);
                                    }
                                }
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(splashScreen.this, " Internet Error ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }, SPLASH_TIME_OUT);
    }
}


