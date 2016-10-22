package com.example.edward.orthography;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.example.edward.orthography.R.*;

public class PlaySeleccion extends AppCompatActivity {

    int idPartida;
    Button btnCalificarSeleccion;
    ProgressBar ProgressBarScore;
    ArrayList<String> MispalabrasActuales = new ArrayList<>();
    String correctaActual;
    String seleccionoActual;
    LinearLayout layout;
    int AvancePreguntas;
    int buenas;
    int malas;
    //variables que vienen del fragmento
    String fcorreo;
    int fnivel;
    int fidUsuario;
    int fpuntos;
    double festrellas;
    String fnombre;
    int fidimagen;
    //datos para el msj personalizado
    TextView m;
    View hView;
    ImageView logomsj;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_seleccion);

        /****
         * *******/

        btnCalificarSeleccion  = (Button)findViewById(id.btnCalificar);
        layout = (LinearLayout) findViewById(id.layout_palabras);
        ProgressBarScore = (ProgressBar) findViewById(id.ProgressBarScore);
        ProgressBarScore.setProgress(0);
        btnCalificarSeleccion.setEnabled(false);

        fcorreo = getIntent().getStringExtra("correo");
        fnivel = getIntent().getIntExtra("nivel",-1);
        fidUsuario = getIntent().getIntExtra("idUsuario",-1);
        fpuntos = getIntent().getIntExtra("puntos",-1);
        festrellas = getIntent().getDoubleExtra("Estrellas",-1);
        fnombre = getIntent().getStringExtra("Nombre");
        fidimagen = getIntent().getIntExtra("Imagen",-1);


        try {
            CrearPartida a = new CrearPartida();
            SoapPrimitive idp = (SoapPrimitive) a.execute(fidUsuario,fnivel).get();
            idPartida = Integer.valueOf(idp.toString());

            juegoSeleccion cons = new juegoSeleccion();
            SoapObject resSoap = cons.execute(fnivel,idPartida).get();
            correctaActual = resSoap.getProperty(0).toString();
            SoapObject items = (SoapObject)resSoap.getProperty(1);
            MispalabrasActuales = new ArrayList<>(items.getPropertyCount());
            for(int i=0;i<items.getPropertyCount();i++){
                MispalabrasActuales.add(String.valueOf(items.getProperty(i)));
            }
            generarListaPalabras(MispalabrasActuales);
            //para la primer pregunta
            AvancePreguntas = 10;
            ProgressBarScore.setProgress(AvancePreguntas);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        btnCalificarSeleccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    boolean flag=false;
                    if(AvancePreguntas>100){ //vamos a terminar partida

                        try {
                            TerminarPartida a = new TerminarPartida();
                            SoapObject idp = null;
                            idp = (SoapObject) a.execute(fidUsuario,idPartida,buenas).get();
                            int Nivel = Integer.valueOf(idp.getProperty(0).toString());
                            double Estrellas = Double.valueOf(idp.getProperty(1).toString());
                            int Puntos = Integer.valueOf(idp.getProperty(2).toString());

                            fnivel = Nivel;
                            festrellas = Estrellas;
                            fpuntos = Puntos;


                            YoYo.with(Techniques.Tada)
                                    .duration(1000)
                                    .playOn(findViewById(id.layout_palabras));
                           // int malas = 10-Score;
                            mensajepostivo("Score\n Buenas : " +buenas +"\nMalas : "+malas,true,3);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }else{
                        if(seleccionoActual==correctaActual){
                            mensajepostivo("Tu respuesta es \n correcta",false,1);
                            buenas = buenas +1;
                        }else{
                            YoYo.with(Techniques.Swing)
                                    .duration(1000)
                                    .playOn(findViewById(id.layout_palabras));
                            mensajepostivo("La respuesta\n correcta es:\n"+correctaActual +"\n",false,2);
                           malas = malas + 1;
                        }
                    }





            }
        });
    }


    @Override
    public void onBackPressed(){
            super.onBackPressed();
            moveTaskToBack(true);
    }

    public void generarListaPalabras(final ArrayList<String> palabras){
         for (int j = 0; j < palabras.size(); j++ ){
            final Button boton = new Button(getApplicationContext());
            boton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            boton.setText(palabras.get(j));
            boton.setGravity(Gravity.CENTER | Gravity.TOP);
            boton.setId(j);
            boton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    YoYo.with(Techniques.Bounce)
                            .duration(1000)
                            .playOn(findViewById(boton.getId()));
                    seleccionoActual = boton.getText().toString();
                    btnCalificarSeleccion.setEnabled(true);

                    boton.setBackgroundColor(getResources().getColor(color.azul500));

                    //bloquear todo lo que tiene adentro el layout
                    for (int i = 0; i < layout.getChildCount(); i++) {
                        View child = layout.getChildAt(i);
                        //child.setEnabled(false);
                          child.setClickable(false);
                    }
                }
            });

            layout.addView(boton);
        }


    }

    public class CrearPartida extends AsyncTask<Integer,String,SoapPrimitive>{
        SoapPrimitive resSoap;

        @Override
        protected SoapPrimitive doInBackground(Integer... params) {
            if(android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();

            final String SOAP_ACTION = "http://tempuri.org/crearPartida";
            final String METHOD_NAME = "crearPartida";
            final String NAMESPACE = "http://tempuri.org/";
            final String URL = "http://www.tesis2016.somee.com/ManejoJuegos.asmx";
            boolean resul = true;

            try {
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

                request.addProperty("idUsuario", params[0]);
                request.addProperty("nivel",params[1]);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true; // para WS ASMX, sólo si fue construido con .Net
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

                androidHttpTransport.call(SOAP_ACTION, envelope);

                // Esta sección está destina si el Métdo del WS retorna valores
                resSoap =(SoapPrimitive)envelope.getResponse();
               // idPartida = Integer.valueOf(resSoap.toString());
            } catch (Exception e) {
                resul = false;
            }
            return resSoap;
        }


    }

    public class juegoSeleccion extends AsyncTask<Integer,String,SoapObject> {

        SoapObject retorno;
        //http://www.tesis2016.somee.com/ManejoJuegos.asmx?WSDL
        @Override
        protected SoapObject doInBackground(Integer... params) {
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

                request.addProperty("nivel", params[0]);
                request.addProperty("idPartida",params[1]);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true; // para WS ASMX, sólo si fue construido con .Net
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

                androidHttpTransport.call(SOAP_ACTION, envelope);

                // Esta sección está destina si el Métdo del WS retorna valores
                retorno =(SoapObject)envelope.getResponse();

            } catch (Exception e) {

            }
            return retorno;
        }

    }

    public class TerminarPartida extends AsyncTask<Integer,String,SoapObject>{
        SoapObject resSoap;

        @Override
        protected SoapObject doInBackground(Integer... params) {
            if(android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();

            final String SOAP_ACTION = "http://tempuri.org/terminarPartida";
            final String METHOD_NAME = "terminarPartida";
            final String NAMESPACE = "http://tempuri.org/";
            final String URL = "http://www.tesis2016.somee.com/ManejoJuegos.asmx";
            boolean resul = true;

            try {
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

                request.addProperty("idUsuario", params[0]);
                request.addProperty("idPartida",params[1]);
                request.addProperty("puntos",params[2]);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true; // para WS ASMX, sólo si fue construido con .Net
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

                androidHttpTransport.call(SOAP_ACTION, envelope);
                // Esta sección está destina si el Métdo del WS retorna valores
                resSoap =(SoapObject)envelope.getResponse();
            } catch (Exception e) {
                resul = false;
            }
            return resSoap;
        }


    }


/*
    public void MensajeBox(String mensaje, String titulo, final boolean bandera) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(mensaje)
                .setTitle(titulo)
                .setCancelable(false)
                .setNeutralButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if(bandera){
                                    Intent i = new Intent(PlaySeleccion.this,MainActivity.class);
                                    i.putExtra("correo",fcorreo);
                                    i.putExtra("nivel",fnivel);
                                    i.putExtra("idUsuario",fidUsuario);
                                    i.putExtra("puntos",fpuntos);
                                    i.putExtra("Estrellas",festrellas);
                                    i.putExtra("Nombre",fnombre);
                                    i.putExtra("Imagen",fidimagen);
                                    startActivity(i);

                                }else{
                                    AvancePreguntas = AvancePreguntas + 10;
                                    ProgressBarScore.setProgress(AvancePreguntas);
                                    layout.removeAllViews();
                                    btnCalificarSeleccion.setEnabled(false);
                                    juegoSeleccion cons = new juegoSeleccion();
                                    SoapObject resSoap = null;
                                    try {
                                        resSoap = cons.execute(fnivel,idPartida).get();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    }
                                    correctaActual = resSoap.getProperty(0).toString();
                                    SoapObject items = (SoapObject)resSoap.getProperty(1);
                                    MispalabrasActuales = new ArrayList<>(items.getPropertyCount());
                                    for(int i=0;i<items.getPropertyCount();i++){
                                        MispalabrasActuales.add(String.valueOf(items.getProperty(i)));
                                    }
                                    generarListaPalabras(MispalabrasActuales);

                                }
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

*/



    public void mensajepostivo(String mensaje, final boolean bandera,int tipo){


        //datos para el msj personalizado
        hView =  getLayoutInflater().inflate(R.layout.layout_msjpersonalizado,null);
        m = (TextView) hView.findViewById(id.txtMsj);
        logomsj = (ImageView) hView.findViewById(id.imgMsj);
        btn = (Button)hView.findViewById(id.btnContinue);
        //tipo 1 = buena
        //tipo 2 = mala
        //tipo 3 = trofeo fin
        if(tipo==1){
            hView.findViewById(id.MsjRBuena).setBackground(getResources().getDrawable(drawable.fondo_azul));
        }else if(tipo==2){
            hView.findViewById(id.MsjRBuena).setBackground(getResources().getDrawable(drawable.fondo_rojo));
        }else if(tipo==3){
            hView.findViewById(id.MsjRBuena).setBackground(getResources().getDrawable(drawable.fondo_verde));
            logomsj.setImageResource(drawable.trofeo);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        m.setText(mensaje);
        builder.setView(hView)
                .setCancelable(false);

        final AlertDialog alert = builder.create();
        alert.show();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bandera){
                    Intent i = new Intent(PlaySeleccion.this,MainActivity.class);
                    i.putExtra("correo",fcorreo);
                    i.putExtra("nivel",fnivel);
                    i.putExtra("idUsuario",fidUsuario);
                    i.putExtra("puntos",fpuntos);
                    i.putExtra("Estrellas",festrellas);
                    i.putExtra("Nombre",fnombre);
                    i.putExtra("Imagen",fidimagen);
                    startActivity(i);

                }else{
                    AvancePreguntas = AvancePreguntas + 10;
                    ProgressBarScore.setProgress(AvancePreguntas);
                    layout.removeAllViews();
                    btnCalificarSeleccion.setEnabled(false);
                    juegoSeleccion cons = new juegoSeleccion();
                    SoapObject resSoap = null;
                    try {
                        resSoap = cons.execute(fnivel,idPartida).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    correctaActual = resSoap.getProperty(0).toString();
                    SoapObject items = (SoapObject)resSoap.getProperty(1);
                    MispalabrasActuales = new ArrayList<>(items.getPropertyCount());
                    for(int i=0;i<items.getPropertyCount();i++){
                        MispalabrasActuales.add(String.valueOf(items.getProperty(i)));
                    }
                    generarListaPalabras(MispalabrasActuales);

                }
                alert.cancel();
            }
        });

        YoYo.with(Techniques.FadeIn)
                .duration(1000)
                .playOn(hView.findViewById(id.contenedor_msj));


    }




}
