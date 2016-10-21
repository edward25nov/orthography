package com.example.edward.orthography;

/**
 * Created by root on 20/10/16.
 */

public class lecciones_entrada {
    private int idImagen;
    private String textotitulo;
    private float ratingBarleccion;

    public lecciones_entrada (int idImagen, String textotitulo, float ratingBarleccion) {
        this.idImagen = idImagen;
        this.textotitulo = textotitulo;
        this.ratingBarleccion = ratingBarleccion;
    }

    public String get_titulo() {
        return textotitulo;
    }

    public float get_rating() {
        return ratingBarleccion;
    }

    public int get_idImagen() {
        return idImagen;
    }



}
