package com.example.edward.orthography;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 */

//variables

public class Miprogreso extends Fragment implements View.OnClickListener{

    GraphView graph;
    //variables
    String correo;
    int nivel;
    int idUsuario;
    int puntos;
    double estrellas;
    String nombre;
    int idimagen;
    TextView txtPromedio;
    public Miprogreso() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View hview = inflater.inflate(R.layout.fragment_miprogreso, container, false);
        //traer variables

        Bundle b = getArguments();
        correo = b.getString("correo");
        idUsuario = b.getInt("idUsuario");
        nivel = b.getInt("nivel");
        puntos = b.getInt("puntos");
        estrellas = b.getDouble("estrellas");
        nombre= b.getString("nombre");
        idimagen = b.getInt("idimagen");

        //definicion de los botones flotantes
        View btn1,btn2,btn3,btn4,btn5;
        btn1 = hview.findViewById(R.id.btn1);
        btn2 = hview.findViewById(R.id.btn2);
        btn3 = hview.findViewById(R.id.btn3);
        btn4 = hview.findViewById(R.id.btn4);
        btn5 = hview.findViewById(R.id.btn5);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        txtPromedio = (TextView)hview.findViewById(R.id.txtpromedio);
        graph = (GraphView) hview.findViewById(R.id.graph);


        graph.getGridLabelRenderer().setVerticalAxisTitle("# Aciertos");
        graph.getGridLabelRenderer().setHorizontalAxisTitle("# Partida");
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.BLUE);
        //graph.getGridLabelRenderer().setHorizontalAxisTitleTextSize(40);
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.BLUE);

        graph.getViewport().setScalable(true);
        // activate horizontal scrolling
        graph.getViewport().setScrollable(true);
        // activate horizontal and vertical zooming and scrolling
        graph.getViewport().setScalableY(true);
        // activate vertical scrolling
        graph.getViewport().setScrollableY(true);

        return hview;
    }

public void generarScenario(int idUsuario,final int n){
    txtPromedio.setText("");
    graph.removeAllSeries();
    RetornoDatosProgreso cons = new RetornoDatosProgreso();
    try {
        SoapObject resSoap  = cons.execute(idUsuario,n).get();
        if(resSoap==null){
            MensajeBox("No se ha podido conectar con el servidor." +
                    " Compruebe su conexión a Internet y vuelve a intentarlo.","Error de conexión");
        }else {

            double promedioCorrectas = Double.valueOf(resSoap.getProperty(0).toString());

            double promedioIncorrectas = Double.valueOf(resSoap.getProperty(1).toString());
            txtPromedio.setText("Promedio de correctas: "+promedioIncorrectas+"");
            SoapObject items = (SoapObject)resSoap.getProperty(2);
            DataPoint[] points = new DataPoint[items.getPropertyCount()];
            for(int i=0;i<items.getPropertyCount();i++){
                points[i] = new DataPoint(i,Integer.valueOf(items.getProperty(i).toString()));
            }

            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);
            series.setTitle("Progreso Lección "+n);
            series.setColor(Color.BLUE);
            series.setDrawBackground(true);
            //series.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.azul100));
            series.setBackgroundColor(Color.argb(100, 90, 255, 255));  //100, 255, 255, 0 amarillo trnas  //100, 204, 119, 119 rojo trnas  //150, 50, 0, 0 cafe trans
            series.setDrawDataPoints(true); //resaltar puntos        //133, 0, 222, 0 verde tranas   //100, 0, 0, 200 morado
            series.setDataPointsRadius(5);
            //  series.setThickness(8); //grosos de la linea

            series.setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series series, DataPointInterface dataPointInterface) {
                    Toast.makeText(getActivity(), "Lección "+n+" punto: " + dataPointInterface, Toast.LENGTH_SHORT).show();
                }
            });
            graph.addSeries(series);
            //alineación de las leyendas
            graph.getLegendRenderer().setVisible(true);
            graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        }
    } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
    }
}



    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn1){
            generarScenario(idUsuario,1);
        }else if(v.getId()==R.id.btn2){
            if(nivel>1){
                generarScenario(idUsuario,2);
            }else{
                Snackbar.make(v, "Lección bloqueada.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }else if(v.getId()==R.id.btn3){
            if(nivel>2){
                generarScenario(idUsuario,3);
            }else{
                Snackbar.make(v, "Lección bloqueada.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }else if(v.getId()==R.id.btn4){
            if(nivel>3){
                generarScenario(idUsuario,4);
            }else{
                Snackbar.make(v, "Lección bloqueada.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }else if(v.getId()==R.id.btn5){
            if(nivel>4){
                generarScenario(idUsuario,5);
            }else{
                Snackbar.make(v, "Lección bloqueada.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    }


    public class RetornoDatosProgreso extends AsyncTask<Integer,String,SoapObject> {

        SoapObject retorno;
        //http://www.tesis2016.somee.com/ManejoJuegos.asmx?WSDL
        @Override
        protected SoapObject doInBackground(Integer... params) {
            //para poder debuggear
            if(android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();

            final String SOAP_ACTION = "http://tempuri.org/obtenerProgreso";
            final String METHOD_NAME = "obtenerProgreso";
            final String NAMESPACE = "http://tempuri.org/";
            final String URL = "http://www.tesis2016.somee.com/ManejoJuegos.asmx";
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
                retorno =(SoapObject)envelope.getResponse();

            } catch (Exception e) {
                //dafadsfasdf
            }
            return retorno;
        }
    } // fin de asyntask



    public void MensajeBox(String mensaje,String titulo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
