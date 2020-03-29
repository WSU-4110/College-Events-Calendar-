package com.example.campusconnect.admin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.campusconnect.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.List;
import java.util.Map;

public class listAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final List list;

    public listAdapter(Context context, List list) {
        super(context, R.layout.activity_listview, list);
        this.context = context;
        this.list = list;
    }
//adopter pattern : to integerate the application to collaborate with each other
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("ViewHolder") final View rowView = inflater.inflate(R.layout.activity_listview, parent, false);
        TextView status = (TextView) rowView.findViewById(R.id.status);
        TextView email = (TextView) rowView.findViewById(R.id.email);

        final Map<String, Object> obj = (Map<String, Object>) list.get(position);
        email.setText(obj.get("Email").toString());
        if (obj.get("Status").toString() == "true")
            status.setText("Allow");
        else
            status.setText("Not Allow");

        Button toggle = (Button) rowView.findViewById(R.id.toggle);
        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final KProgressHUD progressDialog = KProgressHUD.create(rowView.getContext())
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setLabel("Please wait")
                        .setCancellable(false);
                progressDialog.show();
                if (obj.get("Status").toString() == "true")
                    obj.put("Status", false);
                else
                    obj.put("Status", true);

                FirebaseFirestore.getInstance().collection("Users")
                        .document("Organizers")
                        .collection("FirebaseID")
                        .document(obj.get("id").toString()).update(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        notifyDataSetChanged();
                    }
                });

            }
        });
        return rowView;
    }
}