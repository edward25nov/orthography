package com.example.edward.orthography;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

public class PlayEscritura extends AppCompatActivity {

    sessionManager manager;
    //variables globales
    String fcorreo;
    int fnivel;
    int fidUsuario;
    int fpuntos;
    double festrellas;
    String fnombre;
    int fidimagen;
    //variables manejo de juego
    int AvancePreguntas;
    int buenas;
    int malas;
    String correctaActual;
    int idPartida;
    //componentes de layout
    Button calificar;
    TextView oracion;
    EditText EntradaEscritura;
    ProgressBar barra3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_escritura);

        manager = new sessionManager();

        barra3 = (ProgressBar)findViewById(R.id.progressBar3);
        barra3.setProgress(0);
        calificar = (Button) findViewById(R.id.btnEscritura);
        calificar.setEnabled(false);
        EntradaEscritura = (EditText)findViewById(R.id.txtEntradaEscritura);
        oracion = (TextView) findViewById(R.id.txtEscritura);



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

        calificar.setOnClickListener(new View.OnClickListener() {
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


        validarEscritura();

    }


    public void validarEscritura(){
        EntradaEscritura.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0){ // si no esta vaccia habilito el boton
                    calificar.setEnabled(true);
                }else{ // sino bloque el boton
                    calificar.setEnabled(false);
                }
            }
        });
    }

    public void generarScenario(){
        EntradaEscritura.setText("");
        juegoEscrutra cons = new juegoEscrutra();
        try {
            SoapObject resSoap  = cons.execute(fnivel,idPartida).get();
            if(resSoap==null){
                MensajeBox("No se ha podido conectar con el servidor." +
                        " Compruebe su conexión a Internet y vuelve a intentarlo.","Error de conexión");
            }else {
                AvancePreguntas = AvancePreguntas + 10;
                barra3.setProgress(AvancePreguntas);
                String assesment = resSoap.getProperty(0).toString(); //la oración
                correctaActual =  resSoap.getProperty(1).toString(); //la correcta

                StringTokenizer partes = new StringTokenizer(assesment, "|");
                String parte1 = partes.nextToken();
                String parte2 = partes.nextToken();

                oracion.setText(parte1 +" ____________________ " +parte2 );

            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void FinalizarEscenario(){
        Intent i = new Intent(PlayEscritura.this,MainActivity.class);
        i.putExtra("correo",fcorreo);
        i.putExtra("nivel",fnivel);
        i.putExtra("idUsuario",fidUsuario);
        i.putExtra("puntos",fpuntos);
        i.putExtra("Estrellas",festrellas);
        i.putExtra("Nombre",fnombre);
        i.putExtra("Imagen",fidimagen);
        startActivity(i);

        //tengo que revisar esto......
        manager.setPreferences(PlayEscritura.this,"puntos",fpuntos+"");
        manager.setPreferences(PlayEscritura.this,"Estrellas",festrellas+"");
        manager.setPreferences(PlayEscritura.this,"nivel",fnivel+"");
    }

    public void validarRespuesta(boolean finalizar){
        String seleccion = EntradaEscritura.getText().toString().toLowerCase().trim();
        correctaActual = correctaActual.toLowerCase().trim();

        if(finalizar){
            if(seleccion.equals(correctaActual)){
                buenas = buenas +1;
                MsjCorrecto dialogFragment = MsjCorrecto
                        .newInstance("Tu respuesta es\ncorrecta",true,"Score\nBuenas: " +buenas +"\nMalas: "+malas,3);
                dialogFragment.show(getFragmentManager(), "Buena");

            }else{
                malas = malas + 1;
                MsjNegativo dialogFragment = MsjNegativo
                        .newInstance("La respuesta\ncorrecta es:\n"+correctaActual,true,"Score\nBuenas: " +buenas +"\nMalas: "+malas,3);
                dialogFragment.show(getFragmentManager(),"Mala");

            }
        }else{
            if(seleccion.equals(correctaActual)){
                buenas = buenas +1;
                MsjCorrecto dialogFragment = MsjCorrecto
                        .newInstance("Tu respuesta es\ncorrecta",3);
                dialogFragment.show(getFragmentManager(), "Buena");

            }else{
                malas = malas + 1;
                MsjNegativo dialogFragment = MsjNegativo
                        .newInstance("La respuesta\ncorrecta es:\n"+correctaActual,3);
                dialogFragment.show(getFragmentManager(),"Mala");
            }
        }
    }



    public class juegoEscrutra extends AsyncTask<Integer,String,SoapObject> {

        SoapObject retorno;
        //http://www.tesis2016.somee.com/ManejoJuegos.asmx?WSDL
        @Override
        protected SoapObject doInBackground(Integer... params) {
            //para poder debuggear
            if(android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();

            final String SOAP_ACTION = "http://tempuri.org/obtenerEscritura";
            final String METHOD_NAME = "obtenerEscritura";
            final String NAMESPACE = "http://tempuri.org/";
            final String URL = "http://www.tesis2016g1.somee.com/ManejoJuegos.asmx";
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





