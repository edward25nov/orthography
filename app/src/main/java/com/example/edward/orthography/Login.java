package com.example.edward.orthography;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.List;
import java.util.StringTokenizer;

public class Login extends AppCompatActivity implements Validator.ValidationListener{
    @NotEmpty
    @Email(message = "Email incorrecto")
    EditText txtParametro1 ;

    @NotEmpty
    EditText txtParametro2 ;

    Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtParametro1 = (EditText) findViewById(R.id.txtEmail);
        txtParametro2 = (EditText) findViewById(R.id.txtPass);
        validator = new Validator(this);
        validator.setValidationListener(this);

        acceder();
    }

    public void acceder(){
        Button nueva = (Button) findViewById(R.id.btnlogin);
        nueva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();  //validamos


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

    @Override
    public void onValidationSucceeded() { // si cumple validaciones de entrada mando a llamar
        //ServiceJuego consumirWS = new ServiceJuego(1);
       ServiceLogin consumirWS = new ServiceLogin(txtParametro1.getText().toString(),txtParametro2.getText().toString());
        consumirWS.execute();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors)
        {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            }
            else
            {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }


    public class ServiceLogin extends AsyncTask<String,Integer,Boolean> {

        //http://www.tesis2016.somee.com/ManejoUsuario.asmx?WSDL

        String resultado;
        String correo;
        String contrasenia;
        ServiceLogin(String correo, String contrasenia){
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
               // MensajeBox(resultado);

                StringTokenizer st = new StringTokenizer(resultado, "|");
                String a1 = st.nextToken();
                String a2 = st.nextToken();
                if(a1.equals("exito")){
                    MensajeBox("Bienvenido "+ a2);
                    startActivity(new Intent(Login.this,MainActivity.class));
                }else{
                    MensajeBox("Correo o password incorrectos.");
                }
            }else {
                MensajeBox("Error: "+resultado);
            }
        }
    }//fin de clase ServiceLogin




    public class ServiceJuego extends AsyncTask<String,Integer,Boolean> {

        //http://www.tesis2016.somee.com/ManejoUsuario.asmx?WSDL
        String resultado;
        String[] parametros;
        int nivel;
        ServiceJuego(int nivel){
            this.nivel =nivel;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            //para poder debuggear
            if(android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();

            final String SOAP_ACTION = "http://tempuri.org/obtenerSeleccion";
            final String METHOD_NAME = "obtenerSeleccion";
            final String NAMESPACE = "http://tempuri.org/";
            final String URL = "http://www.tesis2016.somee.com/ManejoJuegos.asmx";
            boolean resul = true;
            try {
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

                request.addProperty("nivel", nivel);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true; // para WS ASMX, sólo si fue construido con .Net
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

                androidHttpTransport.call(SOAP_ACTION, envelope);

                // Esta sección está destina si el Métdo del WS retorna valores

                SoapObject resSoap =(SoapObject)envelope.getResponse();

                parametros = new String[4];

                parametros[0] = String.valueOf(resSoap.getProperty(0));
                parametros[1] = String.valueOf(resSoap.getProperty(1));
                parametros[2] = String.valueOf(resSoap.getProperty(2));
                parametros[3] = String.valueOf(resSoap.getProperty(3));

            } catch (Exception e) {
                resul = false;
                resultado  = e.getMessage();
               // MensajeBox(e.getMessage());
            }
            return resul;
        }

        protected void onPostExecute(Boolean result) {
            if (result) {
                MensajeBox("Parametro 1: "+parametros[0] +
                        " Parametro 2: "+parametros[1]+
                        " Parametro 3: "+parametros[2] +
                        " Parametro 4: "+parametros[3]);
            }else {
                MensajeBox("Error: " + resultado);
            }
        }
    }//fin de clase ServiceLogin


}//fin de login



