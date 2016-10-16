package com.example.edward.orthography;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Checked;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Registrar2 extends AppCompatActivity implements Validator.ValidationListener {

    public static String correo = "";
    public static String pass1 ="";
    public static String pass2 = "";
    public static int idavatar=0;

    @NotEmpty(message = "Este campo es requerido")
    @Email(message = "Email incorrecto")
    EditText txtEmail;

    @Password(min = 6,message = "Password incorrecta, mínimo 6 caracteres")
    EditText txtPass ;

    @ConfirmPassword(message = "Passwords no coinciden")
    EditText txtCofirmarPass;

    @Checked(message = "Debes seleccionar un avatar.")
    CheckBox seleccionAvatar;
    /*
    *
    * @Checked(message = "You must agree to the terms.")
       private CheckBox iAgreeCheckBox;
    */
    /*scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS , este es un parametro del password*/
    Button confirmar;
    Validator validator;
    ImageButton elegirAvatar;
    ListView miLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar2);

        txtEmail = (EditText) findViewById(R.id.regEmail);
        txtPass = (EditText) findViewById(R.id.regPass);
        txtCofirmarPass = (EditText) findViewById(R.id.regConfPass);
        confirmar = (Button) findViewById(R.id.btncrearperfil);
        elegirAvatar = (ImageButton) findViewById(R.id.btnAvatar);
        seleccionAvatar = (CheckBox)findViewById(R.id.ck_avatar);

        miLista = new ListView(this);
        miLista.setChoiceMode(ListView.CHOICE_MODE_SINGLE); //para que solo permita seleccionar uno
        miLista.setSelector(R.drawable.itemseleccion);



        validator = new Validator(this);
        validator.setValidationListener(this);
        elegirAvatar.setImageResource(R.drawable.avatar);

        valida();
        desicionAvatar();
    }

    public void valida(){
        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();  //validamos

            }
        });
    }

    public void desicionAvatar(){

        elegirAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Selecciona tu Avatar")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()  {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

               ArrayList<lista_entrada> datos = new ArrayList<lista_entrada>();

                datos.add(new lista_entrada(R.drawable.avatar, "avatar 1", "descripción 1"));
                datos.add(new lista_entrada(R.drawable.avatar2, "avatar 2", "descripción 2"));
                datos.add(new lista_entrada(R.drawable.amigos, "avatar 3", "descripción 3"));



                miLista.setAdapter(new lista_adaptador(v.getContext(), R.layout.layout_entrada, datos){
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

                miLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> pariente, View view, int posicion, long id) {
                        lista_entrada elegido = (lista_entrada) pariente.getItemAtPosition(posicion);

                        /*String texto = "Seleccionado: " + elegido.get_textoEncima() + "idImagen: "+elegido.get_idImagen();
                        Log.i("edward",texto);*/
                      //miLista.setItemChecked(posicion, true);
                        miLista.setSelection(posicion);



                        elegirAvatar.setImageResource(elegido.get_idImagen());
                        idavatar = elegido.get_idImagen();
                        Log.i("ed","el actual es"+idavatar);
                    }


                });




                builder.setView(miLista);
                final Dialog dialog = builder.create();
                dialog.show();

            }
        });
    }



    @Override
    public void onValidationSucceeded() {
        //Toast.makeText(this, "Datos ingresados correctamente", Toast.LENGTH_SHORT).show();
        String c = txtEmail.getText().toString();
        String p = txtPass.getText().toString();
        String p2 = txtCofirmarPass.getText().toString();
        correo = c;
        pass1 = p;
        pass2 = p2;

        ServiceRegistrar consumirWS = new ServiceRegistrar(c,p,idavatar);
        consumirWS.execute();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors)
        {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            }
            else
            {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void MensajeBox(String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(mensaje)
                .setTitle("Información")
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

    public void MensajeDecision(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("¿Confirma la acción seleccionada?")
                .setTitle("Confirmación")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()  {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
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



    public class ServiceRegistrar extends AsyncTask<String,Integer,Boolean> {

        //http://www.tesis2016.somee.com/ManejoUsuario.asmx?WSDL

        String resultado;
        String correo;
        String contrasenia;
        int imagen;
        ServiceRegistrar(String correo, String contrasenia, int imagen){
            this.correo =correo;
            this.contrasenia=contrasenia;
            this.imagen = imagen;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            final String SOAP_ACTION = "http://tempuri.org/crearUsuario";
            final String METHOD_NAME = "crearUsuario";
            final String NAMESPACE = "http://tempuri.org/";
            final String URL = "http://www.tesis2016.somee.com/ManejoUsuario.asmx";
            boolean resul = true;
            try {
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

                request.addProperty("correo", correo);
                request.addProperty("contrasenia", contrasenia);
                request.addProperty("imagen", imagen);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true; // para WS ASMX, sólo si fue construido con .Net
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

                androidHttpTransport.call(SOAP_ACTION, envelope);

                // Esta sección está destina si el Métdo del WS retorna valores
                SoapPrimitive resultado_xml =(SoapPrimitive)envelope.getResponse();
                resultado = resultado_xml.toString();

            } catch (Exception e) {
                resul = false;
                resultado = e.getMessage();
            }
            return resul;
        }

        protected void onPostExecute(Boolean result) {
            if (result) {
              //  MensajeBox(resultado);
                StringTokenizer st = new StringTokenizer(resultado, "|");
                String a1 = st.nextToken();
                String a2 = st.nextToken();

                if(a1.toString().equals("exito")){
                    MensajeBox("Bienvenido "+ a2);
                    startActivity(new Intent(Registrar2.this,Login.class));
                }else{
                    MensajeBox("El correo ya existe, por favor ingrese sus datos nuevamente.");
                }
            }else {
                MensajeBox("Error "+resultado);
            }
        }
    }//fin de clase ServiceLogin

}
