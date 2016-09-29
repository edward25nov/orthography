package com.example.edward.orthography;

/**
 * Created by root on 28/09/16.
 */

public class lista_entrada {
    private int idImagen;
    private String textoEncima;
    private String textoDebajo;

    public lista_entrada (int idImagen, String textoEncima, String textoDebajo) {
        this.idImagen = idImagen;
        this.textoEncima = textoEncima;
        this.textoDebajo = textoDebajo;
    }

    public String get_textoEncima() {
        return textoEncima;
    }

    public String get_textoDebajo() {
        return textoDebajo;
    }

    public int get_idImagen() {
        return idImagen;
    }

}
