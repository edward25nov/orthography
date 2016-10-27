package com.example.edward.orthography;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.Button;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;


public class PlayContexto extends AppCompatActivity {

    //variables que vienen del fragmento
    String fcorreo;
    int fnivel;
    int fidUsuario;
    int fpuntos;
    double festrellas;
    String fnombre;
    int fidimagen;
    TextView txtContextoParrafo;
    Button opcionA;
    Button opcionB;
    Button opcionC;
    Button btnContexto;
    int PartidaActual;
    //variables que usare aquó
    String correctaActual;
    String Oracion;
    ArrayList<String>Opciones;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_contexto);

        //componenetes del activity
        txtContextoParrafo = (TextView)findViewById(R.id.txtContextoParrafo);
        opcionA = (Button)findViewById(R.id.opcionA);
        opcionB = (Button)findViewById(R.id.opcionB);
        opcionC = (Button)findViewById(R.id.opcionC);
        btnContexto = (Button)findViewById(R.id.btnContexto);
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
            SoapPrimitive idp = null;
            idp = (SoapPrimitive) actual.execute(fidUsuario,fnivel).get();
            if(idp==null){
                MensajeBox("No se ha podido conectar con el servidor." +
                        " Compruebe su conexión a Internet y vuelve a intentarlo.","Error de conexión");
            }else{
                PartidaActual = Integer.valueOf(idp.toString());

                juegoContexto play= new juegoContexto();
                SoapObject resSoap = play.execute(fnivel,PartidaActual).get();

                if(resSoap==null){
                    MensajeBox("No se ha podido conectar con el servidor." +
                            " Compruebe su conexión a Internet y vuelve a intentarlo.","Error de conexión");
                }else{
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


                    opcionA.setText(Opciones.get(0));
                    opcionB.setText(Opciones.get(1));
                    opcionC.setText(Opciones.get(2));

                    txtContextoParrafo.setText(parte1 +"                                " +parte2 );

                }

            }



        } catch (InterruptedException e) {
           // e.printStackTrace();
            MensajeBox("No se ha podido conectar con el servidor." +
                    " Compruebe su conexión a Internet y vuelve a intentarlo.","Error de conexión");
        } catch (ExecutionException e) {
            //e.printStackTrace();
            MensajeBox("No se ha podido conectar con el servidor." +
                    " Compruebe su conexión a Internet y vuelve a intentarlo.","Error de conexión");
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


    public class juegoContexto extends AsyncTask<Integer,String,SoapObject> {

        SoapObject retorno;
        //http://www.tesis2016.somee.com/ManejoJuegos.asmx?WSDL
        @Override
        protected SoapObject doInBackground(Integer... params) {
            //para poder debuggear
            if(android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();

            final String SOAP_ACTION = "http://tempuri.org/obtenerContexto";
            final String METHOD_NAME = "obtenerContexto";
            final String NAMESPACE = "http://tempuri.org/";
            final String URL = "http://www.tesis2016.somee.com/ManejoJuegos.asmx";
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

}
