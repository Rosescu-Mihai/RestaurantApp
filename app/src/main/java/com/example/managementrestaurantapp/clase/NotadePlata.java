package com.example.managementrestaurantapp.clase;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NotadePlata implements Parcelable {

    private String idNota;
    private double totalMasa;
    private String metodaPlata;
    private String dataNotaDePlata;
    private List<Preparat> preparateNotadePlata;

    public NotadePlata(){

    }

    public NotadePlata(String idNota, double totalMasa, String metodaPlata, String dataNotaDePlata) {
        this.idNota = idNota;
        this.totalMasa = totalMasa;
        this.metodaPlata = metodaPlata;
        this.dataNotaDePlata = dataNotaDePlata;
    }

    protected NotadePlata(Parcel in) {
        idNota = in.readString();
        totalMasa = in.readDouble();
        metodaPlata = in.readString();
        dataNotaDePlata = in.readString();
        preparateNotadePlata = in.createTypedArrayList(Preparat.CREATOR);
    }

    public static final Creator<NotadePlata> CREATOR = new Creator<NotadePlata>() {
        @Override
        public NotadePlata createFromParcel(Parcel in) {
            return new NotadePlata(in);
        }

        @Override
        public NotadePlata[] newArray(int size) {
            return new NotadePlata[size];
        }
    };

    public String getIdNota() {
        return idNota;
    }

    public void setIdNota(String idNota) {
        this.idNota = idNota;
    }

    public double getTotalMasa() {
        return totalMasa;
    }

    public void setTotalMasa(double totalMasa) {
        this.totalMasa = totalMasa;
    }

    public String getMetodaPlata() {
        return metodaPlata;
    }

    public void setMetodaPlata(String metodaPlata) {
        this.metodaPlata = metodaPlata;
    }

    public String getDataNotaDePlata() {
        return dataNotaDePlata;
    }

    public void setPreparateNotadePlata(List<Preparat> preparatList) { this.preparateNotadePlata = preparatList; }

    public List<Preparat> getPreparateNotadePlata() { return preparateNotadePlata; }

    public void setDataNotaDePlata(String dataNotaDePlata) {
        this.dataNotaDePlata = dataNotaDePlata;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idNota);
        dest.writeDouble(totalMasa);
        dest.writeString(metodaPlata);
        dest.writeString(dataNotaDePlata);
        dest.writeTypedList(preparateNotadePlata);
    }
}
