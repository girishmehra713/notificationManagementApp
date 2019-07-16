package com.example.notificationManagement.loginsignupui;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


public class PhoneAuthentication extends AppCompatActivity {
    private String verificationId;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private EditText codeInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_authentication);
         mAuth = FirebaseAuth.getInstance();
         progressBar = findViewById(R.id.progressBar);
         codeInput = findViewById(R.id.codeEntered);
         String phonenumber = getIntent().getStringExtra("phonenumber");
         sendVerificationCode(phonenumber);
         findViewById(R.id.Verify).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 String codeEnter = codeInput.getText().toString().trim();
                 if(codeEnter.isEmpty()||codeEnter.length()<6){
                     codeInput.setError("Please enter a correct code!");
                     codeInput.requestFocus();
                     return;

                 }


                 verifyCode(codeEnter);
             }
         });

    }
    private void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                         if(task.isSuccessful()){
                             Intent intent = new Intent(PhoneAuthentication.this,PhoneVerified.class);
                             intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                             startActivity(intent);
                         }else{
                             Log.d("myapp: ","Verification Failed "+ task.getException());
                             Toast.makeText(PhoneAuthentication.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                         }
                    }
                });
    }

    private void sendVerificationCode(String number){
         progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks
        );
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code!=null){
                codeInput.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(PhoneAuthentication.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    };
}
