package com.example.edward.orthography;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

public class Playpuntuacion extends AppCompatActivity {

    sessionManager manager;
    //variables que vienen del fragmento
    String fcorreo;
    int fnivel;
    int fidUsuario;
    int fpuntos;
    double festrellas;
    String fnombre;
    int fidimagen;
    //
    TextView txtOracion;
    Button itemA;
    Button itemB;
    Button itemC;
    Button btnPuntuacion;
    int PartidaActual;
    ProgressBar barra;
    //variables que usare aquí
    String seleccionUsuario;
    String correctaActual;
    String Oracion;
    ArrayList<String> Opciones;
    int malas;
    int buenas;
    int avance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playpuntuacion);

        manager = new sessionManager();

        //componenetes del activity
        txtOracion = (TextView)findViewById(R.id.txtspuntuacion);
        itemA = (Button)findViewById(R.id.itemA);
        itemB = (Button)findViewById(R.id.itemB);
        itemC = (Button)findViewById(R.id.itemC);
        btnPuntuacion = (Button)findViewById(R.id.btnPuntuacion);
        btnPuntuacion.setEnabled(false);
        barra = (ProgressBar)findViewById(R.id.progreso5);
        barra.setProgress(0);

        //variables
        fcorreo = getIntent().getStringExtra("correo");
        fnivel = getIntent().getIntExtra("nivel",-1);
        fidUsuario = getIntent().getIntExtra("idUsuario",-1);
        fpuntos = getIntent().getIntExtra("puntos",-1);
        festrellas = getIntent().getDoubleExtra("Estrellas",-1);
        fnombre = getIntent().getStringExtra("Nombre");
        fidimagen = getIntent().getIntExtra("Imagen",-1);


        try {
            CrearPartida actual = new CrearPartida();
            SoapPrimitive idp = actual.execute(fidUsuario,fnivel).get();
            if(idp==null){
                MensajeBox("No se ha podido conectar con el servidor." +
                        " Compruebe su conexión a Internet y vuelve a intentarlo.","Error de conexión");
            }else{
                PartidaActual = Integer.valueOf(idp.toString());
                generarEscenario();
            }

        } catch (InterruptedException |ExecutionException e) {
            // e.printStackTrace();
            MensajeBox("No se ha podido conectar con el servidor." +
                    " Compruebe su conexión a Internet y vuelve a intentarlo.","Error de conexión");
        }



        btnPuntuacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(avance==100){
                    TerminarPartida fin = new TerminarPartida();
                    try {
                        SoapObject idp = fin.execute(fidUsuario, PartidaActual, buenas).get();
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
                        e.printStackTrace();
                    }
                }else{
                    validarRespuesta(false);
                    btnPuntuacion.setEnabled(false);
                }


            }
        });


        itemA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPuntuacion.setEnabled(true);
                seleccionUsuario = itemA.getText().toString();
                itemA.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.boton_opcionseleccion));
                itemB.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.boton_opcionesnormal));
                itemC.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.boton_opcionesnormal));
                YoYo.with(Techniques.Bounce)
                        .duration(1000)
                        .playOn(findViewById(itemA.getId()));
            }
        });

        itemB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPuntuacion.setEnabled(true);
                seleccionUsuario = itemB.getText().toString();
                itemB.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.boton_opcionseleccion));
                itemA.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.boton_opcionesnormal));
                itemC.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.boton_opcionesnormal));
                YoYo.with(Techniques.Bounce)
                        .duration(1000)
                        .playOn(findViewById(itemB.getId()));
            }
        });


        itemC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPuntuacion.setEnabled(true);
                seleccionUsuario = itemC.getText().toString();
                itemC.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.boton_opcionseleccion));
                itemA.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.boton_opcionesnormal));
                itemB.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.boton_opcionesnormal));
                YoYo.with(Techniques.Bounce)
                        .duration(1000)
                        .playOn(findViewById(itemC.getId()));

            }
        });




    }


    public void validarRespuesta(boolean finalizar){
        if(finalizar){
            if(seleccionUsuario.equals(correctaActual)){
                buenas = buenas +1;
                MsjCorrecto dialogFragment = MsjCorrecto
                        .newInstance("Tu respuesta es\ncorrecta",true,"Score\nBuenas: " +buenas +"\nMalas: "+malas,5);
                dialogFragment.show(getFragmentManager(), "Buena");

            }else{
                malas = malas + 1;
                MsjNegativo dialogFragment = MsjNegativo
                        .newInstance("La respuesta\ncorrecta es:\n"+correctaActual,true,"Score\nBuenas: " +buenas +"\nMalas: "+malas,5);
                dialogFragment.show(getFragmentManager(),"Mala");

            }
        }else{
            if(seleccionUsuario.equals(correctaActual)){
                buenas = buenas +1;
                MsjCorrecto dialogFragment = MsjCorrecto
                        .newInstance("Tu respuesta es\ncorrecta",5);
                dialogFragment.show(getFragmentManager(), "Buena");

            }else{
                malas = malas + 1;
                MsjNegativo dialogFragment = MsjNegativo
                        .newInstance("La respuesta\ncorrecta es:\n"+correctaActual,5);
                dialogFragment.show(getFragmentManager(),"Mala");

            }
        }
    }



    public void generarEscenario(){
        juegoleccion5 play= new juegoleccion5();

        try {
            SoapObject resSoap = play.execute(fnivel,PartidaActual).get();
            if(resSoap==null){
                MensajeBox("No se ha podido conectar con el servidor." +
                        " Compruebe su conexión a Internet y vuelve a intentarlo.","Error de conexión");
            }else{
                avance = avance + 10;
                barra.setProgress(avance);
                itemA.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.boton_opcionesnormal));
                itemB.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.boton_opcionesnormal));
                itemC.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.boton_opcionesnormal));
                Oracion = resSoap.getProperty(0).toString();

                StringTokenizer partes = new StringTokenizer(Oracion, "|");
                String parte1 = partes.nextToken();
                String parte2 = partes.nextToken();

                SoapObject items = (SoapObject)resSoap.getProperty(1);
                Opciones = new ArrayList<>(items.getPropertyCount());
                for(int i=0;i<items.getPropertyCount();i++){
                    Opciones.add(String.valueOf(items.getProperty(i)));
                }
                correctaActual = resSoap.getProperty(2).toString();


                itemA.setText(Opciones.get(0));
                itemB.setText(Opciones.get(1));
                itemC.setText(Opciones.get(2));

                txtOracion.setText(parte1 +" ___ " +parte2 );
            }

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }





    public void FinalizarEscenario5(){
        Intent i = new Intent(Playpuntuacion.this,MainActivity.class);
        i.putExtra("correo",fcorreo);
        i.putExtra("nivel",fnivel);
        i.putExtra("idUsuario",fidUsuario);
        i.putExtra("puntos",fpuntos);
        i.putExtra("Estrellas",festrellas);
        i.putExtra("Nombre",fnombre);
        i.putExtra("Imagen",fidimagen);
        startActivity(i);

        //tengo que revisar esto......
        manager.setPreferences(Playpuntuacion.this,"puntos",fpuntos+"");
        manager.setPreferences(Playpuntuacion.this,"Estrellas",festrellas+"");
        manager.setPreferences(Playpuntuacion.this,"nivel",fnivel+"");
    }





    public class juegoleccion5 extends AsyncTask<Integer,String,SoapObject> {

        SoapObject retorno;
        //http://www.tesis2016.somee.com/ManejoJuegos.asmx?WSDL
        @Override
        protected SoapObject doInBackground(Integer... params) {
            //para poder debuggear
            if(android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();

            final String SOAP_ACTION = "http://tempuri.org/obtenerPuntuacion";
            final String METHOD_NAME = "obtenerPuntuacion";
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

            }catch (Exception e){
                //dafadsfasdf
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
