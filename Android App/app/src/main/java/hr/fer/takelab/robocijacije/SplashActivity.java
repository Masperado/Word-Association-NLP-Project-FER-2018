package hr.fer.takelab.robocijacije;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(hr.fer.takelab.robocijacije.R.layout.activity_splash);

        //Splash screen
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent homeIntent = new Intent(SplashActivity.this, MenuActivity.class);
                startActivity(homeIntent);
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.finish();
    }
}
