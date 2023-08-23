package com.example.appcarsharing;

import java.util.List;

public class Notification {

    private String mittente;
    private String titolo;
    private String messaggio;

    public Notification(){}

    public Notification(String mittente, String titolo, String messaggio){
        this.mittente = mittente;
        this.titolo = titolo;
        this.messaggio = messaggio;
    }

    public String getMittente() {
        return mittente;
    }

    public void setMittente(String mittente) {
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
}
