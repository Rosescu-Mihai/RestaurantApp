package com.example.managementrestaurantapp.clase;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.util.List;

public class Preparat implements Parcelable {

    private String id_preparat;
    private String nume_preparat;
    private String tip_preparat;
    private  double pret_unitar;
    private List<Ingredient> ingrediente;
    private int imaginePreparat;
    private int cantitate;
    private boolean isEfectuat;
    private int pozitieInNota;

    public Preparat() {
    }

    public Preparat(String id_preparat, String nume_preparat, String tip_preparat, double pret_unitar, List<Ingredient> ingrediente, int imaginePreparat, int cantitate) {
        this.id_preparat = id_preparat;
        this.nume_preparat = nume_preparat;
        this.tip_preparat = tip_preparat;
        this.pret_unitar = pret_unitar;
        this.ingrediente = ingrediente;
        this.imaginePreparat = imaginePreparat;
        this.cantitate = cantitate;
    }

    public Preparat(String nume_preparat, double pret_unitar, String tip_preparat, List<Ingredient> ingrediente, int imaginePreparat, int cantitate){
        this.nume_preparat = nume_preparat;
        this.pret_unitar = pret_unitar;
        this.tip_preparat = tip_preparat;
        this.ingrediente = ingrediente;
        this.cantitate = cantitate;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected Preparat(Parcel in) {
        id_preparat = in.readString();
        nume_preparat = in.readString();
        tip_preparat = in.readString();
        pret_unitar = in.readDouble();
        ingrediente = in.createTypedArrayList(Ingredient.CREATOR);
        imaginePreparat = in.readInt();
        cantitate = in.readInt();
        isEfectuat = in.readBoolean();
        pozitieInNota = in.readInt();
    }

    public static final Creator<Preparat> CREATOR = new Creator<Preparat>() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public Preparat createFromParcel(Parcel in) {
            return new Preparat(in);
        }

        @Override
        public Preparat[] newArray(int size) {
            return new Preparat[size];
        }
    };

    public int getPozitieInNota() { return pozitieInNota; }

    public void setPozitieInNota(int pozitieInNota) { this.pozitieInNota = pozitieInNota; }

    public boolean isEfectuat() { return isEfectuat; }

    public void setEfectuat(boolean efectuat) { isEfectuat = efectuat; }

    public String getId_preparat() {
        return id_preparat;
    }

    public void setId_preparat(String id_preparat) {
        this.id_preparat = id_preparat;
    }

    public String getNume_preparat() {
        return nume_preparat;
    }

    public void setNume_preparat(String nume_preparat) {
        this.nume_preparat = nume_preparat;
    }

    public String getTip_preparat() {
        return tip_preparat;
    }

    public void setTip_preparat(String tip_preparat) {
        this.tip_preparat = tip_preparat;
    }

    public double getPret_unitar() {
        return pret_unitar;
    }

    public void setPret_unitar(double pret_unitar) {
        this.pret_unitar = pret_unitar;
    }

    public List<Ingredient> getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(List<Ingredient> ingrediente) {
        this.ingrediente = ingrediente;
    }

    public int getCantitate() {
        return cantitate;
    }

    public void setCantitate(int cantitate) {
        this.cantitate = cantitate;
    }

    public double calcultotal(){ return this.cantitate*this.pret_unitar; }

    @Override
    public boolean equals(@Nullable @org.jetbrains.annotations.Nullable Object obj) {
        Preparat preparat = (Preparat) obj;
        return this.nume_preparat.equals(((Preparat) obj).nume_preparat);
    }

    @Override
    public String toString() {
        return "Preparat{" +
                "id_preparat=" + id_preparat +
                ", nume_preparat='" + nume_preparat + '\'' +
                ", tip_preparat='" + tip_preparat + '\'' +
                ", pret_unitar=" + pret_unitar +
                ", ingrediente='" + ingrediente + '\'' +
                ", imaginePreparat=" + imaginePreparat +
                ", cantitate=" + cantitate +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_preparat);
        dest.writeString(nume_preparat);
        dest.writeString(tip_preparat);
        dest.writeDouble(pret_unitar);
        dest.writeTypedList(ingrediente);
        dest.writeInt(imaginePreparat);
        dest.writeInt(cantitate);
        dest.writeBoolean(isEfectuat);
        dest.writeInt(pozitieInNota);
    }
}
