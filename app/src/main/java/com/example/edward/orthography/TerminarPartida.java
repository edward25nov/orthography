package com.example.edward.orthography;

import android.os.AsyncTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by root on 30/10/16.
 */
public class TerminarPartida extends AsyncTask<Integer,String,SoapObject> {
    private SoapObject resSoap;

    @Override
    protected SoapObject doInBackground(Integer... params) {
        if(android.os.Debug.isDebuggerConnected())
            android.os.Debug.waitForDebugger();

        final String SOAP_ACTION = "http://tempuri.org/terminarPartida";
        final String METHOD_NAME = "terminarPartida";
        final String NAMESPACE = "http://tempuri.org/";
        final String URL = "http://www.tesis2016g1.somee.com/ManejoJuegos.asmx";
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("idUsuario", params[0]);
            request.addProperty("idPartida",params[1]);
            request.addProperty("puntos",params[2]);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true; // para WS ASMX, sólo si fue construido con .Net
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            androidHttpTransport.call(SOAP_ACTION, envelope);
            // Esta sección está destina si el Métdo del WS retorna valores
            resSoap =(SoapObject)envelope.getResponse();
        } catch (Exception e) {
            //asdfasdf
        }
        return resSoap;
    }
}