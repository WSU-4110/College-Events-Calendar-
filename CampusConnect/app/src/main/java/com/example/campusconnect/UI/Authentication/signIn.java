


package com.example.campusconnect.UI.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campusconnect.MainActivity;
import com.example.campusconnect.R;
import com.example.campusconnect.admin.orgList;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.Map;
import java.util.Objects;

public class signIn extends AppCompatActivity {
	
	private static final int RC_SIGN_IN = 1;
	private GoogleSignInClient mGoogleSignInClient;
	
	
	//TextWatcher for Password
	TextWatcher passWatcher = new TextWatcher() {
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
		}
		
	};
	private EditText email, password;
	//TextWatcher for Email
	TextWatcher emailWatcher = new TextWatcher() {
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
		}
	};
	private KProgressHUD progressDialog;
	private TextView forgotpassword, registernow;
	private FirebaseAuth mAuth;
	private GoogleSignInOptions gso;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);
		
		Button btn_view = findViewById(R.id.view_calendar_button);
		btn_view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// for users
				startActivity(new Intent(signIn.this, MainActivity.class));
			}
		});
		
		mAuth = FirebaseAuth.getInstance();
		email = findViewById(R.id.email);
		password = findViewById(R.id.password);
		
		email.addTextChangedListener(emailWatcher);
		password.addTextChangedListener(passWatcher);
		
		registernow = findViewById(R.id.register_now);
		registernow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(signIn.this, signUp.class));
			}
		});
		
		gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(getString(R.string.default_web_client_id))
				.requestEmail()
				.build();
		mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
		
		forgotpassword = findViewById(R.id.forgot_pass);
		forgotpassword.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(signIn.this, forgetPassword.class));
			}
		});
		
		Button button = findViewById(R.id.login_button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				progressDialog = KProgressHUD.create(signIn.this)
						.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
						.setLabel("Please wait")
						.setCancellable(false);
				progressDialog.show();
				if (!(email.getText().toString().isEmpty() && password.getText().toString().isEmpty())) {
					FirebaseAuth.getInstance().signInWithEmailAndPassword(
							email.getText().toString(),
							password.getText().toString())
							.addOnCompleteListener(signIn.this, new OnCompleteListener<AuthResult>() {
								@Override
								public void onComplete(@NonNull Task<AuthResult> task) {
									if (!task.isSuccessful()) {
										progressDialog.dismiss();
										Toast.makeText(signIn.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
									}
									else {
										if (email.getText().toString().equals("admin@campus.connect")) {
											progressDialog.dismiss();
											Toast.makeText(signIn.this, "SignIn as Admin", Toast.LENGTH_SHORT).show();
											startActivity(new Intent(signIn.this, orgList.class));
											finish();
										}
										else {
											FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
											assert user != null;
											boolean emailVerified = user.isEmailVerified();
											if (emailVerified) {
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
																progressDialog.dismiss();
																Toast.makeText(signIn.this, "Welcome USER", Toast.LENGTH_SHORT).show();
																startActivity(new Intent(signIn.this, MainActivity.class));
																finish();
															}
															// !! !! CRITICAL only for testing
//															else if(result.getString("Name").equals("Jay")){
//                                                                // for Jay
//                                                                Toast.makeText(signIn.this, "Welcome Organizer", Toast.LENGTH_SHORT).show();
//                                                                startActivity(new Intent(signIn.this, MainActivity.class));
//                                                                finish();
//                                                            }
															else {
																boolean orgcheck = result.getBoolean("Status");
																progressDialog.dismiss();
																if (orgcheck) {
																	// for organizer
																	Toast.makeText(signIn.this, "Welcome Organizer", Toast.LENGTH_SHORT).show();
																	startActivity(new Intent(signIn.this, MainActivity.class));
																	finish();
																}
																else {
																	Toast.makeText(signIn.this, "Org Account is Not Approved Yet", Toast.LENGTH_SHORT).show();
																	FirebaseAuth.getInstance().signOut();
																}
															}
														}
														else {
															progressDialog.dismiss();
															Toast.makeText(signIn.this, " Internet Error ", Toast.LENGTH_SHORT).show();
														}
													}
												});
											}
											else {
												FirebaseAuth.getInstance().signOut(); // Log Out
												progressDialog.dismiss();
												Toast.makeText(signIn.this, "Email not Verify yet", Toast.LENGTH_SHORT).show();
											}
										}
									}
								}
							});
				}
				else {
					progressDialog.dismiss();
					Toast.makeText(signIn.this, "Your Password or Email Cannot be null", Toast.LENGTH_SHORT).show();
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
							// for users
							progressDialog.dismiss();
							FirebaseUser user = mAuth.getCurrentUser();
							assert user != null;
							Toast.makeText(signIn.this, "sign in Successfully " + user.getUid(), Toast.LENGTH_SHORT).show();
							startActivity(new Intent(signIn.this, MainActivity.class));
							finish();
						}
						else {
							progressDialog.dismiss();
							Toast.makeText(signIn.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
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
				assert account != null;
				firebaseAuthWithGoogle(account);
			} catch (ApiException e) {
				progressDialog.dismiss();
				Toast.makeText(signIn.this, "Google sign in failed", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	public void gmailAccount(View view) {
		progressDialog = KProgressHUD.create(signIn.this)
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("Please wait")
				.setCancellable(false);
		progressDialog.show();
		Intent i = mGoogleSignInClient.getSignInIntent();
		startActivityForResult(i, RC_SIGN_IN);
	}
	
	
}
