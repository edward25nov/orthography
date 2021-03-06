package com.example.edward.orthography;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
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
    sessionManager manager;

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
    ArrayList<Button> listaBotones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_seleccion);

        listaBotones = new ArrayList<>();
        manager = new sessionManager();
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
            SoapPrimitive idp = a.execute(fidUsuario,fnivel).get();
            if(idp==null){
                MensajeBox("No se ha podido conectar con el servidor." +
                        " Compruebe su conexión a Internet y vuelve a intentarlo.","Error de conexión");
            }else {
                idPartida = Integer.valueOf(idp.toString());

                generarScenario();
            }
        } catch (InterruptedException | ExecutionException e) {
            MensajeBox("No se ha podido conectar con el servidor." +
                    " Compruebe su conexión a Internet y vuelve a intentarlo.","Error de conexión");
        }

        btnCalificarSeleccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(AvancePreguntas==100){ //vamos a terminar partida
                        try {
                            TerminarPartida a = new TerminarPartida();

                            SoapObject idp = a.execute(fidUsuario, idPartida, buenas).get();
                            if (idp == null) {
                                MensajeBox("No se ha podido conectar con el servidor." +
                                        " Compruebe su conexión a Internet y vuelve a intentarlo.","Error de conexión");
                            }else{
                                int Nivel = Integer.valueOf(idp.getProperty(0).toString());
                                double Estrellas = Double.valueOf(idp.getProperty(1).toString());
                                int Puntos = Integer.valueOf(idp.getProperty(2).toString());

                                fnivel = Nivel;
                                festrellas = Estrellas;
                                fpuntos = Puntos;
                                validarRespuesta(true);
                            }
                        } catch (InterruptedException | ExecutionException e) {
                            MensajeBox("No se ha podido conectar con el servidor." +
                                    " Compruebe su conexión a Internet y vuelve a intentarlo.","Error de conexión");
                        }
                    }else{
                        validarRespuesta(false);
                    }
            }
        });
    }


    public void validarRespuesta(boolean finalizar){
        if(finalizar){
            if(seleccionoActual.equals(correctaActual)){
                buenas = buenas +1;
                MsjCorrecto dialogFragment = MsjCorrecto
                        .newInstance("Tu respuesta es\ncorrecta",true,"Score\nBuenas: " +buenas +"\nMalas: "+malas,1);
                dialogFragment.show(getFragmentManager(), "Buena");

            }else{
                YoYo.with(Techniques.Swing)
                        .duration(1000)
                        .playOn(findViewById(id.layout_palabras));
                malas = malas + 1;
                MsjNegativo dialogFragment = MsjNegativo
                        .newInstance("La respuesta\ncorrecta es:\n"+correctaActual,true,"Score\nBuenas: " +buenas +"\nMalas: "+malas,1);
                dialogFragment.show(getFragmentManager(),"Mala");

            }

        }else{
            if(seleccionoActual.equals(correctaActual)){
                buenas = buenas +1;
                MsjCorrecto dialogFragment = MsjCorrecto
                        .newInstance("Tu respuesta es\ncorrecta",1);
                dialogFragment.show(getFragmentManager(), "Buena");
            }else{
                YoYo.with(Techniques.Swing)
                        .duration(1000)
                        .playOn(findViewById(id.layout_palabras));
                malas = malas + 1;
                MsjNegativo dialogFragment = MsjNegativo
                        .newInstance("La respuesta\ncorrecta es:\n"+correctaActual,1);
                dialogFragment.show(getFragmentManager(),"Mala");

            }

        }


    }


    public void generarScenario(){
        AvancePreguntas = AvancePreguntas + 10;
        ProgressBarScore.setProgress(AvancePreguntas);
        layout.removeAllViews();
        listaBotones.clear();
        btnCalificarSeleccion.setEnabled(false);
        juegoSeleccion cons = new juegoSeleccion();
        try {
            SoapObject resSoap  = cons.execute(fnivel,idPartida).get();

            if(resSoap==null){
                MensajeBox("No se ha podido conectar con el servidor." +
                        " Compruebe su conexión a Internet y vuelve a intentarlo.","Error de conexión");
            }else {

                correctaActual = resSoap.getProperty(0).toString();
                SoapObject items = (SoapObject) resSoap.getProperty(1);
                MispalabrasActuales = new ArrayList<>(items.getPropertyCount());
                for (int i = 0; i < items.getPropertyCount(); i++) {
                    MispalabrasActuales.add(String.valueOf(items.getProperty(i)));
                }
                generarListaPalabras(MispalabrasActuales);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void finalizarScenario(){
        Intent i = new Intent(PlaySeleccion.this,MainActivity.class);
        i.putExtra("correo",fcorreo);
        i.putExtra("nivel",fnivel);
        i.putExtra("idUsuario",fidUsuario);
        i.putExtra("puntos",fpuntos);
        i.putExtra("Estrellas",festrellas);
        i.putExtra("Nombre",fnombre);
        i.putExtra("Imagen",fidimagen);
        startActivity(i);

        manager.setPreferences(PlaySeleccion.this,"puntos",fpuntos+"");
        manager.setPreferences(PlaySeleccion.this,"Estrellas",festrellas+"");
        manager.setPreferences(PlaySeleccion.this,"nivel",fnivel+"");

    }

    public void generarListaPalabras(final ArrayList<String> palabras){

         for (int j = 0; j < palabras.size(); j++ ){
            final Button boton = new Button(getApplicationContext());
            boton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            boton.setText(palabras.get(j));
            boton.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            boton.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.boton_opcionesnormal));
            boton.setId(j);
            boton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    YoYo.with(Techniques.Bounce)
                            .duration(1000)
                            .playOn(findViewById(boton.getId()));
                    seleccionoActual = boton.getText().toString();
                    btnCalificarSeleccion.setEnabled(true);

                    for(int k=0;k<listaBotones.size();k++){
                        Button actual = listaBotones.get(k);
                        if(boton.getId()==actual.getId()){
                            actual.setBackground(ContextCompat.getDrawable(getApplicationContext(), drawable.boton_opcionseleccion));
                        }else{
                            actual.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.boton_opcionesnormal));
                            //actual.setBackgroundColor(getResources().getColor(color.azul500));
                        }
                    }

                }
            });
            layout.addView(boton);
            listaBotones.add(boton);
        }
    }


   /* @Override
    public void onBackPressed(){
        super.onBackPressed();
        moveTaskToBack(true);
    }*/

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
            final String URL = "http://www.tesis2016g1.somee.com/ManejoJuegos.asmx";
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
                  //fadfadf
            }
            return retorno;
        }

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
}
