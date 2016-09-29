package com.example.edward.orthography;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by root on 28/09/16.
 */

public class Avatars extends AppCompatActivity {
    private ListView lista;
    public static String correo = "";
    public static String pass1 ="";
    public static String pass2 = "";
    public static int idAvatar = R.drawable.avatar2; //por defecto


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_avatars);

        ArrayList<lista_entrada> datos = new ArrayList<lista_entrada>();

        datos.add(new lista_entrada(R.drawable.avatar, "avatar 1", "descripción 1"));
        datos.add(new lista_entrada(R.drawable.avatar2, "avatar 2", "descripción 2"));
        datos.add(new lista_entrada(R.drawable.amigos, "avatar 3", "descripción 3"));

        lista = (ListView) findViewById(R.id.listaview_avatars);
        lista.setAdapter(new lista_adaptador(this, R.layout.layout_entrada, datos){
            @Override
            public void onEntrada(Object entrada, View view) {
                TextView texto_superior_entrada = (TextView) view.findViewById(R.id.txtsuperior);
                texto_superior_entrada.setText(((lista_entrada) entrada).get_textoEncima());

                TextView texto_inferior_entrada = (TextView) view.findViewById(R.id.txtinferior);
                texto_inferior_entrada.setText(((lista_entrada) entrada).get_textoDebajo());

                ImageView imagen_entrada = (ImageView) view.findViewById(R.id.imgviewAvatar);
                imagen_entrada.setImageResource(((lista_entrada) entrada).get_idImagen());
            }
        });

        lista.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> pariente, View view, int posicion, long id) {
                lista_entrada elegido = (lista_entrada) pariente.getItemAtPosition(posicion);

               // CharSequence texto = "Seleccionado: " + elegido.get_textoEncima() + "idImagen: "+elegido.get_idImagen();
                idAvatar = elegido.get_idImagen();
                Registrar2.correo = Avatars.correo;
                Registrar2.pass1 = Avatars.pass1;
                Registrar2.pass2 = Avatars.pass2;
                Registrar2.idavatar = Avatars.idAvatar;

                Generardesicion();
                //Toast toast = Toast.makeText(Avatars.this, texto, Toast.LENGTH_LONG);
                //toast.show();
            }
        });

    }


    public void Generardesicion(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("¿Confirma la acción seleccionada?")
                .setTitle("Confirmación")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()  {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        aceptar();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();

    }

    public void aceptar(){
        startActivity(new Intent(Avatars.this,Registrar2.class));
    }



}
