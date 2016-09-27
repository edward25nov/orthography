package com.example.edward.orthography;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Registrar1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar1);
        acceder();
    }

    public void acceder(){
        Button nueva = (Button) findViewById(R.id.btnregistrar1);
        nueva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registrar1.this,Registrar2.class));
            }
        });

    }

}
