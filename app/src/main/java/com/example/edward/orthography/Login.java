package com.example.edward.orthography;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
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


public class Login extends AppCompatActivity implements Validator.ValidationListener{

    sessionManager manager;

    @NotEmpty
    @Email(message = "Email incorrecto")
    EditText txtParametro1 ;

    @NotEmpty
    EditText txtParametro2 ;

    Validator validator;

    Button btnRestablecer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        manager = new sessionManager();

        txtParametro1 = (EditText) findViewById(R.id.txtEmail);
        txtParametro2 = (EditText) findViewById(R.id.txtPass);
        btnRestablecer = (Button) findViewById(R.id.btnRestablecer);
        validator = new Validator(this);
        validator.setValidationListener(this);
        RestablecerPassword();
        acceder();
    }


    public void RestablecerPassword(){

        btnRestablecer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generarPreguntaPassword();
            }
        });
    }


    private void generarPreguntaPassword(){


        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Restablecer contraseña");
        alert.setMessage("Ingresa tu correo electrónico");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        alert.setView(input);

        alert.setPositiveButton("Enviar instrucciones", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String srt = input.getEditableText().toString();
                Log.i("info",srt);
            }
        });
        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();


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


   public void MensajeBox(String mensaje,String titulo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(mensaje)
                .setTitle(titulo)
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

        int [] resultado;
        String correo;
        String contrasenia;
        String ex;
        //estado,idusuario,nivel
        ServiceLogin(String correo, String contrasenia){
            this.correo =correo;
            this.contrasenia=contrasenia;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            if(android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();

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

                SoapObject resSoap =(SoapObject)envelope.getResponse();
                usuario acceso = new usuario();

                //resSoap.getPropertyCount()

                resultado = new int[3];

                resultado[0] = Integer.valueOf(resSoap.getProperty(0).toString());
                resultado[1] = Integer.valueOf(resSoap.getProperty(1).toString());
                if(resSoap.getPropertyCount()==3){
                    resultado[2] = Integer.valueOf(resSoap.getProperty(2).toString());
                }else{
                    resultado[2] = Integer.valueOf(resSoap.getProperty(3).toString());
                }


                // Esta sección está destina si el Métdo del WS retorna valores
                //SoapPrimitive resultado_xml =(SoapPrimitive)envelope.getResponse();
                //resultado = resultado_xml.toString();
                //si es correcto el usuario trae 3 sino trae 4
                //<Nivel>int</Nivel>
               // <IdUsuario>int</IdUsuario>
               // <Mensaje>string</Mensaje>
               // <Estado>int</Estado>

            } catch (Exception e) {
                 resul = false;
                 ex = e.getMessage();
            }
            return resul;
        }

        protected void onPostExecute(Boolean result) {
            if (result) {
               //MensajeBox(resultado[0] + " "+ resultado[1] + " " + resultado[2],"prueba");
                if(resultado[2]==1){
                    manager.setPreferences(Login.this, "status", "1");
                    String status=manager.getPreferences(Login.this,"status");
                    Log.d("status", status);
               //     startActivity(new Intent(Login.this,MainActivity.class));

                    Intent i = new Intent (Login.this, MainActivity.class);
                    i.putExtra("correo", txtParametro1.getText()+"");
                    i.putExtra("nombre", txtParametro2.getText()+"");
                    i.putExtra("imagen",2130837590);
                    startActivity(i);

                }else{

                    MensajeBox("Correo o password incorrectos.","Información");
                }
            }else {
                MensajeBox("No se ha podido conectar con el servidor." +
                        "Compruebe tu conexión a Internet y vuelve a intentarlo","Error de conexión");
            }
        }
    }//fin de clase ServiceLogin




    public class ServiceJuego extends AsyncTask<String,Integer,Boolean> {

        //http://www.tesis2016.somee.com/ManejoJuegos.asmx?WSDL
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
                        " Parametro 4: "+parametros[3],"prueba");
            }else {
                MensajeBox("No se ha podido conectar con el servidor." +
                        "Compruebe tu conexión a Internet y vuelve a intentarlo","Error de conexión");
            }
        }
    }//fin de clase ServiceLogin


}//fin de login



