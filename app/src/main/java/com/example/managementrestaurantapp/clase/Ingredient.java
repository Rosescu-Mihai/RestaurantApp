package com.example.managementrestaurantapp.clase;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {

    private String idPreparat;
    private String numeIngredient;
    private int cantitatePerBuc;
    private float nrBuc;
    private int pretPerBuc;
    private float cantitateTotala;

    public Ingredient() {
    }

    protected Ingredient(Parcel in) {
        idPreparat = in.readString();
        numeIngredient = in.readString();
        cantitatePerBuc = in.readInt();
        nrBuc = in.readFloat();
        pretPerBuc = in.readInt();
        cantitateTotala = in.readFloat();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public String getIdPreparat() {
        return idPreparat;
    }

    public void setIdPreparat(String idPreparat) {
        this.idPreparat = idPreparat;
    }

    public String getNumeIngredient() {
        return numeIngredient;
    }

    public void setNumeIngredient(String numeIngredient) {
        this.numeIngredient = numeIngredient;
    }

    public int getCantitatePerBuc() {
        return cantitatePerBuc;
    }

    public void setCantitatePerBuc(int cantitatePerBuc) { this.cantitatePerBuc = cantitatePerBuc; }

    public float getNrBuc() {
        return nrBuc;
    }

    public void setNrBuc(float nrBuc) {
        this.nrBuc = nrBuc;
    }

    public int getPretPerBuc() {
        return pretPerBuc;
    }

    public void setPretPerBuc(int pretPerBuc) {
        this.pretPerBuc = pretPerBuc;
    }

    public float getCantitateTotala() { return cantitateTotala; }

    public void setCantitateTotala(float cantitateTotala) {
        if (cantitateTotala == 0) {
            this.cantitateTotala = this.cantitatePerBuc * this.nrBuc;
        }else {
            this.cantitateTotala = cantitateTotala;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idPreparat);
        dest.writeString(numeIngredient);
        dest.writeInt(cantitatePerBuc);
        dest.writeFloat(nrBuc);
        dest.writeInt(pretPerBuc);
        dest.writeFloat(cantitateTotala);
    }
}

