package com.example.appcarsharing;

import org.osmdroid.util.GeoPoint;

public class PointOfInterest {

    private String nome;
    private String indirizzo;
    private GeoPoint coordinate;
    private int id;

    public PointOfInterest(String nome, String indirizzo, GeoPoint coordinate, int id) {
        this.nome = nome;
        this.indirizzo = indirizzo;
        this.coordinate = coordinate;
        this.id = id;
    }

    public PointOfInterest(){}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public GeoPoint getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(GeoPoint coordinate) {
        this.coordinate = coordinate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
