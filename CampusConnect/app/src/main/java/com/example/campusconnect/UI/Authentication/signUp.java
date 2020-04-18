package com.example.campusconnect.UI.Authentication;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.campusconnect.R;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

import static com.example.campusconnect.services.conversionImage.convertBitmapToString;

public class signUp extends AppCompatActivity {

    ImageView upload;
    boolean IMAGE_STATUS = false;
    Bitmap profilePicture;
    //TextWatcher for pass -----------------------------------------------------
    TextWatcher passWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //none
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //none
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!validatePassword(s.toString())) {
                edtp1.setError("Password Must consist of 4 to 20 characters");
            }
        }

    };
    static public boolean validatePassword(String password) {
        String check = password;
        if (check.length() < 4 || check.length() > 20) {
            return false;
        }
        return true;
    }
    static public boolean password_matcher(String p1, String p2){
        if(p1==p2) return true;
        else return false;
    }

    static public boolean validate_Email(Context s, String email) {
        String check = email;
        if (check.length() < 8 || check.length() > 40) {
            Toasty.info(s, "Enter the valid email", Toast.LENGTH_LONG).show();
            return false;
        } else if (!check.matches("^[A-za-z0-9.@_]+")) {
            Toasty.info(s, "Enter the valid email", Toast.LENGTH_LONG).show();
            return false;
        } else if (!check.contains("@") || !check.contains(".")) {
            Toasty.info(s, "Enter the valid email", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }




    //TextWatcher for repeat Password ------------------------------------------
    TextWatcher cnfpassWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //none
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //none
        }

        @Override
        public void afterTextChanged(Editable s) {
            String check = s.toString(); // password 02
            if (!check.equals(edtp1.getText().toString())) {
                edtp2.setError("Both the passwords do not match");
            }
        }
    };
    //TextWatcher for Email ----------------------------------------------------
    TextWatcher emailWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //none
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //none
        }

        @Override
        public void afterTextChanged(Editable s) {
            String check = s.toString();
            if (check.length() < 4 || check.length() > 40) {
                edtemail.setError("Email Must consist of 4 to 40 characters");
            } else if (!check.matches("^[A-za-z0-9.@_]+")) {
                edtemail.setError("Only . and _ and @ characters allowed");
            } else if (!check.contains("@") || !check.contains(".")) {
                edtemail.setError("Enter Valid Email");
            }
        }
    };
    private EditText edtname, edtemail, edtp1, edtp2;
    //TextWatcher for Name -----------------------------------------------------
    TextWatcher nameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //none
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //none
        }

        @Override
        public void afterTextChanged(Editable s) {
            String check = s.toString();
            if (check.length() < 5 || check.length() > 20) {
                edtname.setError("Name Must consist of 8 to 20 characters");
            }
        }
    };
    private RadioGroup radioGroup;
    private FirebaseAuth mAuth;
    private boolean isStudent = true;
    private KProgressHUD progressDialog;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtname = findViewById(R.id.name);
        edtemail = findViewById(R.id.email);
        edtp1 = findViewById(R.id.password1);
        edtp2 = findViewById(R.id.password2);

        edtname.addTextChangedListener(nameWatcher);
        edtemail.addTextChangedListener(emailWatcher);
        edtp1.addTextChangedListener(passWatcher);
        edtp2.addTextChangedListener(cnfpassWatcher);


        radioGroup = (RadioGroup) findViewById(R.id.sel_type);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.sel_stu:
                        isStudent = true;
                        break;
                    case R.id.sel_org:
                        isStudent = false;
                        break;
                }
            }
        });

        Button signUp = findViewById(R.id.signUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (!(edtname.getText().toString().isEmpty() && edtemail.getText().toString().isEmpty()
                        && edtp1.getText().toString().isEmpty() && edtp2.getText().toString().isEmpty())) {

                    progressDialog = KProgressHUD.create(signUp.this)
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setLabel("Please wait")
                            .setCancellable(false);
                    progressDialog.show();

                    mAuth = FirebaseAuth.getInstance();
                    String email = edtemail.getText().toString(), password = edtp1.getText().toString();
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(signUp.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();

                                    if (task.isSuccessful()) {
                                        String type;
                                        boolean status = false;

                                        if (isStudent) {
                                            status = true;
                                            type = "Students";
                                        } else type = "Organizers";

                                        Map<String, Object> data = new HashMap<>();
                                        data.put("Name", edtname.getText().toString());
                                        data.put("Email", edtemail.getText().toString());
                                        data.put("Status", status);

                                        FirebaseFirestore.getInstance().collection("Users")
                                                .document(type)
                                                .collection("FirebaseID")
                                                .document(Objects.requireNonNull(mAuth.getUid()))
                                                .set(data)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification();
                                                        mAuth.signOut();
                                                        finish();
                                                        Toast.makeText(signUp.this, "Check Email For Verification", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification();
                                                        mAuth.signOut();
                                                        finish();
                                                        Toast.makeText(signUp.this, "Failed Save a DATA", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(signUp.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(signUp.this, "Fill the data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final TextView login_now = findViewById(R.id.login_now);
        login_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
