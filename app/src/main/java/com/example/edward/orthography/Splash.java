package com.example.edward.orthography;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

public class Splash extends AppCompatActivity {

    //http://www.androidwarriors.com/2015/12/session-management-in-android-example.html
    sessionManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        String carpetaFuente = "fonts/AltamonteNF.ttf";//"fonts/Airmole Shaded.ttf";
        TextView vistaFuente = (TextView) findViewById(R.id.txtLogo);
        // Cargamos la fuente
        Typeface fuente = Typeface.createFromAsset(getAssets(), carpetaFuente);
        // Aplicamos la fuente
        vistaFuente.setTypeface(fuente);

        manager = new sessionManager();

        Animation ani = AnimationUtils.loadAnimation(this, R.anim.movercubo);
        ImageView abc = (ImageView) findViewById(R.id.imglogo);
        abc.setAnimation(ani);

            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String status = manager.getPreferences(Splash.this, "status");
                    Log.d("status", status);
                    if (status.equals("1")) {
                        startActivity(new Intent(Splash.this, MainActivity.class));
                    } else {
                        startActivity(new Intent(Splash.this, Presentacion.class));
                    }
                    finish();
                }
            }, 4000);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
        startActivity(intent);
        finish();
        System.exit(0);
    }

}

