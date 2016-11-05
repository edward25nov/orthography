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

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
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
                .setIcon(R.drawable.info)
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

        //http://www.tesis2016.somee.com/ManejoUsuarioS.asmx?WSDL
        int nivel;
        int idUsuario;
        String Mensaje;
        int Estado;
        int Imagen;
        String Nombre;
        int puntos;
        double Estrellas;

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
            if (android.os.Debug.isDebuggerConnected()){
                android.os.Debug.waitForDebugger();
            }

            final String SOAP_ACTION = "http://tempuri.org/logIn";
            final String METHOD_NAME = "logIn";
            final String NAMESPACE = "http://tempuri.org/";
            final String URL = "http://www.tesis2016g1.somee.com/ManejoUsuarioS.asmx";
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
                nivel = Integer.valueOf(resSoap.getProperty(0).toString());
                idUsuario = Integer.valueOf(resSoap.getProperty(1).toString());
                Mensaje = resSoap.getProperty(2).toString();
                Estado = Integer.valueOf(resSoap.getProperty(3).toString());
                Imagen = Integer.valueOf(resSoap.getProperty(4).toString());
                Nombre = resSoap.getProperty(5).toString();
                puntos = Integer.valueOf(resSoap.getProperty(6).toString());
                Estrellas = Double.valueOf(resSoap.getProperty(7).toString());
            } catch (Exception e) {
                 resul = false;
                 ex = e.getMessage();
            }
            return resul;
        }

        protected void onPostExecute(Boolean result) {
            if (result) {
                if(Estado==1){
                    manager.setPreferences(Login.this, "status", "1");
                    manager.setPreferences(Login.this,"correo",txtParametro1.getText().toString());
                    manager.setPreferences(Login.this,"nivel",nivel+"");
                    manager.setPreferences(Login.this,"idUsuario",idUsuario+"");
                    manager.setPreferences(Login.this,"puntos",puntos+"");
                    manager.setPreferences(Login.this,"Estrellas",Estrellas+"");
                    manager.setPreferences(Login.this,"Nombre",Nombre+"");
                    manager.setPreferences(Login.this,"Imagen",Imagen+"");

                    String status=manager.getPreferences(Login.this,"status");
                    Log.d("status", status);

                    Intent i = new Intent (Login.this, MainActivity.class);
                    i.putExtra("correo", txtParametro1.getText()+"");
                    i.putExtra("nivel",nivel);
                    i.putExtra("idUsuario",idUsuario);
                    i.putExtra("puntos",puntos);
                    i.putExtra("Estrellas",Estrellas);
                    i.putExtra("Nombre",Nombre);
                    i.putExtra("Imagen",Imagen);
                    startActivity(i);

                }else{
                    Toast.makeText(Login.this, "Correo o password incorrectos", Toast.LENGTH_SHORT).show();
                    YoYo.with(Techniques.Swing)
                            .duration(1000)
                            .playOn(findViewById(R.id.txtPass));
                    YoYo.with(Techniques.Swing)
                            .duration(1000)
                            .playOn(findViewById(R.id.txtEmail));

                }
            }else {
                MensajeBox("No se ha podido conectar con el servidor." +
                        " Compruebe su conexión a Internet y vuelve a intentarlo.","Error de conexión");
            }

        }
    }//fin de clase ServiceLogin





}//fin de login



