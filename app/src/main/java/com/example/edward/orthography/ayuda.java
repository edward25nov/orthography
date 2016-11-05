package com.example.edward.orthography;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ayuda extends Fragment {


    public ayuda() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View hview = inflater.inflate(R.layout.fragment_ayuda, container, false);

        Button btn1 = (Button)hview.findViewById(R.id.button1);
        Button btn2 = (Button)hview.findViewById(R.id.button2);
        Button btn3 = (Button)hview.findViewById(R.id.button3);
        Button btn4 = (Button)hview.findViewById(R.id.button4);
        Button btn5 = (Button)hview.findViewById(R.id.button5);
        Button btn6 = (Button)hview.findViewById(R.id.button6);


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MensajeBox("Se busca que a través del juego aplicando la lección 01, se adquiera a nivel básico los puntos propuestos en las necesidades específicas identificadas.\n" +
                        "\nEl nivel ortográfico esperado luego de terminar esta lección es el nivel I: Reglas elementales, reconocer determinados errores y corregirlos.\n");
            }
        });


        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MensajeBox("Se busca que a través del juego aplicando la lección 02, se adquiera en su totalidad el nivel básico y no tenga dificultad con ninguna modalidad de nivel 1.\n\nTambién se dá una pequeña introducción al nivel ortográfico II para mostrar al usuario sus debilidades y que las conozca antes de entrar a nivel intermedio.");

            }
        });


        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MensajeBox("Se busca que a través del juego aplicando la lección 03, se adquiera un nivel ortográfico nivel II. Este nivel es crucial para continuar con el juego, por lo que será mucho más extenso (aproximadamente el doble que el segundo).");
            }
        });


        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MensajeBox("Se busca que a través del juego aplicando la lección 04, se introduce al nivel avanzado, nivel III. En esta lección se procura realizar un repaso de las lecciones anteriores para no perder lo aprendido.");

            }
        });


        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MensajeBox("En este nivel, el usuario tendrá un nivel ortográfico asegurado nivel III. Debido a la metodología por reforzamiento, la aplicación forzará al estudiante a re-evaluar todos los cursos en este nivel.\n\nEsta lección 05 se adquirirá de el conocimiento completo de un curso o seminario de ortografía.");
            }
        });



        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MensajeBox("En el progreso cada lección cuenta con su gráfica individual en el cual se indica el numero de aciertos por partida y el promedio de buenas de cada usuario.");
            }
        });



        return hview;
    }


    public void MensajeBox(String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(mensaje)
                .setTitle("Ayuda")
                .setIcon(R.drawable.helpme)
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
