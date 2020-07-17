


package com.example.campusconnect.Admin.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campusconnect.MainActivity;
import com.example.campusconnect.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

// TODO: Change name to LandingPage?
public class SignIn extends AppCompatActivity {
	
	private static final int RC_SIGN_IN = 1;
	GoogleSignInClient mGoogleSignInClient;
	TextWatcher passWatcher;
	TextWatcher emailWatcher;
	FirebaseAuth mAuth;
	GoogleSignInOptions gso;
	
	Button login;
	Button openCalendar;
	EditText emailField;
	EditText passwordField;
	TextView forgotPassword;
	TextView registerNow;
	boolean altWatcher = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);
		
		setupWatchers();
		setupViews();
		setupGoogleOptions();
		
		emailField.addTextChangedListener(emailWatcher);
		passwordField.addTextChangedListener(passWatcher);
		
		login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				boolean emailWasEmpty = emailField.getText().toString().isEmpty();
				boolean passwordWasEmpty = passwordField.getText().toString().isEmpty();
				
				if (!emailWasEmpty && !passwordWasEmpty) {
					attemptLogin();
				}
				else {
					Toast.makeText(SignIn.this,
							"Your Password or Email Cannot be Empty", Toast.LENGTH_SHORT).show();
				}
			}
		});// login
		
		openCalendar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SignIn.this, MainActivity.class);
				startActivity(intent);
			}
		});// openCalendar
		
		registerNow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(SignIn.this, SignUp.class);
				startActivity(intent);
			}
		});// registerNow
		
		forgotPassword.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(SignIn.this, ForgotPassword.class);
				startActivity(intent);
			}
		});// forgotPassword
		
	}// [ onCreate ]
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == RC_SIGN_IN) {
			Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
			try {
				GoogleSignInAccount account = task.getResult(ApiException.class);
				firebaseAuthWithGoogle(account);
			}
			catch (ApiException e) {
				Toast.makeText(SignIn.this, "Google Sign in Failed", Toast.LENGTH_SHORT).show();
			}
		}
	}// [ onActivityResult ]
	
	
	private void setupViews() {
		emailField = findViewById(R.id.email);
		passwordField = findViewById(R.id.password);
		registerNow = findViewById(R.id.register_now);
		openCalendar = findViewById(R.id.view_calendar_button);
		forgotPassword = findViewById(R.id.forgot_pass);
		login = findViewById(R.id.login_button);
	}
	
	
	private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
		AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
		mAuth.signInWithCredential(credential)
				.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()) {
							FirebaseUser user = mAuth.getCurrentUser();
							Toast.makeText(SignIn.this,
									"Sign in Successfully " + user.getUid(), Toast.LENGTH_SHORT).show();
							startActivity(new Intent(SignIn.this, MainActivity.class));
							finish();
						}
						else {
							Toast.makeText(SignIn.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
						}
					}
				});
		
	}// [ firebaseAuthWithGoogle ]
	
	
	private void setupWatchers() {
		// CHECK: Text watchers needed?
		emailWatcher = new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				String check = s.toString();
				if (check.length() < 8 || check.length() > 40) {
					emailField.setError("Email Must consist of 8 to 40 characters");
				}
				else if (!check.matches("^[A-za-z0-9.@_]+")) {
					emailField.setError("Only . and _ and @ characters allowed");
				}
				else if (!check.contains("@") || !check.contains(".")) {
					emailField.setError("Enter Valid Email");
				}
				else if (check.startsWith("pikachu")) {
					altWatcher = true;
					emailField.setError("Enter Valid Email");
				}
			}
		};// emailWatcher
		
		passWatcher = new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				String password = s.toString();
				if (password.length() < 4 || password.length() > 20) {
					SignIn.this.passwordField.setError("Password Must consist of 4 to 20 characters");
				}
				else if (password.equals("thing"))
					showAlt(); // Jay
			}
		};// passWatcher
		
	}// [ setupWatchers ]
	
	
	private void setupGoogleOptions() {
		gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(getString(R.string.default_web_client_id))
				.requestEmail()
				.build();
		
		mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
	}// [ setupGoogleOptions ]
	
	
	public void googleAccount(View view) {
		Intent intent = mGoogleSignInClient.getSignInIntent();
		startActivityForResult(intent, RC_SIGN_IN);
	}// [ googleAccount ]
	
	
	public void showAlt() {
		final ImageView altview = (ImageView) findViewById(R.id.alt4263336);
		
		emailField.setError("");
		passwordField.setError("");
		altview.setVisibility(View.VISIBLE);
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				altWatcher = false;
				altview.setVisibility(View.INVISIBLE);
				System.out.println("end");
			}
		}, 4000);
	}
	
	// CHECK: Need addOnCompleteListener?
	private void attemptLogin() {
		FirebaseAuth.getInstance().signInWithEmailAndPassword(
				emailField.getText().toString(),
				passwordField.getText().toString())
				.addOnCompleteListener(SignIn.this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						FirebaseUser user;
						boolean emailVerified;
						if (!task.isSuccessful()) {
							Toast.makeText(SignIn.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
							return;
						}
						
						user = FirebaseAuth.getInstance().getCurrentUser();
						emailVerified = user.isEmailVerified();
						if (emailVerified) {
							Toast.makeText(SignIn.this, "Signed In!", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(SignIn.this, MainActivity.class);
							startActivity(intent);
							finish();
						}
						else {
							user.sendEmailVerification();
							FirebaseAuth.getInstance().signOut(); // Log Out
							Toast.makeText(SignIn.this, "Email Not Verified.\n Check Your Email.",
									Toast.LENGTH_LONG).show();
						}
					}
				});// addOnCompleteListener
		
	}// [ attemptLogin ]
	
	
}// class [ SignIn ]