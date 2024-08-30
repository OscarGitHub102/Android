package com.example.cinematics;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;

import es.dmoral.toasty.Toasty;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends Activity {

    private final int DURACION_SPLASH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);


        Thread splashThread = new Thread(() -> {
            try {

                Thread.sleep(DURACION_SPLASH);
            } catch (InterruptedException e) {

                Toasty.error(SplashActivity.this,"Error en la pantalla Splash",Toasty.LENGTH_SHORT).show();
            } finally {

                Intent intent = new Intent(SplashActivity.this, LogInActivity.class);
                startActivity(intent);
                finish();
            }
        });
        splashThread.start();
    }
}