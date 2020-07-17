




package com.example.campusconnect.UI.Authentication;


import com.example.campusconnect.Admin.Authentication.ForgotPassword;
import com.example.campusconnect.Admin.Authentication.SignUp;

import org.junit.Test;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static org.junit.Assert.*;

//private EditText forgotpassemail;
//    private Button sendemail;
//    private TextView back;

public class forgetPasswordTest {

    @Test
    public void emailValidator_CorrectEmailSimple_ReturnsTrue() throws Throwable {
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                assertTrue(ForgotPassword.validateEmail(getApplicationContext(), "test@gamil.com"));
            }
        });
    }
    @Test
    public void email_Validator_CorrectEmailSimple_ReturnsTrue() throws Throwable {
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                assertTrue(SignUp.validate_Email(getApplicationContext(), "test1@gamil.com"));
            }
        });
    }

    @Test
    public void passwordValidator_CorrectEmailSimple_ReturnsTrue() throws Throwable {
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                assertTrue(SignUp.validatePassword("3#124"));
            }
        });
    }
    @Test
    public void password_matcher_CorrectEmailSimple_ReturnsTrue() throws Throwable {
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                assertTrue(SignUp.password_matcher("3#124","3#124"));
            }
        });
    }

}