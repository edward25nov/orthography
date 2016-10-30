package com.example.edward.orthography;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

/**
 * Created by root on 30/10/16.
 */

public class MsjResultado extends DialogFragment {
    TextView m;
    View hView;
    Button btn;

    static MsjResultado newInstance(String mensaje,int contexto){
        MsjResultado fragment = new MsjResultado();
        Bundle args = new Bundle();
        args.putString("mensaje", mensaje);
        args.putInt("contexto",contexto);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String mensaje = getArguments().getString("mensaje");

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        hView = inflater.inflate(R.layout.layout_msjresultado,null);
        m = (TextView) hView.findViewById(R.id.txtMsj3);
        btn = (Button)hView.findViewById(R.id.btnContinue3);
        m.setText(mensaje);
        builder.setView(hView);
        builder.setCancelable(false);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int contexto = getArguments().getInt("contexto");
                if(contexto==1){
                    PlaySeleccion f = (PlaySeleccion)getActivity();
                    f.finalizarScenario();
                }else if(contexto==2){
                    PlayContexto f = (PlayContexto)getActivity();
                    f.FinalizarEscenario();
                }

                getDialog().cancel();
            }
        });

        return builder.create();
    }



}
