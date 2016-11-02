package com.example.edward.orthography;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MsjNegativo extends DialogFragment {


    TextView m;
    View hView;
    Button btn;

    static MsjNegativo newInstance(String mensaje,int contexto){
        MsjNegativo fragment = new MsjNegativo();
        Bundle args = new Bundle();
        args.putString("mensaje", mensaje);
        args.putInt("contexto",contexto);
        fragment.setArguments(args);
        return fragment;
    }

    static MsjNegativo newInstance(String mensaje,Boolean finalizar,String Resultado,int contexto){
        MsjNegativo fragment = new MsjNegativo();
        Bundle args = new Bundle();
        args.putString("mensaje", mensaje);
        args.putBoolean("finalizar",finalizar);
        args.putString("Resultado",Resultado);
        args.putInt("contexto",contexto);
        fragment.setArguments(args);
        return fragment;
    }


   @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
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
                int contexto = getArguments().getInt("contexto");

                if(finalizar){
                    MsjResultado dialogFragment = MsjResultado.newInstance(Resultado,contexto);
                    dialogFragment.show(getFragmentManager(),"Resultado");
                }else{
                    if(contexto==1){
                        PlaySeleccion f = (PlaySeleccion) getActivity();
                        f.generarScenario();
                    }else if(contexto==2){
                        PlayContexto f = (PlayContexto) getActivity();
                        f.generarEscenario();
                    }else if(contexto==3){
                        PlayEscritura f = (PlayEscritura)getActivity();
                        f.generarScenario();
                    }else if(contexto==4){
                        PlayerTextToSpeech f = (PlayerTextToSpeech)getActivity();
                        f.generarEscenario();
                    }
                }
                getDialog().cancel();
            }
        });

        AlertDialog alert = builder.create();
        setCancelable(false); //press back button not cancel dialog, this one works fine
        alert.setCanceledOnTouchOutside(false); //para cancelar táctil exterior
        return alert;
    }

}

