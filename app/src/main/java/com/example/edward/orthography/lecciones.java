package com.example.edward.orthography;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class lecciones extends Fragment {

    public lecciones() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_lecciones,container,false);
        final ListView miLista = (ListView) view.findViewById(R.id.listalecciones);

        Bundle b = getArguments();
        final String correo = b.getString("correo");
        final int idUsuario = b.getInt("idUsuario");
        final int nivel = b.getInt("nivel");
        final int puntos = b.getInt("puntos");
        final double estrellas = b.getDouble("estrellas");
        final String nombre= b.getString("nombre");
        final int idimagen = b.getInt("idimagen");
        ArrayList<lecciones_entrada> datos = new ArrayList<lecciones_entrada>();

        datos.add(new lecciones_entrada(R.drawable.previa, "Lección 1",(float) estrellas));
        datos.add(new lecciones_entrada(R.drawable.previa, "Lección 2",0f));
        datos.add(new lecciones_entrada(R.drawable.previa, "Lección 3",0f));
        datos.add(new lecciones_entrada(R.drawable.previa, "Lección 4",0f));
        datos.add(new lecciones_entrada(R.drawable.previa, "Lección 5",0f));

        miLista.setAdapter(new lista_adaptador(getContext(), R.layout.layout_entradalecciones, datos){
            @Override
            public void onEntrada(Object entrada, View view) {
                TextView titulo = (TextView) view.findViewById(R.id.txtleccion);
                titulo.setText(((lecciones_entrada) entrada).get_titulo());

                RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBarleccion);
                ratingBar.setRating(((lecciones_entrada) entrada).get_rating());

                ImageView imagen_entrada = (ImageView) view.findViewById(R.id.imgleccion);
                imagen_entrada.setImageResource(((lecciones_entrada) entrada).get_idImagen());
            }

        });

        miLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> pariente, View view, int posicion, long id) {
                lecciones_entrada elegido = (lecciones_entrada) pariente.getItemAtPosition(posicion);
                miLista.setSelection(posicion); //asignar el item que esta seleccionado

                if(posicion==0){
                    Intent i = new Intent(getContext(),PlaySeleccion.class);
                    i.putExtra("correo", correo);
                    i.putExtra("idUsuario",idUsuario);
                    i.putExtra("nivel",nivel);
                    i.putExtra("puntos",puntos);
                    i.putExtra("Estrellas",estrellas);
                    i.putExtra("Nombre",nombre);
                    i.putExtra("Imagen",idimagen);
                    startActivity(i);
                }else if(posicion==1){
                    Intent i = new Intent(getContext(),PlayContexto.class);
                    startActivity(i);
                }

            }
        });


        return view;
    }






}
