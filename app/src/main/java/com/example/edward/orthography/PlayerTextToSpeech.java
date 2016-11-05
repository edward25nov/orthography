package com.example.edward.orthography;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Locale;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

public class PlayerTextToSpeech extends AppCompatActivity implements TextToSpeech.OnInitListener {
  //referencia https://sintaxispragmatica.wordpress.com/2013/07/21/usando-la-libreria-texttospeech-texto-a-voz/
  //http://android-developers.blogspot.com/2009/09/introduction-to-text-to-speech-in.html


    //
    sessionManager manager;
   //propias de textTospeech
    private TextToSpeech textToSpeech;
    private static final int MY_DATA_CHECK_CODE =1; //indica que el movil esta listo para hablar
    //variables globales
    String fcorreo;
    int fnivel;
    int fidUsuario;
    int fpuntos;
    double festrellas;
    String fnombre;
    int fidimagen;
    //componentes
    FloatingActionButton fab;
    Button btnCalificar;
    EditText txtrespuesta;
    ProgressBar barra;
    //manejo de juego
    int AvancePreguntas;
    int buenas;
    int malas;
    int idPartida;
    String OracionActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_text_to_speech);

        manager = new sessionManager();


        //comprobar la presencia de los recursos TTS
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);


        btnCalificar = (Button) findViewById(R.id.btndictado);
        btnCalificar.setEnabled(false);
        txtrespuesta = (EditText) findViewById(R.id.txtRespuestaDictado);
        barra = (ProgressBar) findViewById(R.id.progressBar4);
        barra.setProgress(0);
        fab = (FloatingActionButton) findViewById(R.id.floatingReproducir);



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

                generarEscenario();
            }
        } catch (InterruptedException | ExecutionException e) {
            MensajeBox("No se ha podido conectar con el servidor." +
                    " Compruebe su conexión a Internet y vuelve a intentarlo.","Error de conexión");
        }



        btnCalificar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ){

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
        } );



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speak(OracionActual);
            }
        });

        validarEscritura();

    }

    @Override
    public void onInit(int status) { //se creo al implemnetar texttoSpeech
        // textToSpeech.setLanguage( Locale.ENGLISH );
        textToSpeech.setLanguage( new Locale( "spa", "ESP" ) );
        /*if (status == TextToSpeech.LANG_MISSING_DATA | status == TextToSpeech.LANG_NOT_SUPPORTED ) {
            Toast.makeText( this, "NO SOPORTADO, TEXT TO SPEECH", Toast.LENGTH_SHORT ).show();
        }*/
    }


    private void speak( String str )
    {
        textToSpeech.speak( str, TextToSpeech.QUEUE_FLUSH, null );
        textToSpeech.setSpeechRate( 0.0f );
        textToSpeech.setPitch( 0.0f );
    }

    @Override
    protected void onDestroy()
    {
        if ( textToSpeech != null )
        {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_DATA_CHECK_CODE) {  //si esta instaldo se crea la instancia
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                textToSpeech = new TextToSpeech(this, this);
            } else { //si no nos manda a instlarlo
                Intent installIntent = new Intent();
                installIntent.setAction(
                        TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
    }

  //funcionalidad del juego
  public void validarEscritura(){
      txtrespuesta.addTextChangedListener(new TextWatcher() {

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
                  btnCalificar.setEnabled(true);
              }else{ // sino bloque el boton
                  btnCalificar.setEnabled(false);
              }
          }
      });
  }


    public void validarRespuesta(boolean finalizar){
        String seleccion = txtrespuesta.getText().toString().toLowerCase().trim();
        OracionActual = OracionActual.toLowerCase().trim();

        if(finalizar){
            if(seleccion.equals(OracionActual)){
                buenas = buenas +1;
                MsjCorrecto dialogFragment = MsjCorrecto
                        .newInstance("Tu respuesta es\ncorrecta",true,"Score\nBuenas: " +buenas +"\nMalas: "+malas,4);
                dialogFragment.show(getFragmentManager(), "Buena");

            }else{
                malas = malas + 1;
                MsjNegativo dialogFragment = MsjNegativo
                        .newInstance("La respuesta\ncorrecta es:\n"+OracionActual,true,"Score\nBuenas: " +buenas +"\nMalas: "+malas,4);
                dialogFragment.show(getFragmentManager(),"Mala");

            }
        }else{
            if(seleccion.equals(OracionActual)){
                buenas = buenas +1;
                MsjCorrecto dialogFragment = MsjCorrecto
                        .newInstance("Tu respuesta es\ncorrecta",4);
                dialogFragment.show(getFragmentManager(), "Buena");

            }else{
                malas = malas + 1;
                MsjNegativo dialogFragment = MsjNegativo
                        .newInstance("La respuesta\ncorrecta es:\n"+OracionActual,4);
                dialogFragment.show(getFragmentManager(),"Mala");
            }
        }
    }


    public void generarEscenario(){
        txtrespuesta.setText("");
        juegoDictado cons = new juegoDictado();
        try {
            SoapPrimitive resSoap  = cons.execute(fnivel,idPartida).get();
            if(resSoap==null){
                MensajeBox("No se ha podido conectar con el servidor." +
                        " Compruebe su conexión a Internet y vuelve a intentarlo.","Error de conexión");
            }else {
                AvancePreguntas = AvancePreguntas + 10;
                barra.setProgress(AvancePreguntas);
                OracionActual = resSoap.toString(); //la oración

            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


    public void FinalizarEscenario(){
        Intent i = new Intent(PlayerTextToSpeech.this,MainActivity.class);
        i.putExtra("correo",fcorreo);
        i.putExtra("nivel",fnivel);
        i.putExtra("idUsuario",fidUsuario);
        i.putExtra("puntos",fpuntos);
        i.putExtra("Estrellas",festrellas);
        i.putExtra("Nombre",fnombre);
        i.putExtra("Imagen",fidimagen);
        startActivity(i);

        //tengo que revisar esto......
        manager.setPreferences(PlayerTextToSpeech.this,"puntos",fpuntos+"");
        manager.setPreferences(PlayerTextToSpeech.this,"Estrellas",festrellas+"");
        manager.setPreferences(PlayerTextToSpeech.this,"nivel",fnivel+"");
    }



    public class juegoDictado extends AsyncTask<Integer,String,SoapPrimitive> {

        SoapPrimitive retorno;
        //http://www.tesis2016.somee.com/ManejoJuegos.asmx?WSDL
        @Override
        protected SoapPrimitive doInBackground(Integer... params) {
            //para poder debuggear
            if(android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();

            final String SOAP_ACTION = "http://tempuri.org/obtenerDictado";
            final String METHOD_NAME = "obtenerDictado";
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
                retorno =(SoapPrimitive)envelope.getResponse();
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
