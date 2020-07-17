


package com.example.campusconnect.Admin.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public class SignInBAK extends AppCompatActivity {
	
	private static final int RC_SIGN_IN = 1;
	private GoogleSignInClient mGoogleSignInClient;
	/*
	TextWatcher passWatcher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) { }
		
		@Override
		public void afterTextChanged(Editable s) {
			String check = s.toString();
			if (check.length() < 4 || check.length() > 20) {
				password.setError("Password Must consist of 4 to 20 characters");
			}
		}
		
	};
	
	private EditText email, password;
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
	private TextView forgotpassword;
	private TextView registernow;
	private FirebaseAuth mAuth;
	private GoogleSignInOptions gso;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);
		
		Button btn_view;
		
		btn_view = findViewById(R.id.view_calendar_button);
		btn_view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// for users
				startActivity(new Intent(SignIn.this, MainActivity.class));
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
				startActivity(new Intent(SignIn.this, SignUp.class));
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
				startActivity(new Intent(SignIn.this, ForgotPassword.class));
			}
		});
		
		Button button = findViewById(R.id.login_button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				
				final String emailEntered;
				final String passwordEntered;
				
				emailEntered = email.getText().toString();
				passwordEntered = password.getText().toString();
				
				//--------------------------------
				
				boolean emailEmpty;
				boolean passwordEmpty;
				
				//--------------------------------
				progressDialog = KProgressHUD.create(SignIn.this)
						.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
						.setLabel("Please wait")
						.setCancellable(false);
				progressDialog.show();
				
				//--------------------------------
				// Email and password not empty
				emailEmpty = emailEntered.isEmpty();
				passwordEmpty = passwordEntered.isEmpty();
				
				if (emailEmpty && passwordEmpty) {
					progressDialog.dismiss();
					Toast.makeText(SignIn.this, "Your Email and Password Cannot be Empty", Toast.LENGTH_SHORT).show();
				}
				
				//--------------------------------
			// [A]
				FirebaseAuth.getInstance().signInWithEmailAndPassword(emailEntered, passwordEntered)
						.addOnCompleteListener(SignIn.this, new OnCompleteListener<AuthResult>() {
							// [ Y ]
							@Override
							public void onComplete(@NonNull Task<AuthResult> task) {
								
								if (!task.isSuccessful()) {
									progressDialog.dismiss();
									Toast.makeText(SignIn.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
									return;
								}
								
								if (emailEntered.equals("admin@campus.connect")
										&& passwordEntered.equals("admin")) {
									progressDialog.dismiss();
									Toast.makeText(SignIn.this, "Signing in as Admin", Toast.LENGTH_SHORT).show();
									startActivity(new Intent(SignIn.this, OrganizerList.class));
									finish();
									return;        // CHECK: return ok here?
								}
								
								// FirebaseUser: Not null
								FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
								
								if (user == null) {
									FirebaseAuth.getInstance().signOut(); // Log Out
									progressDialog.dismiss();
									Toast.makeText(SignIn.this, "User not found/NULL", Toast.LENGTH_SHORT).show();
									return;
								}
								
								// FirebaseUser: User Verified
								boolean emailVerified;
								emailVerified = user.isEmailVerified();
								
								if (!emailVerified) {
									FirebaseAuth.getInstance().signOut(); // Log Out
									progressDialog.dismiss();
									Toast.makeText(SignIn.this, "Email not Verify yet", Toast.LENGTH_SHORT).show();
									return;
								}
								
							// [B]
								FirebaseFirestore.getInstance()
										.collection("Users")
										.document("Organizers")
										.collection("FirebaseID")
										.document(Objects.requireNonNull(user.getUid()))
										.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
									// [ X ]
									@Override
									public void onComplete(@NonNull Task<DocumentSnapshot> task) {
										
										progressDialog.dismiss();
										
										// Query Successful, with results
										if (!task.isSuccessful()) {
											progressDialog.dismiss();
											Toast.makeText(SignIn.this, " Internet Error ", Toast.LENGTH_SHORT).show();
											return;
										}
										
										//--------------------------------
										// Check user data/account type
										DocumentSnapshot result = task.getResult();
										Map<String, Object> data = result.getData();
										boolean orgCheck;
										
										if (data == null) {
											Toast.makeText(SignIn.this, "Welcome USER", Toast.LENGTH_SHORT).show();
											startActivity(new Intent(SignIn.this, MainActivity.class));
											finish();
										}
										else {
											orgCheck = result.getBoolean("Status");
											
											if (orgCheck) {
												Toast.makeText(SignIn.this, "Welcome Organizer", Toast.LENGTH_SHORT).show();
												startActivity(new Intent(SignIn.this, MainActivity.class));
												finish();
											}
											else {
												Toast.makeText(SignIn.this, "Org Account is Not Approved Yet", Toast.LENGTH_SHORT).show();
												FirebaseAuth.getInstance().signOut();
												
											}// [ Inner if/else ]
											
										}// [ Outer if/else ]
										
									}// [ X onComplete ]
									
								});// [ B FirebaseFirestore.getInstance() ]
								
							}// [ Y onComplete ]
							
						});// [ A FirebaseFirestore.getInstance() ]
				
			}// [ onClick ]
			
		});// [ button ]
		
	}// [ onCreate ]
	
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
	}// [ firebaseAuthWithGoogle ]
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == RC_SIGN_IN) {
			Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
			try {
				GoogleSignInAccount account = task.getResult(ApiException.class);
				assert account != null;
				firebaseAuthWithGoogle(account);
			}
			catch (ApiException e) {
				progressDialog.dismiss();
				Toast.makeText(SignIn.this, "Google sign in failed", Toast.LENGTH_SHORT).show();
			}
		}
	}// [ onActivityResult ]
	
	
	public void gmailAccount(View view) {
		
		progressDialog = KProgressHUD.create(SignIn.this)
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setLabel("Please wait")
				.setCancellable(false);
		
		progressDialog.show();
		
		Intent intent = mGoogleSignInClient.getSignInIntent();
		startActivityForResult(intent, RC_SIGN_IN);
		
	}// [ gmailAccount ]
	*/
	
}// class [ SignInBAK ]