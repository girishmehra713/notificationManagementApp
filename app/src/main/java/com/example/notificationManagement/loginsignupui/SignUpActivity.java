package com.example.notificationManagement.loginsignupui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

public class SignUpActivity extends AppCompatActivity{

    // static constants
    public static final String CHAT_PREFS = "ChatPrefs";
    public static final String DISPLAY_NAME_KEY = "username";



    private EditText email,password,name,phone;
    private CheckBox checkBox;
    private ImageButton signup;
    private ProgressDialog progressDialog;
    private Button signin;
    // Firebase instance variables
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        email = (EditText)findViewById(R.id.emailUser);
        password = (EditText)findViewById(R.id.passwordUser);
        name = (EditText)findViewById(R.id.nameUser);
        phone = (EditText)findViewById(R.id.phoneUser);

        checkBox = (CheckBox)findViewById(R.id.checkboxUser);

        signup =(ImageButton)findViewById(R.id.signupUser);

        progressDialog = new ProgressDialog(this);

        signin = (Button) findViewById(R.id.signinUser);

        // Keyboard sign in action
        checkBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == (200) || id == EditorInfo.IME_NULL) {
                    attemptRegistration();
                    return true;
                }
                return false;
            }
        });
        mAuth = FirebaseAuth.getInstance();
        signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Toast.makeText(SignUpActivity.this,"Registering User...",Toast.LENGTH_LONG).show();
                signUp();
            }
        });

        signin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });

    }
    public void signUp() {
        attemptRegistration();
    }
    private void attemptRegistration() {

        // Reset errors displayed in the form.
        email.setError(null);
        password.setError(null);

        // Store values at the time of the login attempt.
        String emailInput = email.getText().toString();
        String passwordInput = password.getText().toString();
        String phoneInput = phone.getText().toString();
        Boolean checkBoxInput = checkBox.isChecked();

        boolean cancel = false;
        View focusView = null;



        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(passwordInput) || !isPasswordValid(passwordInput)) {
            password.setError(getString(R.string.error_invalid_password));
            focusView = password;
            cancel = true;
        }


        // Check for a valid mobile number
       if(TextUtils.isEmpty(phoneInput) || phoneInput.length()<10 || phoneInput.length()>10){
           phone.setError(getString(R.string.phone_number_invalid));
           focusView = phone;
           cancel = true;
       }


        // Check for a valid email address.
        if (TextUtils.isEmpty(emailInput)) {
            email.setError(getString(R.string.error_field_required));
            focusView = email;
            cancel = true;
        } else if (!isEmailValid(emailInput)) {
            email.setError(getString(R.string.error_invalid_email));
            focusView = email;
            cancel = true;
        }
        if(!checkBoxInput){
            showErrorMethod("Please accept the Terms and Conditions");
            focusView = checkBox;
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

    private boolean isPasswordValid(String password) {
        //TODO: Add own logic to check for a valid password (minimum 6 characters)
        return password.length()>6;
    }

    // TODO: Create a Firebase user
    private void createFirebaseUser(){
        String emailInput = email.getText().toString();
        String passwordInput = password.getText().toString();
        final String phoneInput = "+91" + phone.getText().toString();
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("Registering");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(emailInput,passwordInput).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("Notification","Create User Completion " + task.isSuccessful());
                if(!task.isSuccessful()) {
                    Log.d("Notification","create user completion failed "+ task.getException());
                    showErrorMethod("Registration Failed!! Please enter the valid Credentials");
                    progressDialog.dismiss();
                }
                else{

                    saveDisplayName();
                    Intent intent = new Intent(SignUpActivity.this,PhoneAuthentication.class);
                    intent.putExtra("phonenumber",phoneInput);
                    finish();

                    startActivity(intent);
                    progressDialog.dismiss();

                }
            }
        });
    }

    // TODO: Save the display name to Shared Preferences
    private void saveDisplayName(){
        String userName = name.getText().toString();
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
