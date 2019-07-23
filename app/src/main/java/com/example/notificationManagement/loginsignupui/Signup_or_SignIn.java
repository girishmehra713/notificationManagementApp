package com.example.notificationManagement.loginsignupui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;

public class Signup_or_SignIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Animation anim;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_or__sign_in);

        Button signinbutton = (Button) findViewById(R.id.sign_in_button);

        Button signupbutton = (Button) findViewById(R.id.sign_up_button);

        ImageView person = (ImageView) findViewById(R.id.person);










        person.setTranslationY(-500f);
        person.animate().alpha(1).translationYBy(500f).setDuration(2000);

        signinbutton.setTranslationY(-500f);
        signinbutton.animate().alpha(1).translationYBy(500f).setDuration(2000);

        signupbutton.setTranslationY(500f);
        signupbutton.animate().alpha(1).translationYBy(-500f).setDuration(2000);


        signinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signup_or_SignIn.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signup_or_SignIn.this,SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

}
