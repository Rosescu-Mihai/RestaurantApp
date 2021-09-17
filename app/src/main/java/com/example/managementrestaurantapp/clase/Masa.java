package com.example.managementrestaurantapp.clase;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Masa implements Parcelable {

    private String idMasa;
    private boolean esteLibera;
    private String tipMasa;
    private int nrMasa;
    private NotadePlata notadePlata;
    private String numeAngajat;

    public Masa(){

    }

    public Masa(String idMasa, boolean esteLibera, String tipMasa, int nrMasa, NotadePlata notadePlata) {
        this.idMasa = idMasa;
        this.esteLibera = esteLibera;
        this.tipMasa = tipMasa;
        this.nrMasa = nrMasa;
        this.notadePlata = notadePlata;
    }

    protected Masa(Parcel in) {
        idMasa = in.readString();
        esteLibera = in.readByte() != 0;
        tipMasa = in.readString();
        nrMasa = in.readInt();
        notadePlata = in.readParcelable(NotadePlata.class.getClassLoader());
    }

    public static final Creator<Masa> CREATOR = new Creator<Masa>() {
        @Override
        public Masa createFromParcel(Parcel in) {
            return new Masa(in);
        }

        @Override
        public Masa[] newArray(int size) {
            return new Masa[size];
        }
    };

    public String getNumeAngajat() { return numeAngajat; }

    public void setNumeAngajat(String numeAngajat) { this.numeAngajat = numeAngajat; }

    public String getIdMasa() {
        return idMasa;
    }

    public void setIdMasa(String idMasa) {
        this.idMasa = idMasa;
    }

    public boolean isEsteLibera() {
        return esteLibera;
    }

    public void setEsteLibera(boolean esteLibera) {
        this.esteLibera = esteLibera;
    }

    public String getTipMasa() {
        return tipMasa;
    }

    public void setTipMasa(String tipMasa) {
        this.tipMasa = tipMasa;
    }

    public int getNrMasa() {
        return nrMasa;
    }

    public void setNrMasa(int nrMasa) {
        this.nrMasa = nrMasa;
    }

    public NotadePlata getNotadePlata() {
        return notadePlata;
    }

    public void setNotadePlata(NotadePlata notadePlata) {
        this.notadePlata = notadePlata;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idMasa);
        dest.writeByte((byte) (esteLibera ? 1 : 0));
        dest.writeString(tipMasa);
        dest.writeInt(nrMasa);
        dest.writeParcelable(notadePlata, flags);
    }
}
