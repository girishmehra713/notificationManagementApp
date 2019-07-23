package com.example.notificationManagement.loginsignupui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    boolean firstTime;

    private ViewPager viewPager;
    int pos;
    Button nextbutton, previousbutton, tabLayout, getStartedbutton;
    Animation animation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_home);

        //sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);

        //firstTime = sharedPreferences.getBoolean("firsttime", true);

        //if (firstTime) {

        //  SharedPreferences.Editor editor = sharedPreferences.edit();

        //firstTime = false;

        //editor.putBoolean("firsttime", false);

        //editor.apply();

        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_animation);


        getStartedbutton = (Button) findViewById(R.id.getstarted_button);
        previousbutton = (Button) findViewById(R.id.prev_button);
        nextbutton = (Button) findViewById(R.id.next_button);
        YoYo.with(Techniques.Swing)
                .duration(1000).repeat(1).playOn(previousbutton);
        YoYo.with(Techniques.Swing)
                .duration(1000).repeat(1).playOn(nextbutton);


        final TabLayout tabLayout;
        tabLayout = findViewById(R.id.tab_layout);


        viewPager = findViewById(R.id.screen_pager);


        // viewPager.setBackgroundColor(Color.TRANSPARENT));


        final List<ScreenObject> mlist = new ArrayList<>();

        mlist.add(new ScreenObject("INTRODUCTION", "This is an Application for Handling The University Alerts and Notifications In One panel.", R.drawable.logo_app, R.color.colorAccent));

        mlist.add(new ScreenObject("USER FRIENDLY", "The User Interface of the app is very Interactive and Impressive for handling smooth Responses ", R.drawable.uiux, R.color.colorPrimary));

        mlist.add(new ScreenObject("SECURITY", "There Is Mobile Verification of users In the Application for Security so that no third party person can Interfere", R.drawable.ic_security_black_24dp, R.color.colorPrimaryDark));

        final ScreenPagerAdapter screenPagerAdapter = new ScreenPagerAdapter(this, mlist);

        viewPager.setAdapter(screenPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == mlist.size() - 1) {
                    nextbutton.setVisibility(View.GONE);
                    previousbutton.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.VISIBLE);
                    getStartedbutton.setVisibility(View.VISIBLE);
                    getStartedbutton.setAnimation(animation);
                    YoYo.with(Techniques.Landing)
                            .duration(1000)
                            .playOn(getStartedbutton);


                } else {

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        previousbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos = viewPager.getCurrentItem();
                if (pos >= 0) {
                    pos = mlist.size();
                    viewPager.setCurrentItem(pos);

                }
            }
        });


        nextbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                pos = viewPager.getCurrentItem();
                if (pos < mlist.size()) {

                    pos++;
                    viewPager.setCurrentItem(pos);

                    //   viewPager.setBackgroundColor(R.color.backgroundview2);


                }

                if (pos == mlist.size() - 1) {


                    nextbutton.setVisibility(View.GONE);
                    previousbutton.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.VISIBLE);
                    getStartedbutton.setVisibility(View.VISIBLE);
                    getStartedbutton.setAnimation(animation);


                }


            }
        });


        getStartedbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, Signup_or_SignIn.class));
                finish();
            }
        });


        /*}
        else {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        }
        */

    }


}
