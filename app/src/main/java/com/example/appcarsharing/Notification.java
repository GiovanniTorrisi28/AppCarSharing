package com.example.appcarsharing;

import java.util.List;

public class Notification {

    private Utente mittente;
    private String titolo;
    private String messaggio;
    private String data;

    public Notification(){}

    public Notification(Utente mittente, String titolo, String messaggio, String data){
        this.mittente = mittente;
        this.titolo = titolo;
        this.messaggio = messaggio;
        this.data = data;
    }

    public Utente getMittente() {
        return mittente;
    }

    public void setMittente(Utente mittente) {
        this.mittente = mittente;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
