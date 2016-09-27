package com.example.edward.orthography;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Presentacion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentacion);
        NuevaCuenta();
        Cuentaold();
    }


    public void NuevaCuenta(){
        Button nueva = (Button) findViewById(R.id.btnNuevaCuenta);

        nueva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Presentacion.this,Registrar1.class));
            }
        });
    }

    public void Cuentaold(){
        Button nueva = (Button) findViewById(R.id.btnTengoCuenta);

        nueva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Presentacion.this,Login.class));
            }
        });

   }



}
