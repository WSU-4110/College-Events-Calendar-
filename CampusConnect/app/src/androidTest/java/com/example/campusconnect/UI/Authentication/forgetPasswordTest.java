package com.example.campusconnect.UI.Authentication;

import android.widget.Toast;

import com.example.campusconnect.admin.orgList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static org.junit.Assert.*;

public class forgetPasswordTest {

    @Test
    public void emailValidator_CorrectEmailSimple_ReturnsTrue() throws Throwable {
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                assertTrue(forgetPassword.validateEmail(getApplicationContext(), "test@gamil.com"));
            }
        });
    }
    @Test
    public void email_Validator_CorrectEmailSimple_ReturnsTrue() throws Throwable {
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                assertTrue(signUp.validate_Email(getApplicationContext(), "test1@gamil.com"));
            }
        });
    }

    @Test
    public void passwordValidator_CorrectEmailSimple_ReturnsTrue() throws Throwable {
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                assertTrue(signUp.validatePassword("3#124"));
            }
        });
    }
    @Test
    public void password_matcher_CorrectEmailSimple_ReturnsTrue() throws Throwable {
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                assertTrue(signUp.password_matcher("3#124","3#124"));
            }
        });
    }

}