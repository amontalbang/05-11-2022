package com.example.a05_11_2022;

public class PuntModelo {

    private int id;
    private String nombre;
    private int tiempo;

    public PuntModelo(int id, String nombre, int tiempo) {
        this.id = id;
        this.nombre = nombre;
        this.tiempo = tiempo;
    }

    public PuntModelo(int tiempo) {
        this.tiempo = tiempo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getTiempo() {
        return tiempo;
    }

    public int getId() {
        return id;
    }
}
