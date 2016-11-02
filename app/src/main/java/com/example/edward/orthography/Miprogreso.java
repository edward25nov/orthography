package com.example.edward.orthography;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.NumberFormat;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 */
public class Miprogreso extends Fragment {


    int idPartida;
    public Miprogreso() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View hview = inflater.inflate(R.layout.fragment_miprogreso, container, false);
        // Inflate the layout for this fragment

        /*
        *  DataPoint[] points = new DataPoint[100];
        for (int i = 0; i < points.length; i++) {
            points[i] = new DataPoint(i, Math.sin(i*0.5) * 20*(Math.random()*10+1));
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);

        *
        * */

        GraphView graph = (GraphView) hview.findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 0),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 3),
                new DataPoint(5, 9),
                new DataPoint(6, 2),
                new DataPoint(7, 6),
                new DataPoint(8, 3),
                new DataPoint(9, 3),
                new DataPoint(10, 4),
        });

        // activate horizontal zooming and scrolling
        graph.getViewport().setScalable(true);
        // activate horizontal scrolling
        graph.getViewport().setScrollable(true);
        // activate horizontal and vertical zooming and scrolling
        graph.getViewport().setScalableY(true);
        // activate vertical scrolling
        graph.getViewport().setScrollableY(true);

        series.setTitle("Progreso en Identificación");
        series.setColor(Color.BLUE);
        series.setDrawBackground(true);
        //series.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.azul100));
        series.setBackgroundColor(Color.argb(100, 90, 255, 255));  //100, 255, 255, 0 amarillo trnas  //100, 204, 119, 119 rojo trnas  //150, 50, 0, 0 cafe trans
        series.setDrawDataPoints(true); //resaltar puntos        //133, 0, 222, 0 verde tranas   //100, 0, 0, 200 morado
        series.setDataPointsRadius(5);
        //  series.setThickness(8); //grosos de la linea

        //alineación de las leyendas
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPointInterface) {
                Toast.makeText(getActivity(), "Series1: On Data Point clicked: " + dataPointInterface, Toast.LENGTH_SHORT).show();
            }
        });

        graph.getGridLabelRenderer().setVerticalAxisTitle("Aciertos");
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Eje x");
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.BLUE);
        //graph.getGridLabelRenderer().setHorizontalAxisTitleTextSize(40);
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.BLUE);
        graph.addSeries(series);


        generarScenario();



        return hview;
    }

/**
 *
 FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
 fab.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
.setAction("Action", null).show();
}
});
 *
 */

public void generarScenario(){

    /*
    *  <PromedioCorrectas>double</PromedioCorrectas>
        <PromedioIncorrectas>double</PromedioIncorrectas>
        <Punteos>
          <anyType />
          <anyType />
        </Punteos>*/

    RetornoDatosProgreso cons = new RetornoDatosProgreso();
    try {
        SoapObject resSoap  = cons.execute(16,1).get();
        if(resSoap==null){
            MensajeBox("No se ha podido conectar con el servidor." +
                    " Compruebe su conexión a Internet y vuelve a intentarlo.","Error de conexión");
        }else {

        }
    } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
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
