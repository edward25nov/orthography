package com.example.edward.orthography;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
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

        validator = new Validator(this);
        validator.setValidationListener(this);
        /*asignare datos por defecto, para cuando regresen de elegir su avatar*/
        txtEmail.setText(avatar.correo);
        txtPass.setText(avatar.pass1);
        txtCofirmarPass.setText(avatar.pass2);
        idavatar = avatar.idAvatar;

        elegirAvatar.setImageResource(idavatar);

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
            public void onClick(View v) {
                String c = txtEmail.getText().toString();
                String p = txtPass.getText().toString();
                String p2 = txtCofirmarPass.getText().toString();
                correo = c;
                pass1 = p;
                pass2 = p2;
                idavatar = avatar.idAvatar;

                avatar.correo = Registrar2.correo;
                avatar.pass1 = Registrar2.pass1;
                avatar.pass2 = Registrar2.pass2;
                avatar.idAvatar = Registrar2.idavatar;

                startActivity(new Intent(Registrar2.this, avatar.class));

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
        idavatar = avatar.idAvatar;

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
