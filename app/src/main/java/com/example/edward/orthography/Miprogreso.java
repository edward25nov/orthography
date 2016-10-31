package com.example.edward.orthography;


import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
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

import java.text.NumberFormat;


/**
 * A simple {@link Fragment} subclass.
 */
public class Miprogreso extends Fragment {


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
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
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
                Toast.makeText(getActivity(), "Series1: On Data Point clicked: "+dataPointInterface, Toast.LENGTH_SHORT).show();
            }
        });

        graph.getGridLabelRenderer().setVerticalAxisTitle("Aciertos");
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Eje x");
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.BLUE);
        //graph.getGridLabelRenderer().setHorizontalAxisTitleTextSize(40);
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.BLUE);
        graph.addSeries(series);


        return hview;
    }

}
