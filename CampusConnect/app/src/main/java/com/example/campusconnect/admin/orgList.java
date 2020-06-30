package com.example.campusconnect.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campusconnect.R;
import com.example.campusconnect.UI.Authentication.signIn;
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

public class orgList extends AppCompatActivity {

    private KProgressHUD progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_list);

        progressDialog = KProgressHUD.create(orgList.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false);
        progressDialog.show();
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
                    ListAdapter adapter = new ListAdapter(orgList.this, list);
                    ListView listView = (ListView) findViewById(R.id.orgList);
                    listView.setAdapter(adapter);
                    progressDialog.dismiss();
                    adapter.notifyDataSetChanged();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(orgList.this, "Error getting documents: ", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void logOut(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(orgList.this, signIn.class));
    }
}
