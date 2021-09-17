package com.example.managementrestaurantapp.clase;

import java.util.List;

public class Comanda {
    private String idComanda;
    private String numeAngajat;
    private int nrmMasa;
    private boolean isLivrata;
    private List<Preparat> preparateDeEfectuat;

    public Comanda(){

    }

    public Comanda(String numeAngajat, int nrmMasa) {
        this.numeAngajat = numeAngajat;
        this.nrmMasa = nrmMasa;
    }

    public String getNumeAngajat() { return numeAngajat; }

    public int getNrmMasa() {
        return nrmMasa;
    }

    public String getIdComanda() { return idComanda; }

    public void setIdComanda(String idComanda) { this.idComanda = idComanda; }

    public List<Preparat> getPreparateDeEfectuat() {
        return preparateDeEfectuat;
    }

    public boolean isLivrata() { return isLivrata; }

    public void setLivrata(boolean livrata) { isLivrata = livrata; }

    public void setPreparateDeEfectuat(List<Preparat> preparateDeEfectuat) {
        this.preparateDeEfectuat = preparateDeEfectuat;
    }
}
