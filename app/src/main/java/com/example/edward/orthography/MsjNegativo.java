package com.example.edward.orthography;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by root on 30/10/16.
 */

public class MsjNegativo extends DialogFragment {


    TextView m;
    View hView;
    Button btn;

    static MsjNegativo newInstance(String mensaje){
        MsjNegativo fragment = new MsjNegativo();
        Bundle args = new Bundle();
        args.putString("mensaje", mensaje);
        fragment.setArguments(args);
        return fragment;
    }

    static MsjNegativo newInstance(String mensaje,Boolean finalizar,String Resultado){
        MsjNegativo fragment = new MsjNegativo();
        Bundle args = new Bundle();
        args.putString("mensaje", mensaje);
        args.putBoolean("finalizar",finalizar);
        args.putString("Resultado",Resultado);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String mensaje = getArguments().getString("mensaje");
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        hView =  inflater.inflate(R.layout.layout_msjincorrecto,null);

        m = (TextView) hView.findViewById(R.id.txtMsj2);
        btn = (Button)hView.findViewById(R.id.btnContinue2);
        m.setText(mensaje);
        builder.setView(hView);
        builder.setCancelable(false);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean finalizar = getArguments().getBoolean("finalizar");
                String Resultado = getArguments().getString("Resultado");
                if(finalizar){
                    MsjResultado dialogFragment = MsjResultado.newInstance(Resultado);
                    dialogFragment.show(getFragmentManager(),"Resultado");
                }else{
                    PlayContexto f = (PlayContexto) getActivity();
                    f.generarEscenario();
                }
                getDialog().cancel();
            }
        });

        return builder.create();
    }

}

