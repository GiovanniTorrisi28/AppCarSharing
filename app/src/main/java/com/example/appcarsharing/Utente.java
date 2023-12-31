package com.example.appcarsharing;

import androidx.annotation.Nullable;

public class Utente {
    private String email;
    private String password;
    private String nome;
    private String cognome;
    private String telefono;

    public Utente(String email,String password,String nome,String cognome,String telefono){
        this.email = email;
        this.password = password;
        this.nome = nome;
        this.cognome = cognome;
        this.telefono = telefono;
    }

    public Utente(String email) {
        this.email = email;
    }

    public Utente(){}

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    public void SetEmail(String email){
        this.email = email;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getKey() {
        return this.email.substring(0,this.email.indexOf("@"));
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Utente compareUser = (Utente) obj;
        return this.getEmail().equals(compareUser.getEmail());
    }

    public String toString(){
        return nome + " " + cognome + " " + email + " " + password;
    }
}
