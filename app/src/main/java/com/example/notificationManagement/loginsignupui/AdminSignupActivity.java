package com.example.notificationManagement.loginsignupui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AdminSignupActivity extends AppCompatActivity {

    // public constants

    public static final String CHAT_PREFS = "ChatPrefs";
    public static final String DISPLAY_NAME_KEY = "username";
    private static final String SECRET_CODE_KEY = "admin_hod";



   //private variables for accessing the input data

    private EditText emailAdmin,passwordAdmin,nameAdmin,secretcodeAdmin;
    private CheckBox checkBoxAdmin;
    private ImageButton signupAdmin;
    private Button signinAdmin;

    // Firebase instance variables
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_admin);
        emailAdmin = (EditText)findViewById(R.id.emailAdmin);
        passwordAdmin = (EditText)findViewById(R.id.passwordAdmin);
        nameAdmin = (EditText)findViewById(R.id.nameAdmin);
        secretcodeAdmin = (EditText) findViewById(R.id.secret_key) ;

        checkBoxAdmin = (CheckBox)findViewById(R.id.checkboxAdmin);

        signupAdmin =(ImageButton)findViewById(R.id.signupAdmin);

        signinAdmin = (Button) findViewById(R.id.signinAdmin);



        signinAdmin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminSignupActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });
        // Keyboard sign in action
        checkBoxAdmin.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == (200) || id == EditorInfo.IME_NULL) {
                    attemptRegistration();
                    return true;
                }
                return false;
            }
        });
        // TODO: Get hold of an instance of FirebaseAuth

        mAuth = FirebaseAuth.getInstance();
        signupAdmin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminSignupActivity.this,"Registering...",Toast.LENGTH_LONG).show();
                signUp();
            }
        });
    }
    // Executed when Sign Up button is pressed.
    public void signUp() {
        attemptRegistration();
    }


    private void attemptRegistration() {

        // Reset errors displayed in the form.
        emailAdmin.setError(null);
        passwordAdmin.setError(null);

        // Store values at the time of the login attempt.
        String email = emailAdmin.getText().toString();
        String password = passwordAdmin.getText().toString();
        String secretKey = secretcodeAdmin.getText().toString();
        Boolean checkBox = checkBoxAdmin.isChecked();

        boolean cancel = false;
        View focusView = null;



        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            passwordAdmin.setError(getString(R.string.error_invalid_password));
            focusView = passwordAdmin;
            cancel = true;
        }


        // Check for a valid secret key for admin
        if(TextUtils.isEmpty(secretKey) || !isSecretKeyValid(secretKey)){
            secretcodeAdmin.setError(getString(R.string.error_invalid_secretcode));
            focusView = secretcodeAdmin;
            cancel = true;
        }


        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailAdmin.setError(getString(R.string.error_field_required));
            focusView = emailAdmin;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailAdmin.setError(getString(R.string.error_invalid_email));
            focusView = emailAdmin;
            cancel = true;
        }
        if(!checkBox){
            showErrorMethod("Please accept the Terms and Conditions");
            focusView = checkBoxAdmin;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // TODO: Call create FirebaseUser() here
            createFirebaseUser();

        }

    }

    private boolean isEmailValid(String email) {
        // You can add more checking logic here.
        return email.contains("@");
    }
    private boolean isSecretKeyValid(String secretCode) {
        // You can add more checking logic here.
        return secretCode.equals(SECRET_CODE_KEY);
    }

    private boolean isPasswordValid(String password) {
        //TODO: Add own logic to check for a valid password (minimum 6 characters)
        return password.length()>6;
    }
    // TODO: Create a Firebase user
    private void createFirebaseUser(){
        String email = emailAdmin.getText().toString();
        String password = passwordAdmin.getText().toString();
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("Notification","Create User Completion " + task.isSuccessful());
                if(!task.isSuccessful()) {
                    Log.d("Notification","create user completion failed "+ task.getException());
                    showErrorMethod("Registration Failed!! Please enter the valid Credentials");
                }
                else{
                    saveDisplayName();
                    Intent intent = new Intent(AdminSignupActivity.this,LoginActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        });
    }
    // TODO: Save the display name to Shared Preferences
    private void saveDisplayName(){
        String userName = nameAdmin.getText().toString();
        // Check if User name is empty
        if(userName.equals("")){
            userName = "Anonomous";
        }
        SharedPreferences prefs = getSharedPreferences(CHAT_PREFS,0);
        prefs.edit().putString(DISPLAY_NAME_KEY,userName).apply();
    }
    // TODO: Create an alert dialog to show in case registration failed
    private void showErrorMethod(String message){
        new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
