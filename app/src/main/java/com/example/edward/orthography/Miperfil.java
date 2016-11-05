package com.example.edward.orthography;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Text;

import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 */
public class Miperfil extends Fragment implements Validator.ValidationListener{

    sessionManager manager;
    String correo;
    int nivel;
    int idUsuario;
    int puntos;
    double estrellas;
    String nombre;
    int idimagen;


    public Miperfil() {
        // Required empty public constructor
    }

    @NotEmpty(message = "Este campo es requerido")
    EditText Rpass;

    @Password(min = 6,message = "Password incorrecta, mínimo 6 caracteres")
    EditText Rnueva;
    @ConfirmPassword(message = "Passwords no coinciden")
    EditText Rconfirma;

    @NotEmpty(message = "Este campo es requerido")
    EditText Rname;
    @Email(message = "Email incorrecto")
    EditText Rcorreo;


    Validator validator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View hview = inflater.inflate(R.layout.fragment_miperfil, container, false);

        validator = new Validator(this);
        validator.setValidationListener(this);

        manager = new sessionManager();

        Bundle b = getArguments();
        correo = b.getString("correo");
        idUsuario = b.getInt("idUsuario");
        nivel = b.getInt("nivel");
        puntos = b.getInt("puntos");
        estrellas = b.getDouble("estrellas");
        nombre= b.getString("nombre");
        idimagen = b.getInt("idimagen");

        Rpass = (EditText)hview.findViewById(R.id.Rpassword);
        Rname = (EditText)hview.findViewById(R.id.Rname);
        Rcorreo = (EditText)hview.findViewById(R.id.Rcorreo);
        Rnueva = (EditText) hview.findViewById(R.id.RNueva);
        Rconfirma = (EditText)hview.findViewById(R.id.Rconfirma);


        TextView Rpuntos = (TextView)hview.findViewById(R.id.Rpuntos);
        TextView RNivel = (TextView)hview.findViewById(R.id.Rnivel);
        ImageView Rimg = (ImageView)hview.findViewById(R.id.Rimg);

        Button Rguardar = (Button)hview.findViewById(R.id.Rguardar);

        Rpuntos.setText("Puntos: "+puntos);
        RNivel.setText("Nivel: "+nivel);
        Rcorreo.setText(correo.trim());
        Rname.setText(nombre.trim());
        Rimg.setImageResource(idimagen);


        Rguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();  //validamos

            }
        });








        return hview;
    }

    @Override
    public void onValidationSucceeded() {
        //Registrar2.ServiceRegistrar consumirWS = new Registrar2.ServiceRegistrar(c,p,idavatar,nombre);
        //consumirWS.execute();
 //hacer trim antes d einsertar los datos
        try {
            ValidarPassword a = new ValidarPassword();
            String us = String.valueOf(idUsuario);
            String enviar =us+";"+Rpass.getText().toString();
            SoapPrimitive idp = a.execute(enviar).get();
            if(idp==null){
                MensajeBox("No se ha podido conectar con el servidor." +
                        " Compruebe su conexión a Internet y vuelve a intentarlo.","Error de conexión");
            }else {

                String rtn = idp.toString();
                if(rtn.equals("1")) {

                    UpdatePerfil up = new UpdatePerfil();
                    String en = us+";"+Rnueva.getText().toString()+";"+Rcorreo.getText().toString().trim()+";"+Rname.getText().toString().trim();
                    SoapPrimitive h = up.execute(en).get();

                    if(h==null){
                        MensajeBox("No se ha podido conectar con el servidor." +
                                " Compruebe su conexión a Internet y vuelve a intentarlo.","Error de conexión");
                    }else{
                        String r = h.toString();
                        if(r.equals("1")){

                            MensajeBox("Se han actualizado sus datos.","Información");
                            //actualizar variablles de sesión
                            manager.setPreferences(getContext(),"correo",Rcorreo.getText().toString().trim());
                            manager.setPreferences(getContext(),"Nombre",Rname.getText().toString().trim());


                        }else{
                            MensajeBox("No se ha podido actualizar sus datos" +
                                    ", intentelo de nuevo.","Información");
                        }

                    }



                }else{
                    MensajeBox("El password ingresado no coincide con el actual.","Password incorrecto");
                }


            }
        } catch (InterruptedException | ExecutionException e) {
            MensajeBox("No se ha podido conectar con el servidor." +
                    " Compruebe su conexión a Internet y vuelve a intentarlo.","Error de conexión");
        }


    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getContext());

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            }else{
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }
    }


    public void MensajeBox(String mensaje,String titulo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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


    public class ValidarPassword extends AsyncTask<String,String,SoapPrimitive> {
        private SoapPrimitive resSoap;

        @Override
        protected SoapPrimitive doInBackground(String... params) {
            if (android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();

            final String SOAP_ACTION = "http://tempuri.org/verificarAcceso";
            final String METHOD_NAME = "verificarAcceso";
            final String NAMESPACE = "http://tempuri.org/";
            final String URL = "http://www.tesis2016g1.somee.com/ManejoUsuarios.asmx";

            try {
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

                request.addProperty("datos", params[0]);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true; // para WS ASMX, sólo si fue construido con .Net
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

                androidHttpTransport.call(SOAP_ACTION, envelope);

                // Esta sección está destina si el Métdo del WS retorna valores
                resSoap = (SoapPrimitive) envelope.getResponse();
                // idPartida = Integer.valueOf(resSoap.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return resSoap;
        }
    }




    public class UpdatePerfil extends AsyncTask<String,String,SoapPrimitive> {
        private SoapPrimitive resSoap;

        @Override
        protected SoapPrimitive doInBackground( String... params) {
            if(android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();

            final String SOAP_ACTION = "http://tempuri.org/actualizarPerfil";
            final String METHOD_NAME = "actualizarPerfil";
            final String NAMESPACE = "http://tempuri.org/";
            final String URL = "http://www.tesis2016g1.somee.com/ManejoUsuarios.asmx";

            try {
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

                request.addProperty("datos", params[0]);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true; // para WS ASMX, sólo si fue construido con .Net
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

                androidHttpTransport.call(SOAP_ACTION, envelope);

                // Esta sección está destina si el Métdo del WS retorna valores
                resSoap =(SoapPrimitive)envelope.getResponse();
                // idPartida = Integer.valueOf(resSoap.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resSoap;
        }
    }




}
