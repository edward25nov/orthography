package com.example.edward.orthography;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import org.w3c.dom.Text;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Miperfil extends Fragment implements Validator.ValidationListener{


    String correo;
    int nivel;
    int idUsuario;
    int puntos;
    double estrellas;
    String nombre;
    int idimagen;


    public Miperfil() {
        // Required empty public constructor
    }

    @NotEmpty(message = "Este campo es requerido")
    EditText Rpass;

    @Password(min = 6,message = "Password incorrecta, mínimo 6 caracteres")
    EditText Rnueva;
    @ConfirmPassword(message = "Passwords no coinciden")
    EditText Rconfirma;

    @NotEmpty(message = "Este campo es requerido")
    EditText Rname;
    @Email(message = "Email incorrecto")
    EditText Rcorreo;


    Validator validator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View hview = inflater.inflate(R.layout.fragment_miperfil, container, false);

        validator = new Validator(this);
        validator.setValidationListener(this);

        Bundle b = getArguments();
        correo = b.getString("correo");
        idUsuario = b.getInt("idUsuario");
        nivel = b.getInt("nivel");
        puntos = b.getInt("puntos");
        estrellas = b.getDouble("estrellas");
        nombre= b.getString("nombre");
        idimagen = b.getInt("idimagen");

        Rpass = (EditText)hview.findViewById(R.id.Rpassword);
        Rname = (EditText)hview.findViewById(R.id.Rname);
        Rcorreo = (EditText)hview.findViewById(R.id.Rcorreo);
        Rnueva = (EditText) hview.findViewById(R.id.RNueva);
        Rconfirma = (EditText)hview.findViewById(R.id.Rconfirma);


        TextView Rpuntos = (TextView)hview.findViewById(R.id.Rpuntos);
        TextView RNivel = (TextView)hview.findViewById(R.id.Rnivel);
        ImageView Rimg = (ImageView)hview.findViewById(R.id.Rimg);

        Button Rguardar = (Button)hview.findViewById(R.id.Rguardar);

        Rpuntos.setText("Puntos: "+puntos);
        RNivel.setText("Nivel: "+nivel);
        Rcorreo.setText(correo);
        Rname.setText(nombre);
        Rimg.setImageResource(idimagen);


        Rguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();  //validamos

            }
        });



        return hview;
    }

    @Override
    public void onValidationSucceeded() {
        //Registrar2.ServiceRegistrar consumirWS = new Registrar2.ServiceRegistrar(c,p,idavatar,nombre);
        //consumirWS.execute();

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getContext());

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            }else{
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
