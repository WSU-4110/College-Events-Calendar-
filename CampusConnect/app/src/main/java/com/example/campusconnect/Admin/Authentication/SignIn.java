


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
import com.kaopiz.kprogresshud.KProgressHUD;

// TODO: Change name to LandingPage?
public class SignIn extends AppCompatActivity {
	
	private static final int RC_SIGN_IN = 1;
	private GoogleSignInClient mGoogleSignInClient;
	private static boolean altWatcher = false;
	TextWatcher passWatcher;
	TextWatcher emailWatcher;
	
	private EditText email;
	private EditText password;
	Button openCalendar_button;
	Button login_button;
	
	private KProgressHUD progressDialog;
	private TextView forgotpassword;
	private TextView registernow;
	private FirebaseAuth mAuth;
	private GoogleSignInOptions gso;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);
		
		setupWatchers();
		setupViews();
		email.addTextChangedListener(emailWatcher);
		password.addTextChangedListener(passWatcher);
		
		openCalendar_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SignIn.this, MainActivity.class);
				startActivity(intent);
			}
		});
		
		registernow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(SignIn.this, SignUp.class);
				startActivity(intent);
			}
		});
		
		gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(getString(R.string.default_web_client_id))
				.requestEmail()
				.build();
		mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
		
		
		forgotpassword.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(SignIn.this, ForgetPassword.class));
			}
		});
		
		
		login_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				progressDialog = KProgressHUD.create(SignIn.this)
						.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
						.setLabel("Please wait")
						.setCancellable(false);
				progressDialog.show();
				
				boolean emailWasEmpty = email.getText().toString().isEmpty();
				boolean passwordWasEmpty = password.getText().toString().isEmpty();
				
				if (!emailWasEmpty && !passwordWasEmpty) {
					FirebaseAuth.getInstance().signInWithEmailAndPassword(
							email.getText().toString(),
							password.getText().toString())
							.addOnCompleteListener(SignIn.this, new OnCompleteListener<AuthResult>() {
								@Override
								public void onComplete(@NonNull Task<AuthResult> task) {
									if (!task.isSuccessful()) {
										progressDialog.dismiss();
										Toast.makeText(SignIn.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
									}
									else {
										FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
										boolean emailVerified = user.isEmailVerified();
										if (emailVerified) {
											progressDialog.dismiss();
											Toast.makeText(SignIn.this, "sign in Successfully", Toast.LENGTH_SHORT).show();
											startActivity(new Intent(SignIn.this, MainActivity.class));
											finish();
										}
										else {
											progressDialog.dismiss();
											user.sendEmailVerification();
											FirebaseAuth.getInstance().signOut(); // Log Out
											Toast.makeText(SignIn.this, "Email not Verify yet", Toast.LENGTH_SHORT).show();
										}
									}
								}
							});
				}
				else {
					progressDialog.dismiss();
					Toast.makeText(SignIn.this, "Your Password or Email Cannot be null", Toast.LENGTH_SHORT).show();
				}
			}
			
		});
	}
	
	private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
		AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
		mAuth.signInWithCredential(credential)
				.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()) {
							progressDialog.dismiss();
							FirebaseUser user = mAuth.getCurrentUser();
							Toast.makeText(SignIn.this, "sign in Successfully " + user.getUid(), Toast.LENGTH_SHORT).show();
							startActivity(new Intent(SignIn.this, MainActivity.class));
							finish();
						}
						else {
							progressDialog.dismiss();
							Toast.makeText(SignIn.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
						}
					}
				});
	}
	
	
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
				progressDialog.dismiss();
				Toast.makeText(SignIn.this, "Google sign in failed", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	public void gmailAccount(View view) {
		progressDialog = KProgressHUD.create(SignIn.this)
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("Please wait")
				.setCancellable(false);
		progressDialog.show();
		Intent i = mGoogleSignInClient.getSignInIntent();
		startActivityForResult(i, RC_SIGN_IN);
	}
	
	public void showAlt() {
		final ImageView altview = (ImageView) findViewById(R.id.alt4263336);
		
		email.setError("");
		password.setError("");
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
	
	private void setupViews(){
		email = findViewById(R.id.email);
		password = findViewById(R.id.password);
		registernow = findViewById(R.id.register_now);
		openCalendar_button = findViewById(R.id.view_calendar_button);
		forgotpassword = findViewById(R.id.forgot_pass);
		login_button = findViewById(R.id.login_button);
	}
	
	private void setupWatchers() {
		passWatcher = new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				String check = s.toString();
				if (check.length() < 4 || check.length() > 20) {
					password.setError("Password Must consist of 4 to 20 characters");
				}
				else if (check.equals("thing"))
					showAlt(); // Jay
			}
			
		};
		
		emailWatcher = new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				String check = s.toString();
				if (check.length() < 8 || check.length() > 40) {
					email.setError("Email Must consist of 8 to 40 characters");
				}
				else if (!check.matches("^[A-za-z0-9.@_]+")) {
					email.setError("Only . and _ and @ characters allowed");
				}
				else if (!check.contains("@") || !check.contains(".")) {
					email.setError("Enter Valid Email");
				}
				else if (check.startsWith("pikachu")) {
					altWatcher = true;
					email.setError("Enter Valid Email");
				}
			}
		};
		
	}// [ setupWatchers ]
	
}// class [ SignIn ]