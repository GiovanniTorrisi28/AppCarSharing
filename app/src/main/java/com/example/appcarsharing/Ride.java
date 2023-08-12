package com.example.appcarsharing;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class Ride {

    private String sorgente;
    private String destinazione;
    private String data;
    private String orario;
    private String targa;
    private String dettagliVeicolo;

    public Ride() {}

    public Ride(String sorgente, String destinazione, String data, String orario, String targa, String dettagliVeicolo) {
        this.sorgente = sorgente;
        this.destinazione = destinazione;
        this.orario = orario;
        this.data = data;
        this.targa = targa;
        this.dettagliVeicolo = dettagliVeicolo;
    }

    public String getSorgente() {
        return sorgente;
    }

    public void setSorgente(String sorgente) {
        this.sorgente = sorgente;
    }

    public String getDestinazione() {
        return destinazione;
    }

    public void setDestinazione(String destinazione) {
        this.destinazione = destinazione;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getOrario() {
        return orario;
    }

    public void setOrario(String orario) {
        this.orario = orario;
    }

    public String getTarga() {
        return targa;
    }

    public void setTarga(String targa) {
        this.targa = targa;
    }

    public String getDettagliVeicolo() {
        return dettagliVeicolo;
    }

    public void setDettagliVeicolo(String dettagliVeicolo) {
        this.dettagliVeicolo = dettagliVeicolo;
    }
}
