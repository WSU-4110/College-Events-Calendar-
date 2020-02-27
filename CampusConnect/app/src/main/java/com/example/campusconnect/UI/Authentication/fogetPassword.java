
package com.example.campusconnect.UI.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.campusconnect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.kaopiz.kprogresshud.KProgressHUD;

import es.dmoral.toasty.Toasty;

public class fogetPassword extends AppCompatActivity {

    private EditText forgotpassemail;
    private Button sendemail;
    private TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foget_password);

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
                if (validateEmail()) {

                    final KProgressHUD progressDialog = KProgressHUD.create(fogetPassword.this)
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
                                        Toasty.info(fogetPassword.this, "Check your Email", Toast.LENGTH_LONG).show();
                                        finish();
                                    } else {
                                        Toasty.info(fogetPassword.this, "Error sending Email", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });
    }


    private boolean validateEmail() {
        String check = forgotpassemail.getText().toString();
        if (check.length() < 8 || check.length() > 40) {
            Toasty.info(fogetPassword.this, "Enter the valid email", Toast.LENGTH_LONG).show();
            return false;
        } else if (!check.matches("^[A-za-z0-9.@_]+")) {
            Toasty.info(fogetPassword.this, "Enter the valid email", Toast.LENGTH_LONG).show();
            return false;
        } else if (!check.contains("@") || !check.contains(".")) {
            Toasty.info(fogetPassword.this, "Enter the valid email", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

}
