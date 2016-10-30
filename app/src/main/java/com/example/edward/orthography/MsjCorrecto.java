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

public class MsjCorrecto extends DialogFragment {
    TextView m;
    View hView;
    Button btn;
    /*
    * Hemos definido el método newInstance(). Con este método permite crear una nueva instancia del fragment,
    * y al mismo tiempo acepta un argumento que especifica el mensaje que mostrará el AlertDialog,
    * el cual será guardado en un objeto Bundle.
    * */
    static MsjCorrecto newInstance(String mensaje){
        MsjCorrecto fragment = new MsjCorrecto();
        Bundle args = new Bundle();
        args.putString("mensaje", mensaje);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String mensaje = getArguments().getString("mensaje");
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        hView = inflater.inflate(R.layout.layout_msjpersonalizado, null);
        btn = (Button)hView.findViewById(R.id.btnContinue);
        m = (TextView) hView.findViewById(R.id.txtMsj);
        m.setText(mensaje);
        builder.setView(hView);
        builder.setCancelable(false);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                      getDialog().cancel();
            }
        });

        return builder.create();
    }

}







