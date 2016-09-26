package com.example.edward.orthography;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class Login extends AppCompatActivity {
    EditText txtParametro1 ;
    EditText txtParametro2 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtParametro1 = (EditText) findViewById(R.id.txtEmail);
        txtParametro2 = (EditText) findViewById(R.id.txtPass);
        acceder();
    }

    public void acceder(){
        Button nueva = (Button) findViewById(R.id.btnlogin);
        nueva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // startActivity(new Intent(Login.this,MainActivity.class));
               // Toast.makeText(Login.this, "Bienvenido!!!", Toast.LENGTH_LONG).show();
                ServicioLogin consumirWS = new ServicioLogin(txtParametro1.getText().toString(),txtParametro2.getText().toString());
                consumirWS.execute();
            }
        });

    }


   public void MensajeBox(String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(mensaje)
                .setTitle("Información")
                .setCancelable(false)
                .setNeutralButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public class ServicioLogin extends AsyncTask<String,Integer,Boolean> {

        //http://www.tesis2016.somee.com/ManejoUsuario.asmx?WSDL

        String resultado;
        String correo;
        String contrasenia;
        ServicioLogin(String correo,String contrasenia){
            this.correo =correo;
            this.contrasenia=contrasenia;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            final String SOAP_ACTION = "http://tempuri.org/logIn";
            final String METHOD_NAME = "logIn";
            final String NAMESPACE = "http://tempuri.org/";
            final String URL = "http://www.tesis2016.somee.com/ManejoUsuario.asmx";
            boolean resul = true;
            try {
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

                request.addProperty("correo", correo);
                request.addProperty("contrasenia", contrasenia);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true; // para WS ASMX, sólo si fue construido con .Net
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

                androidHttpTransport.call(SOAP_ACTION, envelope);

                // Esta sección está destina si el Métdo del WS retorna valores
                SoapPrimitive resultado_xml =(SoapPrimitive)envelope.getResponse();
                resultado = resultado_xml.toString();

            } catch (Exception e) {
                 resul = false;
                  resultado = e.getMessage();

                // MensajeBox(e.getMessage());
            }
            return resul;
        }

        protected void onPostExecute(Boolean result) {
            if (result) {
                MensajeBox(resultado);
            }else {
                MensajeBox("Error "+resultado);
            }
        }

    }//fin de clase ServicioLogin

}//fin de login



