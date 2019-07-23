package com.example.notificationManagement.loginsignupui;

        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Intent;
        import android.os.Bundle;
        import android.os.Handler;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        TextView textView = (TextView) findViewById(R.id.logo_text);

        ImageView imageView = (ImageView) findViewById(R.id.logo_image);

        imageView.setTranslationY(-500f);
        textView.setTranslationY(500f);
        imageView.animate().alpha(1).translationYBy(500f).setDuration(2000);
        textView.animate().alpha(1).translationYBy(-500f).setDuration(2000);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this,HomeActivity.class);


                startActivity(intent);

                finish();
            }
        },SPLASH_TIME_OUT);

    }

}



