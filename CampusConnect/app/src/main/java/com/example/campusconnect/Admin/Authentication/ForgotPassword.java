
package com.example.campusconnect.Admin.Authentication;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campusconnect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.kaopiz.kprogresshud.KProgressHUD;

import es.dmoral.toasty.Toasty;

public class ForgotPassword extends AppCompatActivity {
	
	private EditText forgotpassemail;
	private Button sendemail;
	private TextView back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_password);
		
		forgotpassemail = findViewById(R.id.forgotpassemail);
		sendemail = findViewById(R.id.sendemail);
		back = findViewById(R.id.goback);
		
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		
		sendemail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (validateEmail(ForgotPassword.this, forgotpassemail.getText().toString())) {
					
					final KProgressHUD progressDialog = KProgressHUD.create(ForgotPassword.this)
							.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
							.setLabel("Please wait")
							.setCancellable(false);
					progressDialog.show();
					
					FirebaseAuth.getInstance().sendPasswordResetEmail(forgotpassemail.getText().toString())
							.addOnCompleteListener(new OnCompleteListener<Void>() {
								@Override
								public void onComplete(@NonNull Task<Void> task) {
									
									progressDialog.dismiss();
									if (task.isSuccessful()) {
										Toasty.info(ForgotPassword.this, "Check your Email", Toast.LENGTH_LONG).show();
										finish();
									}
									else {
										Toasty.info(ForgotPassword.this, "Error sending Email", Toast.LENGTH_LONG).show();
									}
								}
							});
				}
			}
		});
	}
	
	
	static public boolean validateEmail(Context s, String email) {
		String check = email;
		if (check.length() < 8 || check.length() > 40) {
			Toasty.info(s, "Enter the valid email", Toast.LENGTH_LONG).show();
			return false;
		}
		else if (!check.matches("^[A-za-z0-9.@_]+")) {
			Toasty.info(s, "Enter the valid email", Toast.LENGTH_LONG).show();
			return false;
		}
		else if (!check.contains("@") || !check.contains(".")) {
			Toasty.info(s, "Enter the valid email", Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}
	
}
