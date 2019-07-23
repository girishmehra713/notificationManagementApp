package com.example.notificationManagement.loginsignupui;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity{


    private EditText email,password;
    private CheckBox checkBox;
    private ImageButton signin;
    private Button signup;
    private FirebaseAuth mAuth;
    private String userRole;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressDialog = new ProgressDialog(this);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);

        checkBox = (CheckBox)findViewById(R.id.checkbox);

        signin =(ImageButton)findViewById(R.id.signin);

        signup = (Button) findViewById(R.id.signup);

        signin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Toast.makeText(LoginActivity.this,"LogIn in Progress..",Toast.LENGTH_LONG).show();
                attemptLogin();
            }
        });
        signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(i);
            }
        });


        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == 100 || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        // TODO: Grab an instance of FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
    }

    // TODO: Complete the attemptLogin() method
    private void attemptLogin() {

        String emailInput = email.getText().toString();
        String passwordInput = password.getText().toString();
        if(emailInput.equals("")||passwordInput.equals("")){ showErrorMethod("Please Enter the Credentials To Login");}
        mProgressDialog.setTitle("Log In");
        mProgressDialog.setMessage("Authenticating");
        mProgressDialog.setCanceledOnTouchOutside(true);
        mProgressDialog.show();
//        Toast.makeText(this,"Login in Progress",Toast.LENGTH_SHORT).show();
        // TODO: Use FirebaseAuth to sign in with email & password
        mAuth.signInWithEmailAndPassword(emailInput,passwordInput).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("FlashChat","sign in = "+ task.isSuccessful());
                if(!task.isSuccessful()){
                    Log.d("FlashChat","Sign in Failed:" + task.getException());
                    showErrorMethod("Please Enter Valid Credentials!");
                    mProgressDialog.dismiss();
                }else{

                    Intent intent = new Intent(LoginActivity.this,UserChatActivity.class);
                    finish();
                    startActivity(intent);
                    mProgressDialog.dismiss();
//                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                    if (user != null) {
//                        String uid = user.getUid();
//                        if(!uid.equals("95SXwZNvQwS4fSQV4o7EPUEPe6J3")){
//                            userRole = "user";
//                        }
//                        else{
//                            userRole = "admin";
//                        }
//
//                    }
//                    if(userRole.equals("user")){
//                        Intent intent = new Intent(LoginActivity.this,UserChatActivity.class);
//                        finish();
//                        startActivity(intent);
//                    }
//                    else if(userRole.equals("admin")){


                }
            }
        });

    }

    // TODO: Show error on screen with an alert dialog

    private void showErrorMethod(String message){
        new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}