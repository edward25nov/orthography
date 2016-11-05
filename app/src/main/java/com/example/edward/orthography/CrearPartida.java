package com.example.edward.orthography;

import android.os.AsyncTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by root on 30/10/16.
 */

public class CrearPartida extends AsyncTask<Integer,String,SoapPrimitive> {
    private SoapPrimitive resSoap;

    @Override
    protected SoapPrimitive doInBackground(Integer... params) {
        if(android.os.Debug.isDebuggerConnected())
            android.os.Debug.waitForDebugger();

        final String SOAP_ACTION = "http://tempuri.org/crearPartida";
        final String METHOD_NAME = "crearPartida";
        final String NAMESPACE = "http://tempuri.org/";
        final String URL = "http://www.tesis2016g1.somee.com/ManejoJuegos.asmx";

        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("idUsuario", params[0]);
            request.addProperty("nivel",params[1]);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true; // para WS ASMX, sólo si fue construido con .Net
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            androidHttpTransport.call(SOAP_ACTION, envelope);

            // Esta sección está destina si el Métdo del WS retorna valores
            resSoap =(SoapPrimitive)envelope.getResponse();
            // idPartida = Integer.valueOf(resSoap.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resSoap;
    }
}
