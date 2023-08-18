package com.example.appcarsharing;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Ride {

    private Utente guidatore;
    private String sorgente;
    private String destinazione;
    private String data;
    private String orario;
    private String targa;
    private String dettagliVeicolo;
    private String posti;
    private String id;
    private List<Utente> utenti;

    public Ride() {}

    public Ride(String id, Utente guidatore, String sorgente, String destinazione, String data, String orario, String targa, String dettagliVeicolo, String posti, List<Utente> utenti) {
        this.id = id;
        this.guidatore = guidatore;
        this.sorgente = sorgente;
        this.destinazione = destinazione;
        this.orario = orario;
        this.data = data;
        this.targa = targa;
        this.dettagliVeicolo = dettagliVeicolo;
        this.posti = posti;
        this.utenti = utenti;
    }


    public Utente getGuidatore() {
        return guidatore;
    }

    public void setGuidatore(Utente guidatore) {
        this.guidatore = guidatore;
    }

    public List<Utente> getUtenti() {
        return utenti;
    }

    public void setUtenti(List<Utente> utenti) {
        this.utenti = utenti;
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

    @Override
    public String toString() {
        return "Ride{" +
                "guidatore='" + guidatore + '\'' +
                ", sorgente='" + sorgente + '\'' +
                ", destinazione='" + destinazione + '\'' +
                ", data='" + data + '\'' +
                ", orario='" + orario + '\'' +
                ", targa='" + targa + '\'' +
                ", dettagliVeicolo='" + dettagliVeicolo + '\'' +
                ", posti='" + posti + '\'' +
                ", utenti=" + utenti +
                '}';
    }

    public String getPosti() {
        return posti;
    }

    public void setPosti(String posti) {
        this.posti = posti;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

